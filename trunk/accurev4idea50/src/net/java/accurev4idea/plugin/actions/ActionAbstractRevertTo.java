/*
 * ActionRevert.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jul 3, 2005, 7:06:20 AM
 */
package net.java.accurev4idea.plugin.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vcs.AbstractVcsHelper;
import com.intellij.openapi.vcs.FileStatus;
import com.intellij.openapi.vcs.FileStatusManager;
import com.intellij.openapi.vfs.*;
import com.intellij.vcsUtil.VcsUtil;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.List;

import net.java.accurev4idea.api.AccuRev;
import net.java.accurev4idea.plugin.AccuRevVcs;

/**
 * Revert local changes to a selected file.
 *
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version $Id: ActionAbstractRevertTo.java,v 1.2 2005/11/07 00:57:38 ifedulov Exp $
 * @since 0.1
 */
public abstract class ActionAbstractRevertTo extends ActionBase {
    /**
     * Log4j audit channel
     */
    private static Logger log = Logger.getLogger(ActionAbstractRevertTo.class);

    /**
     * @see net.java.accurev4idea.plugin.actions.ActionBase#performAction(com.intellij.openapi.actionSystem.AnActionEvent)
     */
    protected void performAction(AnActionEvent event) {
        // get necessary elements from event
        VirtualFile virtualFile = ActionUtils.getVirtualFile(event);
        Project project = ActionUtils.getProject(event);
        AccuRevVcs vcs = AccuRevVcs.getInstance(project);
        // extract file location that is being reverted
        final String location = virtualFile.getPresentableUrl();
        // show "OK/Cancel" dialog with confirmation question
        switch(Messages.showOkCancelDialog(project, "Revert changes to "+location+"?", "Revert local changes", Messages.getQuestionIcon())) {
            case 0: // "OK" was selected, revert changes and mark file as upto date
                if(log.isDebugEnabled()) {
                    log.debug("Reverting local changes for ["+location+"]");
                }
                // rollback local changes
                final File file = new File(location);
                // do the actual revert
                performRevertTo(file, vcs.getCommandExecListeners());
                // refresh the virtual file manager so the changes are loaded from the file system
                VirtualFileManager.getInstance().refresh(true);
                // mark the file as upto date in VCS system 
                AbstractVcsHelper.getInstance(project).markFileAsUpToDate(virtualFile);
                break;
            default:
                if(log.isDebugEnabled()) {
                    log.debug("Revert for ["+location+"] was cancelled.");
                }
        }
    }

    /**
     * Let subclasses perform proper action
     *
     * @param file
     */
    protected abstract void performRevertTo(File file, List listeners);

    /**
     * @see net.java.accurev4idea.plugin.actions.ActionBase#shouldSave()
     */
    protected boolean shouldSave() {
        return true;
    }

    /**
     * @see net.java.accurev4idea.plugin.actions.ActionBase#isEnabled(com.intellij.openapi.project.Project,com.intellij.openapi.vfs.VirtualFile)
     */
    protected boolean isEnabled(Project project, VirtualFile file) {
        return !FileStatus.UNKNOWN.equals(FileStatusManager.getInstance(project).getStatus(file));
    }
}
