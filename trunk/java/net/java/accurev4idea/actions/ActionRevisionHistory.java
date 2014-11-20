/*
 * ActionDiff.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jun 11, 2005, 3:56:32 PM
 */
package net.java.accurev4idea.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vcs.FileStatus;
import com.intellij.openapi.vcs.FileStatusManager;
import net.java.accurev4idea.AccuRevVcs;
import net.java.accurev4idea.components.DiffPanelDataElement;
import net.java.accurev4idea.gui.RevisionHistoryWindow;

/**
 * This {@link ActionBase} implementation handles diff of editor copy (local)
 * against list of revision history transactions.
 *
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version $Id: ActionRevisionHistory.java,v 1.6 2005/07/12 00:44:47 ifedulov Exp $
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
        AccuRevVcs vcs = ActionUtils.getAccuRevVcs(event);

        DiffPanelDataElement left = new DiffPanelDataElement();
        left.setTitle("AccuRev version");
        left.setDiffContent(vcs.getAccuRevDAO().getBackedDiffContent(virtualFile));
        DiffPanelDataElement right = new DiffPanelDataElement();
        right.setTitle("Local version");
        right.setDiffContent(vcs.getAccuRevDAO().getEditorDiffContent(virtualFile));

        RevisionHistoryWindow revisionHistoryWindow = new RevisionHistoryWindow(vcs.getAccuRevDAO(), project, virtualFile, left, right);
        revisionHistoryWindow.setVisible(true);
    }

    /**
     * @see ActionBase#shouldSave()
     */
    protected boolean shouldSave() {
        return false;
    }

    /**
     * @see ActionBase#isEnabled(com.intellij.openapi.project.Project, net.java.accurev4idea.AccuRevVcs, com.intellij.openapi.vfs.VirtualFile)
     */
    protected boolean isEnabled(Project project, AccuRevVcs vcs, VirtualFile file) {
        return !FileStatus.UNKNOWN.equals(FileStatusManager.getInstance(project).getStatus(file));
    }
}
