/*
 * LocalFileRevision.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jul 3, 2005, 9:00:58 AM
 */
package net.java.accurev4idea.plugin.components;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.vcs.RepositoryLocation;
import com.intellij.openapi.vcs.history.VcsFileRevision;
import com.intellij.openapi.vcs.history.VcsRevisionNumber;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Date;

/**
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version $Id: LocalFileRevision.java,v 1.1 2005/11/05 16:56:19 ifedulov Exp $
 * @since 0.1
 */
public class LocalFileRevision implements VcsFileRevision {
    /**
     * Log4j audit channel
     */
    private static Logger log = Logger.getLogger(LocalFileRevision.class);

    private VirtualFile virtualFile = null;

    public LocalFileRevision(VirtualFile virtualFile) {
        this.virtualFile = virtualFile;
    }

    public byte[] getContent() throws IOException {
        Document document = FileDocumentManager.getInstance().getDocument(virtualFile);
        return document.getText().getBytes();
    }

    public Date getRevisionDate() {
        return new Date(virtualFile.getTimeStamp());
    }

    public byte[] loadContent() {
        return new byte[0]; //FIXED
    }

    public String getCommitMessage() {
        return StringUtils.EMPTY;
    }

    public String getAuthor() {
        return SystemUtils.USER_NAME;
    }

    public VcsRevisionNumber getRevisionNumber() {
        return VcsRevisionNumber.NULL;
    }

    public String getBranchName() {
        return null;
    }

    @Nullable
    @Override
    public RepositoryLocation getChangedRepositoryPath() {
        return null; //TODO
    }

    public int compareTo(Object o) {
        if (o instanceof AccuRevFileRevision) {
            return 0;
        } else {
            return 1;
        }
    }
}
