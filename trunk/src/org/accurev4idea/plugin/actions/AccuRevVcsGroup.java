package org.accurev4idea.plugin.actions;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.AbstractVcs;
import com.intellij.openapi.vcs.actions.StandardVcsGroup;
import org.accurev4idea.plugin.AccuRevVcs;


public class AccuRevVcsGroup extends StandardVcsGroup {

  @Override
  public AbstractVcs getVcs(Project project) {
    return AccuRevVcs.getInstance(project);
  }


}
