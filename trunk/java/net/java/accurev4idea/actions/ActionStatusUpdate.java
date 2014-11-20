/*
 * ActionStatusUpdate.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jul 10, 2005, 3:27:09 PM
 */
package net.java.accurev4idea.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import net.java.accurev4idea.AccuRevVcs;
import net.java.accurev4idea.providers.AccuRevFileStatusProvider;
import org.apache.log4j.Logger;

/**
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version $Id: ActionStatusUpdate.java,v 1.1 2005/07/12 00:44:47 ifedulov Exp $
 * @since 0.1
 */
public class ActionStatusUpdate extends ActionBase {
    /**
     * Log4j audit channel
     */
    private static Logger log = Logger.getLogger(ActionStatusUpdate.class);

    protected void performAction(AnActionEvent event) {
        AccuRevVcs vcs = ActionUtils.getAccuRevVcs(event);
        if (vcs.getFileStatusProvider() instanceof AccuRevFileStatusProvider) {
            AccuRevFileStatusProvider provider = (AccuRevFileStatusProvider) vcs.getFileStatusProvider();
            provider.refreshCache();
        }
    }

    protected boolean shouldSave() {
        return true;
    }

    protected boolean isEnabled(Project project, AccuRevVcs vcs, VirtualFile file) {
        return true;
    }
}
