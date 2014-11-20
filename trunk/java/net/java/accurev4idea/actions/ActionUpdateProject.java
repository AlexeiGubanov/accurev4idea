/*
 * ActionUpdateProject.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jun 19, 2005, 8:43:26 AM
 */
package net.java.accurev4idea.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.ui.Messages;
import net.java.accurev4idea.AccuRevVcs;
import net.java.accurev4idea.dao.AccuRevDAOFacade;
import net.java.savant.plugin.scm.accurev.components.Workspace;
import net.java.savant.plugin.scm.accurev.components.AccuRevFile;
import net.java.savant.plugin.scm.accurev.exceptions.WorkspaceUpdateException;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.List;
import java.util.Iterator;

/**
 * This class handles the "accurev update" menu action.
 * 
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version $Id: ActionUpdateProject.java,v 1.2 2005/07/12 00:44:47 ifedulov Exp $
 * @since 0.1
 */
public class ActionUpdateProject extends ActionBase {
    /**
     * Log4j audit channel
     */
    private static Logger log = Logger.getLogger(ActionUpdateProject.class);

    protected void performAction(AnActionEvent event) {
        AccuRevVcs vcs = ActionUtils.getAccuRevVcs(event);
        VirtualFile file = ActionUtils.getVirtualFile(event);
        AccuRevDAOFacade dao = vcs.getAccuRevDAO();
        Workspace ws = dao.getWorkspaceByFileForCurrentPrincipal(new File(file.getPresentableUrl()));
        try {
            dao.updateWorkspace(ws.getName());
        } catch (WorkspaceUpdateException e) {
            // Log the error and full exception stack trace
            log.error(e.getLocalizedMessage(), e);
            // TODO: Show list of files in better format
            StringBuffer errorMessage = new StringBuffer().append(e.getLocalizedMessage()).append('\n');
            List files = e.getModifiedButNotKeptFiles();
            for (Iterator i = files.iterator(); i.hasNext();) {
                AccuRevFile accuRevFile = (AccuRevFile) i.next();
                errorMessage.append('[').append(accuRevFile.getAbsolutePath()).append(']').append('\n');
            }
            Messages.showErrorDialog(errorMessage.toString(), "AccuRev Plugin Error");
        }
    }

    protected boolean shouldSave() {
        return false;
    }

    protected boolean isEnabled(Project project, AccuRevVcs vcs, VirtualFile file) {
        return true;
    }
}
