package net.java.accurev4idea.plugin.gui.renderers;

import net.java.accurev4idea.api.components.AccuRevFile;
import net.java.accurev4idea.plugin.gui.GuiUtils;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;

/**
 * $Id: FileTableCellRenderer.java,v 1.1 2005/11/10 18:03:03 ifedulov Exp $
 * User: aantonov
 * Date: Nov 10, 2005
 * Time: 11:26:00 AM
 */
public class FileTableCellRenderer extends DefaultTableCellRenderer {

    public Component getTableCellRendererComponent(JTable table,
                                                   Object value,
                                                   boolean isSelected,
                                                   boolean hasFocus,
                                                   int row,
                                                   int column) {

        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (isAccuRevFile(value)) {
            AccuRevFile file = (AccuRevFile) value;

            setText(GuiUtils.getFileName(file));
        }

        return this;
    }

    private boolean isAccuRevFile(Object value) {
        return isClass(value, AccuRevFile.class);
    }

    private boolean isClass(Object value, Class clazz) {
        if (value.getClass().isAssignableFrom(clazz)) {
            return true;
        }
        return false;
    }
}
