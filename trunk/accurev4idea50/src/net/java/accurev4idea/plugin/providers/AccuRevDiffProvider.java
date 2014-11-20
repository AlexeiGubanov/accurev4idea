package net.java.accurev4idea.plugin.providers;

import com.intellij.openapi.vcs.diff.DiffProvider;
import com.intellij.openapi.vcs.history.VcsRevisionNumber;
import com.intellij.openapi.vcs.history.VcsFileContent;
import com.intellij.openapi.vcs.VcsException;
import com.intellij.openapi.vfs.VirtualFile;
import net.java.accurev4idea.plugin.AccuRevVcs;
import net.java.accurev4idea.plugin.components.AccuRevRevisionNumber;
import net.java.accurev4idea.api.AccuRev;
import net.java.accurev4idea.api.exceptions.AccuRevRuntimeException;
import net.java.accurev4idea.api.components.AccuRevFile;
import net.java.accurev4idea.api.components.AccuRevVersion;

import java.io.File;

import org.apache.log4j.Logger;

/**
 * $Id: AccuRevDiffProvider.java,v 1.4 2005/11/11 22:42:54 ifedulov Exp $
 * User: aantonov
 * Date: Nov 4, 2005
 * Time: 4:39:44 PM
 */
public class AccuRevDiffProvider implements DiffProvider {
    private static final Logger log = Logger.getLogger(AccuRevDiffProvider.class);

    private AccuRevVcs vcs;

    public AccuRevDiffProvider(AccuRevVcs myVcs) {
        this.vcs = myVcs;
    }

    public VcsRevisionNumber getCurrentRevision(VirtualFile virtualFile) {
        try {
            final AccuRevFile accuRevFile = AccuRev.getAccuRevFile(new File(virtualFile.getPresentableUrl()), vcs.getCommandExecListeners());
            return new AccuRevRevisionNumber(accuRevFile.getVersion());
        } catch (AccuRevRuntimeException e) {
            log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    public VcsRevisionNumber getLastRevision(VirtualFile virtualFile) {
        try {
            final AccuRevFile accuRevFile = AccuRev.getAccuRevFile(new File(virtualFile.getPresentableUrl()), vcs.getCommandExecListeners());
            return new AccuRevRevisionNumber(accuRevFile.getVersion());
        } catch (AccuRevRuntimeException e) {
            log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    public VcsFileContent createFileContent(VcsRevisionNumber revisionNumber, final VirtualFile selectedFile) {
        final AccuRevVersion version = ((AccuRevRevisionNumber) revisionNumber).getVersion();
        return new VcsFileContent() {
            private String content;

            public void loadContent() throws VcsException {
                content = AccuRev.getFileContentsByVersion(new File(selectedFile.getPresentableUrl()), version, vcs.getCommandExecListeners());
            }

            public byte[] getContent() {
                return content.getBytes();
            }
        };
    }
}
