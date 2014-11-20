/*
 * AccuRevVcs.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on May 26, 2005
 */
package net.java.accurev4idea;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.AbstractVcs;
import com.intellij.openapi.vcs.AbstractVcsCapabilities;
import com.intellij.openapi.vcs.EditFileProvider;
import com.intellij.openapi.vcs.FileStatusProvider;
import com.intellij.openapi.vcs.TransactionProvider;
import com.intellij.openapi.vcs.UpToDateRevisionProvider;
import com.intellij.openapi.vcs.VcsConfiguration;
import com.intellij.openapi.vcs.VcsException;
import com.intellij.openapi.vcs.history.VcsHistoryProvider;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import net.java.accurev4idea.dao.AccuRevDAOFacade;
import net.java.accurev4idea.gui.GuiUtils;
import net.java.accurev4idea.listeners.AccuRevFileEditorManagerListener;
import net.java.accurev4idea.providers.AccuRevEditFileProvider;
import net.java.accurev4idea.providers.AccuRevFileStatusProvider;
import net.java.accurev4idea.providers.AccuRevHistoryProvider;
import net.java.accurev4idea.providers.AccuRevTransactionProvider;
import net.java.accurev4idea.providers.AccuRevUpToDateRevisionProvider;
import net.java.savant.plugin.scm.accurev.components.Depot;
import net.java.savant.plugin.scm.accurev.exceptions.AccuRevRuntimeException;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import java.awt.BorderLayout;

/**
 * This implementation is a {@link ProjectComponent} plugin for AccuRev
 *
 * @author Igor Fedulov
 * @author Alex Antonov
 * @version $Id: AccuRevVcs.java,v 1.8 2005/07/12 11:28:52 ifedulov Exp $
 * @since 0.1
 */
public class AccuRevVcs extends AbstractVcs implements ProjectComponent {
    /**
     * Log audit channel
     */
    private static final Logger log = Logger.getInstance(AccuRevVcs.class.getName());

    /**
     * Reference to the accurev DAO
     */
    private final AccuRevDAOFacade accurev;

    /**
     * Name of the plugin to show up on the toolbar
     */
    private static final String TOOL_WINDOW_ID = "AccuRev";

    /**
     * AccuRev menu text, "c" is the shortcut character
     */
    private static final String MENU_ITEM_TEXT = "Ac_cuRev";

    /**
     * Reference to the content panel for this plugin
     */
    private JPanel accuRevContentPanel;

    /**
     * Reference to the output text
     */
    private JTextArea outputText;

    private final AbstractVcsCapabilities vcsCapabilities = new AccuRevVcsCapabilities();
    private final AccuRevTransactionProvider transactionProvider = new AccuRevTransactionProvider();
    private final AccuRevEditFileProvider editFileProvider = new AccuRevEditFileProvider();
    private final AccuRevFileStatusProvider fileStatusProvider;
    private final AccuRevUpToDateRevisionProvider upToDateRevisionProvider;
    private AccuRevFileEditorManagerListener fileEditorManagerListener;
    private final VcsConfiguration configuration;

    /**
     * @see ProjectComponent
     */
    public AccuRevVcs(Project project) {
        super(project);
        log.debug("Project component created with [" + project.getProjectFile() + "]");
        this.fileStatusProvider = new AccuRevFileStatusProvider(this);
        this.upToDateRevisionProvider = new AccuRevUpToDateRevisionProvider(this);
        this.accurev = new AccuRevDAOFacade(this);

        this.configuration = VcsConfiguration.createEmptyConfiguration();

        // following in theory should be read from configuration!
        getConfiguration().FORCE_NON_EMPTY_COMMENT = true;
        getConfiguration().PUT_FOCUS_INTO_COMMENT = true;
        getConfiguration().SHOW_CHECKIN_OPTIONS = true;
    }

    /**
     * @see com.intellij.openapi.components.ProjectComponent#projectOpened()
     */
    public void projectOpened() {
        log.debug("Project opened");
    }

    /**
     * @see com.intellij.openapi.components.ProjectComponent#projectClosed()
     */
    public void projectClosed() {
        log.debug("Project closed");
    }

    /**
     * Returns value from {@link Class#getName()}
     *
     * @see com.intellij.openapi.components.BaseComponent#getComponentName()
     */
    public String getComponentName() {
        return AccuRevVcs.class.getName();
    }

    /**
     * @see com.intellij.openapi.components.BaseComponent#initComponent()
     */
    public void initComponent() {
        log.debug("Init component");
    }

    /**
     * @see com.intellij.openapi.components.BaseComponent#disposeComponent()
     */
    public void disposeComponent() {
        log.debug("Dispose component");
    }

    /* AbstractVcs Method Implementations */
    public String getName() {
        return "AccuRev";
    }

    public String getDisplayName() {
        return "AccuRev";
    }

