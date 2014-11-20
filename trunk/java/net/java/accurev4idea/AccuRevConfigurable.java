/*
 * AccuRevConfigurable.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on June 11, 2005
 */
package net.java.accurev4idea;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.JDOMExternalizable;
import com.intellij.openapi.vcs.VcsConfiguration;
import net.java.accurev4idea.gui.GuiUtils;
import net.java.savant.plugin.scm.accurev.AccuRevExecutable;
import net.java.savant.plugin.scm.accurev.exceptions.AccuRevRuntimeException;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Implementation for {@link Configurable} interface.
 * 
 * @see Configurable
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version $Id: AccuRevConfigurable.java,v 1.8 2005/07/12 00:57:47 ifedulov Exp $
 * @since 0.1
 */
public class AccuRevConfigurable extends VcsConfiguration implements Configurable, JDOMExternalizable {
    private static transient final Logger log = Logger.getInstance(AccuRevConfigurable.class.getName());
    
    private transient JPanel configurationPanel = new JPanel(){{
        // create etched border with title "AccuRev Configuration"
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "AccuRev Configuration"));
        final JButton testConnectionButton = new JButton("Test");
        final JLabel accuRevExecutableLabel = new JLabel("Executable location:");
        final JTextField accuRevExecutableLocation = new JTextField(AccuRevExecutable.getAbsolutePath());
        setLayout(new GridBagLayout());
        add(accuRevExecutableLabel);
        add(accuRevExecutableLocation);
        add(testConnectionButton);

        // bind events
        testConnectionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try {
                    AccuRevExecutable.setAbsolutePath(accuRevExecutableLocation.getText());
                    vcs.getAccuRevDAO().getInfo();
                    Messages.showInfoMessage("Success!", "AccuRev Connection");
                } catch (IllegalArgumentException e) {
                    log.error(e.getLocalizedMessage(), e);
                    Messages.showErrorDialog(e.getLocalizedMessage(), "AccuRev Connection");
                } catch (AccuRevRuntimeException e) {
                    log.error(e.getLocalizedMessage(), e);
                    Messages.showErrorDialog(e.getCommandResult().getErr(), "AccuRev Connection");
                }

            }
        });
    }};
    private AccuRevVcs vcs;

    public AccuRevConfigurable(AccuRevVcs vcs) {
        this.vcs = vcs;
    }

    public static final AccuRevConfigurable getAccuRevConfigurable(Project project) {
        return (AccuRevConfigurable)project.getComponent(AccuRevConfigurable.class);
    }

    public String getDisplayName() {
        return "AccuRev";
    }

    public Icon getIcon() {
        return GuiUtils.ACCUREV_ICON_16x16;
    }

    public String getHelpTopic() {
        return "AccuRev";
    }

    public JComponent createComponent() {
        return configurationPanel;
    }

    public boolean isModified() {
        return false;
    }

    public void apply() throws ConfigurationException {
        log.debug("Apply is invoked");
    }

    public void reset() {
        log.debug("Reset is invoked");
    }

    public void disposeUIResources() {
        log.debug("Disposing UI resources.");
        configurationPanel.invalidate();
        configurationPanel = null;
    }
}
