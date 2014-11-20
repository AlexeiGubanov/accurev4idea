package net.java.accurev4idea.gui.eventlisteners;

import com.intellij.openapi.diagnostic.Logger;
import net.java.accurev4idea.dao.AccuRevDAOFacade;
import net.java.savant.plugin.scm.accurev.components.Depot;
import net.java.savant.plugin.scm.accurev.components.Stream;
import net.java.savant.plugin.scm.accurev.exceptions.AccuRevRuntimeException;

import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.MutableTreeNode;
import java.util.Iterator;
import java.util.Map;
import java.util.Collections;

/**
 * $Id: DepotTreeListener.java,v 1.6 2005/07/12 18:33:32 ifedulov Exp $
 * User: aantonov
 * Date: May 31, 2005
 * Time: 2:26:17 PM
 */
public class DepotTreeListener implements TreeSelectionListener, TreeWillExpandListener {
    /**
     * Name of this plugin component
     */
    private static final String COMPONENT_NAME = "DepotTreeListener";

    /**
     * Log audit channel
     */
    private static final Logger log = Logger.getInstance(COMPONENT_NAME);

    private JTree depotTree;
    private DefaultMutableTreeNode selectedNode;
    private DefaultMutableTreeNode rootNode;
    private DefaultTreeModel model;
    private AccuRevDAOFacade accurev;

    public DepotTreeListener(JTree tree, DefaultMutableTreeNode rootNode, AccuRevDAOFacade accurev) {
        this.depotTree = tree;
        this.model = (DefaultTreeModel)tree.getModel();
        this.rootNode = rootNode;
        this.selectedNode = rootNode;
        this.accurev = accurev;
    }

    /** Required by TreeSelectionListener interface. */
    public void valueChanged(TreeSelectionEvent event) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                           event.getPath().getLastPathComponent();

        if (node == null) return;

        Object nodeInfo = node.getUserObject();
        log.debug("Selection: " + nodeInfo + ":\n");
    }

    public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                           event.getPath().getLastPathComponent();

        if (node == null) return;

        Object nodeInfo = node.getUserObject();
        log.debug("Collapsed Leaf: " + nodeInfo + ":\n");

        if (nodeInfo.getClass().isAssignableFrom(Depot.class)) {
            clearCurrentNode(node);
            addObject(Collections.EMPTY_MAP, node, new DefaultMutableTreeNode("Loading..."), true);
        }
        depotTree.repaint();
    }

    public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                           event.getPath().getLastPathComponent();

        if (node == null) return;

        Map allowedWspaces = accurev.getWorkspacesForCurrentPrincipal();

        Object nodeInfo = node.getUserObject();
        log.debug("Expanded Leaf: " + nodeInfo + ":\n");

        if (nodeInfo.getClass().isAssignableFrom(Depot.class)) {
            Depot depot = (Depot) nodeInfo;
            Stream rootStream = null;
            try {
                rootStream = accurev.getStreamsTree(depot);
            } catch (AccuRevRuntimeException e) {
                // Log the error and full exception stack trace
                log.error(e.getLocalizedMessage(), e);
            }
            clearCurrentNode(node);

            addObject(allowedWspaces, node, rootStream, true);
        }
        depotTree.repaint();
    }

    /**
     *
     * @param allowedWorkspaces
     * @param parent
     * @param child
     * @param shouldBeVisible
     * @return would return a DefaultMutableTreeNode wraping the child object if valid,
     *  or null if the child object is a Workspace belonging to a different user than
     *  currently logged in.
     */
    private DefaultMutableTreeNode addObject(Map allowedWorkspaces, DefaultMutableTreeNode parent,
                                            Object child,
                                            boolean shouldBeVisible) {
        DefaultMutableTreeNode childNode =
                new DefaultMutableTreeNode(child);

        if (parent == null) {
            parent = rootNode;
        }

        model.insertNodeInto(childNode, parent,
                                 parent.getChildCount());

        if (child.getClass().isAssignableFrom(Stream.class)) {
            Stream stream = (Stream) child;
            //TODO: Until Igor fixes the hasChildren() call to return true if children present use getChildren().size() > 0
            if (stream.getChildren().size() > 0) {
                //addObject(childNode, new DefaultMutableTreeNode("Loading..."), true);
                for (Iterator iter = stream.getChildren().iterator(); iter.hasNext();) {
                    Stream o = (Stream) iter.next();
                    if (o.isWorkspace() && !allowedWorkspaces.containsKey(o.getName())) {
                        continue;
                    }
                    addObject(allowedWorkspaces, childNode, o, true);
                }
            }
        }

        // Iterate over the child nodes (streams or workspaces)

        //Make sure the user can see the lovely new node.
        if (shouldBeVisible) {
            //depotTree.scrollPathToVisible(new TreePath(childNode.getPath()));
        }
        return childNode;
    }

    private void clearCurrentNode(MutableTreeNode parent) {
        if (parent != null) {
            for (int i = model.getChildCount(parent)-1; i > -1; i--) {
                MutableTreeNode o = (MutableTreeNode) model.getChild(parent, i);
                model.removeNodeFromParent(o);
            }
            return;
        }
    }
}
