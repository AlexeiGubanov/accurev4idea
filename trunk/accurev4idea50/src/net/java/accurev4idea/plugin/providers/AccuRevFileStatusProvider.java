/*
 * AccuRevFileStatusProvider.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jun 19, 2005, 2:09:47 PM
 */
package net.java.accurev4idea.plugin.providers;

import com.intellij.openapi.vcs.FileStatus;
import com.intellij.openapi.vcs.FileStatusProvider;
import com.intellij.openapi.vcs.VcsException;
import com.intellij.openapi.vfs.*;
import net.java.accurev4idea.plugin.AccuRevToIdeaAdapter;
import net.java.accurev4idea.plugin.AccuRevVcs;
import net.java.accurev4idea.plugin.AccuRevConfiguration;
import net.java.accurev4idea.plugin.AccuRevConfigurable;
import net.java.accurev4idea.api.AccuRev;
import net.java.accurev4idea.api.exceptions.AccuRevRuntimeException;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;

import sun.misc.MessageUtils;

/**
 * AccuRev implementation for {@link com.intellij.openapi.vcs.FileStatusProvider}. This implementation drives the
 * coloring of the files/directories in the project tree and is a basis for project commit
 * check as well.
 *
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version $Id: AccuRevFileStatusProvider.java,v 1.9 2006/06/20 19:55:20 ifedulov Exp $
 * @since 0.1
 */
public class AccuRevFileStatusProvider implements FileStatusProvider {
    /**
     * Log4j audit channel
     */
    private static Logger log = Logger.getLogger(AccuRevFileStatusProvider.class);

    private static VirtualFileChangeListener myListener = null;

    private AccuRevVcs vcs;

    public AccuRevFileStatusProvider(AccuRevVcs vcs) {
        this.vcs = vcs;

        if (myListener == null) {
            myListener = new VirtualFileChangeListener();
            VirtualFileManager.getInstance().addVirtualFileListener(myListener);
        }
    }

    public FileStatus getStatus(VirtualFile virtualFile) {
        return AccuRevToIdeaAdapter.getInstance(vcs.getProject()).getFileStatus(virtualFile);
    }

    class VirtualFileChangeListener extends VirtualFileAdapter {

        public void beforePropertyChange(VirtualFilePropertyEvent virtualFilePropertyEvent) {
            final VirtualFile file = virtualFilePropertyEvent.getFile();
            final VirtualFile parent = virtualFilePropertyEvent.getParent();
            log.debug("Property change fired with [" + file.getPresentableUrl() + "]");

            String propChanged = virtualFilePropertyEvent.getPropertyName();

            if ("name".equals(propChanged)) {
                log.debug("File name has changed from " + virtualFilePropertyEvent.getOldValue() + " to " + virtualFilePropertyEvent.getNewValue());

                File sourceFile = new File(new File(parent.getPresentableUrl()), (String) virtualFilePropertyEvent.getOldValue());
                File destinationFile = new File(new File(parent.getPresentableUrl()), (String) virtualFilePropertyEvent.getNewValue());

                AccuRev.moveFile(sourceFile, destinationFile, vcs.getCommandExecListeners());

                // we need to move the file back, so IntelliJ could move it
                // to the new place
                destinationFile.renameTo(sourceFile);
            }
            super.beforePropertyChange(virtualFilePropertyEvent);
        }

        public void propertyChanged(VirtualFilePropertyEvent event) {
            String propChanged = event.getPropertyName();
            final VirtualFile file = event.getFile();

            if ("name".equals(propChanged)) {
                // update cached status for file
                AccuRevToIdeaAdapter.getInstance(vcs.getProject()).refreshFileStatusCacheForFile(file);
            }
            super.propertyChanged(event);
        }

        public void contentsChanged(VirtualFileEvent virtualFileEvent) {
            final VirtualFile file = virtualFileEvent.getFile();
            log.debug("Contents change fired with [" + file.getPresentableUrl() + "]");
            // update cached status for file
            // AccuRevToIdeaAdapter.refreshFileStatusCacheForFile(file);
        }

        public void beforeFileMovement(VirtualFileMoveEvent event) {
            final VirtualFile source = event.getFile();
            final VirtualFile parent = event.getNewParent();
            final String newFileName = event.getFileName();

            log.debug("File has been moved from " + event.getOldParent().getPresentableUrl() + " to " + event.getNewParent().getPresentableUrl());

            File sourceFile = new File(source.getPresentableUrl());
            File destinationFile = new File(new File(parent.getPresentableUrl()), newFileName);

            try {
                AccuRev.moveFile(sourceFile, destinationFile, vcs.getCommandExecListeners());
            } catch (RuntimeException e) {
                log.info(e.getLocalizedMessage(), e);
            }

            // we need to move the file back, so IntelliJ could move it
            // to the new place
            destinationFile.renameTo(sourceFile);
        }

        public void fileMoved(VirtualFileMoveEvent event) {
            AccuRevToIdeaAdapter.getInstance(vcs.getProject()).refreshFileStatusCacheForFile(event.getFile());
        }

        public void fileCreated(VirtualFileEvent event) {
            final VirtualFile file = event.getFile();

            if(file.getName().endsWith("class")) {
                // don't auto add class files
                return;
            }

            final AccuRevConfigurable accuRevConfigurable = (AccuRevConfigurable) vcs.getConfigurable();
            AccuRevConfiguration config = accuRevConfigurable.getConfiguration();
            if(config.isAutoAddEnabled()) {
                AccuRev.addFile(new File(file.getPresentableUrl()), vcs.getCommandExecListeners());
                AccuRevToIdeaAdapter.getInstance(vcs.getProject()).refreshFileStatusCacheForFile(file);
            }
        }

        public void beforeFileDeletion(VirtualFileEvent event) {
            final VirtualFile virtualFile = event.getFile();
            final File file = new File(virtualFile.getPresentableUrl());

            // only continue calling if file still exists
            // because it causes a nasty bug when outside system deletes a file and intellij
            // syncrhonizes with file system
            if(!file.exists()) {
                return;
            }

            try {
                AccuRev.deleteFile(file, vcs.getCommandExecListeners());
            } catch (RuntimeException e) {
                // log the exception
                log.info(e.getLocalizedMessage(), e);
            }

            // create a temporary file on place of deleted file so that intellij delets it
            try {
                if(!file.exists()) {
                    file.createNewFile();
                    file.deleteOnExit();
                }
            } catch (Exception e) {
            }
        }
    }
}
