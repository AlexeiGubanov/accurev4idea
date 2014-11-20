package net.java.accurev4idea.gui;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.ui.Messages;
import net.java.accurev4idea.dao.AccuRevDAOFacade;
import net.java.accurev4idea.gui.eventlisteners.DepotTreeListener;
import net.java.accurev4idea.gui.eventlisteners.StreamOptionsPopupListener;
import net.java.accurev4idea.gui.renderers.DepotTreeCellRenderer;
import net.java.savant.plugin.scm.accurev.components.AccuRevInfo;
import net.java.savant.plugin.scm.accurev.components.Depot;
import net.java.savant.plugin.scm.accurev.exceptions.AccuRevRuntimeException;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;
import java.awt.BorderLayout;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * $Id: DepotPanel.java,v 1.6 2005/07/12 18:33:38 ifedulov Exp $
 * User: aantonov
 * Date: Jun 8, 2005
 * Time: 11:11:59 AM
 */
public class DepotPanel extends JPanel {
    /**
     * Log4j audit channel
     */
    private static Logger log = Logger.getInstance(DepotPanel.class.getName());
    private JTree depotTree;
    private AccuRevDAOFacade accurev;

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

    public DepotPanel(AccuRevDAOFacade accurev) {
        super(new BorderLayout());
        this.accurev = accurev;
        initializeDepotJTree();
    }

    public void addTreeSelectionListener(TreeSelectionListener tsl) {
        depotTree.addTreeSelectionListener(tsl);
    }

    public void removeTreeSelectionListener(TreeSelectionListener tsl) {
        depotTree.removeTreeSelectionListener(tsl);
    }

    private void initializeDepotJTree() {
        DefaultMutableTreeNode rootNode = initializeDepotList();
        depotTree = new JTree(rootNode);
        /*ImageIcon leafIcon = GuiUtils.createImageIcon("/fw/images/depot.png");
        ImageIcon openIcon = GuiUtils.createImageIcon("/fw/images/depot.png");
        ImageIcon closedIcon = GuiUtils.createImageIcon("/fw/images/depot.png");*/

        DefaultTreeCellRenderer renderer = new DepotTreeCellRenderer(accurev);
        /*renderer.setLeafIcon(leafIcon);
        renderer.setOpenIcon(openIcon);
        renderer.setClosedIcon(closedIcon);*/

        depotTree.setCellRenderer(renderer);

        depotTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        depotTree.setShowsRootHandles(true);
        depotTree.setRootVisible(true);

        DepotTreeListener listener = new DepotTreeListener(depotTree, rootNode, accurev);
        depotTree.addTreeSelectionListener(listener);
        depotTree.addTreeWillExpandListener(listener);

        StreamOptionsPopupListener pListener = new StreamOptionsPopupListener(accurev);
        depotTree.addMouseListener(pListener);

        JScrollPane treeView = new JScrollPane(depotTree);

        // Enable tooltips
        ToolTipManager.sharedInstance().registerComponent(depotTree);

        add(treeView);
    }

    private DefaultMutableTreeNode initializeDepotList() {
        List depots = Collections.EMPTY_LIST;
        try {
            depots = accurev.getDepots();
        } catch (AccuRevRuntimeException e) {
            // Log the error and full exception stack trace
            log.error(e.getLocalizedMessage(), e);
            Messages.showErrorDialog(e.getCommandResult().toString(), e.getCommandResult().getErr());
            return new DefaultMutableTreeNode(e.getCommandResult().getErr());
        }
        //Vector rootNodes = new Vector(depots.size() * 2);
        AccuRevInfo info = null;
        try {
            info = accurev.getInfo();
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
}
