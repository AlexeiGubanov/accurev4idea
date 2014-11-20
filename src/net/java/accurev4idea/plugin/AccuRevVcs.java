package net.java.accurev4idea.plugin;

import com.intellij.openapi.vcs.*;
//import com.intellij.openapi.vcs.fileView.FileViewEnvironment;
import com.intellij.openapi.vcs.diff.DiffProvider;
import com.intellij.openapi.vcs.impl.FileStatusProvider;
import com.intellij.openapi.vcs.update.UpdateEnvironment;
import com.intellij.openapi.vcs.checkin.CheckinEnvironment;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
//import com.intellij.localVcs.fileView.LvcsFileViewEnvironment;

import java.util.Map;
import java.util.LinkedList;
import java.util.List;

import net.java.accurev4idea.plugin.providers.AccuRevFileStatusProvider;
import net.java.accurev4idea.plugin.providers.AccuRevDiffProvider;
import net.java.accurev4idea.plugin.providers.AccuRevUpToDateRevisionsProvider;
import net.java.accurev4idea.plugin.gui.AccuRevInfoPane;
import net.java.accurev4idea.plugin.gui.GuiUtils;
import net.java.accurev4idea.api.AccuRev;
import org.apache.log4j.Logger;

/**
 * $Id: AccuRevVcs.java,v 1.6 2006/02/21 22:50:06 ifedulov Exp $
 * User: aantonov
 * Date: Oct 25, 2005
 * Time: 10:20:21 AM
 */
