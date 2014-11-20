/*
 * AccuRevHistoryProvider.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jul 3, 2005, 8:50:55 AM
 */
package net.java.accurev4idea.providers;

import com.intellij.openapi.ui.ColumnInfo;
import com.intellij.openapi.vcs.history.VcsFileRevision;
import com.intellij.openapi.vcs.history.VcsHistoryProvider;
import com.intellij.openapi.vcs.FileStatus;
import com.intellij.openapi.vfs.VirtualFile;
import net.java.accurev4idea.components.LocalFileRevision;
import net.java.accurev4idea.components.AccuRevFileRevision;
import net.java.accurev4idea.AccuRevVcs;
import net.java.savant.plugin.scm.accurev.components.AccuRevTransaction;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version $Id: AccuRevHistoryProvider.java,v 1.1 2005/07/12 00:44:53 ifedulov Exp $
 * @since 0.1
 */
public class AccuRevHistoryProvider implements VcsHistoryProvider {
    /**
     * Log4j audit channel
     */
    private static Logger log = Logger.getLogger(AccuRevHistoryProvider.class);
    private AccuRevVcs vcs;
    private final List revisions;

    public AccuRevHistoryProvider(AccuRevVcs vcs) {
        this.vcs = vcs;
        this.revisions = new LinkedList();
    }

    public ColumnInfo[] getRevisionColumns() {
        return new ColumnInfo[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List getRevisionsList(VirtualFile virtualFile) {
        return revisions;
    }

    public void loadRevisions(VirtualFile virtualFile) {
        final List revisions = vcs.getAccuRevDAO().getRevisionHistory(virtualFile);
        for (Iterator i = revisions.iterator(); i.hasNext();) {
            AccuRevTransaction accuRevTransaction = (AccuRevTransaction) i.next();
            AccuRevFileRevision revision = new AccuRevFileRevision(virtualFile, accuRevTransaction, vcs);
            revisions.add(revision);
        }
    }

    public boolean fileIsUnderVcs(VirtualFile virtualFile) {
        return !FileStatus.UNKNOWN.equals(vcs.getAccuRevDAO().getFileStatus(virtualFile));
    }

    public VcsFileRevision createCurrentRevision(VirtualFile virtualFile) {
        return new LocalFileRevision(virtualFile);
    }

    public String getHelpId() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
