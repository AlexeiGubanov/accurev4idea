/*
 * AccuRevFileRenameProvider.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jul 3, 2005, 8:30:49 AM
 */
package net.java.accurev4idea.providers;

import org.apache.log4j.Logger;
import com.intellij.openapi.vcs.FileRenameProvider;
import com.intellij.openapi.vcs.VcsException;

/**
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version $Id: AccuRevFileRenameProvider.java,v 1.1 2005/07/12 00:44:53 ifedulov Exp $
 * @since 0.1
 */
public class AccuRevFileRenameProvider implements FileRenameProvider {
    /**
     * Log4j audit channel
     */
    private static Logger log = Logger.getLogger(AccuRevFileRenameProvider.class);

    public void renameAndCheckInFile(String s, String s1, Object o) throws VcsException {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
