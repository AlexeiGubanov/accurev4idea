/*
 * ActionDiff.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jun 11, 2005, 3:56:32 PM
 */
package net.java.accurev4idea.plugin.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vcs.FileStatus;
import com.intellij.openapi.vcs.FileStatusManager;
import net.java.accurev4idea.plugin.AccuRevToIdeaAdapter;
import net.java.accurev4idea.plugin.gui.RevisionHistoryWindow;
import net.java.accurev4idea.plugin.components.DiffPanelDataElement;

/**
 * This {@link ActionBase} implementation handles diff of editor copy (local)
 * against list of revision history transactions.
 *
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version $Id: ActionRevisionHistory.java,v 1.1 2005/11/05 16:56:18 ifedulov Exp $
 * @since 0.1
 */
public class ActionRevisionHistory extends ActionBase {
    /**
     * Log4j audit channel
     */
    private static final Logger log = Logger.getInstance(ActionRevisionHistory.class.getName());

    /**
     * @see ActionBase#performAction(com.intellij.openapi.actionSystem.AnActionEvent)
     */
    protected void performAction(AnActionEvent event) {
        final VirtualFile virtualFile = ActionUtils.getVirtualFile(event);
        final Project project = ActionUtils.getProject(event);

        DiffPanelDataElement left = new DiffPanelDataElement();
        left.setTitle("AccuRev version");
        left.setDiffContent(AccuRevToIdeaAdapter.getInstance(project).getBackedDiffContent(virtualFile));
        DiffPanelDataElement right = new DiffPanelDataElement();
        right.setTitle("Local version");
        right.setDiffContent(AccuRevToIdeaAdapter.getInstance(project).getEditorDiffContent(virtualFile));

        RevisionHistoryWindow revisionHistoryWindow = new RevisionHistoryWindow(project, virtualFile, left, right);
        revisionHistoryWindow.setVisible(true);
    }

    /**
     * @see ActionBase#shouldSave()
     */
    protected boolean shouldSave() {
        return false;
    }

    /**
     * @see ActionBase#isEnabled(com.intellij.openapi.project.Project,com.intellij.openapi.vfs.VirtualFile)
     */
    protected boolean isEnabled(Project project, VirtualFile file) {
        return !FileStatus.UNKNOWN.equals(FileStatusManager.getInstance(project).getStatus(file));
    }
}
