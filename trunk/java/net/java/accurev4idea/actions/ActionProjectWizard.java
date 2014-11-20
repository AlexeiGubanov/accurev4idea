/*
 * ActionProjectWizard.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jun 19, 2005, 9:55:27 AM
 */
package net.java.accurev4idea.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.vfs.VirtualFile;
import com.nexes.wizard.Wizard;
import net.java.accurev4idea.AccuRevVcs;
import net.java.accurev4idea.gui.wizard.ProjectWizard;
import org.apache.log4j.Logger;
/**
 * This action handles the "AccuRev Project Wizard" action from "File" menu list.
 *
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version $Id: ActionProjectWizard.java,v 1.5 2005/07/12 18:33:31 ifedulov Exp $
 * @since 0.1
 */
public class ActionProjectWizard extends ActionBase {
    /**
     * Log4j audit channel
     */
    private static Logger log = Logger.getLogger(ActionProjectWizard.class);

    protected void performAction(AnActionEvent event) {
        Project project = ActionUtils.getProject(event);
        // following line has a great potential for NPE because
        // there will be no project available when you create one
        // from scratch using wizard, thus project.getXXXX will throw NPE
        AccuRevVcs accurevVcs = (AccuRevVcs) project.getComponent(AccuRevVcs.class);

        //To change body of implemented methods use File | Settings | File Templates.
        //Messages.showMessageDialog("This feature is not implemented yet", "Not implemented yet", Messages.getInformationIcon());

        /*DialogWrapper wizardDialog = new ProjectWizardPanel(project, accurevVcs);
        wizardDialog.show();*/
        Wizard wizard = new ProjectWizard(project, accurevVcs);
        int ret = wizard.showModalDialog();
    }

    protected boolean shouldSave() {
        return false;
    }

    protected boolean isEnabled(Project project, AccuRevVcs vcs, VirtualFile file) {
        return true;
    }
}
