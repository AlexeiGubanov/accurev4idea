package net.java.accurev4idea.plugin.gui;

import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.GridConstraints;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.*;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;

import net.java.accurev4idea.api.exec.CommandResult;
import net.java.accurev4idea.api.CommandExecListener;
import net.java.accurev4idea.api.AccuRev;
import net.java.accurev4idea.api.components.Stream;
import net.java.accurev4idea.api.components.Workspace;
import net.java.accurev4idea.api.components.AccuRevFile;
import net.java.accurev4idea.api.parsers.JDOMUtils;
import net.java.accurev4idea.plugin.AccuRevConfiguration;
import net.java.accurev4idea.plugin.AccuRevVcs;
import net.java.accurev4idea.plugin.AccuRevConfigurable;
import net.java.accurev4idea.plugin.gui.listeners.FileTreeListener;
import net.java.accurev4idea.plugin.gui.listeners.FileOptionsPopupListener;
import org.accurev4idea.plugin.gui.renderers.FileTreeCellRenderer;
import org.accurev4idea.plugin.gui.renderers.FileTableCellRenderer;
import org.accurev4idea.plugin.gui.DepotPanel;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jdom.Document;

/**
 * $Id: AccuRevInfoPane.java,v 1.10 2005/11/11 22:42:53 ifedulov Exp $
 * User: aantonov
 * Date: Nov 2, 2005
 * Time: 1:02:58 PM
 */
public class AccuRevInfoPane extends MouseAdapter implements CommandExecListener, TreeSelectionListener {
    private JPanel mainPanel;
    private JTabbedPane tabPanel;
    private JTable commandHistoryPanel;
    private JScrollPane cmdHistScroll;
    private JSplitPane depotSplitPanel;
    private JTable selectedFileTreePanel;
    private JPanel buttonPanel;
    private JPanel commandHistoryWrapper;
    private JPanel rightPanel;
    private JTree selectedStreamFileTree;

    private DepotPanel depotPanel;

    private DefaultTableModel dtm;
    private DefaultTableModel fileDtm;
    private DefaultTreeModel fileTreeModel;

    private static final Logger log = Logger.getLogger(AccuRevInfoPane.class);

    private AccuRevVcs vcs;
    private JSplitPane rightSplitPanel;


    public AccuRevInfoPane(AccuRevVcs vcs) {
        AccuRevConfiguration config =
                ((AccuRevConfigurable) vcs.getConfigurable()).getConfiguration();

        this.vcs = vcs;

        if (!config.isShowAccuRevOutput()) {
            tabPanel.remove(cmdHistScroll);
        }

        setupDepotTable();

        // Adding Depot Panel
        depotPanel = new DepotPanel(vcs);
        depotSplitPanel.setLeftComponent(depotPanel.getComponent());

        setupFileTable();
    }

    public JComponent getComponent() {
        return mainPanel;
    }

    public void commandExecuted(CommandResult command) {
        String commandString = StringUtils.join(command.getCommand(), " ");
        dtm.addRow(new Object[] {commandString, command.getOut(), command.getErr()});
    }

    public void mouseClicked(MouseEvent e)
    {
        if (e.getClickCount() == 2 && e.getComponent() == commandHistoryPanel)
        {
            Point p = e.getPoint();
            int row = commandHistoryPanel.rowAtPoint(p);
            int column = commandHistoryPanel.columnAtPoint(p); // This is the view column!
            String xml = (String) dtm.getValueAt(row, column);

            try {
                Document doc = JDOMUtils.getDocument(xml);

                XMLTreeViewer viewer = new XMLTreeViewer(doc);
                viewer.pack();
                viewer.show();
            } catch (Exception ex) {
                // Looks like this is not an XML document, so show as a popup.
                TextAreaViewer viewer = new TextAreaViewer(xml);
                viewer.pack();
                viewer.show();
            }
            // ...
        }
    }

    public void valueChanged(TreeSelectionEvent event) {
        DefaultMutableTreeNode selected = (DefaultMutableTreeNode)
                           event.getPath().getLastPathComponent();
        if (selected != null) {
            if (selected.getUserObject().getClass().isAssignableFrom(Stream.class)) {
                handleStreamSelection((Stream) selected.getUserObject());
            }
        }
    }

