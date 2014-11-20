package org.accurev4idea.plugin.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import net.java.accurev4idea.api.exceptions.AccuRevRuntimeException;

public abstract class ActionBase extends AnAction {

  private static final Logger log = Logger.getInstance(ActionBase.class.getName());

  @Override
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
