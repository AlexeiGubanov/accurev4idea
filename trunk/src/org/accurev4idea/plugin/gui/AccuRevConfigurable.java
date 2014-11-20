package org.accurev4idea.plugin.gui;

import com.intellij.openapi.options.BaseConfigurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import net.java.accurev4idea.api.AccuRev;
import net.java.accurev4idea.api.AccuRevExecutable;
import net.java.accurev4idea.api.exceptions.AccuRevRuntimeException;
import org.accurev4idea.plugin.AccuRevConfiguration;
import org.accurev4idea.plugin.AccuRevVcs;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class AccuRevConfigurable extends BaseConfigurable {

  private static Logger log = Logger.getLogger(AccuRevConfigurable.class);

  private JPanel mainPanel;
  private TextFieldWithBrowseButton accurevPathTxt;
  private JButton testButton;
  private JCheckBox showAccuRevOutputChk;
  private JCheckBox enableAutoAddChk;

  private Project project;
  private AccuRevConfiguration configuration;

  public AccuRevConfigurable(Project project, AccuRevConfiguration configuration) {
    this.project = project;
    this.configuration = configuration;
    loadConfigurationFrom(configuration);

    // check if accurev path in loaded config is not empty, if it's not empty
    // make sure to set the accurev executable accordingly, otherwise modify the
    // config with default location of accurev binary
    // TODO
//    if (StringUtils.isNotBlank(config.getAccurevExecPath())) {
//      AccuRevExecutable.setAbsolutePath(config.getAccurevExecPath());
//    } else {
//      config.setAccurevExecPath(AccuRevExecutable.getAbsolutePath());
//    }
  }

  private void saveConfigurationTo(AccuRevConfiguration config) {
    config.setAccurevExecPath(accurevPathTxt.getText());
    config.setShowAccuRevOutput(showAccuRevOutputChk.isSelected());
    config.setEnableAutoAdd(enableAutoAddChk.isSelected());
  }

  private void loadConfigurationFrom(AccuRevConfiguration config) {
    accurevPathTxt.setText(config.getAccurevExecPath());
    showAccuRevOutputChk.setSelected(config.isShowAccuRevOutput());
    enableAutoAddChk.setSelected(config.isEnableAutoAdd());
  }

  @Nls
  @Override
  public String getDisplayName() {
    return AccuRevVcs.NAME;
  }


  @Nullable
  @Override
  public String getHelpTopic() {
    // TODO
    return null;
  }

  @Nullable
  @Override
  public JComponent createComponent() {
    // TODO add action listener on testButton
//    testButton.addActionListener(new ActionListener() {
//      public void actionPerformed(ActionEvent event) {
//        try {
//          AccuRevExecutable.setAbsolutePath(accurevPathTxt.getText());
//          AccuRev.getInfo();
//          Messages.showInfoMessage("AccuRev connection was successful!", "AccuRev Connection");
//        } catch (IllegalArgumentException e) {
//          log.error(e.getLocalizedMessage(), e);
//          Messages.showErrorDialog(e.getLocalizedMessage(), "AccuRev Connection");
//        } catch (AccuRevRuntimeException e) {
//          log.error(e.getLocalizedMessage(), e);
//          Messages.showErrorDialog(e.getCommandResult().getErr(), "AccuRev Connection");
//        }
//      }
//    });

    // add action listener for the accurevPathTxt field
    accurevPathTxt.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        // create new file chooser
        JFileChooser fileChooser = new JFileChooser();
        // obtain all current (default) file filters from the chooser
        FileFilter[] filters = fileChooser.getChoosableFileFilters();
        // remove all filters first, to add later only accurev binary file filter
        for (int i = 0; i < filters.length; i++) {
          fileChooser.removeChoosableFileFilter(filters[i]);
        }
        // add filter that only accepts filen with name "accurev"
        fileChooser.addChoosableFileFilter(new FileFilter() {
          public boolean accept(File f) {
            // if file is a directory show it in the list to allow
            // descend into directories
            if (f.isDirectory()) {
              return true;
            }
            // file is a "file", check if it's name
            // starts with "accurev" (which on windows might be "accurevw.exe")
            return f.getName().toLowerCase().indexOf("accurev") > -1;
          }

          /**
           * @return a string to show in the filter dropdown
           * @see javax.swing.filechooser.FileFilter#getDescription()
           */
          public String getDescription() {
            return "AccuRev executable";
          }
        });
        // if user pressed something other then "OK" or "APPLY" button, simply return
        if (JFileChooser.APPROVE_OPTION != fileChooser.showOpenDialog(WindowManager.getInstance().suggestParentWindow(project))) {
          return;
        }
        // pick the selected file from the file chooser
        File selection = fileChooser.getSelectedFile();
        // store it in the accurevPathTxt field
        accurevPathTxt.setText(selection.getAbsolutePath());
      }
    });

    return mainPanel;
  }

  @Override
  public void apply() throws ConfigurationException {
    saveConfigurationTo(configuration);
  }

  @Override
  public void reset() {
    loadConfigurationFrom(configuration);
  }

  @Override
  public boolean isModified() {
    AccuRevConfiguration tmp = new AccuRevConfiguration();
    saveConfigurationTo(tmp);
    return !tmp.equals(configuration);
  }

  @Override
  public void disposeUIResources() {
    //foo mainPanel = null;
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
    mainPanel.setBorder(BorderFactory.createTitledBorder("AccuRev Plugin Settings"));
    final Spacer spacer1 = new Spacer();
    mainPanel.add(spacer1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null));
    final JPanel panel1 = new JPanel();
    panel1.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
    mainPanel.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null));
    final JLabel label1 = new JLabel();
    label1.setText("AccuRev Executable Path:");
    panel1.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null));
    accurevPathTxt = new TextFieldWithBrowseButton();
    accurevPathTxt.setText("");
    panel1.add(accurevPathTxt, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null));
    testButton = new JButton();
    testButton.setText("Test Settings");
    panel1.add(testButton, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null));
    final JLabel label2 = new JLabel();
    label2.setText("Show AccuRev Output:");
    panel1.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null));
    final JPanel panel2 = new JPanel();
    panel2.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
    panel1.add(panel2, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null));
    showAccuRevOutputChk = new JCheckBox();
    showAccuRevOutputChk.setText("");
    panel2.add(showAccuRevOutputChk, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(20, -1), null));
    final JLabel label3 = new JLabel();
    label3.setText("(Changing this value requires restart)");
    panel2.add(label3, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null));
    final Spacer spacer2 = new Spacer();
    panel2.add(spacer2, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null));
  }
}
