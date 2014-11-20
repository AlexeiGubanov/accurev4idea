package net.java.accurev4idea.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.FileStatus;
import com.intellij.openapi.vcs.FileStatusManager;
import com.intellij.openapi.vcs.VcsException;
import com.intellij.openapi.vfs.VirtualFile;
import net.java.accurev4idea.AccuRevVcs;
import org.apache.log4j.Logger;

/**
 * Action handling the "add" function
 *
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version $Id: ActionAdd.java,v 1.2 2005/07/12 01:05:10 ifedulov Exp $
 * @since 0.1
 */
public class ActionAdd extends ActionBase {
    /**
     * Log4j audit channel
     */
    private static final Logger log = Logger.getLogger(ActionAdd.class);

    /**
     * @see ActionBase#performAction(com.intellij.openapi.actionSystem.AnActionEvent)
     */
    protected void performAction(AnActionEvent event) {
        VirtualFile file = ActionUtils.getVirtualFile(event);
        AccuRevVcs vcs = ActionUtils.getAccuRevVcs(event);
        try {
            if (file.isDirectory()) {
                vcs.addDirectory(file.getParent().getPresentableUrl(), file.getName(), null);
            } else {
                vcs.addFile(file.getParent().getPresentableUrl(), file.getName(), null);
            }
        } catch (VcsException e) {
            // Log the error and full exception stack trace
            log.error(e.getLocalizedMessage(), e);
            throw new RuntimeException(e.getLocalizedMessage(), e);
        }

    }

    /**
     * @see ActionBase#shouldSave()
     */
    protected boolean shouldSave() {
        return false;
    }

    /**
     * @see ActionBase#isEnabled(com.intellij.openapi.project.Project, net.java.accurev4idea.AccuRevVcs, com.intellij.openapi.vfs.VirtualFile)
     */
    protected boolean isEnabled(Project project, AccuRevVcs vcs, VirtualFile file) {
        return FileStatus.UNKNOWN.equals(FileStatusManager.getInstance(project).getStatus(file));
    }
}
