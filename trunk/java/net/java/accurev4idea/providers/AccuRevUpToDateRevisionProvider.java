/*
 * AccuRevUpToDateRevisionProvider.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jul 10, 2005, 3:40:00 PM
 */
package net.java.accurev4idea.providers;

import com.intellij.openapi.vcs.UpToDateRevisionProvider;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.localVcs.LvcsRevision;
import com.intellij.openapi.diagnostic.Logger;
import net.java.accurev4idea.AccuRevVcs;
import net.java.accurev4idea.dao.AccuRevDAOFacade;
import net.java.savant.plugin.scm.accurev.exceptions.AccuRevRuntimeException;
import net.java.savant.plugin.scm.accurev.exceptions.NoSuchObjectException;

import java.io.File;

/**
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version $Id: AccuRevUpToDateRevisionProvider.java,v 1.1 2005/07/12 00:44:53 ifedulov Exp $
 * @since 0.1
 */
public class AccuRevUpToDateRevisionProvider implements UpToDateRevisionProvider {
    /**
     * Log4j audit channel
     */
    private static Logger log = Logger.getInstance(AccuRevUpToDateRevisionProvider.class.getName());

    private AccuRevVcs vcs = null;

    public AccuRevUpToDateRevisionProvider(AccuRevVcs vcs) {
        this.vcs = vcs;
    }
    
    public String getLastUpToDateContentFor(VirtualFile virtualFile) {
        final AccuRevDAOFacade accuRevDAO = vcs.getAccuRevDAO();
        final File file = new File(virtualFile.getPresentableUrl());
        final String backedFileContents;
        try {
            backedFileContents = accuRevDAO.getBackedFileContents(file);
        } catch (AccuRevRuntimeException e) {
            // Log the error and full exception stack trace
            log.error(e.getLocalizedMessage(), e);
            return null;
        } catch (NoSuchObjectException e) {
            // Log the error and full exception stack trace
            log.error(e.getLocalizedMessage(), e);
            return null;
        }
        return backedFileContents;
    }

    public boolean itemCanBePurged(LvcsRevision lvcsRevision) {
        return false;
    }
}
