/*
 * AccuRevVcsGroup.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Oct 29, 2005, 9:41:17 PM
 */
package net.java.accurev4idea.plugin;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.AbstractVcs;
import com.intellij.openapi.vcs.actions.StandardVcsGroup;
import org.apache.log4j.Logger;

/**
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version $Id: AccuRevVcsGroup.java,v 1.1 2005/11/05 16:56:28 ifedulov Exp $
 * @since 0.1
 */
public class AccuRevVcsGroup extends StandardVcsGroup {
    /**
     * Log4j audit channel
     */
    private static Logger log = Logger.getLogger(AccuRevVcsGroup.class);

    public AbstractVcs getVcs(Project project) {
        return AccuRevVcs.getInstance(project);
    }
}
