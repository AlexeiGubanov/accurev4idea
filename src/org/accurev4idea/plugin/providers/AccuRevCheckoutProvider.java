package org.accurev4idea.plugin.providers;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.CheckoutProvider;
import org.accurev4idea.plugin.AccuRevVcs;
import org.accurev4idea.plugin.gui.wizard.ProjectWizard;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class AccuRevCheckoutProvider implements CheckoutProvider, com.intellij.openapi.components.ApplicationComponent {

  private static final Logger log = Logger.getLogger(AccuRevCheckoutProvider.class);

  @Override
  public void doCheckout(Project project, Listener listener) {
    AccuRevVcs vcs = project.getComponent(AccuRevVcs.class);
    ProjectWizard pw = new ProjectWizard(vcs);
    int i = pw.showModalDialog();
    log.info("Checkout done with " + i);
  }

  @Override
  public String getVcsName() {
    return AccuRevVcs.NAME;
  }

  @Override
  public void initComponent() {
    // foo
  }

  @Override
  public void disposeComponent() {
    // foo

  }

  @NotNull
  @Override
  public String getComponentName() {
    return this.getClass().getSimpleName();
  }
}
