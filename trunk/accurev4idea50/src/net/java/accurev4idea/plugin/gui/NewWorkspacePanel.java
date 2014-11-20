package net.java.accurev4idea.plugin.gui;

import net.java.accurev4idea.api.Newline;
import net.java.accurev4idea.api.components.Locking;

import javax.swing.*;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;

import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.Spacer;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;
import java.io.File;

/**
 * $Id: NewWorkspacePanel.java,v 1.3 2005/11/07 18:32:33 ifedulov Exp $
 * User: aantonov
 * Date: Nov 3, 2005
 * Time: 4:07:10 PM
 */
public class NewWorkspacePanel {
    private JPanel mainPanel;
    private JPanel workspaceName;
    private JPanel workspaceFile;
    private JPanel workspaceNewline;
    private JPanel workspaceLocking;

    private JTextField workspaceNameTxt;
    private JButton workspaceFileBtn;

    private ButtonGroup workspaceFileRbtn;
    private JRadioButton workspaceFileSelectedRtn;
    private JRadioButton workspaceFileNewRtn;

    private boolean isWorkspaceSelectedFile = false;

    private ButtonGroup workspaceNewlineRbtn;
    private JRadioButton workspaceNewlineDefaultRtn;
    private JRadioButton workspaceNewlineUnixRtn;
    private JRadioButton workspaceNewlineMacRtn;
    private JRadioButton workspaceNewlineWindowsRtn;

    private ButtonGroup workspaceLockingRbtn;
    private JRadioButton workspaceLockingDefaultRtn;
    private JRadioButton workspaceLockingAnchorRtn;
    private JRadioButton workspaceLockingExclusiveRtn;

