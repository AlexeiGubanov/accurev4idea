/*
 * RevisionHistoryTableModel.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jun 18, 2005, 1:52:24 PM
 */
package net.java.accurev4idea.plugin.gui;

import net.java.accurev4idea.api.components.AccuRevTransaction;
import org.apache.commons.lang.StringUtils;

import javax.swing.table.AbstractTableModel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Table model that adapts display of {@link AccuRevTransaction}s one per each row.
 *
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version $Id: RevisionHistoryTableModel.java,v 1.1 2005/11/05 16:56:24 ifedulov Exp $
 * @since 0.1
 */
public class RevisionHistoryTableModel extends AbstractTableModel {
    /**
     * Date formatter used to format the date column
     */
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * Internal storage for backing data, in case of this model it's {@link AccuRevTransaction}s
     */
    private List backingData = new ArrayList();
    /**
     * Column headers for the table
     */
    private String[] columnNames = {"Transaction", "Date", "Action", "Stream / Workspace", "Real Version", "Virtual Version", "User", "Comments"};

    /**
     * Obtain the backing data element ({@link AccuRevTransaction}) for the currently selected row.
     *
     * @param rowId currently selected row
     * @return backing data element for the given rowId
     */
    public AccuRevTransaction getAccuRevTransactionAt(int rowId) {
        return (AccuRevTransaction) backingData.get(rowId);
    }

    /**
     * Constructor that should be called on this implementation. Adds all elements from given
     * list into backing store
     *
     * @param list List of {@link AccuRevTransaction}s
     */
    public RevisionHistoryTableModel(List list) {
        backingData.addAll(list);
    }

    /**
     * @see javax.swing.table.TableModel#getRowCount()
     */
    public int getRowCount() {
        return backingData.size();
    }

    /**
     * @see javax.swing.table.TableModel#getColumnName(int)
     */
    public String getColumnName(int i) {
        return columnNames[i];
    }

    /**
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount() {
        return columnNames.length;
    }

    /**
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int row, int col) {
        AccuRevTransaction accuRevTransaction = (AccuRevTransaction) backingData.get(row);
        switch (col) {
            case 0:
                return String.valueOf(accuRevTransaction.getTransactionId());
            case 1:
                return sdf.format(accuRevTransaction.getDate());
            case 2:
                return (accuRevTransaction.getType() == null)? StringUtils.EMPTY:accuRevTransaction.getType().getName();
            case 3:
                return accuRevTransaction.getVersion().getVirtual().getStream().getName();
            case 4:
                return accuRevTransaction.getVersion().getReal().getVersionString();
            case 5:
                return accuRevTransaction.getVersion().getVirtual().getVersionString();
            case 6:
                return accuRevTransaction.getUserName();
            case 7:
                return accuRevTransaction.getComment();
            default:
                return null;
        }
    }
}
