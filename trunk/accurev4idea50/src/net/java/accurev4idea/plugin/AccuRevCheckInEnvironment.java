package net.java.accurev4idea.plugin;

import com.intellij.openapi.vcs.checkin.CheckinEnvironment;
import com.intellij.openapi.vcs.checkin.RevisionsFactory;
import com.intellij.openapi.vcs.checkin.DifferenceType;
import com.intellij.openapi.vcs.checkin.VcsOperation;
import com.intellij.openapi.vcs.checkin.changeListBasedCheckin.CommitChangeOperation;
import com.intellij.openapi.vcs.RollbackProvider;
import com.intellij.openapi.vcs.FilePath;
import com.intellij.openapi.vcs.VcsException;
import com.intellij.openapi.vcs.CheckinProjectDialogImplementer;
import com.intellij.openapi.vcs.ui.RefreshableOnComponent;
import com.intellij.openapi.vcs.ui.Refreshable;
import com.intellij.openapi.vcs.versions.AbstractRevisions;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.project.Project;
import com.intellij.util.ui.ColumnInfo;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.GridConstraints;

import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.*;

import org.apache.log4j.Logger;
import net.java.accurev4idea.plugin.factories.AccuRevRevisionsFactory;
import net.java.accurev4idea.api.components.AccuRevFile;
import net.java.accurev4idea.api.components.AccuRevFileStatus;
import net.java.accurev4idea.api.AccuRev;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: igor
 * Date: Oct 30, 2005
 * Time: 12:36:42 AM
 * To change this template use File | Settings | File Templates.
 */
public class AccuRevCheckInEnvironment implements CheckinEnvironment, RefreshableOnComponent {

    private static final Logger log = Logger.getLogger(AccuRevCheckInEnvironment.class);

    private AccuRevVcs vcs;
    private JRadioButton promoteRtn;
    private JRadioButton keepRtn;
    private JPanel mainPanel;

    private ButtonGroup btnGroup;

    public AccuRevCheckInEnvironment(AccuRevVcs vcs) {
        this.vcs = vcs;

        btnGroup = new ButtonGroup();
        btnGroup.add(keepRtn);
        btnGroup.add(promoteRtn);
    }

    public JComponent getComponent() {
        return mainPanel;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void refresh() {

    }

    public void saveState() {

    }

    public void restoreState() {

    }

    public RevisionsFactory getRevisionsFactory() {
        return new AccuRevRevisionsFactory(vcs);
    }

    public RollbackProvider createRollbackProviderOn(AbstractRevisions[] selectedRevisions, final boolean containsExcluded) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public DifferenceType[] getAdditionalDifferenceTypes() {
        return new DifferenceType[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    public ColumnInfo[] getAdditionalColumns(int index) {
        return new ColumnInfo[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    public RefreshableOnComponent createAdditionalOptionsPanelForCheckinProject(Refreshable panel) {
        return this;
    }

    public RefreshableOnComponent createAdditionalOptionsPanelForCheckinFile(Refreshable panel) {
        //return this;
        return null;
    }

    public RefreshableOnComponent createAdditionalOptionsPanel(Refreshable panel, boolean checkinProject) {
        //return this;
        return null;
    }

    public String getDefaultMessageFor(FilePath[] filesToCheckin) {
        return "Default Message";  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void onRefreshFinished() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void onRefreshStarted() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public AnAction[] getAdditionalActions(int index) {
        return new AnAction[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String prepareCheckinMessage(String text) {
        return text;
    }

    public String getHelpId() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List commit(CheckinProjectDialogImplementer dialog, Project project) {
        return handleCommit(collectFilePaths(dialog.getCheckinProjectPanel().getCheckinOperations(this)),
                dialog.getPreparedComment(this), true, false);
    }

    public List commit(FilePath[] roots, Project project, String preparedComment) {
        return handleCommit(collectPaths(roots), preparedComment, false, true);
    }

    public String getCheckinOperationName() {
        return "Keep";  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean showCheckinDialogInAnyCase() {
        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private List handleCommit(List paths, final String comment, final boolean force, final boolean recursive) {
        return AccuRev.keepFiles(comment, paths, null, vcs.getCommandExecListeners());
    }

    private List collectPaths(FilePath[] roots) {
        ArrayList result = new ArrayList();
        for (int i = 0; i < roots.length; i++) {
            FilePath file = roots[i];
            result.add(file.getPath());
        }
        return result;
    }

    private List collectFilePaths(List checkinOperations) {
        ArrayList result = new ArrayList();
        for (Iterator iterator = checkinOperations.iterator(); iterator.hasNext();) {
            CommitChangeOperation operation = (CommitChangeOperation) iterator.next();
            AccuRevFile file = (AccuRevFile) operation.getChange();
            if (file.getStatus() == AccuRevFileStatus.MODIFIED) {
                result.add(operation.getFile().getAbsolutePath());
            }
        }
        return result;
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.setBorder(BorderFactory.createTitledBorder("AccuRev:"));
        keepRtn = new JRadioButton();
        keepRtn.setSelected(true);
        keepRtn.setText("Keep");
        mainPanel.add(keepRtn, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null));
        promoteRtn = new JRadioButton();
        promoteRtn.setText("Promote");
        mainPanel.add(promoteRtn, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null));
    }
}