public class AccuRevVcs extends AbstractVcs
  implements ProjectComponent, EditFileProvider, TransactionProvider /*TODO , StandardOperationsProvider */ {

    /**
     * Log4j audit channel
     */
    private static Logger log = Logger.getLogger(AccuRevVcs.class);

    private AccuRevConfigurable configurable;

    private AccuRevFileStatusProvider fileStatusProvider;
    private AccuRevDiffProvider diffProvider;

    private AccuRevCheckInEnvironment checkinEnvironment;
    private AccuRevUpdateEnvironment updateEnvironment;
    private AccuRevStatusEnvironment statusEnvironment;
//    private LvcsFileViewEnvironment fileViewEnvironment;
//    private UpToDateRevisionProvider upToDateRevisionProvider;

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
    private AccuRevInfoPane infoPane;

    public AccuRevVcs(Project project) {
        super(project, "AccuRev");
    }

    public Project getProject() {
        return myProject;
    }

    public FileStatusProvider getFileStatusProvider() {
        return fileStatusProvider;
    }

    public DiffProvider getDiffProvider() {
        return diffProvider;
    }

    // TODO: Not sure if this is needed, but let's leave it here for now, just in case... :-)
//TODO    public FileRenameProvider getFileRenamer() {
//        return new FileRenameProvider() {
//            public void renameAndCheckInFile(String path, String newName, Object parameters) throws VcsException {
//                String file = path;
//                //AccuRev.moveFile(sourceFile, destinationFile, vcs.getCommandExecListeners());
//            }
//        };
//    }

    public String getDisplayName() {
        return this.getName();
    }

    public Configurable getConfigurable() {
        return configurable;
    }

    public static AccuRevVcs getInstance(Project project) {
        return project.getComponent(AccuRevVcs.class);
    }

    public void projectOpened() {
        //To change body of implemented methods use File | Settings | File Templates.

        // Register the Content Pane to keep track of the command history
        registerToolWindow();
    }

    public void projectClosed() {
        //To change body of implemented methods use File | Settings | File Templates.

        // Upon closing the project need to deregister the Content Pane
        unregisterToolWindow();
    }

    public String getComponentName() {
        return "AccuRev";
    }

    public void initComponent() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void disposeComponent() {
        //TODO fileViewEnvironment = null;
    }

    public void editFiles(VirtualFile[] files) throws VcsException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getRequestText() {
        return "Would you like to invoke 'CheckOut' command?";
    }

    public void startTransaction(Object parameters) throws VcsException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void commitTransaction(Object parameters) throws VcsException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void rollbackTransaction(Object parameters) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void checkinFile(String path, Object parameters, Map userData) throws VcsException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void addFile(String folderPath, String name, Object parameters, Map userData) throws VcsException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void removeFile(String path, Object parameters, Map userData) throws VcsException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void addDirectory(String parentPath, String name, Object parameters, Map userData) throws VcsException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void removeDirectory(String path, Object parameters, Map userData) throws VcsException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public CheckinEnvironment getCheckinEnvironment() {
        // If this is not null, then the "Version Control" menu will enable 'Commit Project...' option
        return checkinEnvironment;
    }

    public UpdateEnvironment getUpdateEnvironment() {
        // If this is not null, then the "Version Control" menu will enable 'Update Project...' option
        return updateEnvironment;
    }

    public UpdateEnvironment getStatusEnvironment() {
        // If this is not null, then the "Version Control" menu will enable 'Check Project Status...' option
        return statusEnvironment;    //To change body of overridden methods use File | Settings | File Templates.
    }

//TODO    public FileViewEnvironment getFileViewEnvironment() {
//        return fileViewEnvironment;
//    }

//TODO    public UpToDateRevisionProvider getUpToDateRevisionProvider() {
//        return upToDateRevisionProvider;
//    }

    public void shutdown() throws VcsException {
        configurable = null;
        infoPane = null;

        fileStatusProvider = null;
        diffProvider = null;

        checkinEnvironment = null;
        updateEnvironment = null;
        statusEnvironment = null;
//TODO        fileViewEnvironment = null;
//        upToDateRevisionProvider = null;

        super.shutdown();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void start() throws VcsException {
        configurable = new AccuRevConfigurable(myProject);
        infoPane = new AccuRevInfoPane(this);

        fileStatusProvider = new AccuRevFileStatusProvider(this);
        diffProvider = new AccuRevDiffProvider(this);

        checkinEnvironment = new AccuRevCheckInEnvironment(this);
        updateEnvironment = new AccuRevUpdateEnvironment(this);
        statusEnvironment = new AccuRevStatusEnvironment(this);
//TODO        fileViewEnvironment = new LvcsFileViewEnvironment(myProject);
//TODO        upToDateRevisionProvider = new AccuRevUpToDateRevisionsProvider(this);

        super.start();
    }

    /**
     * This method provides the ability to add/remove/override the possible FileStatus options available under AccuRev
     * @return FileStatus array of supported file statuses
     */
    public FileStatus[] getProvidedStatuses() {
        return super.getProvidedStatuses();
    }

    public List getCommandExecListeners() {
        List listeners = new LinkedList();
        listeners.add(infoPane);

        return listeners;
    }

    /**
     * Initilalize the content panel and register it with the tool window manager
     * {@link ToolWindowManager#getInstance(com.intellij.openapi.project.Project)}, create a {@link javax.swing.JTree}
     * will all root nodes as {@link net.java.accurev4idea.api.components.Depot}s in current accurev configuration.
     */
    private void registerToolWindow() {
        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(myProject);
        ToolWindow toolWindow = toolWindowManager.getToolWindow(TOOL_WINDOW_ID);
        if (toolWindow != null) {
            // already registered
            return;
        }

        // Check to make sure the project has AccuRev selected as a project VCS
        final ProjectLevelVcsManager vcsManager = ProjectLevelVcsManager.getInstance(myProject);
        if (!vcsManager.checkVcsIsActive(AccuRevVcs.getInstance(myProject))) {
            // this project does not have AccuRev as one of active VCS providers
            return;
        }

        toolWindow = toolWindowManager.registerToolWindow(TOOL_WINDOW_ID, infoPane.getComponent(), ToolWindowAnchor.BOTTOM);
        toolWindow.setTitle("AccuRev Command History");
        toolWindow.setIcon(GuiUtils.ACCUREV_ICON_16x16);
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
}