    public void valueChanged(ListSelectionEvent e) {
        //Ignore extra messages.
        if (e.getValueIsAdjusting()) return;

        ListSelectionModel lsm = (ListSelectionModel)e.getSource();
        if (lsm.isSelectionEmpty()) {
            //no rows are selected
        } else {
            int selectedRow = lsm.getMinSelectionIndex();
            //selectedRow is selected
            //commandExecuted(new CommandResult(new String[] {"Mouse Clicked"}, 0, "", ""));
        }
    }

    private void setupDepotTable() {
        String[] columnNames = {"Command", "Output", "Errors"};

        Object[][] data = new Object[0][0];

        dtm = new DefaultTableModel(data, columnNames);

        commandHistoryPanel.setModel(dtm);
        commandHistoryPanel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        commandHistoryPanel.setCellSelectionEnabled(true);

        commandHistoryPanel.addMouseListener(this);

        //Ask to be notified of selection changes.
        ListSelectionModel rowSM = commandHistoryPanel.getSelectionModel();
        //rowSM.addListSelectionListener(this);
    }

    private void setupFileTable() {
        String[] columnNames = {"Location", "Status", "Version"};

        Object[][] data = new Object[0][0];

        fileDtm = new DefaultTableModel(data, columnNames);

        selectedFileTreePanel.setModel(fileDtm);
        selectedFileTreePanel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        selectedFileTreePanel.setCellSelectionEnabled(true);
        selectedFileTreePanel.setEnabled(false);

        FileOptionsPopupListener fopl =
                new FileOptionsPopupListener(selectedStreamFileTree, 
                        selectedFileTreePanel, vcs.getProject());

        selectedFileTreePanel.addMouseListener(fopl);
        selectedFileTreePanel.setDefaultRenderer(Object.class,
                new FileTableCellRenderer());

        depotPanel.addTreeSelectionListener(this);

        FileTreeListener listener = new FileTreeListener(selectedStreamFileTree, selectedFileTreePanel,
                new DefaultMutableTreeNode(), vcs);

        selectedStreamFileTree.addTreeWillExpandListener(listener);
        selectedStreamFileTree.addTreeSelectionListener(listener);

        selectedStreamFileTree.setCellRenderer(new FileTreeCellRenderer());

        selectedStreamFileTree.setModel(new DefaultTreeModel(new DefaultMutableTreeNode()));

        selectedStreamFileTree.setPreferredSize(new Dimension(150, -1));
    }

    private void handleStreamSelection(Stream s) {
        Workspace w = null;
        if (s.isWorkspace()) {
            w = AccuRev.getWorkspaceByNameForCurrentPrincipal(s.getName(), vcs.getCommandExecListeners());

        }
        // Create Stream Tree Node
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(s);

        fileDtm.getDataVector().clear();

        List files = AccuRev.getAccuRevFilesInStream(w == null? s: w
                , null, vcs.getCommandExecListeners());

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
                rootNode.add(defaultMutableTreeNode);
            }

            fileDtm.addRow(new Object[] {f,
                f.getStatus().getName(),
                f.getVersion().getReal().getVersionString()});
        }

        fileTreeModel = new DefaultTreeModel(rootNode);
        selectedStreamFileTree.setModel(fileTreeModel);
        selectedStreamFileTree.setSelectionRow(0);
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
        tabPanel = new JTabbedPane();
        tabPanel.setTabPlacement(3);
        mainPanel.add(tabPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null));
        commandHistoryWrapper = new JPanel();
        commandHistoryWrapper.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        tabPanel.addTab("Command History", commandHistoryWrapper);
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        commandHistoryWrapper.add(buttonPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(30, -1), new Dimension(30, -1)));
        cmdHistScroll = new JScrollPane();
        commandHistoryWrapper.add(cmdHistScroll, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null));
        commandHistoryPanel = new JTable();
        commandHistoryPanel.setDragEnabled(false);
        commandHistoryPanel.setEnabled(false);
        cmdHistScroll.setViewportView(commandHistoryPanel);
        depotSplitPanel = new JSplitPane();
        tabPanel.addTab("Depots", depotSplitPanel);
        rightSplitPanel = new JSplitPane();
        depotSplitPanel.setRightComponent(rightSplitPanel);
        final JScrollPane scrollPane1 = new JScrollPane();
        rightSplitPanel.setLeftComponent(scrollPane1);
        selectedStreamFileTree = new JTree();
        scrollPane1.setViewportView(selectedStreamFileTree);
        final JScrollPane scrollPane2 = new JScrollPane();
        rightSplitPanel.setRightComponent(scrollPane2);
        selectedFileTreePanel = new JTable();
        scrollPane2.setViewportView(selectedFileTreePanel);
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
