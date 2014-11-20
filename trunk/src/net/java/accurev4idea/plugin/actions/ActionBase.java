/*
 * ActionBase.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jun 11, 2005, 2:56:09 PM
 */
package net.java.accurev4idea.plugin.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import net.java.accurev4idea.api.exceptions.AccuRevRuntimeException;
import net.java.accurev4idea.plugin.AccuRevVcs;

/**
 * This is class should be used as a basis for every action handler.
 *
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version $Id: ActionBase.java,v 1.1 2005/11/05 16:56:16 ifedulov Exp $
 * @since 0.1
 */
public abstract class ActionBase extends AnAction {
    /**
     * Log4j audit channel
     */
    private static final Logger log = Logger.getInstance(ActionBase.class.getName());

    /**
     * @see com.intellij.openapi.actionSystem.AnAction#actionPerformed(com.intellij.openapi.actionSystem.AnActionEvent)
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
            Messages.showErrorDialog(e.getLocalizedMessage(), e.getCommandResult().getCommand().toString());
        } catch (RuntimeException e) {
            // Log the error and full exception stack trace
            log.error(e.getLocalizedMessage(), e);
            Messages.showErrorDialog(e.getLocalizedMessage(), getClass().getName());
        }
        // refresh file manager after returning from perform action
        // VirtualFileManager.getInstance().refresh(true);
    }

    /**
     * Override for {@link com.intellij.openapi.actionSystem.AnAction#update(com.intellij.openapi.actionSystem.AnActionEvent)}, handles
     * presentation visibility of the menu entry corresponding to the action.
     *
     * @param event
     */
    public void update(AnActionEvent event) {
        super.update(event);

        Presentation presentation = event.getPresentation();
        presentation.setVisible(true);
        presentation.setEnabled(true);
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
     * @param file
     * @return
     */
    protected abstract boolean isEnabled(Project project, VirtualFile file);

}
