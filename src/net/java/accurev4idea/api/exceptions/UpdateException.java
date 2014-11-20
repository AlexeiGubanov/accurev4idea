/*
 * UpdateException.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jul 10, 2005, 12:20:42 PM
 */
package net.java.accurev4idea.api.exceptions;

import java.util.List;
import java.util.Collections;


/**
 * This exception is thrown by {@link net.java.accurev4idea.api.AccuRev#updateWorkspace(String)}
 * method when update fails because workspace contains modified but not kept files.
 * 
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version $Id: UpdateException.java,v 1.1 2005/11/07 00:57:37 ifedulov Exp $
 * @since 0.1
 */
public class UpdateException extends RuntimeException {
    /**
     * List of modified but not kept files that caused this exception
     */
    private List modifiedNotKeptFiles = Collections.EMPTY_LIST;

    /**
     * Do not allow public construction of this exception without arguments
     */
    private UpdateException() {
    }

    /**
     * The only available constructor, takes error message and list of files
     * that caused this exception to occur
     */
    public UpdateException(String message, List files) {
        super(message);
        if(files != null && !files.isEmpty()) {
            this.modifiedNotKeptFiles = files;
        }
    }

    /**
     * Obtain modified but not kept files that caused this exception to occur, each object in the
     * returned list is an {@link net.java.accurev4idea.api.components.AccuRevFile}
     * @return
     */
    public List getModifiedButNotKeptFiles() {
        return modifiedNotKeptFiles;
    }
}
