/*
 * AccuRevFileRevision.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jul 3, 2005, 8:57:14 AM
 */
package net.java.accurev4idea.components;

import com.intellij.openapi.vcs.history.VcsFileRevision;
import com.intellij.openapi.vfs.VirtualFile;
import net.java.accurev4idea.AccuRevVcs;
import net.java.savant.plugin.scm.accurev.components.AccuRevTransaction;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version $Id: AccuRevFileRevision.java,v 1.1 2005/07/12 00:44:48 ifedulov Exp $
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

    public void loadContent() {
        contents = vcs.getAccuRevDAO().getFileContentsByVersion(new File(virtualFile.getPresentableUrl()), transaction.getVersion());
    }

    public String getCommitMessage() {
        return transaction.getComment();
    }

    public String getAuthor() {
        return transaction.getUserName();
    }

    public String getRevisionNumber() {
        return transaction.getVersion().getVirtual().toString();
    }

    public int compareTo(Object o) {
        if (o instanceof AccuRevFileRevision) {
            return 0;
        } else {
            return 1;
        }
    }
}
