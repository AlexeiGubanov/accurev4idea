package net.java.accurev4idea.plugin.gui.wizard;

import com.nexes.wizard.Wizard;
import com.nexes.wizard.WizardPanelDescriptor;

import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.File;

import net.java.accurev4idea.plugin.gui.DepotPanel;
import net.java.accurev4idea.plugin.gui.NewWorkspacePanel;
import net.java.accurev4idea.plugin.AccuRevVcs;
import net.java.accurev4idea.api.components.Stream;
import net.java.accurev4idea.api.components.Locking;
import net.java.accurev4idea.api.Newline;

/**
 * $Id: ProjectWizard.java,v 1.2 2005/11/07 18:00:49 ifedulov Exp $
 * User: aantonov
 * Date: Jul 12, 2005
 * Time: 12:07:12 PM
 */
public class ProjectWizard extends Wizard {

    private DepotPanel depot;
    private NewWorkspacePanel workspace;

    public ProjectWizard(AccuRevVcs vcs) {
        super();

        depot = new DepotPanel(vcs);
        workspace = new NewWorkspacePanel();

        getDialog().setTitle("Accurev Project Creation Wizard");

        WizardPanelDescriptor depot = new DepotPanelDescriptor();

        WizardPanelDescriptor workspace = new NewWorkspacePanelDescriptor();

        registerWizardPanel(DepotPanelDescriptor.IDENTIFIER, depot);
        registerWizardPanel(NewWorkspacePanelDescriptor.IDENTIFIER, workspace);

        setCurrentPanel(DepotPanelDescriptor.IDENTIFIER);
        setNextFinishButtonEnabled(false);
    }

    public int showModalDialog() {
        return super.showModalDialog();
    }

    private void handleStreamSelection(Stream stream) {
        if (stream.isWorkspace()) {
            //Messages.showInfoMessage("Ready to download files from workspace...", "Stream Selection");
        } else {
            //Messages.showInfoMessage("Ready to create a new workspace...", "Stream Selection");
        }
    }

    private void handleWorkspaceCreation(Stream stream, File file, String workspaceName,
                                         Newline newline, Locking locking) {
        //Messages.showInfoMessage(workspaceName, "Stream Selection");

        //Workspace newWorkspace = accurev.createWorkspace(stream, file, workspaceName, newline, locking);
    }

    // Inner Private descriptor classes

    private class DepotPanelDescriptor extends WizardPanelDescriptor implements TreeSelectionListener {
        public static final String IDENTIFIER = "DEPOT_PANEL";

        public DepotPanelDescriptor() {
            super(IDENTIFIER, depot.getComponent());
            depot.addTreeSelectionListener(this);
        }

        public Object getNextPanelDescriptor() {
            return NewWorkspacePanelDescriptor.IDENTIFIER;
        }

        public Object getBackPanelDescriptor() {
            return null;
        }

        public void valueChanged(TreeSelectionEvent e) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                           e.getPath().getLastPathComponent();

            if (node == null) return;

            Object selected = node.getUserObject();
            if (selected != null) {
                if (selected.getClass().isAssignableFrom(Stream.class)) {
                    setNextFinishButtonEnabled(true);
                } else {
                    setNextFinishButtonEnabled(false);
                }
            } else {
                setNextFinishButtonEnabled(false);
            }
        }
    }

    private class NewWorkspacePanelDescriptor extends WizardPanelDescriptor {
        public static final String IDENTIFIER = "NEW_WORKSPACE_PANEL";

        public NewWorkspacePanelDescriptor() {
            super(IDENTIFIER, workspace.getComponent());
        }

        public Object getNextPanelDescriptor() {
            return FINISH;
        }

        public Object getBackPanelDescriptor() {
            return DepotPanelDescriptor.IDENTIFIER;
        }

        public void aboutToDisplayPanel() {
            DefaultMutableTreeNode selected = (DefaultMutableTreeNode)
                    depot.getSelectedTreeNode();
            if (selected != null) {
                if (selected.getUserObject().getClass().isAssignableFrom(Stream.class)) {
                    handleStreamSelection((Stream) selected.getUserObject());
                }
            }
            //setNextFinishButtonEnabled(false);
        }

        public void aboutToHidePanel() {
            DefaultMutableTreeNode selected = (DefaultMutableTreeNode)
                    depot.getSelectedTreeNode();
            if (selected != null) {
                if (selected.getUserObject().getClass().isAssignableFrom(Stream.class) &&
                        workspace.getWorkspaceStorageDirectory() != null) {
                    handleWorkspaceCreation((Stream) selected.getUserObject(),
                            new File(workspace.getWorkspaceStorageDirectory()),
                            workspace.getNewWorkspaceName(),
                            workspace.getNewWorkspaceNewline(),
                            workspace.getNewWorkspaceLocking());
                }
            }
        }
    }
}