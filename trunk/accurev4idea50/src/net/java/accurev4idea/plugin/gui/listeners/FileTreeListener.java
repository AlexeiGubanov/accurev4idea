package net.java.accurev4idea.plugin.gui.listeners;

import net.java.accurev4idea.api.components.Stream;
import net.java.accurev4idea.api.components.StreamType;
import net.java.accurev4idea.api.components.AccuRevFile;
import net.java.accurev4idea.api.AccuRev;
import net.java.accurev4idea.plugin.AccuRevVcs;
import net.java.accurev4idea.plugin.gui.GuiUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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
 * $Id: FileTreeListener.java,v 1.3 2005/11/10 18:03:02 ifedulov Exp $
 * User: aantonov
 * Date: May 31, 2005
 * Time: 2:26:17 PM
 */
public class FileTreeListener implements TreeSelectionListener, TreeWillExpandListener {
    /**
     * Name of this plugin component
     */
    private static final String COMPONENT_NAME = "FileTreeListener";

    /**
     * Log audit channel
     */
    private static final Logger log = Logger.getLogger(FileTreeListener.class);

    private JTree fileTree;
    private JTable fileTable;
    private DefaultMutableTreeNode selectedNode;
    private DefaultMutableTreeNode rootNode;

    private AccuRevVcs vcs;

    public FileTreeListener(JTree tree, JTable table,
                            DefaultMutableTreeNode rootNode, AccuRevVcs vcs) {
        this.fileTree = tree;
        this.rootNode = rootNode;
        this.selectedNode = rootNode;

        this.fileTable = table;

        this.vcs = vcs;
    }

    /** Required by TreeSelectionListener interface. */
    public void valueChanged(TreeSelectionEvent event) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                           event.getPath().getLastPathComponent();

        DefaultTreeModel treeModel = (DefaultTreeModel) fileTree.getModel();
        DefaultTableModel tableModel = (DefaultTableModel) fileTable.getModel();

        if (node == null) return;

        selectedNode = node;

        Object nodeInfo = node.getUserObject();
        log.debug("Selection: " + nodeInfo + ":\n");

        if (nodeInfo == null) return;

        // Clear Table and add new files.
        clearCurrentTable();

        // The root of the tree is always a Stream
        rootNode = (DefaultMutableTreeNode) treeModel.getRoot();
        Stream s = (Stream) rootNode.getUserObject();

        List files = Collections.EMPTY_LIST;

        if (nodeInfo.getClass().isAssignableFrom(AccuRevFile.class)) {
            AccuRevFile file = (AccuRevFile) nodeInfo;

            files = AccuRev.getAccuRevFilesInStream(s,
                    file.getAbsolutePath(), vcs.getCommandExecListeners());
        } else if (nodeInfo.getClass().isAssignableFrom(Stream.class)) {
            files = AccuRev.getAccuRevFilesInStream(s,
                    null, vcs.getCommandExecListeners());
        }

        List fileList = new ArrayList();
        List dirList = new ArrayList();

        for (Iterator iter = files.iterator(); iter.hasNext();) {
            AccuRevFile f =  (AccuRevFile) iter.next();
            if (f.isDirectory()) {
                dirList.add(f);
            } else {
                fileList.add(f);
            }
        }

        Collections.sort(dirList, new AccuRevFileNameComparator());
        Collections.sort(fileList, new AccuRevFileNameComparator());
        files.clear();
        files.addAll(dirList);
        files.addAll(fileList);

