package net.java.accurev4idea.plugin.providers;

import com.intellij.openapi.vcs.CheckoutProvider;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import net.java.accurev4idea.plugin.gui.wizard.ProjectWizard;
import net.java.accurev4idea.plugin.AccuRevVcs;
import org.apache.log4j.Logger;

/**
 * $Id: AccuRevCheckoutProvider.java,v 1.2 2005/11/07 18:00:51 ifedulov Exp $
 * User: aantonov
 * Date: Oct 25, 2005
 * Time: 6:03:33 PM
 */
public class AccuRevCheckoutProvider implements CheckoutProvider {

    private static final Logger log = Logger.getLogger(AccuRevCheckoutProvider.class);

    public void doCheckout() {
        final Project project = ProjectManager.getInstance().getDefaultProject();

        AccuRevVcs vcs = AccuRevVcs.getInstance(project);

        ProjectWizard pw = new ProjectWizard(vcs);
        int i = pw.showModalDialog();
        log.info("Checkout done with " + i);
    }

    public String getVcsName() {
        return "_AccuRev";
    }

    public String getComponentName() {
        return "AccuRevCheckoutProvider";
    }

    public void initComponent() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void disposeComponent() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
