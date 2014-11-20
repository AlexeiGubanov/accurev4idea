package org.accurev4idea.plugin;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.AbstractVcs;
import com.intellij.openapi.vcs.ProjectLevelVcsManager;
import com.intellij.openapi.vcs.VcsException;
import org.accurev4idea.plugin.gui.AccuRevConfigurable;
import org.jetbrains.annotations.NotNull;

public class AccuRevVcs extends AbstractVcs implements ProjectComponent {

  public static String NAME = "AccuRev";

  private AccuRevConfigurable configurable;
  private AccuRevConfiguration configuration;

  public AccuRevVcs(Project project, AccuRevConfiguration configuration) {
    super(project, NAME);
    this.configuration = configuration;
  }

  @Override
  public String getDisplayName() {
    return NAME;
  }

  @Override
  public Configurable getConfigurable() {
    return configurable;
  }

  public static AccuRevVcs getInstance(Project project) {
    return project != null && !project.isDisposed()?(AccuRevVcs) ProjectLevelVcsManager.getInstance(project).findVcsByName(NAME):null;
  }

  @Override
  public void projectOpened() {
    // Register the Content Pane to keep track of the command history
    // TODO registerToolWindow();
  }

  @Override
  public void projectClosed() {
    // Upon closing the project need to deregister the Content Pane
    // TODO unregisterToolWindow();
  }

  @Override
  public void initComponent() {
    //foo
  }

  @Override
  public void disposeComponent() {
    //TODO
  }

  @NotNull
  @Override
  public String getComponentName() {
    return this.getClass().getSimpleName();
  }

  @Override
  protected void start() throws VcsException {
    configurable = new AccuRevConfigurable(myProject, configuration);
    super.start();
  }

  @Override
  protected void shutdown() throws VcsException {
    configurable = null;
    super.shutdown();
  }
}
