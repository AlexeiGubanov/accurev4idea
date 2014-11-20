package net.java.accurev4idea.plugin.providers;

import com.intellij.openapi.vcs.FilePath;
import com.intellij.openapi.vcs.changes.ContentRevision;
import com.intellij.openapi.vcs.diff.DiffProvider;
import com.intellij.openapi.vcs.diff.ItemLatestState;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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


    @Override
    public VcsRevisionNumber getCurrentRevision(VirtualFile virtualFile) {
        try {
            final AccuRevFile accuRevFile = AccuRev.getAccuRevFile(new File(virtualFile.getPresentableUrl()), vcs.getCommandExecListeners());
            return new AccuRevRevisionNumber(accuRevFile.getVersion());
        } catch (AccuRevRuntimeException e) {
            log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }


    @Override
    public VcsRevisionNumber getLatestCommittedRevision(VirtualFile virtualFile) {
        //TODO
        try {
            final AccuRevFile accuRevFile = AccuRev.getAccuRevFile(new File(virtualFile.getPresentableUrl()), vcs.getCommandExecListeners());
            return new AccuRevRevisionNumber(accuRevFile.getVersion());
        } catch (AccuRevRuntimeException e) {
            log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    @Override
    public ItemLatestState getLastRevision(VirtualFile virtualFile) {
        try {
            final AccuRevFile accuRevFile = AccuRev.getAccuRevFile(new File(virtualFile.getPresentableUrl()), vcs.getCommandExecListeners());
            AccuRevVersion version = accuRevFile.getVersion();
            VcsRevisionNumber vcsRevisionNumber = new VcsRevisionNumber.Long(version.getReal().getVersionId()); //TODO
            return new ItemLatestState(vcsRevisionNumber,true,true); //FIXED AccuRevRevisionNumber(accuRevFile.getVersion());
        } catch (AccuRevRuntimeException e) {
            log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    @Override
    public ItemLatestState getLastRevision(FilePath virtualFile) {
        //TODO
        try {
            final AccuRevFile accuRevFile = AccuRev.getAccuRevFile(new File(virtualFile.getPresentableUrl()), vcs.getCommandExecListeners());
            AccuRevVersion version = accuRevFile.getVersion();
            VcsRevisionNumber vcsRevisionNumber = new VcsRevisionNumber.Long(version.getReal().getVersionId()); //TODO
            return new ItemLatestState(vcsRevisionNumber,true,true); //FIXED AccuRevRevisionNumber(accuRevFile.getVersion());
        } catch (AccuRevRuntimeException e) {
            log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }


    public ContentRevision createFileContent(VcsRevisionNumber revisionNumber, final VirtualFile selectedFile) {
        final AccuRevVersion version = ((AccuRevRevisionNumber) revisionNumber).getVersion();
        return new ContentRevision() {
            private String content;

            public byte[] loadContent() throws VcsException {
                content = AccuRev.getFileContentsByVersion(new File(selectedFile.getPresentableUrl()), version, vcs.getCommandExecListeners());
                return content.getBytes(); //FIXED
            }

            public String getContent() {
                return content; //FIXED
            }

            @NotNull
            @Override
            public FilePath getFile() {
                return null; //TODO
            }

            @NotNull
            @Override
            public VcsRevisionNumber getRevisionNumber() {
                return null; //TODO
            }
        };
    }


}
