package net.java.accurev4idea.plugin.gui.renderers;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.Component;

import com.intellij.openapi.diagnostic.Logger;
import net.java.accurev4idea.api.components.Depot;
import net.java.accurev4idea.api.components.Stream;
import net.java.accurev4idea.api.components.AccuRevInfo;
import net.java.accurev4idea.plugin.gui.GuiUtils;

/**
 * $Id $
 * User: aantonov
 * Date: Jun 3, 2005
 * Time: 10:57:27 AM
 */
public class DepotTreeCellRenderer extends DefaultTreeCellRenderer {
    /**
     * Log audit channel
     */
    private static final Logger log = Logger.getInstance(DepotTreeCellRenderer.class.getName());

    public static ImageIcon rootIcon = GuiUtils.createImageIcon("/fw/images/accli16.gif");

    public static ImageIcon depotIcon = GuiUtils.createImageIcon("/fw/images/depot.png");

    public static ImageIcon streamIcon = GuiUtils.createImageIcon("/_cvs/showAsTree.png");

    public static ImageIcon workspaceIcon = GuiUtils.createImageIcon("/fw/images/workspace.png");

    public static ImageIcon snapshotIcon = GuiUtils.createImageIcon("/fw/images/snapshot.png");

    public Component getTreeCellRendererComponent(
                        JTree tree,
                        Object value,
                        boolean sel,
                        boolean expanded,
                        boolean leaf,
                        int row,
                        boolean hasFocus) {

        super.getTreeCellRendererComponent(
                        tree, value, sel,
                        expanded, leaf, row,
                        hasFocus);

        if (isStream(value)) {
            Stream stream = (Stream) ((DefaultMutableTreeNode)value).getUserObject();
            setText(stream.getName() + " (" + stream.getType().getName() + ")");
            if (stream.isSnapshot()) {
                setIcon(snapshotIcon);
            } else if (stream.isWorkspace()) {
                setIcon(workspaceIcon);
            } else {
                setIcon(streamIcon);
            }
        } else if (isDepot(value)) {
            Depot depot = (Depot) ((DefaultMutableTreeNode)value).getUserObject();
            setText(depot.getName());
            setIcon(depotIcon);
        } else if (isAccuRevInfo(value)) {
            AccuRevInfo info = (AccuRevInfo) ((DefaultMutableTreeNode)value).getUserObject();
            setText(info.getPrincipal() + " on " + info.getServerName() +
                    " (" + info.getHost() + ":" + info.getPort() + ")");
            setIcon(rootIcon);
        }

        log.debug(((DefaultMutableTreeNode)value).getUserObject().toString() + " [" +
                "sel=" + sel + ",expanded=" + expanded + ",leaf=" + leaf +
                ",row=" + row + ",hasFocus=" + hasFocus);
        log.debug("Leaf:" + getLeafIcon() + ",Open:" + getOpenIcon() + ",Closed:" + getClosedIcon());

        setToolTipText(((DefaultMutableTreeNode)value).getUserObject().toString() + " [" +
                "sel=" + sel + ",expanded=" + expanded + ",leaf=" + leaf +
                ",row=" + row + ",hasFocus=" + hasFocus);

        return this;
    }

    protected boolean isStream(Object value) {
        return isClass(value, Stream.class);
    }

    protected boolean isDepot(Object value) {
        return isClass(value, Depot.class);
    }

    protected boolean isAccuRevInfo(Object value) {
        return isClass(value, AccuRevInfo.class);
    }

    private boolean isClass(Object value, Class clazz) {
        DefaultMutableTreeNode node =
                (DefaultMutableTreeNode)value;

        if (node.getUserObject().getClass().isAssignableFrom(clazz)) {
            return true;
        }
        return false;
    }

    //
    public Icon getDefaultClosedIcon() {
        log.debug("getDefaultClosedIcon");
        return super.getDefaultClosedIcon();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public Icon getDefaultLeafIcon() {
        log.debug("getDefaultLeafIcon");
        return super.getDefaultLeafIcon();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public Icon getDefaultOpenIcon() {
        log.debug("getDefaultOpenIcon");
        return super.getDefaultOpenIcon();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public Icon getLeafIcon() {
        log.debug("getLeafIcon");
        return super.getLeafIcon();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public Icon getOpenIcon() {
        log.debug("getOpenIcon");
        return super.getOpenIcon();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public Icon getClosedIcon() {
        log.debug("getClosedIcon");
        return super.getClosedIcon();    //To change body of overridden methods use File | Settings | File Templates.
    }
}