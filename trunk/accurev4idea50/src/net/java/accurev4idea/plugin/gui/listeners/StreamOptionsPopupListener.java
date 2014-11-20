package net.java.accurev4idea.plugin.gui.listeners;

import org.apache.log4j.Logger;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import net.java.accurev4idea.api.components.Stream;

/**
 * $Id $
 * User: aantonov
 * Date: Jun 3, 2005
 * Time: 10:07:06 AM
 */
public class StreamOptionsPopupListener extends MouseAdapter implements ActionListener {
    /**
     * Name of this plugin component
     */
    private static final String COMPONENT_NAME = "StreamOptionsPopupListener";

    /**
     * Log audit channel
     */
    private static final Logger log = Logger.getLogger(StreamOptionsPopupListener.class);

    private JPopupMenu popup;

    public StreamOptionsPopupListener() {
        this.popup = createPopupMenu();
    }

    public void mousePressed(MouseEvent e) {
        maybeShowPopup(e);
    }

    public void mouseReleased(MouseEvent e) {
        maybeShowPopup(e);
    }

    public void actionPerformed(ActionEvent e) {
        log.debug("Action Performed: " + e);
    }

    private void maybeShowPopup(MouseEvent e) {
        if (e.isPopupTrigger() && e.getSource().getClass().isAssignableFrom(JTree.class)) {
            JTree tree = (JTree) e.getSource();
            TreePath path = tree.getPathForLocation(e.getX(), e.getY());
            if (path == null) {
                return;
            }

            DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                           path.getLastPathComponent();

            if (node.getUserObject().getClass().isAssignableFrom(Stream.class)) {
                log.debug("Showing popup for " + node);

                tree.setSelectionPath(path);
                tree.scrollPathToVisible(path);
                popup.show(tree, e.getX(), e.getY());
            } else {
                return;
            }
        }
    }

    private JPopupMenu createPopupMenu() {
        //Create the popup menu.
        JPopupMenu popup = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem("Checkout Stream");
        menuItem.addActionListener(this);
        popup.add(menuItem);
        menuItem = new JMenuItem("Update Stream");
        menuItem.addActionListener(this);
        popup.add(menuItem);

        return popup;
    }
}