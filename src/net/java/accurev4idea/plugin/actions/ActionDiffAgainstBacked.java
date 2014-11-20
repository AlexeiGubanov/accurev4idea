/*
 * ActionDiffAgainstBacked.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jun 11, 2005, 3:56:32 PM
 */
package net.java.accurev4idea.plugin.actions;

import com.intellij.openapi.diff.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import net.java.accurev4idea.plugin.AccuRevToIdeaAdapter;
import org.apache.log4j.Logger;

/**
 * This {@link ActionBase} implementation handles diff of editor (local) file against AccuRev workspace copy.
 *
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version $Id: ActionDiffAgainstBacked.java,v 1.1 2005/11/05 16:56:17 ifedulov Exp $
 * @since 0.1
 */
public class ActionDiffAgainstBacked extends ActionAbstractDiffAgainst {
    /**
     * Log4j audit channel
     */
    private static final Logger log = Logger.getLogger(ActionDiffAgainstBacked.class);

    /**
     * @see ActionAbstractDiffAgainst#getRemoteDiffContent(com.intellij.openapi.project.Project, com.intellij.openapi.vfs.VirtualFile)
     */
    protected DiffContent getRemoteDiffContent(Project project, VirtualFile file) {
        return AccuRevToIdeaAdapter.getInstance(project).getBackedDiffContent(file);
    }

}
