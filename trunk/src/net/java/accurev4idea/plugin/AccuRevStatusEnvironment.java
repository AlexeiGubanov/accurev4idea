package net.java.accurev4idea.plugin;

import com.intellij.openapi.util.Ref;
import com.intellij.openapi.vcs.update.SequentialUpdatesContext;
import org.apache.log4j.Logger;
import com.intellij.openapi.vcs.update.UpdateEnvironment;
import com.intellij.openapi.vcs.update.UpdatedFiles;
import com.intellij.openapi.vcs.update.UpdateSession;
import com.intellij.openapi.vcs.update.UpdateSessionAdapter;
import com.intellij.openapi.vcs.update.FileGroup;
import com.intellij.openapi.vcs.FilePath;
import com.intellij.openapi.vcs.AbstractVcsHelper;
import com.intellij.openapi.vcs.VcsException;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashSet;
import java.io.File;

/**
 * $Id: AccuRevStatusEnvironment.java,v 1.2 2006/02/21 22:50:06 ifedulov Exp $
 * User: aantonov
 * Date: Nov 4, 2005
 * Time: 10:51:39 AM
 */
public class AccuRevStatusEnvironment implements UpdateEnvironment {

    private static final Logger log = Logger.getLogger(AccuRevStatusEnvironment.class);

    private AccuRevVcs vcs;

    public AccuRevStatusEnvironment(AccuRevVcs vcs) {
        this.vcs = vcs;
    }

    public void fillGroups(UpdatedFiles updatedFiles) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @NotNull
    @Override
    public UpdateSession updateDirectories(FilePath[] filePaths, UpdatedFiles updatedFiles, ProgressIndicator progressIndicator, Ref<SequentialUpdatesContext> ref) throws ProcessCanceledException {
        return null; //TODO
    }

    @Override
    public boolean validateOptions(Collection<FilePath> collection) {
        return false; //TODO
    }

    public UpdateSession updateDirectories(FilePath[] contentRoots, UpdatedFiles updatedFiles, ProgressIndicator progressIndicator) throws ProcessCanceledException {
        final ArrayList exceptions = new ArrayList();

        return new UpdateSessionAdapter(exceptions, false);
    }

    public Configurable createConfigurable(Collection files) {
        // there is nothing to configure for update to run
        return null;
    }
}
