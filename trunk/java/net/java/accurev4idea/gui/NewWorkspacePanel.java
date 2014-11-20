package net.java.accurev4idea.gui;

import net.java.accurev4idea.AccuRevVcs;
import net.java.savant.Newline;
import net.java.savant.plugin.scm.Locking;

import javax.swing.*;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;

/**
 * $Id: NewWorkspacePanel.java,v 1.3 2005/07/13 22:30:11 ifedulov Exp $
 * User: aantonov
 * Date: Jun 24, 2005
 * Time: 4:28:47 PM
 */
public class NewWorkspacePanel extends JPanel {
    private JTextField _workspaceName;
    private JLabel _workspaceNameLbl;

    private JLabel _workspaceFileLbl;
    private JButton _workspaceFileBtn;

    private JRadioButton _workspaceFileSelected;
    private JRadioButton _workspaceFileNew;
    private boolean _workspaceSelectedFile;

    private ButtonGroup _workspaceNewlineRbtn;
    private JRadioButton _workspaceNewlineDefault;
    private JRadioButton _workspaceNewlineUnix;
    private JRadioButton _workspaceNewlineMac;
    private JRadioButton _workspaceNewlineWindows;

    private ButtonGroup _workspaceLockingRbtn;
    private JRadioButton _workspaceLockingDefault;
    private JRadioButton _workspaceLockingAnchor;
    private JRadioButton _workspaceLockingExclusive;

    private AccuRevVcs vcs;

    public NewWorkspacePanel(AccuRevVcs vcs) {
        super();
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        initializePanel();
    }

    public String getNewWorkspaceName() {
        return _workspaceName.getText();
    }

    public String getWorkspaceStorageDirectory() {
        if (_workspaceFileNew.isSelected()) {
            return _workspaceFileNew.getText();
        } else if (_workspaceFileSelected.isSelected()) {
            return _workspaceFileSelected.getText();
        }
        return null;
    }

    public Newline getNewWorkspaceNewline() {
        if (_workspaceNewlineUnix.isSelected()) {
            return Newline.UNIX;
        } else if (_workspaceNewlineMac.isSelected()) {
            return Newline.MAC;
        } else if (_workspaceNewlineWindows.isSelected()) {
            return Newline.WINDOWS;
        }
        return Newline.DEFAULT;
    }

    public Locking getNewWorkspaceLocking() {
        if (_workspaceLockingAnchor.isSelected()) {
            return Locking.ANCHOR;
        } else if (_workspaceLockingExclusive.isSelected()) {
            return Locking.EXCLUSIVE;
        }
        return Locking.DEFAULT;
    }

