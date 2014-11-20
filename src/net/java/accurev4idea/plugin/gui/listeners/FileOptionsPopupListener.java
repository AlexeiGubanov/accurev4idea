package net.java.accurev4idea.plugin.gui.listeners;

import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.*;

import net.java.accurev4idea.api.components.Stream;
import net.java.accurev4idea.api.components.AccuRevFile;
import net.java.accurev4idea.plugin.AccuRevToIdeaAdapter;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.project.Project;

/**
 * $Id $
 * User: aantonov
 * Date: Jun 3, 2005
 * Time: 10:07:06 AM
 */
public class FileOptionsPopupListener extends MouseAdapter implements ActionListener {
    /**
     * Name of this plugin component
     */
    private static final String COMPONENT_NAME = "StreamOptionsPopupListener";

    /**
     * Log audit channel
     */
    private static final Logger log = Logger.getLogger(FileOptionsPopupListener.class);

    private JPopupMenu popup;

    private JTree fileTree;
    private JTable fileTable;

    private Project project;

    private AccuRevFile lastSelectedFile;
    private Stream lastSelectedParentStream;

    public FileOptionsPopupListener(JTree tree, JTable table, Project project) {
        this.popup = createPopupMenu();
        this.fileTree = tree;
        this.fileTable = table;

        this.project = project;
    }

    public void mousePressed(MouseEvent e) {
        maybeShowPopup(e);
    }

    public void mouseReleased(MouseEvent e) {
        maybeShowPopup(e);
    }

    public void actionPerformed(ActionEvent e) {
        log.debug("Action Performed: " + e);
        if (lastSelectedFile != null) {
            Messages.showInfoMessage(fileTable, lastSelectedFile.getAbsolutePath(), lastSelectedParentStream.getName());

            AccuRevToIdeaAdapter adapter = AccuRevToIdeaAdapter.getInstance(project);

            //TODO: Add support for diffing.
            //adapter.getEditorDiffContent(null);

            //adapter.getDiffContentByVersion()

            //ActionAbstractDiffAgainst.showDiffDialog(project, "Diff...",);
        }
    }

    private void maybeShowPopup(MouseEvent e) {
        if (e.isPopupTrigger() && e.getSource().getClass().isAssignableFrom(JTable.class)) {
            JTable table = (JTable) e.getSource();
            Point p = e.getPoint();
            int row = table.rowAtPoint(p);
            int column = table.columnAtPoint(p);
            AccuRevFile selectedFile = (AccuRevFile) table.getValueAt(row, 0);

            TreePath path = fileTree.getSelectionPath();
            if (path == null) {
                return;
            }

            lastSelectedParentStream = (Stream)
                    ((DefaultMutableTreeNode)
                            fileTree.getModel().getRoot()).getUserObject();

            if (!selectedFile.isDirectory()) {
                lastSelectedFile = selectedFile;
                popup.show(table, e.getX(), e.getY());
            } else {
                lastSelectedFile = null;
                lastSelectedParentStream = null;
                return;
            }
        }
    }

    private JPopupMenu createPopupMenu() {
        //Create the popup menu.
        JPopupMenu popup = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem("Compare with open file");
        menuItem.addActionListener(this);
        popup.add(menuItem);
        /*menuItem = new JMenuItem("Update Stream");
        menuItem.addActionListener(this);
        popup.add(menuItem);*/

        return popup;
    }
}