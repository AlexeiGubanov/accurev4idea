/*
 * ActionRevert.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jul 3, 2005, 7:06:20 AM
 */
package net.java.accurev4idea.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vcs.AbstractVcsHelper;
import com.intellij.openapi.vcs.FileStatus;
import com.intellij.openapi.vcs.FileStatusManager;
import com.intellij.openapi.vfs.VirtualFile;
import net.java.accurev4idea.AccuRevVcs;
import org.apache.log4j.Logger;

import java.io.File;

/**
 * Revert local changes to a selected file.
 *
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version $Id: ActionRevert.java,v 1.2 2005/07/12 00:56:36 ifedulov Exp $
 * @since 0.1
 */
public class ActionRevert extends ActionBase {
    /**
     * Log4j audit channel
     */
    private static Logger log = Logger.getLogger(ActionRevert.class);

    /**
     * @see ActionBase#performAction(com.intellij.openapi.actionSystem.AnActionEvent)
     */
    protected void performAction(AnActionEvent event) {
        // get necessary elements from event
        VirtualFile file = ActionUtils.getVirtualFile(event);
        Project project = ActionUtils.getProject(event);
        AccuRevVcs vcs = ActionUtils.getAccuRevVcs(event);
        // extract file location that is being reverted
        final String location = file.getPresentableUrl();
        // show "OK/Cancel" dialog with confirmation question
        switch(Messages.showOkCancelDialog(project, "Revert all local changes to "+location+"?", "Revert local changes", Messages.getQuestionIcon())) {
            case 0: // "OK" was selected, revert changes and mark file as upto date
                if(log.isDebugEnabled()) {
                    log.debug("Reverting local changes to ["+location+"]");
                }
                // rollback local changes
                vcs.getAccuRevDAO().rollbackLocalChanges(new File(location));
                // mark file as upto date
                AbstractVcsHelper.getInstance(project).markFileAsUpToDate(file);
                break;
            default:
                if(log.isDebugEnabled()) {
                    log.debug("Revert for ["+location+"] was cancelled.");
                }
        };
    }

    /**
     * @see ActionBase#shouldSave()
     */
    protected boolean shouldSave() {
        return true;
    }

    /**
     * @see ActionBase#isEnabled(com.intellij.openapi.project.Project, net.java.accurev4idea.AccuRevVcs, com.intellij.openapi.vfs.VirtualFile)
     */
    protected boolean isEnabled(Project project, AccuRevVcs vcs, VirtualFile file) {
        return !FileStatus.UNKNOWN.equals(FileStatusManager.getInstance(project).getStatus(file));
    }
}
