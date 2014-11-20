/*
 * AccuRevDAOFacade.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jun 18, 2005, 10:35:08 AM
 */
package net.java.accurev4idea.plugin;

import com.intellij.openapi.diff.BinaryContent;
import com.intellij.openapi.diff.DiffContent;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.vcs.FileStatus;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import net.java.accurev4idea.api.AccuRev;
import net.java.accurev4idea.api.components.AccuRevFile;
import net.java.accurev4idea.api.components.AccuRevTransaction;
import org.apache.commons.lang.SystemUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.List;

/**
 * This adapter provides extra methods on top of {@link AccuRevToIdeaAdapter} implementation
 * that are written to fit the execution environment of Idea plugin.
 *
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version $Id: AccuRevToIdeaAdapter.java,v 1.3 2005/11/11 22:42:54 ifedulov Exp $
 * @since 0.1
 */
public class AccuRevToIdeaAdapter implements ProjectComponent {
    /**
     * Log4j audit channel
     */
    private static Logger log = Logger.getLogger(AccuRevToIdeaAdapter.class);

    /**
     * Backing cache for the status fetching
     */
    private AccuRevFileStatusCache backingCache;

    private AccuRevVcs vcs;

    public AccuRevToIdeaAdapter(AccuRevVcs vcs) {
        this.vcs = vcs;
        this.backingCache = new AccuRevFileStatusCache(vcs);
    }

    public static AccuRevToIdeaAdapter getInstance(Project project) {
        return (AccuRevToIdeaAdapter) project.getComponent(AccuRevToIdeaAdapter.class);
    }

    public void projectOpened() {

    }

    public void projectClosed() {

    }

    public String getComponentName() {
        return getClass().getName();
    }

    public void initComponent() {

    }

    public void disposeComponent() {

    }

    /**
     */
    public DiffContent getBackedDiffContent(VirtualFile virtualFile) {
        return getDiffContent(virtualFile, new Command() {
            public String execute(File file) {
                return AccuRev.getBackedFileContents(file, vcs.getCommandExecListeners());
            }
        });
    }

    /**
     */
    public DiffContent getLastKeptDiffContent(VirtualFile virtualFile) {
        return getDiffContent(virtualFile, new Command() {
            public String execute(File file) {
                return AccuRev.getLastKeptFileContents(file, vcs.getCommandExecListeners());
            }
        });
    }

    /**
     */
    public DiffContent getDiffContentByVersion(VirtualFile virtualFile, final AccuRevTransaction transaction) {
        return getDiffContent(virtualFile, new Command() {
            public String execute(File file) {
                return AccuRev.getFileContentsByVersion(file, transaction.getVersion(), vcs.getCommandExecListeners());
            }
        });
    }

    /**
     */
    public DiffContent getEditorDiffContent(VirtualFile virtualFile) {
        if (virtualFile == null) {
            throw new IllegalArgumentException("File can't be null.");
        }
        Document doc = FileDocumentManager.getInstance().getCachedDocument(virtualFile);
        if (doc == null) {
            doc = FileDocumentManager.getInstance().getDocument(virtualFile);
        }
        return new BinaryContent(doc.getText().getBytes(), SystemUtils.FILE_ENCODING, FileTypeManager.getInstance().getFileTypeByFile(virtualFile));
    }

    /**
     */
    public List getRevisionHistory(VirtualFile virtualFle) {
        if (virtualFle == null) {
            throw new IllegalArgumentException("File can't be null.");
        }
        final File file = new File(virtualFle.getPresentableUrl());
        final AccuRevFile accuRevFile = AccuRev.getAccuRevFile(file, vcs.getCommandExecListeners());
        return AccuRev.getRevisionHistory(accuRevFile, file, vcs.getCommandExecListeners());
    }

    /**
     */
    public synchronized FileStatus getFileStatus(VirtualFile virtualFile) {
        return backingCache.getFileStatus(virtualFile);
    }


    public void refreshFileStatusCache() {
        backingCache.refreshCache();
    }

    public void refreshFileStatusCacheForFile(VirtualFile file) {
        backingCache.refreshCache(file);
    }

    /**
     * Obtain {@link com.intellij.openapi.diff.DiffContent} for given {@link com.intellij.openapi.vfs.VirtualFile} and {@link Command} implementation.
     *
     * @param virtualFile
     * @param command
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
         * Execute implementation with given {@link java.io.File} as an argument
         *
         * @see 
         */
        public abstract String execute(File file);
    }
}