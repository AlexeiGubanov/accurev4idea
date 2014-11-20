/*
 * AccuRevDAOFacade.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jun 18, 2005, 10:35:08 AM
 */
package net.java.accurev4idea.dao;

import com.intellij.openapi.diff.BinaryContent;
import com.intellij.openapi.diff.DiffContent;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.vcs.FileStatus;
import com.intellij.openapi.vfs.VirtualFile;
import net.java.accurev4idea.AccuRevVcs;
import net.java.savant.Newline;
import net.java.savant.plugin.scm.Locking;
import net.java.savant.plugin.scm.accurev.StatusSelectionType;
import net.java.savant.plugin.scm.accurev.components.AccuRevFile;
import net.java.savant.plugin.scm.accurev.components.AccuRevFileStatus;
import net.java.savant.plugin.scm.accurev.components.AccuRevInfo;
import net.java.savant.plugin.scm.accurev.components.AccuRevTransaction;
import net.java.savant.plugin.scm.accurev.components.AccuRevVersion;
import net.java.savant.plugin.scm.accurev.components.Depot;
import net.java.savant.plugin.scm.accurev.components.Stream;
import net.java.savant.plugin.scm.accurev.components.Workspace;
import net.java.savant.plugin.scm.accurev.dao.AccuRevDAO;
import net.java.savant.plugin.scm.accurev.dao.AccuRevDAOImpl;
import net.java.savant.plugin.scm.accurev.exceptions.AccuRevRuntimeException;
import net.java.savant.plugin.scm.accurev.exceptions.NoSuchObjectException;
import net.java.savant.plugin.scm.accurev.exceptions.WorkspaceUpdateException;
import org.apache.commons.lang.SystemUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * This adapter provides extra methods on top of {@link AccuRevDAO} implementation
 * that are written to fit the execution environment of Idea plugin.
 *
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version $Id: AccuRevDAOFacade.java,v 1.6 2005/07/12 00:44:49 ifedulov Exp $
 * @since 0.1
 */
public class AccuRevDAOFacade implements AccuRevDAO, VcsPluginDAO {
    /**
     * Log4j audit channel
     */
    private static Logger log = Logger.getLogger(AccuRevDAOFacade.class);

    /**
     * Adapted instance of {@link AccuRevDAO} implementation
     */
    private AccuRevDAO accuRevDAO;
    /**
     * Backing cache for the status fetching
     */
    private AccuRevFileStatusCache backingCache;

    private AccuRevVcs vcs = null;

    public AccuRevDAOFacade(AccuRevVcs vcs) {
        this.vcs = vcs;
        this.backingCache = new AccuRevFileStatusCache(vcs);
        this.accuRevDAO = new AccuRevDAOImpl();
    }

    /**
     * @see VcsPluginDAO#getBackedDiffContent(com.intellij.openapi.vfs.VirtualFile)
     */
    public DiffContent getBackedDiffContent(VirtualFile virtualFile) {
        return getDiffContent(virtualFile, new Command() {
            public String execute(File file) {
                return accuRevDAO.getBackedFileContents(file);
            }
        });
    }

    /**
     * @see VcsPluginDAO#getDiffContentByVersion(com.intellij.openapi.vfs.VirtualFile, net.java.savant.plugin.scm.accurev.components.AccuRevTransaction)
     */
    public DiffContent getDiffContentByVersion(VirtualFile virtualFile, final AccuRevTransaction transaction) {
        return getDiffContent(virtualFile, new Command() {
            public String execute(File file) {
                return accuRevDAO.getFileContentsByVersion(file, transaction.getVersion());
            }
        });
    }

    /**
     * @see VcsPluginDAO#getEditorDiffContent(com.intellij.openapi.vfs.VirtualFile)
     */
    public DiffContent getEditorDiffContent(VirtualFile virtualFile) {
        if (virtualFile == null) {
            throw new IllegalArgumentException("File can't be null.");
        }
        Document doc = FileDocumentManager.getInstance().getCachedDocument(virtualFile);
        if (doc == null) {
            doc = FileDocumentManager.getInstance().getDocument(virtualFile);
        }
        return DiffContent.fromDocument(doc);
    }

    /**
     * @see VcsPluginDAO#getRevisionHistory(com.intellij.openapi.vfs.VirtualFile)
     */
    public List getRevisionHistory(VirtualFile virtualFle) {
        if (virtualFle == null) {
            throw new IllegalArgumentException("File can't be null.");
        }
        final File file = new File(virtualFle.getPresentableUrl());
        final AccuRevFile accuRevFile = getAccuRevFile(file);
        return getRevisionHistory(accuRevFile, file);
    }

    /**
     * @see VcsPluginDAO#getRevisionHistory(com.intellij.openapi.vfs.VirtualFile)
     */
    public synchronized FileStatus getFileStatus(VirtualFile virtualFile) {
        return backingCache.getFileStatus(virtualFile);
    }

    /**
     * @see net.java.savant.plugin.scm.accurev.dao.AccuRevDAO#getInfo()
     */
    public AccuRevInfo getInfo() {
        return accuRevDAO.getInfo();
    }

    /**
     * @see net.java.savant.plugin.scm.accurev.dao.AccuRevDAO#getDepots()
     */
    public List getDepots() {
        return accuRevDAO.getDepots();
    }

