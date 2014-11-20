package net.java.accurev4idea.gui.renderers;

import net.java.accurev4idea.dao.AccuRevDAOFacade;
import net.java.accurev4idea.gui.GuiUtils;
import net.java.accurev4idea.AccuRevVcs;
import net.java.savant.plugin.scm.accurev.components.Stream;
import net.java.savant.plugin.scm.accurev.components.Depot;
import net.java.savant.plugin.scm.accurev.components.StreamType;
import net.java.savant.plugin.scm.accurev.components.AccuRevInfo;
import net.java.savant.plugin.scm.accurev.dao.AccuRevDAO;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.Component;

import com.intellij.openapi.diagnostic.Logger;

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

    private AccuRevDAO accurev;

    public static ImageIcon leafIcon = GuiUtils.createImageIcon("/fw/images/rootdir.png");
    public static ImageIcon openIcon = GuiUtils.createImageIcon("/fw/images/dir_mode.png");
    public static ImageIcon closedIcon = GuiUtils.createImageIcon("/fw/images/rootdir.png");

    public static ImageIcon leafIconDepot = GuiUtils.createImageIcon("/fw/images/depot.png");
    public static ImageIcon openIconDepot = GuiUtils.createImageIcon("/fw/images/depot_open.png");
    public static ImageIcon closedIconDepot = GuiUtils.createImageIcon("/fw/images/depot.png");

    public static ImageIcon leafIconStream = GuiUtils.createImageIcon("/fw/images/sbrowse.png");
    public static ImageIcon openIconStream = GuiUtils.createImageIcon("/fw/images/vbrowse.png");
    public static ImageIcon closedIconStream = GuiUtils.createImageIcon("/fw/images/sbrowse.png");

    public static ImageIcon leafIconStreamWspace = GuiUtils.createImageIcon("/fw/images/workspace.png");
    public static ImageIcon openIconStreamWspace = GuiUtils.createImageIcon("/fw/images/workspace_open.png");
    public static ImageIcon closedIconStreamWspace = GuiUtils.createImageIcon("/fw/images/workspace.png");

    public static ImageIcon leafIconStreamSnap = GuiUtils.createImageIcon("/fw/images/snapshot.png");
    public static ImageIcon openIconStreamSnap = GuiUtils.createImageIcon("/fw/images/snapshot.png");
    public static ImageIcon closedIconStreamSnap = GuiUtils.createImageIcon("/fw/images/snapshot.png");

    public DepotTreeCellRenderer(AccuRevDAO accurev) {
        this.accurev = accurev;
    }

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
            setText(stream.getName() + " (" + getStreamTypeText(stream) + ")");
            if (stream.isSnapshot()) {
                if (!leaf && expanded) {
                    setIcon(openIconStreamSnap);
                } else {
                    setIcon(closedIconStreamSnap);
                }
            } else if (stream.isWorkspace()) {
                if (!leaf && expanded) {
                    setIcon(openIconStreamWspace);
                } else {
                    setIcon(closedIconStreamWspace);
                }
            } else {
                if (!leaf && expanded) {
                    setIcon(openIconStream);
                } else {
                    setIcon(closedIconStream);
                }
            }
        } else if (isDepot(value)) {
            Depot depot = (Depot) ((DefaultMutableTreeNode)value).getUserObject();
            setText(depot.getName());
            if (!leaf && expanded) {
                setIcon(openIconDepot);
            } else {
                setIcon(closedIconDepot);
            }
        } else if (isAccuRevInfo(value)) {
            AccuRevInfo info = (AccuRevInfo) ((DefaultMutableTreeNode)value).getUserObject();
            setText(info.getPrincipal() + " on " + info.getServerName() +
                    " (" + info.getHost() + ":" + info.getPort() + ")");
            if (!leaf && expanded) {
                setIcon(openIcon);
            } else {
                setIcon(closedIcon);
            }
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

    protected String getStreamTypeText(Stream stream) {
        if (stream.isNormalStream()) {
            return "Normal";
        } else if (stream.isSnapshot()) {
            return "Snapshot";
        } else if (stream.isWorkspace()) {
            return "Workspace";
        } else {
            return "";
        }
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
