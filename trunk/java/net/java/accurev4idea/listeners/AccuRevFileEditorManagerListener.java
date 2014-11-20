/*
 * AccuRevFileEditorManagerListener.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jul 3, 2005, 8:14:15 AM
 */
package net.java.accurev4idea.listeners;

import org.apache.log4j.Logger;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.vfs.VirtualFile;

/**
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version $Id: AccuRevFileEditorManagerListener.java,v 1.1 2005/07/12 00:44:51 ifedulov Exp $
 * @since 0.1
 */
public class AccuRevFileEditorManagerListener implements FileEditorManagerListener {
    /**
     * Log4j audit channel
     */
    private static Logger log = Logger.getLogger(AccuRevFileEditorManagerListener.class);

    public void fileOpened(FileEditorManager fileEditorManager, VirtualFile virtualFile) {
        log.debug("File opened ["+virtualFile+"]");
    }

    public void fileClosed(FileEditorManager fileEditorManager, VirtualFile virtualFile) {
        log.debug("File closed ["+virtualFile+"]");
    }

    public void selectionChanged(FileEditorManagerEvent event) {
        log.debug("Selection changed ["+event+"]");
    }
}
