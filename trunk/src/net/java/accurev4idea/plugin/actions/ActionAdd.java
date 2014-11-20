/*
 * ActionRevert.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jul 3, 2005, 7:06:20 AM
 */
package net.java.accurev4idea.plugin.actions;

import net.java.accurev4idea.api.AccuRev;
import net.java.accurev4idea.api.exceptions.AccuRevRuntimeException;
import net.java.accurev4idea.plugin.AccuRevVcs;
import net.java.accurev4idea.plugin.AccuRevToIdeaAdapter;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vcs.AbstractVcsHelper;
import com.intellij.openapi.vcs.FileStatus;
import com.intellij.openapi.vcs.FileStatusManager;

/**
 * Revert local changes to a selected file to "backed" version (i.e. stream version)
 *
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version $Id: ActionAdd.java,v 1.2 2006/06/20 19:55:19 ifedulov Exp $
 * @since 0.1
 */
public class ActionAdd extends ActionBase {
    /**
     * Log4j audit channel
     */
    private static Logger log = Logger.getLogger(ActionAbstractRevertTo.class);

    /**
     * @see net.java.accurev4idea.plugin.actions.ActionBase#performAction(com.intellij.openapi.actionSystem.AnActionEvent)
     */
    protected void performAction(AnActionEvent event) {
        // get necessary elements from event
        final VirtualFile virtualFile = ActionUtils.getVirtualFile(event);
        final Project project = ActionUtils.getProject(event);
        AccuRevVcs vcs = AccuRevVcs.getInstance(project);
        // extract file location that is being reverted
        final String location = virtualFile.getPresentableUrl();
        // show "OK/Cancel" dialog with confirmation question
        switch(Messages.showOkCancelDialog(project, "Add "+location+" to current workspace?", "Add file", Messages.getQuestionIcon())) {
            case 0: // "OK" was selected, revert changes and mark file as upto date
                if(log.isDebugEnabled()) {
                    log.debug("Adding ["+location+"] to AccuRev");
                }
                // rollback local changes
                final File file = new File(location);
                // do the actual "add"
                try {
                    AccuRev.addFile(file, vcs.getCommandExecListeners());
                } catch (AccuRevRuntimeException e) {
                    log.info(e.getLocalizedMessage(), e);
                    Messages.showErrorDialog(project, e.getCommandResult().getErr(), "Error During Add Operation");
                }
                // refresh the virtual file manager so the changes are loaded from the file system
                VirtualFileManager.getInstance().asyncRefresh(new Runnable() { //FIXED
                    public void run() {
                        AccuRevToIdeaAdapter.getInstance(project).refreshFileStatusCacheForFile(virtualFile.getParent());
                    }
                });
                // mark the file as upto date in VCS system
                //TODO AbstractVcsHelper.getInstance(project).markFileAsUpToDate(virtualFile);
                break;
            default:
                if(log.isDebugEnabled()) {
                    log.debug("Add for ["+location+"] was cancelled.");
                }
        }
    }

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