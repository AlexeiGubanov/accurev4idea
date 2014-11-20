/*
 * VcsPluginDAO.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jun 18, 2005, 12:41:42 PM
 */
package net.java.accurev4idea.dao;

import com.intellij.openapi.diff.DiffContent;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vcs.FileStatus;
import net.java.savant.plugin.scm.accurev.components.AccuRevTransaction;
import net.java.savant.exec.SavantExecutionException;

import java.util.List;

/**
 * VCS centric DAO, arguments and return results are a direct use from within VCS plugin.
 *
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version $Id: VcsPluginDAO.java,v 1.2 2005/06/19 18:56:06 ifedulov Exp $
 * @since 0.1
 */
public interface VcsPluginDAO {
    /**
     * Obtain {@link DiffContent} from backed version of the given {@link VirtualFile}
     *
     * @param virtualFile
     * @return
     * @throws SavantExecutionException
     */
    DiffContent getBackedDiffContent(VirtualFile virtualFile) throws SavantExecutionException;

    /**
     * Obtain {@link DiffContent} for given {@link VirtualFile} by given {@link AccuRevTransaction}
     * transaction
     *
     * @param virtualFile
     * @param transaction
     * @return
     * @throws SavantExecutionException
     */
    DiffContent getDiffContentByVersion(VirtualFile virtualFile, AccuRevTransaction transaction) throws SavantExecutionException;

    /**
     * Obtain {@link DiffContent} for given {@link VirtualFile} by reading current editor contents
     *
     * @param virtualFile
     * @return
     */
    DiffContent getEditorDiffContent(VirtualFile virtualFile);

    /**
     * Obtain revision history list in form of {@link AccuRevTransaction}s for given {@link VirtualFile}
     *
     * @param virtualFle
     * @return
     */
    List getRevisionHistory(VirtualFile virtualFle) throws SavantExecutionException;

    /**
     * Obtain {@link FileStatus} for given {@link VirtualFile}
     *
     * @param virtualFile a file to check the status for
     * @return current file status
     */
    FileStatus getFileStatus(VirtualFile virtualFile) throws SavantExecutionException;
}
