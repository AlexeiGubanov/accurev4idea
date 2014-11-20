/*
 * AccuRevEditFileProvider.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jun 19, 2005, 2:10:41 PM
 */
package net.java.accurev4idea.providers;

import com.intellij.openapi.vcs.EditFileProvider;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.log4j.Logger;

/**
 * AccuRev implementation for {@link EditFileProvider} interface.
 *
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version $Id: AccuRevEditFileProvider.java,v 1.1 2005/06/19 20:40:14 ifedulov Exp $
 * @since 0.1
 */
// TODO: Implement
public class AccuRevEditFileProvider implements EditFileProvider {
    /**
     * Log4j audit channel
     */
    private static Logger log = Logger.getLogger(AccuRevEditFileProvider.class);

    public void editFiles(VirtualFile[] virtualFiles) {
    }

    public String getRequestText() {
        return null;
    }

    public boolean isUnderVcs(VirtualFile virtualFile) {
        return false;
    }
}
