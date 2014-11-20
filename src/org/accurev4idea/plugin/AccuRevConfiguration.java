package org.accurev4idea.plugin;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.RoamingType;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
  name = "AccuRevConfiguration",
  //@Storage(file = "$APP_CONFIG$/vcs.xml"),
  storages = { @Storage(file = "$APP_CONFIG$/accurev.xml", roamingType = RoamingType.PER_PLATFORM)})
public class AccuRevConfiguration implements PersistentStateComponent<AccuRevConfiguration> {

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
    if (state == null) {
      state = new AccuRevConfiguration();
    }
    XmlSerializerUtil.copyBean(state, this);
  }

}