    private void initializePanel() {
        JPanel workspaceName = new JPanel(new BorderLayout());
        workspaceName.setBorder(BorderFactory.createTitledBorder("Enter new workspace name"));
        //workspaceName.setPreferredSize(new Dimension(300, 50));

        _workspaceNameLbl = new JLabel("Workspace name");
        _workspaceName = new JTextField(15);

        workspaceName.add(_workspaceNameLbl, BorderLayout.WEST);
        workspaceName.add(_workspaceName, BorderLayout.EAST);

        add(workspaceName);

        // Selecting the directory where to save the workspace.
        JPanel workspaceFile = new JPanel(new BorderLayout());
        JPanel workspaceFileButton = new JPanel(new BorderLayout());
        JPanel workspaceFileChoose = new JPanel(new BorderLayout());

        ButtonGroup workspaceFileGroup = new ButtonGroup();
        _workspaceFileSelected = new JRadioButton();
        _workspaceFileNew = new JRadioButton();

        _workspaceFileSelected.setEnabled(false);
        _workspaceFileNew.setEnabled(false);

        workspaceFileGroup.add(_workspaceFileSelected);
        workspaceFileGroup.add(_workspaceFileNew);

        workspaceFileChoose.add(_workspaceFileSelected, BorderLayout.NORTH);
        workspaceFileChoose.add(_workspaceFileNew, BorderLayout.SOUTH);

        workspaceFile.setBorder(BorderFactory.createTitledBorder("Select workspace storage directory"));

        _workspaceFileLbl = new JLabel("Workspace directory");
        _workspaceFileBtn = new JButton("Select directory");
        _workspaceFileBtn.setEnabled(false);

        _workspaceFileBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                int ret = fc.showOpenDialog(NewWorkspacePanel.this);

                if (ret == JFileChooser.APPROVE_OPTION) {
                    File workspaceSelectedFile = fc.getSelectedFile();
                    _workspaceSelectedFile = true;
                    _workspaceFileSelected.setText(workspaceSelectedFile.getAbsolutePath());
                    _workspaceFileNew.setText(workspaceSelectedFile.getAbsolutePath() + System.getProperty("file.separator") + getNewWorkspaceName());

                    _workspaceFileSelected.setEnabled(true);
                    _workspaceFileNew.setEnabled(true);
                }
            }
        });

        _workspaceName.addCaretListener(new CaretListener() {
            public void caretUpdate(CaretEvent e) {
                if (_workspaceName.getText().length() > 0) {
                    _workspaceFileBtn.setEnabled(true);
                    if (_workspaceSelectedFile) {
                        _workspaceFileNew.setText(_workspaceFileSelected.getText() + System.getProperty("file.separator") + getNewWorkspaceName());
                    }
                } else {
                    _workspaceFileBtn.setEnabled(false);
                }
            }
        });

        workspaceFileButton.add(_workspaceFileLbl, BorderLayout.WEST);
        workspaceFileButton.add(_workspaceFileBtn, BorderLayout.EAST);

        workspaceFileChoose.add(_workspaceFileSelected, BorderLayout.NORTH);
        workspaceFileChoose.add(_workspaceFileNew, BorderLayout.SOUTH);

        workspaceFile.add(workspaceFileButton, BorderLayout.NORTH);
        workspaceFile.add(workspaceFileChoose, BorderLayout.SOUTH);

        add(workspaceFile);

        // Selecting the NewLine type
        JPanel workspaceNewline = new JPanel(new FlowLayout());
        workspaceNewline.setBorder(BorderFactory.createTitledBorder("Select new line termination"));

        _workspaceNewlineRbtn = new ButtonGroup();

        _workspaceNewlineDefault = new JRadioButton("Default");
        _workspaceNewlineUnix = new JRadioButton("Unix");
        _workspaceNewlineMac = new JRadioButton("Mac");
        _workspaceNewlineWindows = new JRadioButton("Windows");

        _workspaceNewlineRbtn.add(_workspaceNewlineDefault);
        _workspaceNewlineRbtn.add(_workspaceNewlineUnix);
        _workspaceNewlineRbtn.add(_workspaceNewlineMac);
        _workspaceNewlineRbtn.add(_workspaceNewlineWindows);

        _workspaceNewlineDefault.setSelected(true);

        workspaceNewline.add(_workspaceNewlineDefault);
        workspaceNewline.add(_workspaceNewlineUnix);
        workspaceNewline.add(_workspaceNewlineMac);
        workspaceNewline.add(_workspaceNewlineWindows);

        add(workspaceNewline);

        // Selecting the Locking type
        JPanel workspaceLocking = new JPanel(new FlowLayout());
        workspaceLocking.setBorder(BorderFactory.createTitledBorder("Select the locking mode"));

        _workspaceLockingRbtn = new ButtonGroup();

        _workspaceLockingDefault = new JRadioButton("Default");
        _workspaceLockingAnchor = new JRadioButton("Anchor");
        _workspaceLockingExclusive = new JRadioButton("Exclusive");

        _workspaceLockingRbtn.add(_workspaceLockingDefault);
        _workspaceLockingRbtn.add(_workspaceLockingAnchor);
        _workspaceLockingRbtn.add(_workspaceLockingExclusive);

        _workspaceLockingDefault.setSelected(true);

        workspaceLocking.add(_workspaceLockingDefault);
        workspaceLocking.add(_workspaceLockingAnchor);
        workspaceLocking.add(_workspaceLockingExclusive);

        add(workspaceLocking);

        add(Box.createVerticalStrut(130));
    }
}
