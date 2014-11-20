/*
 * ActionDiff.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jun 11, 2005, 3:56:32 PM
 */
package net.java.accurev4idea.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.diff.BinaryContent;
import com.intellij.openapi.diff.DiffContent;
import com.intellij.openapi.diff.DiffManager;
import com.intellij.openapi.diff.DiffTool;
import com.intellij.openapi.diff.SimpleDiffRequest;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.vcs.FileStatus;
import com.intellij.openapi.vcs.FileStatusManager;
import net.java.accurev4idea.AccuRevVcs;
import net.java.accurev4idea.dao.AccuRevDAOFacade;
import org.apache.commons.lang.SystemUtils;

/**
 * This {@link ActionBase} implementation handles diff of editor (local) file against AccuRev workspace copy.
 *
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version $Id: ActionDiff.java,v 1.3 2005/07/12 00:44:46 ifedulov Exp $
 * @since 0.1
 */
public class ActionDiff extends ActionBase {
    /**
     * Log4j audit channel
     */
    private static final Logger log = Logger.getInstance(ActionDiff.class.getName());

    /**
     * @see ActionBase#performAction(com.intellij.openapi.actionSystem.AnActionEvent)
     */
    protected void performAction(AnActionEvent event) {
        final Project project = ActionUtils.getProject(event);
        final String editorContents = ActionUtils.getEditorContents(event);

        if (log.isDebugEnabled()) {
            log.debug("Editor contents [" + editorContents + "]");
        }
        if (editorContents == null) {
            return;
        }

        final AccuRevVcs vcs = ActionUtils.getAccuRevVcs(event);
        final VirtualFile virtualFile = ActionUtils.getVirtualFile(event);
        log.debug("Processing virtual file [" + virtualFile + "]");
        if (virtualFile != null && !virtualFile.isDirectory()) {
            showDiffDialog(virtualFile, editorContents, vcs, project);
        } else {
            Messages.showMessageDialog(project, "File [" + virtualFile + "] is either null or a directory", null, Messages.getWarningIcon());
        }
        // refresh file manager after returning from diff
        VirtualFileManager.getInstance().refresh(true);
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

    /**
     * Show "diff" dialog for given virtual virtualFile.
     *
     * @param virtualFile    the virtualFile that is being diffed
     * @param editorContents current edit contents, or filesystem contents if virtualFile is not in editor
     * @param vcs            reference to current VCS plugin (AccuRevVcs)
     * @param project        reference to current project
     */
    private static void showDiffDialog(VirtualFile virtualFile, String editorContents, AccuRevVcs vcs, Project project) {
        final AccuRevDAOFacade accuRevDAO = vcs.getAccuRevDAO();
        DiffContent workspaceFile = null;
        workspaceFile = accuRevDAO.getBackedDiffContent(virtualFile);
        BinaryContent localFile = new BinaryContent(editorContents.getBytes(), SystemUtils.FILE_ENCODING, workspaceFile.getContentType());
        showDiffDialog(project, virtualFile.getPresentableUrl(), workspaceFile, localFile);
    }

    /**
     * Show the "diff" window dialog
     *
     * @param project current {@link Project} reference
     * @param windowTitle the title for the diff window to show
     * @param workspaceFile the {@link com.intellij.openapi.diff.DiffContent} of the file from server
     * @param localFile the {@link com.intellij.openapi.diff.DiffContent} of the local filesystem file
     */
    private static void showDiffDialog(final Project project, final String windowTitle, final DiffContent workspaceFile, final DiffContent localFile) {
        SimpleDiffRequest diffRequest = new SimpleDiffRequest(project, windowTitle);
        diffRequest.setContentTitles("Workspace version", "Local version");
        diffRequest.setContents(workspaceFile, localFile);
        DiffTool diffTool = DiffManager.getInstance().getIdeaDiffTool();
        if (diffTool.canShow(diffRequest)) {
            diffTool.show(diffRequest);
        } else {
            log.error("Can't show diff request ["+diffRequest+"]");
            Messages.showMessageDialog(project, "Can't show diff request", "Can't show diff request", Messages.getWarningIcon());
        }
    }

}