    /**
     * @see net.java.savant.plugin.scm.accurev.dao.AccuRevDAO#getWorkspacesForCurrentPrincipal()
     */
    public Map getWorkspacesForCurrentPrincipal() {
        return accuRevDAO.getWorkspacesForCurrentPrincipal();
    }

    /**
     * @see AccuRevDAO#getWorkspaceByNameForCurrentPrincipal(String)
     */
    public Workspace getWorkspaceByNameForCurrentPrincipal(String name) {
        return accuRevDAO.getWorkspaceByNameForCurrentPrincipal(name);
    }

    /**
     * @see AccuRevDAO#getStreamsTree(net.java.savant.plugin.scm.accurev.components.Depot)
     */
    public Stream getStreamsTree(Depot depot) {
        return accuRevDAO.getStreamsTree(depot);
    }

    /**
     * @see AccuRevDAO#getBackedFileContents(java.io.File)
     */
    public String getBackedFileContents(File file) {
        return accuRevDAO.getBackedFileContents(file);
    }

    /**
     * @see AccuRevDAO#getFileContentsByVersion(java.io.File, net.java.savant.plugin.scm.accurev.components.AccuRevVersion)
     */
    public String getFileContentsByVersion(File file, AccuRevVersion version) {
        return accuRevDAO.getFileContentsByVersion(file, version);
    }

    /**
     * @see AccuRevDAO#getRevisionHistory(net.java.savant.plugin.scm.accurev.components.AccuRevFile, java.io.File)
     */
    public List getRevisionHistory(final AccuRevFile accuRevFile, final File file) {
        return accuRevDAO.getRevisionHistory(accuRevFile, file);
    }

    /**
     * @see AccuRevDAO#getAccuRevFile(java.io.File)
     */
    public AccuRevFile getAccuRevFile(final File file) {
        return accuRevDAO.getAccuRevFile(file);
    }

    /**
     * @see AccuRevDAO#getAccuRevFilesInWorkspaceByStatus(net.java.savant.plugin.scm.accurev.components.Workspace, net.java.savant.plugin.scm.accurev.StatusSelectionType)
     */
    public List getAccuRevFilesInWorkspaceByStatus(Workspace workspace, StatusSelectionType type) {
        return accuRevDAO.getAccuRevFilesInWorkspaceByStatus(workspace, type);
    }

    public AccuRevFileStatus getAccuRevFileStatus(File file) throws AccuRevRuntimeException, NoSuchObjectException {
        return accuRevDAO.getAccuRevFileStatus(file);
    }

    /**
     * @see AccuRevDAO#createWorkspace(net.java.savant.plugin.scm.accurev.components.Stream, java.io.File, String, net.java.savant.Newline, net.java.savant.plugin.scm.Locking)
     */
    public Workspace createWorkspace(Stream parentStream, File location, String name, Newline newline, Locking locking) {
        return accuRevDAO.createWorkspace(parentStream, location, name, newline, locking);
    }

    /**
     * @see AccuRevDAO#updateWorkspace(String)
     */
    public void updateWorkspace(String name) throws AccuRevRuntimeException, NoSuchObjectException, WorkspaceUpdateException {
        try {
            accuRevDAO.updateWorkspace(name);
        } catch (AccuRevRuntimeException e) {
            // Log the error and full exception stack trace
            log.error(e.getLocalizedMessage(), e);
            // workspace update failed, try
        }
    }

    /**
     * @see AccuRevDAO#getWorkspaceByFileForCurrentPrincipal(java.io.File)
     */
    public Workspace getWorkspaceByFileForCurrentPrincipal(File file) {
        return accuRevDAO.getWorkspaceByFileForCurrentPrincipal(file);
    }

    /**
     * @see AccuRevDAO#rollbackLocalChanges(java.io.File)
     */
    public void rollbackLocalChanges(File file) throws AccuRevRuntimeException, NoSuchObjectException {
        accuRevDAO.rollbackLocalChanges(file);
    }

    public void refreshFileStatusCache() {
        backingCache.refreshCache();
    }

    public void refreshFileStatusCacheForFile(VirtualFile file) {
        backingCache.refreshCache(file);
    }

    /**
     * Obtain {@link DiffContent} for given {@link VirtualFile} and {@link Command} implementation.
     *
     * @param virtualFile
     * @param command
     * @return
     * @
     * @see Command
     */
    private DiffContent getDiffContent(VirtualFile virtualFile, Command command) {
        byte[] fileContents = null;
        final String fileLocation = virtualFile.getPresentableUrl();
        final File file = new File(fileLocation);
        FileType fileType = FileTypeManager.getInstance().getFileTypeByFile(virtualFile);

        final String string = command.execute(file);

        fileContents = string.getBytes();

        BinaryContent diffContent = new BinaryContent(fileContents, SystemUtils.FILE_ENCODING, fileType);
        return diffContent;
    }


    /**
     * Since it's not possible to have private interfaces, this private abstract class
     * fulfills this role.
     */
    private abstract class Command {
        /**
         * Execute implementation with given {@link File} as an argument
         *
         * @see AccuRevDAOFacade#getDiffContent(com.intellij.openapi.vfs.VirtualFile, net.java.accurev4idea.dao.AccuRevDAOFacade.Command)
         */
        public abstract String execute(File file);
    }
}