    public Configurable getConfigurable() {
        log.debug("Returing the configurable");
        return new AccuRevConfigurable(this);
    }

    public AbstractVcsCapabilities getCapabilities() {
        return vcsCapabilities;
    }

    public TransactionProvider getTransactionProvider() {
        return transactionProvider;
    }

    public EditFileProvider getEditFileProvider() {
        return editFileProvider;
    }

    public FileStatusProvider getFileStatusProvider() {
        return fileStatusProvider;
    }

    public UpToDateRevisionProvider getUpToDateRevisionProvider() {
        return upToDateRevisionProvider; //LocalVcsServices.getInstance(myProject).getUpToDateRevisionProvider();
    }

    public boolean markExternalChangesAsUpToDate() {
        return false;
    }

    public String getMenuItemText() {
        return MENU_ITEM_TEXT;
    }

    public VcsHistoryProvider getVcsHistoryProvider() {
        return new AccuRevHistoryProvider(this);
    }

    public void checkinFile(String s, Object o) throws VcsException {
        if (log.isDebugEnabled()) {
            log.debug("checkinFile: s=[" + s + "], o=[" + o + "]");
        }
    }

    public void addFile(String s, String s1, Object o) throws VcsException {
        if (log.isDebugEnabled()) {
            log.debug("addFile: s=[" + s + "], s1=[" + s1 + "], o=[" + o + "]");
        }
    }

    public void removeFile(String s, Object o) throws VcsException {
        if (log.isDebugEnabled()) {
            log.debug("removeFile: s=[" + s + "], o=[" + o + "]");
        }

        try {
            try {
                // first reomve all the changes on the file.
            } catch (Exception e) {
                // igonre all the exceptions...
            }

            // now defunct the file

            // ok and add the file to the change pallete

        } catch (AccuRevRuntimeException e) {
            log.error(e.getLocalizedMessage(), e);
            throw new VcsException(e.getMessage());
        }

    }

    public void addDirectory(String s, String s1, Object o) throws VcsException {
        if (log.isDebugEnabled()) {
            log.debug("addDirectory: s=[" + s + "], s1=[" + s1 + "], o=[" + o + "]");
        }
    }

    public void removeDirectory(String s, Object o) throws VcsException {
        if (log.isDebugEnabled()) {
            log.debug("removeDirectory: s=[" + s + "], o=[" + o + "]");
        }
    }

    public void appendTextLineToOutput(String text) {
        outputText.append("\n" + text);
    }

    public void clearTextOutput() {
        outputText.setText("");
    }

    public AccuRevDAOFacade getAccuRevDAO() {
        return accurev;
    }

    public VcsConfiguration getConfiguration() {
        return configuration;
    }

    /**
     * Initilalize the content panel and register it with the tool window manager
     * {@link ToolWindowManager#getInstance(com.intellij.openapi.project.Project)}, create a {@link JTree}
     * will all root nodes as {@link Depot}s in current accurev configuration.
     */
    private void registerToolWindow() {
        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(myProject);
        ToolWindow toolWindow = toolWindowManager.getToolWindow(TOOL_WINDOW_ID);
        if (toolWindow != null) {
            // already registered
            return;
        }

        accuRevContentPanel = new JPanel(new BorderLayout());

        toolWindow = toolWindowManager.registerToolWindow(TOOL_WINDOW_ID, accuRevContentPanel, ToolWindowAnchor.BOTTOM);
        toolWindow.setTitle("AccuRev Command History");
        toolWindow.setIcon(GuiUtils.ACCUREV_ICON_16x16);

        outputText = new JTextArea();
        outputText.setEditable(false);

        accuRevContentPanel.add(new JScrollPane(outputText));
    }

    /**
     * Remove the tool window registration. This method is called once the project is closed
     *
     * @see #projectClosed()
     */
    private void unregisterToolWindow() {
        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(myProject);
        ToolWindow toolWindow = toolWindowManager.getToolWindow(TOOL_WINDOW_ID);
        if (toolWindow != null) {
            toolWindowManager.unregisterToolWindow(TOOL_WINDOW_ID);
        }
    }

    public Project getProject() {
        return myProject;
    }

    public void start() throws VcsException {
        super.start();
        // verify that accurev is available
        try {
            accurev.getInfo();
        } catch (AccuRevRuntimeException e) {
            // Log the error and full exception stack trace
            log.error(e.getLocalizedMessage(), e);
            throw new VcsException(e.getCommandResult().getErr());
        }
        this.fileEditorManagerListener = new AccuRevFileEditorManagerListener();
        FileEditorManager.getInstance(myProject).addFileEditorManagerListener(fileEditorManagerListener);
        registerToolWindow();
    }

    public void shutdown() throws VcsException {
        super.shutdown();
        if(fileEditorManagerListener != null) {
            FileEditorManager.getInstance(myProject).removeFileEditorManagerListener(fileEditorManagerListener);
        }
        unregisterToolWindow();
    }

}
