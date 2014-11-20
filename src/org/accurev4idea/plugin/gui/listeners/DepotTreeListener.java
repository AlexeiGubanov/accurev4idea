package org.accurev4idea.plugin.gui.listeners;

import net.java.accurev4idea.api.exceptions.AccuRevRuntimeException;
import net.java.accurev4idea.api.components.Stream;
import net.java.accurev4idea.api.components.Depot;
import net.java.accurev4idea.api.components.StreamType;
import net.java.accurev4idea.api.AccuRev;

import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.MutableTreeNode;
import java.util.*;

import org.apache.log4j.Logger;

/**
 * $Id: DepotTreeListener.java,v 1.5 2005/11/11 22:42:53 ifedulov Exp $
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
    private static final Logger log = Logger.getLogger(DepotTreeListener.class);

    private JTree depotTree;
    private DefaultMutableTreeNode selectedNode;
    private DefaultMutableTreeNode rootNode;
    private DefaultTreeModel model;

    public DepotTreeListener(JTree tree, DefaultMutableTreeNode rootNode) {
        this.depotTree = tree;
        this.model = (DefaultTreeModel)tree.getModel();
        this.rootNode = rootNode;
        this.selectedNode = rootNode;
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

        // TODO: Get the AccuRevVcs reference to get the command exec listeners
        Map allowedWspaces = AccuRev.getWorkspacesForCurrentPrincipal(Collections.EMPTY_LIST);

        Object nodeInfo = node.getUserObject();
        log.debug("Expanded Leaf: " + nodeInfo + ":\n");

        if (nodeInfo.getClass().isAssignableFrom(Depot.class)) {
            Depot depot = (Depot) nodeInfo;
            Stream rootStream = null;
            try {
                rootStream = AccuRev.getStreamsTree(depot);
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
            if (stream.hasChildren()) {
                List streams = stream.getChildren();
                // first do sort by stream type
                Collections.sort(streams, new StreamTypeComparator());
                // next do sort for snapshots
                Collections.sort(streams, new SnapshotComparator());
                for (Iterator iter = streams.iterator(); iter.hasNext();) {
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
        }
    }

    private class StreamTypeComparator implements Comparator {
        /**
         * This is a simple type to numeric map to use already implemented compareTo from Integer
         * This map will basically allow sorting in a following way:
         * 1. Workspaces
         * 2. Regular Streams
         * 3. Snapshots
         */
        private Map typeToInteger = new HashMap() {{
            put(StreamType.WORKSPACE, new Integer(1));
            put(StreamType.NORMAL, new Integer(2));
            put(StreamType.SNAPSHOT, new Integer(3));
        }};
        public int compare(Object o1, Object o2) {
            Stream s1 = (Stream) o1;
            Stream s2 = (Stream) o2;
            // workspaces are always at the top then streams and then snapshots
            return ((Integer)typeToInteger.get(s1.getType())).compareTo((Integer) typeToInteger.get(s2.getType()));
        }
    }

    /**
     * Descending comparator for snapshots, using creation date for basis of comparasion
     */
    private class SnapshotComparator implements Comparator {
        public int compare(Object o1, Object o2) {
            Stream s1 = (Stream) o1;
            Stream s2 = (Stream) o2;
            if(s1.getType().equals(StreamType.SNAPSHOT) && s2.getType().equals(StreamType.SNAPSHOT)) {
                return -1 * s1.getCreationDate().compareTo(s2.getCreationDate());
            } else {
                return 0;
            }
        }
    }
}