package net.java.accurev4idea.plugin.providers;

//import com.intellij.openapi.vcs.RollbackProvider;
//import com.intellij.openapi.vcs.versions.AbstractRevisions;

/**
 * $Id: AccuRevRollbackProvider.java,v 1.1 2005/11/05 16:56:26 ifedulov Exp $
 * User: aantonov
 * Date: Nov 4, 2005
 * Time: 2:11:33 PM
 */
public class AccuRevRollbackProvider /*TODO implements RollbackProvider */ {

//    private AbstractRevisions[] selectedRevisions;
//
//    public AccuRevRollbackProvider(AbstractRevisions[] selectedRevisions) {
//        this.selectedRevisions = selectedRevisions;
//    }

    public boolean performRollback() {
        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isVisible() {
        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isEnabled() {
        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getActionName() {
        return "Revert";  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean requestConfirmation() {
        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
