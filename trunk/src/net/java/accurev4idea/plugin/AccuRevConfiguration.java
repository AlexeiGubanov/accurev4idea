package net.java.accurev4idea.plugin;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.util.JDOMExternalizable;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.openapi.util.DefaultJDOMExternalizer;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.module.Module;
//import com.intellij.openapi.vcs.ModuleLevelVcsManager;
import org.jdom.Element;
import net.java.accurev4idea.api.AccuRev;
import net.java.accurev4idea.api.components.AccuRevInfo;

/**
 * $Id: AccuRevConfiguration.java,v 1.3 2006/02/21 22:52:56 ifedulov Exp $
 * User: aantonov
 * Date: Oct 25, 2005
 * Time: 10:48:14 AM
 */
public class AccuRevConfiguration implements ProjectComponent, JDOMExternalizable {

    // This is where we declare variables that would be externaly saved.
    //  For them to be saved, they must be public.
    public String accurevExecPath;
    public boolean showAccuRevOutput = true;
    public boolean enableAutoAdd = true;

    public static AccuRevConfiguration getInstance(Project project){
        return (AccuRevConfiguration) project.getComponent(AccuRevConfiguration.class);
    }

    public boolean isEnabled(Module module) {
//        ModuleLevelVcsManager moduleLevelVcsManager = ModuleLevelVcsManager.getInstance(module);
//        if (moduleLevelVcsManager == null) return false;
//        return moduleLevelVcsManager.getActiveVcs() instanceof AccuRevVcs;
        return true;
    }

    /**  **/
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

    public void setEnableAutoAdd(boolean selected) {
        this.enableAutoAdd = selected;
    }

    public boolean isAutoAddEnabled() {
        return enableAutoAdd;
    }

    /**  **/

    public void projectOpened() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void projectClosed() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getComponentName() {
        return "AccuRevConfiguration";
    }

    public void initComponent() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void disposeComponent() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void readExternal(Element element) throws InvalidDataException {
        DefaultJDOMExternalizer.readExternal(this, element);
    }

    public void writeExternal(Element element) throws WriteExternalException {
        DefaultJDOMExternalizer.writeExternal(this, element);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final AccuRevConfiguration that = (AccuRevConfiguration) o;

        if (showAccuRevOutput != that.showAccuRevOutput) return false;
        if (enableAutoAdd != that.enableAutoAdd) return false;
        if (!accurevExecPath.equals(that.accurevExecPath)) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (accurevExecPath != null) ? accurevExecPath.hashCode() : 0;
        result = 29 * result + (showAccuRevOutput ? 1 : 0);
        return result;
    }

}
