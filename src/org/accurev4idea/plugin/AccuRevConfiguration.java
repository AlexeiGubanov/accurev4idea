package org.accurev4idea.plugin;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StoragePathMacros;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.DefaultJDOMExternalizer;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.JDOMExternalizable;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
  name = "AccuRevConfiguration",
  storages = { @Storage(id = "default", file = StoragePathMacros.WORKSPACE_FILE) }
)
public class AccuRevConfiguration implements ProjectComponent, PersistentStateComponent<AccuRevConfiguration> {

  private String accurevExecPath;
  private boolean showAccuRevOutput = true;
  private boolean enableAutoAdd = true;

  public String getAccurevExecPath() {
    return accurevExecPath;
  }

  public void setAccurevExecPath(String accurevExecPath) {
    this.accurevExecPath = accurevExecPath;
  }

  public boolean isShowAccuRevOutput() {
    return showAccuRevOutput;
  }

  public void setShowAccuRevOutput(boolean showAccuRevOutput) {
    this.showAccuRevOutput = showAccuRevOutput;
  }

  public boolean isEnableAutoAdd() {
    return enableAutoAdd;
  }

  public void setEnableAutoAdd(boolean enableAutoAdd) {
    this.enableAutoAdd = enableAutoAdd;
  }

  @Override
  public void projectOpened() {
    // foo
  }

  @Override
  public void projectClosed() {
    // foo
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    AccuRevConfiguration that = (AccuRevConfiguration) o;

    if (enableAutoAdd != that.enableAutoAdd) return false;
    if (showAccuRevOutput != that.showAccuRevOutput) return false;
    if (accurevExecPath != null ? !accurevExecPath.equals(that.accurevExecPath) : that.accurevExecPath != null)
      return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = accurevExecPath != null ? accurevExecPath.hashCode() : 0;
    result = 31 * result + (showAccuRevOutput ? 1 : 0);
    result = 31 * result + (enableAutoAdd ? 1 : 0);
    return result;
  }

  @Nullable
  @Override
  public AccuRevConfiguration getState() {
    return this;
  }

  @Override
  public void loadState(AccuRevConfiguration state) {
    XmlSerializerUtil.copyBean(state, this);
  }
}
