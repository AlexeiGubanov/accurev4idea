/*
 * ActionDiffAgainstBacked.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jun 11, 2005, 3:56:32 PM
 */
package net.java.accurev4idea.plugin.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diff.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vcs.FileStatus;
import com.intellij.openapi.vcs.FileStatusManager;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.log4j.Logger;

/**
 * This {@link ActionBase} implementation handles diff of editor (local) file against AccuRev workspace copy.
 *
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version $Id: ActionAbstractDiffAgainst.java,v 1.2 2005/11/10 23:25:04 ifedulov Exp $
 * @since 0.1
 */
public abstract class ActionAbstractDiffAgainst extends ActionBase {
    /**
     * Log4j audit channel
     */
    private static final Logger log = Logger.getLogger(ActionAbstractDiffAgainst.class);

    /**
     * @see ActionBase#performAction(com.intellij.openapi.actionSystem.AnActionEvent)
     */
    protected void performAction(AnActionEvent event) {
        final Project project = ActionUtils.getProject(event);
        final VirtualFile virtualFile = ActionUtils.getVirtualFile(event);
        if (virtualFile == null) {
            return;
        }

        if (!virtualFile.isDirectory()) {
            showDiffDialog(virtualFile, project);
        } else {
            Messages.showMessageDialog(project, "File [" + virtualFile + "] is either null or a directory", null, Messages.getWarningIcon());
        }
    }

    /**
     * @see ActionBase#shouldSave()
     */
    protected boolean shouldSave() {
        return true;
    }

    /**
     * @see ActionBase#isEnabled(com.intellij.openapi.project.Project,com.intellij.openapi.vfs.VirtualFile)
     */
    protected boolean isEnabled(Project project, VirtualFile file) {
        return !FileStatus.UNKNOWN.equals(FileStatusManager.getInstance(project).getStatus(file));
    }

    /**
     * Show "diff" dialog for given virtual virtualFile.
     *
     * @param virtualFile    the virtualFile that is being diffed
     * @param project        reference to current project
     */
    private void showDiffDialog(VirtualFile virtualFile, Project project) {
        DiffContent remoteFile = getRemoteDiffContent(project, virtualFile);
        DiffContent localFile = FileContent.fromFile(project, virtualFile);
        showDiffDialog(project, virtualFile.getPresentableUrl(), remoteFile, localFile);
    }

    /**
     * Obtain a copy of DiffContent for given VirtualFile
     *
     * @return DiffContent for given VirtualFile
     */
    protected abstract DiffContent getRemoteDiffContent(Project project, VirtualFile file);

    /**
     * Show the "diff" window dialog
     *
     * @param project current {@link com.intellij.openapi.project.Project} reference
     * @param windowTitle the title for the diff window to show
     * @param workspaceFile the {@link com.intellij.openapi.diff.DiffContent} of the file from server
     * @param localFile the {@link com.intellij.openapi.diff.DiffContent} of the local filesystem file
     */
    public static void showDiffDialog(final Project project, final String windowTitle, final DiffContent workspaceFile, final DiffContent localFile) {
        SimpleDiffRequest diffRequest = new SimpleDiffRequest(project, windowTitle);
        diffRequest.setContentTitles("AccuRev version", "Local version");
        diffRequest.setContents(workspaceFile, localFile);
        DiffTool diffTool = DiffManager.getInstance().getIdeaDiffTool();
        if (diffTool.canShow(diffRequest)) {
            diffTool.show(diffRequest);
        } else {
            log.error("Can't show diff request ["+diffRequest+"]");
            Messages.showWarningDialog("Can't show diff request", "Can't show diff request");
        }
    }
}
