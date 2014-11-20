package org.accurev4idea.plugin.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.FileStatus;
import com.intellij.openapi.vcs.FileStatusManager;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.log4j.Logger;

public class ActionAdd extends ActionBase {

  private static Logger log = Logger.getLogger(ActionAdd.class);


  @Override
  protected void performAction(AnActionEvent event) {
    //TODO
  }

  @Override
  protected boolean shouldSave() {
    return true;
  }

  @Override
  protected boolean isEnabled(Project project, VirtualFile file) {
    return !FileStatus.UNKNOWN.equals(FileStatusManager.getInstance(project).getStatus(file));
  }
}
