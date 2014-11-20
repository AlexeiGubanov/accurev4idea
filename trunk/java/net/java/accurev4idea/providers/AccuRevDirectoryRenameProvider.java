/*
 * AccuRevDirectoryRenameProvider.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jul 3, 2005, 8:34:06 AM
 */
package net.java.accurev4idea.providers;

import org.apache.log4j.Logger;
import com.intellij.openapi.vcs.DirectoryRenameProvider;
import com.intellij.openapi.vcs.VcsException;

/**
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version $Id: AccuRevDirectoryRenameProvider.java,v 1.1 2005/07/12 00:44:52 ifedulov Exp $
 * @since 0.1
 */
public class AccuRevDirectoryRenameProvider implements DirectoryRenameProvider {
    /**
     * Log4j audit channel
     */
    private static Logger log = Logger.getLogger(AccuRevDirectoryRenameProvider.class);

    public void renameDirectory(String s, String s1, Object o) throws VcsException {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
