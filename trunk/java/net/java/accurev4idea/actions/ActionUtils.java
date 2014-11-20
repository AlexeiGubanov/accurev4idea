/*
 * ActionUtils.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jun 18, 2005, 9:52:24 AM
 */
package net.java.accurev4idea.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataConstants;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.VcsManager;
import com.intellij.openapi.vfs.VirtualFile;
import net.java.accurev4idea.AccuRevConfigurable;
import net.java.accurev4idea.AccuRevVcs;

/**
 * Set of utility methods useful for operations with {@link AnActionEvent}s
 *
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version $Id: ActionUtils.java,v 1.2 2005/07/12 00:44:47 ifedulov Exp $
 * @since 0.1
 */
public abstract class ActionUtils {
    /**
     * Obtain {@link Editor} from {@link com.intellij.openapi.actionSystem.AnActionEvent#getDataContext()} }
     *
     * @param event current {@link AnActionEvent} event
     * @return Editor looked up in event's data context
     */
    public static final Editor getEditor(AnActionEvent event) {
        return (Editor) event.getDataContext().getData(DataConstants.EDITOR);
    }

    /**
     * Obtain current editor contents
     *
     * @param event
     * @return
     */
    public static final String getEditorContents(AnActionEvent event) {
        return (String) event.getDataContext().getData(DataConstants.FILE_TEXT);
    }

    /**
     * Obtain current {@link VirtualFile} the event is triggered on
     *
     * @param event
     * @return
     */
    public static final VirtualFile getVirtualFile(AnActionEvent event) {
        return (VirtualFile) event.getDataContext().getData(DataConstants.VIRTUAL_FILE);
    }

    /**
     * Obtain array of {@link VirtualFile}s
     *
     * @param event
     * @return
     */
    public static final VirtualFile[] getVirtualFiles(AnActionEvent event) {
        return (VirtualFile[]) event.getDataContext().getData(DataConstants.VIRTUAL_FILE_ARRAY);
    }

    /**
     * Obtain current {@link Project} reference
     *
     * @param event
     * @return
     */
    public static final Project getProject(AnActionEvent event) {
        return (Project) event.getDataContext().getData(DataConstants.PROJECT);
    }

    /**
     * Obtain {@link AccuRevConfigurable} using {@link AccuRevConfigurable#getAccuRevConfigurable(com.intellij.openapi.project.Project)} call.
     *
     * @param event
     * @return
     */
    public static final AccuRevConfigurable getAccuRevConfigurable(AnActionEvent event) {
        return AccuRevConfigurable.getAccuRevConfigurable(getProject(event));
    }

    /**
     * Get reference to current VCS, this method assumes that active VCS is the {@link AccuRevVcs}
     * @param event
     * @return
     */
    public static final AccuRevVcs getAccuRevVcs(AnActionEvent event) {
        return (AccuRevVcs) VcsManager.getInstance(getProject(event)).getActiveVcs();
    }
}
