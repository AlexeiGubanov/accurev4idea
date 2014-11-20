/*
 * RevisionHistoryWindow.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jun 12, 2005, 9:40:54 PM
 */
package net.java.accurev4idea.gui;

import com.intellij.openapi.actionSystem.DataProvider;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.diff.DiffManager;
import com.intellij.openapi.diff.DiffPanel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import net.java.accurev4idea.components.DiffPanelDataElement;
import net.java.accurev4idea.dao.AccuRevDAOFacade;
import net.java.savant.plugin.scm.accurev.components.AccuRevTransaction;
import net.java.savant.plugin.scm.accurev.exceptions.AccuRevRuntimeException;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Toolkit;


/**
 * This class encapsulates functonality behind bringing up and displaying the revision
 * history window for a selected file.
 *
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version $Id: RevisionHistoryWindow.java,v 1.3 2005/07/12 00:44:51 ifedulov Exp $
 * @since 0.1
 */
public class RevisionHistoryWindow extends JFrame implements DataProvider {
    /**
     * Log4j audit channel
     */
    private static final Logger log = Logger.getInstance(RevisionHistoryWindow.class.getName());

    /**
     * {@link DiffPanel} reference, needed so that different revisions can be loaded on the fly
     */
    private DiffPanel diffPanel = null;
    /**
     * {@link JTable} reference to the actual revision history where each row is backed by {@link AccuRevTransaction}
     * element
     */
    private JTable revisionHistoryTable = null;

    /**
     * DAO Facade reference, used to obtain actual content of the revision version as well as
     * entire revision history for a file
     */
    private AccuRevDAOFacade accuRevDAOFacade;

    /**
     * Reference to current project, is needed for {@link DataProvider#getData(String)} lookup method.
     */
    private Project project;

    /**
     * Current {@link VirtualFile} that is being viewed in revision history
     */
    private VirtualFile virtualFile;

    /**
     * POJO describing the left data element on a diff panel (a file from AccuRev)
     */
    private DiffPanelDataElement leftDataElement;

    /**
     * POJO describing the right data element on a diff panel (a file from the editor)
     */
    private DiffPanelDataElement rightDataElement;

    /**
     * Prefered constructor to use, initialized in {@link net.java.accurev4idea.actions.ActionRevisionHistory#performAction(com.intellij.openapi.actionSystem.AnActionEvent)}
     *
     * @param accuRevDAOFacade reference to {@link AccuRevDAOFacade} instance for data access
     * @param project current {@link Project]
     * @param virtualFile current {@link VirtualFile} that is subject to revision history
     * @param left the left pane data element
     * @param right the right pane data element
     */
    public RevisionHistoryWindow(AccuRevDAOFacade accuRevDAOFacade, Project project, VirtualFile virtualFile, DiffPanelDataElement left, DiffPanelDataElement right) {
        this.accuRevDAOFacade = accuRevDAOFacade;
        this.project = project;
        this.virtualFile = virtualFile;
        this.leftDataElement = left;
        this.rightDataElement = right;
        this.diffPanel = DiffManager.getInstance().createDiffPanel(this, project);

        // setup window elements
        setupWindowElements();

        // populate diff panel with the content from left and right data elements
        populateDiffPanel();
    }

    public Object getData(String dataId) {
        if ("project".equals(dataId)) {
            return project;
        } else {
            // log.error("Call to getData() with [" + dataId + "] parameter, unable to handle.", new Exception());
            return null;
        }
    }

    /**
     * Populate {@link #diffPanel}, set both titles and set the contents for the panel
     */
    private void populateDiffPanel() {
        this.diffPanel.setTitle1(leftDataElement.getTitle());
        this.diffPanel.setTitle2(rightDataElement.getTitle());
        this.diffPanel.setContents(leftDataElement.getDiffContent(), rightDataElement.getDiffContent());
    }

    /**
     * Setup and position GUI elements on this frame.
     */
    private void setupWindowElements() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        final Image image = (new ImageIcon(getClass().getResource("/diff/Diff.png"))).getImage();
        setIconImage(image);
        setTitle(virtualFile.getPresentableUrl());

        JComponent diffs = diffPanel.getComponent();
        JComponent revisions = createRevisionsPanel();

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, diffs, revisions);
        splitPane.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        splitPane.setDividerLocation(.8);

        getContentPane().add(splitPane);

        pack();

        setExtendedState(Frame.MAXIMIZED_BOTH);


        // TODO: Figure out how to save the size of the diff window after it's closed
        //       and how to restore it back on new revision history request
    }


    /**
     * Create revision history panel as a {@link JScrollPane} with {@link JTable} sitting on
     * top.
     *
     * @return JScrollPane with the revisions history table as main component
     */
    private JComponent createRevisionsPanel() {
        try {
            final RevisionHistoryTableModel tableModel = new RevisionHistoryTableModel(accuRevDAOFacade.getRevisionHistory(virtualFile));
            this.revisionHistoryTable = new JTable(tableModel);
        } catch (AccuRevRuntimeException e) {
            log.error(e.getLocalizedMessage(), e);
            Messages.showErrorDialog(project, e.getCommandResult().toString(), e.getCommandResult().getErr());
            this.revisionHistoryTable = new JTable();
        }
        this.revisionHistoryTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        this.revisionHistoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ListSelectionModel rowSelectionModel = this.revisionHistoryTable.getSelectionModel();
        rowSelectionModel.addListSelectionListener(new RevisionHistoryTableListSelectionListener());
        return new JScrollPane(revisionHistoryTable);
    }

    /**
     * @see ListSelectionListener
     */
    private class RevisionHistoryTableListSelectionListener implements ListSelectionListener {
        /**
         * Handle the selection change in the revision history table
         * @param event
         */
        public void valueChanged(final ListSelectionEvent event) {
            // ignore extra messages.
            if (event.getValueIsAdjusting())
                return;

            // get list selection model from the event
            ListSelectionModel lsm = (ListSelectionModel)event.getSource();

            // if nothing is selected, return
            if (lsm.isSelectionEmpty()) {
                return;
            } else {
                // get the selected row from the model
                final int selectedRow = lsm.getMinSelectionIndex();
                final RevisionHistoryTableModel model = (RevisionHistoryTableModel) revisionHistoryTable.getModel();
                final AccuRevTransaction transaction = model.getAccuRevTransactionAt(selectedRow);
                if(log.isDebugEnabled()) {
                    log.debug("Selected row is ["+selectedRow+"], corresponding object is ["+transaction+"]");
                }
                try {
                    leftDataElement.setDiffContent(accuRevDAOFacade.getDiffContentByVersion(virtualFile,transaction));
                } catch (AccuRevRuntimeException e) {
                    // Log the error and full exception stack trace
                    log.error(e.getLocalizedMessage(), e);
                    Messages.showErrorDialog(project, e.getCommandResult().toString(), e.getCommandResult().getErr());
                }
                // redraw the diff
                populateDiffPanel();
            }
        }
    }
}
