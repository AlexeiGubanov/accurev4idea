package org.accurev4idea.plugin.gui.renderers;

import net.java.accurev4idea.api.components.AccuRevFile;
import net.java.accurev4idea.api.components.Stream;
import org.accurev4idea.plugin.gui.GuiUtils;

import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.*;
import java.awt.*;

/**
 * $Id: FileTreeCellRenderer.java,v 1.2 2005/11/08 20:41:30 ifedulov Exp $
 * User: aantonov
 * Date: Nov 8, 2005
 * Time: 10:41:44 AM
 */
public class FileTreeCellRenderer extends DefaultTreeCellRenderer {

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

        if (isAccuRevFile(value)) {
            AccuRevFile file = (AccuRevFile) ((DefaultMutableTreeNode)value).getUserObject();
            setText(GuiUtils.getFileName(file));

            if (file.isDirectory() && leaf) {
                this.setIcon(getClosedIcon());
            }
        } else  if (isStream(value)) {
            Stream stream = (Stream) ((DefaultMutableTreeNode)value).getUserObject();
            setText(stream.getName());
        }

        return this;
    }

    private boolean isStream(Object value) {
        return isClass(value, Stream.class);
    }

    private boolean isAccuRevFile(Object value) {
        return isClass(value, AccuRevFile.class);
    }

    private boolean isClass(Object value, Class clazz) {
        DefaultMutableTreeNode node =
                (DefaultMutableTreeNode)value;

        if (node.getUserObject() == null) {
            return false;
        }

        if (node.getUserObject().getClass().isAssignableFrom(clazz)) {
            return true;
        }
        return false;
    }
}
