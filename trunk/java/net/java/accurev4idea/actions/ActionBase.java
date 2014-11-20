/*
 * ActionBase.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jun 11, 2005, 2:56:09 PM
 */
package net.java.accurev4idea.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.AbstractVcs;
import com.intellij.openapi.vcs.VcsManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import net.java.accurev4idea.AccuRevVcs;
import net.java.accurev4idea.gui.GuiUtils;
import net.java.savant.plugin.scm.accurev.exceptions.AccuRevRuntimeException;

/**
 * This is class should be used as a basis for every action handler.
 *
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version $Id: ActionBase.java,v 1.4 2005/07/12 00:44:45 ifedulov Exp $
 * @since 0.1
 */
public abstract class ActionBase extends AnAction {
    /**
     * Log4j audit channel
     */
    private static final Logger log = Logger.getInstance(ActionBase.class.getName());

    /**
     * @see AnAction#actionPerformed(com.intellij.openapi.actionSystem.AnActionEvent)
     */
    public void actionPerformed(AnActionEvent event) {
        log.debug("Call to actionPerformed with [" + event + "]");
        if (shouldSave()) {
            ApplicationManager.getApplication().runWriteAction(new Runnable() {
                public void run() {
                    FileDocumentManager.getInstance().saveAllDocuments();
                }
            });
        }
        // delegate to subclass to perform the actual action
        try {
            performAction(event);
        } catch (AccuRevRuntimeException e) {
            // Log the error and full exception stack trace
            log.error(e.getLocalizedMessage(), e);
            GuiUtils.showErrorMessage(e);
        } catch (RuntimeException e) {
            // Log the error and full exception stack trace
            log.error(e.getLocalizedMessage(), e);
            GuiUtils.showErrorMessage(e);
        }
        // refresh file manager after returning from perform action
        VirtualFileManager.getInstance().refresh(true);
    }

    /**
     * Override for {@link AnAction#update(com.intellij.openapi.actionSystem.AnActionEvent)}, handles
     * presentation visibility of the menu entry corresponding to the action.
     *
     * @param event
     */
    public void update(AnActionEvent event) {
        super.update(event);

        Presentation presentation = event.getPresentation();

        final Project project = ActionUtils.getProject(event);
        if (project == null) {
            presentation.setEnabled(false);
            presentation.setVisible(false);
            return;
        }

        final VcsManager manager = VcsManager.getInstance(project);
        if (manager == null) {
            presentation.setEnabled(false);
            presentation.setVisible(false);
            return;
        }

        final AbstractVcs activeVcs = manager.getActiveVcs();
        if (activeVcs == null || !activeVcs.getClass().isAssignableFrom(AccuRevVcs.class)) {
            presentation.setEnabled(false);
            presentation.setVisible(false);
            return;
        }

        final VirtualFile file = ActionUtils.getVirtualFile(event);
        if (file == null) {
            presentation.setEnabled(false);
            presentation.setVisible(true);
            return;
        }

        presentation.setEnabled(isEnabled(project, (AccuRevVcs) activeVcs, file));
        presentation.setVisible(true);
    }


    /**
     * Subclasses should implement this method as they would implement the {@link #actionPerformed(com.intellij.openapi.actionSystem.AnActionEvent)}
     *
     * @param event
     */
    protected abstract void performAction(AnActionEvent event);

    /**
     * Subclasses should return either true or false. if true is returned all files
     * will be saved prior to running the subclass action impl.
     *
     * @return
     */
    protected abstract boolean shouldSave();

    /**
     * Subclasses should return either true or false based on given arguments. If false
     * is returned corresponing action item will be grayed out in selection menu.
     *
     * @param project
     * @param vcs
     * @param file
     * @return
     */
    protected abstract boolean isEnabled(Project project, AccuRevVcs vcs, VirtualFile file);

}
