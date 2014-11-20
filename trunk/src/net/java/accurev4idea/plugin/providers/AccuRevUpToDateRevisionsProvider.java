package net.java.accurev4idea.plugin.providers;

//TODO import com.intellij.openapi.vcs.UpToDateRevisionProvider;
import com.intellij.openapi.vcs.VcsException;
import com.intellij.openapi.vfs.VirtualFile;
//TODO import com.intellij.openapi.localVcs.LvcsRevision;
import com.intellij.openapi.application.ApplicationManager;
import org.apache.log4j.Logger;
import net.java.accurev4idea.plugin.AccuRevVcs;
import net.java.accurev4idea.api.AccuRev;

import java.io.File;

/**
 * $Id: AccuRevUpToDateRevisionsProvider.java,v 1.2 2005/11/07 23:41:18 ifedulov Exp $
 * User: aantonov
 * Date: Nov 7, 2005
 * Time: 12:24:15 PM
 */
public class AccuRevUpToDateRevisionsProvider /*TODO implements UpToDateRevisionProvider */ {

    private static final Logger log = Logger.getLogger(AccuRevUpToDateRevisionsProvider.class);

    private AccuRevVcs vcs;

    public AccuRevUpToDateRevisionsProvider(AccuRevVcs vcs) {
        this.vcs = vcs;
    }

    /**
     * This takes a virtual file and expects to get back the latest content of this
     * file that is stored in the VCS
     *
     * @param vFile
     * @param quietly
     * @return
     * @throws VcsException
     */
    public String getLastUpToDateContentFor(final VirtualFile vFile, boolean quietly) throws VcsException {
        final File[] file = new File[1];
        // TODO: Figure out about the charset for unicode files
        final String[] charset = new String[1];
        ApplicationManager.getApplication().runReadAction(new Runnable() {
            public void run() {
                file[0] = new File(vFile.getPath());
                charset[0] = vFile.getCharset().name();
            }
        });
        return AccuRev.getLastKeptFileContents(file[0], vcs.getCommandExecListeners());
    }

    //TODO public boolean itemCanBePurged(LvcsRevision lvcsObject) {
//        return true;
//    }
}