        for (Iterator iter = files.iterator(); iter.hasNext();) {
            AccuRevFile f =  (AccuRevFile) iter.next();
            if (f.isDirectory()) {
                DefaultMutableTreeNode defaultMutableTreeNode = new DefaultMutableTreeNode(f, true);
                DefaultMutableTreeNode newChild = new DefaultMutableTreeNode("Loading...");
                defaultMutableTreeNode.add(newChild);
                selectedNode.add(defaultMutableTreeNode);
            }

            tableModel.addRow(new Object[] {f,
                f.getStatus().getName(),
                f.getVersion().getReal().getVersionString()});

        }
    }

    public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                           event.getPath().getLastPathComponent();

        if (node == null) return;

        Object nodeInfo = node.getUserObject();
        log.debug("Collapsed Leaf: " + nodeInfo + ":\n");

        if (nodeInfo == null) return;

        clearCurrentNode(node);

        generateObjectTree(node);

        fileTree.repaint();
    }

    public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                           event.getPath().getLastPathComponent();

        if (node == null) return;

        Object nodeInfo = node.getUserObject();
        log.debug("Expanded Leaf: " + nodeInfo + ":\n");

        if (nodeInfo == null) return;

        clearCurrentNode(node);

        generateObjectTree(node);

        fileTree.repaint();
    }

    /**
     *
     * @param parent
     *
     */
    private void generateObjectTree(DefaultMutableTreeNode parent) {
/*        DefaultMutableTreeNode childNode =
                new DefaultMutableTreeNode(child);*/

        DefaultTreeModel treeModel = (DefaultTreeModel) fileTree.getModel();
        DefaultTableModel tableModel = (DefaultTableModel) fileTable.getModel();

        // The root of the tree is always a Stream
        rootNode = (DefaultMutableTreeNode) treeModel.getRoot();
        Stream s = (Stream) rootNode.getUserObject();

        if (parent == null) {
            parent = rootNode;
        }

        List files = Collections.EMPTY_LIST;

        Object nodeInfo = parent.getUserObject();

        if (nodeInfo.getClass().isAssignableFrom(AccuRevFile.class)) {
            AccuRevFile file = (AccuRevFile) nodeInfo;

            files = AccuRev.getAccuRevFilesInStream(s,
                    file.getAbsolutePath(), vcs.getCommandExecListeners());
        } else if (nodeInfo.getClass().isAssignableFrom(Stream.class)) {
            files = AccuRev.getAccuRevFilesInStream(s,
                    null, vcs.getCommandExecListeners());
        }

        List fileList = new ArrayList();
        List dirList = new ArrayList();

        for (Iterator iter = files.iterator(); iter.hasNext();) {
            AccuRevFile f =  (AccuRevFile) iter.next();
            if (f.isDirectory()) {
                dirList.add(f);
            } else {
                fileList.add(f);
            }
        }

        Collections.sort(dirList, new AccuRevFileNameComparator());
        Collections.sort(fileList, new AccuRevFileNameComparator());
        files.clear();
        files.addAll(dirList);
        files.addAll(fileList);

        for (Iterator iter = files.iterator(); iter.hasNext();) {
            AccuRevFile f =  (AccuRevFile) iter.next();
            if (f.isDirectory()) {
                DefaultMutableTreeNode defaultMutableTreeNode = new DefaultMutableTreeNode(f, true);
                DefaultMutableTreeNode newChild = new DefaultMutableTreeNode("Loading...");
                defaultMutableTreeNode.add(newChild);
                //parent.add(defaultMutableTreeNode);
                treeModel.insertNodeInto(defaultMutableTreeNode, parent,
                                 parent.getChildCount());
            }
        }
    }

    private void clearCurrentNode(MutableTreeNode parent) {
        DefaultTreeModel treeModel = (DefaultTreeModel) fileTree.getModel();
        DefaultTableModel tableModel = (DefaultTableModel) fileTable.getModel();

        if (parent != null) {
            for (int i = treeModel.getChildCount(parent)-1; i > -1; i--) {
                MutableTreeNode o = (MutableTreeNode) treeModel.getChild(parent, i);
                treeModel.removeNodeFromParent(o);
            }
        }
    }

    private void clearCurrentTable() {
        DefaultTableModel tableModel = (DefaultTableModel) fileTable.getModel();

        tableModel.getDataVector().clear();
    }

    private class AccuRevFileNameComparator implements Comparator {
        public int compare(Object o1, Object o2) {
            AccuRevFile s1 = (AccuRevFile) o1;
            AccuRevFile s2 = (AccuRevFile) o2;

            String n1 = GuiUtils.getFileName(s1);
            String n2 = GuiUtils.getFileName(s2);

            return n1.compareToIgnoreCase(n2);
        }
    }
}