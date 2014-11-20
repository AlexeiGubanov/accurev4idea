package net.java.accurev4idea.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.VcsException;
import com.intellij.openapi.vfs.VirtualFile;
import net.java.accurev4idea.AccuRevVcs;
import org.apache.log4j.Logger;

/**
 * Action that handles file/directory deletes
 *
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version $Id: ActionDelete.java,v 1.2 2005/07/12 01:04:20 ifedulov Exp $
 * @since 0.1
 */
public class ActionDelete extends ActionBase {
    /**
     * Log4j audit channel
     */
    private static Logger log = Logger.getLogger(ActionDelete.class);

    protected void performAction(AnActionEvent event) {
        VirtualFile file = ActionUtils.getVirtualFile(event);
        AccuRevVcs vcs = ActionUtils.getAccuRevVcs(event);
        try {
            if (file.isDirectory()) {
                vcs.removeDirectory(file.getPath(), null);
            } else {
                vcs.removeFile(file.getPath(), null);
            }
        } catch (VcsException e) {
            // Log the error and full exception stack trace
            log.error(e.getLocalizedMessage(), e);
            // rethrow as runtime
            throw new RuntimeException(e.getLocalizedMessage(), e);
        }
    }

    protected boolean shouldSave() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    protected boolean isEnabled(Project project, AccuRevVcs vcs, VirtualFile file) {
        return true;
    }
}