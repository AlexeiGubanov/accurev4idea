/*
 * ActionKeepProject.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jun 11, 2005, 2:58:10 PM
 */
package net.java.accurev4idea.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.AbstractVcsHelper;
import com.intellij.openapi.vcs.CheckinProjectDialogImplementer;
import com.intellij.openapi.vcs.VcsManager;
import com.intellij.openapi.vfs.VirtualFile;
import net.java.accurev4idea.AccuRevVcs;
import org.apache.log4j.Logger;

/**
 * This action handles the "Commit Project..." dialog
 *
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version $Id: ActionCommitProject.java,v 1.3 2005/07/12 00:57:18 ifedulov Exp $
 * @since 0.1
 */
public class ActionCommitProject extends ActionBase {
    /**
     * Log4j audit channel
     */
    private static Logger log = Logger.getLogger(ActionCommitProject.class);

    public void performAction(AnActionEvent event) {
        commitProject(ActionUtils.getAccuRevVcs(event), ActionUtils.getProject(event));
    }

    protected boolean shouldSave() {
        return true;
    }

    protected boolean isEnabled(Project project, AccuRevVcs vcs, VirtualFile file) {
        return false;
    }

    private void commitProject(AccuRevVcs vcs, final Project project) {
        final CheckinProjectDialogImplementer checkinProjectDialogImplementer =
                VcsManager
                .getInstance(project)
                .createCheckinProjectDialog();

        Runnable runnable = new Runnable() {
            public void run() {
                checkinProjectDialogImplementer.show();
                if (!checkinProjectDialogImplementer.isOK()) {
                    return;
                } else {
                    AbstractVcsHelper.getInstance(project).doCheckinProject(checkinProjectDialogImplementer.getCheckinProjectPanel(), checkinProjectDialogImplementer.getPreparedComment());
                    return;
                }
            }
        };
        // "true" for "async" processing
        checkinProjectDialogImplementer.analyzeChanges(true, runnable);
    }
}
