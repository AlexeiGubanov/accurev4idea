package net.java.accurev4idea.plugin.gui;

import net.java.accurev4idea.api.exceptions.AccuRevRuntimeException;
import net.java.accurev4idea.api.components.Depot;
import net.java.accurev4idea.api.components.AccuRevInfo;
import net.java.accurev4idea.api.AccuRev;
import net.java.accurev4idea.plugin.gui.renderers.DepotTreeCellRenderer;
import net.java.accurev4idea.plugin.gui.listeners.DepotTreeListener;
import net.java.accurev4idea.plugin.gui.listeners.StreamOptionsPopupListener;
import net.java.accurev4idea.plugin.AccuRevVcs;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.event.TreeSelectionListener;
import java.util.List;
import java.util.Iterator;
import java.util.Collections;
import java.util.Comparator;
import java.awt.*;
import java.awt.event.MouseListener;

import com.intellij.openapi.ui.Messages;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.GridConstraints;
import org.apache.log4j.Logger;

/**
 * $Id: DepotPanel.java,v 1.3 2005/11/07 18:32:33 ifedulov Exp $
 * User: aantonov
 * Date: Nov 3, 2005
 * Time: 3:42:21 PM
 */
public class DepotPanel {
    private static final Logger log = Logger.getLogger(DepotPanel.class);

    private JPanel mainPanel;
    private JTree depotTree;

    private DefaultTreeModel treeModel;
    private AccuRevVcs vcs;

    public DepotPanel(AccuRevVcs vcs) {
        this.vcs = vcs;

        DefaultMutableTreeNode rootNode = initializeDepotList();
        treeModel = new DefaultTreeModel(rootNode);

        decorateDepotTree(depotTree);
    }

    public JComponent getComponent() {
        return mainPanel;
    }

    protected JTree getWrappedJTree() {
        return depotTree;
    }

    /**
     * Delegates a call to an underlying JTree object.getSelectedPath().getLastPathComponent()
     * If the selected path is null, then null is returned, else returns the mapping object.
     *
     */
    public Object getSelectedTreeNode() {
        if (depotTree.getSelectionPath() != null) {
            return depotTree.getSelectionPath().getLastPathComponent();
        }
        return null;
    }

    public void addTreeSelectionListener(TreeSelectionListener tsl) {
        depotTree.addTreeSelectionListener(tsl);
    }

    public void removeTreeSelectionListener(TreeSelectionListener tsl) {
        depotTree.removeTreeSelectionListener(tsl);
    }

    public void addMouseListener(MouseListener ml) {
        depotTree.addMouseListener(ml);
    }

    public void removeMouseListener(MouseListener ml) {
        depotTree.removeMouseListener(ml);
    }

    private void decorateDepotTree(JTree tree) {
        tree.setModel(treeModel);

        DefaultTreeCellRenderer renderer = new DepotTreeCellRenderer();

        depotTree.setCellRenderer(renderer);

        depotTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        depotTree.setShowsRootHandles(true);
        depotTree.setRootVisible(true);

        DepotTreeListener listener = new DepotTreeListener(depotTree,
                (DefaultMutableTreeNode) treeModel.getRoot());
        depotTree.addTreeSelectionListener(listener);
        depotTree.addTreeWillExpandListener(listener);

        //StreamOptionsPopupListener pListener = new StreamOptionsPopupListener();
        //depotTree.addMouseListener(pListener);
    }

    private DefaultMutableTreeNode initializeDepotList() {
        List depots = Collections.EMPTY_LIST;
        try {
            depots = AccuRev.getDepots();
            Collections.sort(depots, new DepotNameComparator());
        } catch (AccuRevRuntimeException e) {
            // Log the error and full exception stack trace
            log.error(e.getLocalizedMessage(), e);
            Messages.showErrorDialog(e.getCommandResult().toString(), e.getCommandResult().getErr());
            return new DefaultMutableTreeNode(e.getCommandResult().getErr());
        }
        //Vector rootNodes = new Vector(depots.size() * 2);
        AccuRevInfo info = null;
        try {
            info = AccuRev.getInfo();
        } catch (AccuRevRuntimeException e) {
            Messages.showErrorDialog(e.getCommandResult().toString(), e.getCommandResult().getErr());
            return new DefaultMutableTreeNode(e.getCommandResult());
        }
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(info);
        for (Iterator i = depots.iterator(); i.hasNext();) {
            Depot depot = (Depot) i.next();
            DefaultMutableTreeNode defaultMutableTreeNode = new DefaultMutableTreeNode(depot, true);
            DefaultMutableTreeNode newChild = new DefaultMutableTreeNode("Loading...");
            defaultMutableTreeNode.add(newChild);
            rootNode.add(defaultMutableTreeNode);
        }
        return rootNode;
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        final JScrollPane scrollPane1 = new JScrollPane();
        mainPanel.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null));
        depotTree = new JTree();
        scrollPane1.setViewportView(depotTree);
    }

    private class DepotNameComparator implements Comparator {
        public int compare(Object o1, Object o2) {
            Depot d1 = (Depot) o1;
            Depot d2 = (Depot) o2;

            return d1.getName().compareTo(d2.getName());
        }
    }

}
