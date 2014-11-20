/*
 * AccuRevFileStatusProvider.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jun 19, 2005, 2:09:47 PM
 */
package net.java.accurev4idea.providers;

import com.intellij.openapi.vcs.FileStatus;
import com.intellij.openapi.vcs.FileStatusProvider;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileAdapter;
import com.intellij.openapi.vfs.VirtualFileEvent;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.vfs.VirtualFilePropertyEvent;
import com.intellij.openapi.localVcs.LocalVcsServices;
import net.java.accurev4idea.AccuRevVcs;
import org.apache.log4j.Logger;

/**
 * AccuRev implementation for {@link FileStatusProvider}. This implementation drives the
 * coloring of the files/directories in the project tree and is a basis for project commit
 * check as well.
 *
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version $Id: AccuRevFileStatusProvider.java,v 1.3 2005/07/12 00:44:53 ifedulov Exp $
 * @since 0.1
 */
public class AccuRevFileStatusProvider implements FileStatusProvider {
    /**
     * Log4j audit channel
     */
    private static Logger log = Logger.getLogger(AccuRevFileStatusProvider.class);

    private final AccuRevVcs vcs;

    public AccuRevFileStatusProvider(AccuRevVcs vcs) {
        this.vcs = vcs;
        VirtualFileManager.getInstance().addVirtualFileListener(new VirtualFileChangeListener());
    }

    public FileStatus getStatus(VirtualFile virtualFile) {
        FileStatusProvider lvcsFileStatusProvider = LocalVcsServices.getInstance(vcs.getProject()).getFileStatusProvider();

        FileStatus lvcsStatus = lvcsFileStatusProvider.getStatus(virtualFile);
        if (log.isDebugEnabled()) {
            log.debug("Local VCS status for [" + virtualFile.getPresentableUrl() + "] is [" + lvcsStatus + "]");
        }
        return vcs.getAccuRevDAO().getFileStatus(virtualFile);
    }

    public void refreshCache() {
        vcs.getAccuRevDAO().refreshFileStatusCache();
    }

    class VirtualFileChangeListener extends VirtualFileAdapter {
        public void propertyChanged(VirtualFilePropertyEvent virtualFilePropertyEvent) {
            final VirtualFile file = virtualFilePropertyEvent.getFile();
            log.debug("Property change fired with ["+file.getPresentableUrl()+"]");
            // update cached status for file
            vcs.getAccuRevDAO().refreshFileStatusCacheForFile(file);
        }

        public void contentsChanged(VirtualFileEvent virtualFileEvent) {
            final VirtualFile file = virtualFileEvent.getFile();
            log.debug("Contents change fired with ["+file.getPresentableUrl()+"]");
            // update cached status for file
            vcs.getAccuRevDAO().refreshFileStatusCacheForFile(file);
        }
    }
}
