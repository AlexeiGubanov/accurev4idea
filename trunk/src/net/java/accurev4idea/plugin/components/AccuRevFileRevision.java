/*
 * AccuRevFileRevision.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jul 3, 2005, 8:57:14 AM
 */
package net.java.accurev4idea.plugin.components;

import com.intellij.openapi.vcs.RepositoryLocation;
import com.intellij.openapi.vcs.history.VcsFileRevision;
import com.intellij.openapi.vcs.history.VcsRevisionNumber;
import com.intellij.openapi.vfs.VirtualFile;
import net.java.accurev4idea.plugin.AccuRevVcs;
import net.java.accurev4idea.api.components.AccuRevTransaction;
import net.java.accurev4idea.api.AccuRev;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version $Id: AccuRevFileRevision.java,v 1.2 2005/11/07 23:41:17 ifedulov Exp $
 * @since 0.1
 */
public class AccuRevFileRevision implements VcsFileRevision {
    /**
     * Log4j audit channel
     */
    private static Logger log = Logger.getLogger(AccuRevFileRevision.class);

    private VirtualFile virtualFile = null;
    private AccuRevTransaction transaction = null;
    private AccuRevVcs vcs = null;
    private String contents;

    public AccuRevFileRevision(VirtualFile virtualFile, AccuRevTransaction accuRevTransaction, AccuRevVcs vcs) {
        this.virtualFile = virtualFile;
        this.transaction = accuRevTransaction;
        this.vcs = vcs;
    }

    public byte[] getContent() throws IOException {
        return contents.getBytes();
    }

    public Date getRevisionDate() {
        return transaction.getDate();
    }

    public byte[] loadContent() {
        contents = AccuRev.getFileContentsByVersion(new File(virtualFile.getPresentableUrl()), transaction.getVersion(), vcs.getCommandExecListeners());
        return contents.getBytes(); //FIXED
    }

    public String getCommitMessage() {
        return transaction.getComment();
    }

    public String getAuthor() {
        return transaction.getUserName();
    }

    public VcsRevisionNumber getRevisionNumber() {
        return VcsRevisionNumber.NULL; //transaction.getVersion().getVirtual().toString();
    }

    public String getBranchName() {
        return null;
    }

    @Nullable
    @Override
    public RepositoryLocation getChangedRepositoryPath() {
        //TODO
        return null;
    }

    public int compareTo(Object o) {
        if (o instanceof AccuRevFileRevision) {
            return 0;
        } else {
            return 1;
        }
    }
}