    public NewWorkspacePanel() {
        workspaceFileBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                int ret = fc.showOpenDialog(getComponent());

                if (ret == JFileChooser.APPROVE_OPTION) {
                    File workspaceSelectedFile = fc.getSelectedFile();
                    isWorkspaceSelectedFile = true;
                    workspaceFileSelectedRtn.setText(workspaceSelectedFile.getAbsolutePath());
                    workspaceFileNewRtn.setText(workspaceSelectedFile.getAbsolutePath() + System.getProperty("file.separator") + getNewWorkspaceName());

                    workspaceFileSelectedRtn.setEnabled(true);
                    workspaceFileNewRtn.setEnabled(true);
                }
            }
        });

        workspaceNameTxt.addCaretListener(new CaretListener() {
            public void caretUpdate(CaretEvent e) {
                if (workspaceNameTxt.getText().length() > 0) {
                    workspaceFileBtn.setEnabled(true);
                    if (isWorkspaceSelectedFile) {
                        workspaceFileNewRtn.setText(workspaceFileSelectedRtn.getText() + System.getProperty("file.separator") + getNewWorkspaceName());
                    }
                } else {
                    workspaceFileBtn.setEnabled(false);
                }
            }
        });

        // Group Buttons
        workspaceFileRbtn = new ButtonGroup();
        workspaceFileRbtn.add(workspaceFileNewRtn);
        workspaceFileRbtn.add(workspaceFileSelectedRtn);

        workspaceNewlineRbtn = new ButtonGroup();
        workspaceNewlineRbtn.add(workspaceNewlineDefaultRtn);
        workspaceNewlineRbtn.add(workspaceNewlineUnixRtn);
        workspaceNewlineRbtn.add(workspaceNewlineMacRtn);
        workspaceNewlineRbtn.add(workspaceNewlineWindowsRtn);

        workspaceLockingRbtn = new ButtonGroup();
        workspaceLockingRbtn.add(workspaceLockingDefaultRtn);
        workspaceLockingRbtn.add(workspaceLockingAnchorRtn);
        workspaceLockingRbtn.add(workspaceLockingExclusiveRtn);
    }

    public JComponent getComponent() {
        return mainPanel;
    }

    public String getNewWorkspaceName() {
        return workspaceNameTxt.getText();
    }

    public String getWorkspaceStorageDirectory() {
        if (workspaceFileNewRtn.isSelected()) {
            return workspaceFileNewRtn.getText();
        } else if (workspaceFileSelectedRtn.isSelected()) {
            return workspaceFileSelectedRtn.getText();
        }
        return null;
    }

    public Newline getNewWorkspaceNewline() {
        if (workspaceNewlineUnixRtn.isSelected()) {
            return Newline.UNIX;
        } else if (workspaceNewlineMacRtn.isSelected()) {
            return Newline.MAC;
        } else if (workspaceNewlineWindowsRtn.isSelected()) {
            return Newline.WINDOWS;
        }
        return Newline.DEFAULT;
    }

    public Locking getNewWorkspaceLocking() {
        if (workspaceLockingAnchorRtn.isSelected()) {
            return Locking.ANCHOR;
        } else if (workspaceLockingExclusiveRtn.isSelected()) {
            return Locking.EXCLUSIVE;
        }
        return Locking.DEFAULT;
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
        mainPanel.setLayout(new GridLayoutManager(5, 1, new Insets(0, 0, 0, 0), -1, -1));
        workspaceName = new JPanel();
        workspaceName.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(workspaceName, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null));
        workspaceName.setBorder(BorderFactory.createTitledBorder("Enter new workspace name"));
        final JLabel label1 = new JLabel();
        label1.setText("Workspace name:");
        workspaceName.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null));
        workspaceNameTxt = new JTextField();
        workspaceNameTxt.setColumns(15);
        workspaceName.add(workspaceNameTxt, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null));
        workspaceFile = new JPanel();
        workspaceFile.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(workspaceFile, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null));
        workspaceFile.setBorder(BorderFactory.createTitledBorder("Select workspace storage directory"));
        final JLabel label2 = new JLabel();
        label2.setText("Workspace directory:");
        workspaceFile.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        workspaceFile.add(panel1, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null));
        workspaceFileSelectedRtn = new JRadioButton();
        workspaceFileSelectedRtn.setText("");
        panel1.add(workspaceFileSelectedRtn, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        workspaceFile.add(panel2, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null));
        workspaceFileNewRtn = new JRadioButton();
        workspaceFileNewRtn.setText("");
        panel2.add(workspaceFileNewRtn, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null));
        workspaceFileBtn = new JButton();
        workspaceFileBtn.setEnabled(false);
        workspaceFileBtn.setText("Select directory");
        workspaceFile.add(workspaceFileBtn, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null));
        workspaceNewline = new JPanel();
        workspaceNewline.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(workspaceNewline, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null));
        workspaceNewline.setBorder(BorderFactory.createTitledBorder("Select new line termination"));
        workspaceNewlineDefaultRtn = new JRadioButton();
        workspaceNewlineDefaultRtn.setSelected(true);
        workspaceNewlineDefaultRtn.setText("Default");
        workspaceNewline.add(workspaceNewlineDefaultRtn, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null));
        workspaceNewlineUnixRtn = new JRadioButton();
        workspaceNewlineUnixRtn.setText("Unix");
        workspaceNewline.add(workspaceNewlineUnixRtn, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null));
        workspaceNewlineMacRtn = new JRadioButton();
        workspaceNewlineMacRtn.setText("Mac");
        workspaceNewline.add(workspaceNewlineMacRtn, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null));
        workspaceNewlineWindowsRtn = new JRadioButton();
        workspaceNewlineWindowsRtn.setText("Windows");
        workspaceNewline.add(workspaceNewlineWindowsRtn, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null));
        workspaceLocking = new JPanel();
        workspaceLocking.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(workspaceLocking, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null));
        workspaceLocking.setBorder(BorderFactory.createTitledBorder("Select the locking mode"));
        workspaceLockingDefaultRtn = new JRadioButton();
        workspaceLockingDefaultRtn.setSelected(true);
        workspaceLockingDefaultRtn.setText("Default");
        workspaceLocking.add(workspaceLockingDefaultRtn, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null));
        workspaceLockingAnchorRtn = new JRadioButton();
        workspaceLockingAnchorRtn.setText("Anchor");
        workspaceLocking.add(workspaceLockingAnchorRtn, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null));
        workspaceLockingExclusiveRtn = new JRadioButton();
        workspaceLockingExclusiveRtn.setText("Exclusive");
        workspaceLocking.add(workspaceLockingExclusiveRtn, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null));
        final Spacer spacer1 = new Spacer();
        mainPanel.add(spacer1, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null));
    }
}
