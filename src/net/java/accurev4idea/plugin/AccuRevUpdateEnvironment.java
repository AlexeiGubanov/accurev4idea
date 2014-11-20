package net.java.accurev4idea.plugin;

import com.intellij.openapi.util.Ref;
import com.intellij.openapi.vcs.update.*;
import com.intellij.openapi.vcs.FilePath;
import com.intellij.openapi.vcs.VcsException;
import com.intellij.openapi.vcs.AbstractVcsHelper;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.util.text.LineTokenizer;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;

import java.util.*;
import java.io.File;

import org.apache.log4j.Logger;
import net.java.accurev4idea.api.AccuRev;
import net.java.accurev4idea.api.components.AccuRevFile;
import net.java.accurev4idea.api.exceptions.UpdateException;
import org.jetbrains.annotations.NotNull;

/**
 * $Id: AccuRevUpdateEnvironment.java,v 1.3 2006/06/20 19:55:20 ifedulov Exp $
 * User: aantonov
 * Date: Nov 4, 2005
 * Time: 10:49:51 AM
 */
public class AccuRevUpdateEnvironment implements UpdateEnvironment {
    /**
     * Log4j audit channel
     */
    private static final Logger log = Logger.getLogger(AccuRevUpdateEnvironment.class);

    private static final String DELETED_MESSAGE = " - deleted as ";
    private static final String UPDATING_MESSAGE = " - updating ";
    private static final String ADDED_MESSAGE = " - added as ";


    private AccuRevVcs vcs;

    public AccuRevUpdateEnvironment(AccuRevVcs vcs) {
        this.vcs = vcs;
    }

    public void fillGroups(UpdatedFiles updatedFiles) {
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
        final ArrayList vcsExceptions = new ArrayList();

        for (int i = 0; i < contentRoots.length; i++) {
            FilePath contentRoot = contentRoots[i];

            try {
                final File location = new File(contentRoot.getPresentableUrl());
                AccuRev.updateDirectory(location.isDirectory()? location:location.getParentFile(), vcs.getCommandExecListeners());
            } catch (UpdateException ue) {
                VcsException vcsException = new VcsException(ue.getLocalizedMessage());
                vcsException.setIsWarning(true);
                vcsException.setVirtualFile(contentRoot.getVirtualFile());
                //vcsExceptions.add(vcsException);

                // fill in the list of files that are not kept
                for (Iterator iter = ue.getModifiedButNotKeptFiles().iterator(); iter.hasNext();) {
                    AccuRevFile accuRevFile = (AccuRevFile) iter.next();
                    updatedFiles.getGroupById(FileGroup.MODIFIED_ID).add(accuRevFile.getAbsolutePath(), "vcsName", null); //TODO
                }
            }
        }
        // make sure that file status is refreshed once update operation is finished.
        AccuRevToIdeaAdapter.getInstance(vcs.getProject()).refreshFileStatusCache();
        return new UpdateSessionAdapter(vcsExceptions, false);
    }

    private void fillUpdateInformationFromTheLine(String line, UpdatedFiles updatedFiles) {
      if (processLine(line, updatedFiles, DELETED_MESSAGE, FileGroup.REMOVED_FROM_REPOSITORY_ID)) {
        return;
      }
      if (processLine(line, updatedFiles, UPDATING_MESSAGE, FileGroup.UPDATED_ID)) {
        return;
      }
      processLine(line, updatedFiles, ADDED_MESSAGE, FileGroup.CREATED_ID);
    }

    private boolean processLine(String line, UpdatedFiles updatedFiles,
                                String message, String fileGroupId) {
      int messageStart = line.indexOf(message);
      if (messageStart < 0) return false;
      int messageEnd = messageStart + message.length();
      String filePath = line.substring(messageEnd);
      updatedFiles.getGroupById(fileGroupId).add(filePath, "vcsName", null); //TODO
      return true;
    }



    public Configurable createConfigurable(Collection files) {
        return null; //TODO
    }
}
