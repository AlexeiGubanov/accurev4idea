/*
 * AccuRevFileStatusCache.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jul 11, 2005, 1:45:39 PM
 */
package net.java.accurev4idea.dao;

import com.intellij.openapi.vcs.FileStatus;
import com.intellij.openapi.vfs.VirtualFile;
import net.java.accurev4idea.AccuRevVcs;
import net.java.accurev4idea.PluginUtils;
import net.java.savant.plugin.scm.accurev.components.AccuRevFileStatus;
import net.java.savant.plugin.scm.accurev.components.Workspace;
import net.java.savant.plugin.scm.accurev.components.AccuRevFile;
import net.java.savant.plugin.scm.accurev.StatusSelectionType;
import org.apache.log4j.Logger;

import java.awt.Color;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version $Id: AccuRevFileStatusCache.java,v 1.1 2005/07/12 00:44:49 ifedulov Exp $
 * @since 0.1
 */
class AccuRevFileStatusCache {
    /**
     * Log4j audit channel
     */
    private static Logger log = Logger.getLogger(AccuRevFileStatusCache.class);
    /**
     * File status used to identify "stale" files, i.e. that are backed but
     * are out of date.
     */
    private static final FileStatus STALE = new FileStatus() {
        public Color getColor() {
            return Color.LIGHT_GRAY;
        }
        public String getText() {
            return "STALE";
        }
    };
    private AccuRevVcs vcs;
    private static final Map fileStatusCache = new HashMap();

    AccuRevFileStatusCache(AccuRevVcs vcs) {
        this.vcs = vcs;
    }

    synchronized FileStatus getFileStatus(VirtualFile virtualFile) {
        // if cache is empty (first hit) populate it with data
        if(fileStatusCache.isEmpty()) {
            refreshCache();
        }

        final String absolutePath = new File(virtualFile.getPresentableUrl()).getAbsolutePath();
        FileStatus status = (FileStatus) fileStatusCache.get(absolutePath);
        if(status == null) {
            if (log.isDebugEnabled()) {
                log.debug("Cache miss for ["+absolutePath+"]");
            }
            fileStatusCache.put(absolutePath, FileStatus.UNKNOWN);
            return FileStatus.UNKNOWN;
        } else {
            log.debug("Cache hit for ["+absolutePath+"], status is ["+status+"]");
            return status;
        }

    }

    synchronized void refreshCache() {
        // ensure that cache is purged before proceeding
        fileStatusCache.clear();
        List workspaces = PluginUtils.getProjectModuleContentRoots(vcs.getProject());
        for (Iterator i = workspaces.iterator(); i.hasNext();) {
            VirtualFile workspaceFile = (VirtualFile) i.next();
            // first thing, put the workspace content location root into
            // file status cache with "NOT_CHANGED" status
            final File workspaceDir = new File(workspaceFile.getPresentableUrl());
            if (log.isDebugEnabled()) {
                log.debug("Caching ["+workspaceDir.getAbsolutePath()+"] with status ["+FileStatus.NOT_CHANGED+"]");
            }
            fileStatusCache.put(workspaceDir.getAbsolutePath(), FileStatus.NOT_CHANGED);

            final AccuRevDAOFacade accuRevDAO = vcs.getAccuRevDAO();
            Workspace ws = accuRevDAO.getWorkspaceByFileForCurrentPrincipal(workspaceDir);
            List files = accuRevDAO.getAccuRevFilesInWorkspaceByStatus(ws, StatusSelectionType.SELECT_ALL);
            for (Iterator j = files.iterator(); j.hasNext();) {
                AccuRevFile accuRevFile = (AccuRevFile) j.next();
                final FileStatus status = remapFileStatus(accuRevFile.getStatus());
                final String absolutePath = accuRevFile.getAbsolutePath();
                if (log.isDebugEnabled()) {
                    log.debug("Caching ["+absolutePath+"] with status ["+status+"]");
                }
                fileStatusCache.put(absolutePath, status);
            }

        }

        if(log.isDebugEnabled()) {
            log.debug("There are ["+fileStatusCache.size()+"] entries in file status cache.");
        }
    }

    synchronized void refreshCache(VirtualFile virtualFile) {
        final File file = new File(virtualFile.getPresentableUrl());
        final String absolutePath = file.getAbsolutePath();
        // update existing entry
        fileStatusCache.put(absolutePath, remapFileStatus(vcs.getAccuRevDAO().getAccuRevFileStatus(file)));
    }

    private FileStatus remapFileStatus(AccuRevFileStatus status) {
        if(AccuRevFileStatus.EXTERNAL.equals(status) ||
           AccuRevFileStatus.MISSING.equals(status)) {
            return FileStatus.UNKNOWN;
        } else if (AccuRevFileStatus.MODIFIED.equals(status) ||
                   AccuRevFileStatus.OVERLAP_MODIFIED.equals(status)) {
            return FileStatus.MODIFIED;
        } else if (AccuRevFileStatus.DEFUNCT_KEPT_MEMBER.equals(status)) {
            return FileStatus.DELETED;
        } else if(AccuRevFileStatus.KEPT_MEMBER.equals(status) ||
                  AccuRevFileStatus.BACKED.equals(status)) {
            return FileStatus.NOT_CHANGED;
        } else if(AccuRevFileStatus.BACKED_STALE.equals(status)) {
            return STALE;
        } else {
            log.error("Unhandled accuRevFile status ["+status+"]");
        }
        return FileStatus.UNKNOWN;
    }



}
