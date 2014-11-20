/*
 * AccuRevVcsCapabilities.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on June 11, 2005
 */
package net.java.accurev4idea;

import com.intellij.openapi.vcs.AbstractVcsCapabilities;

/**
 * @see AbstractVcsCapabilities
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version $Id: AccuRevVcsCapabilities.java,v 1.4 2005/07/12 00:44:54 ifedulov Exp $
 * @since 0.1
 */
public class AccuRevVcsCapabilities extends AbstractVcsCapabilities {
    /**
     * @see com.intellij.openapi.vcs.AbstractVcsCapabilities#isFileMovingSupported()
     * @return true
     */
    public boolean isFileMovingSupported() {
        return true;
    }

    /**
     * @see com.intellij.openapi.vcs.AbstractVcsCapabilities#isFileRenamingSupported()
     * @return true
     */
    public boolean isFileRenamingSupported() {
        return true;
    }

    /**
     * @see com.intellij.openapi.vcs.AbstractVcsCapabilities#isDirectoryMovingSupported()
     * @return true
     */
    public boolean isDirectoryMovingSupported() {
        return true;
    }

    /**
     * @see com.intellij.openapi.vcs.AbstractVcsCapabilities#isDirectoryRenamingSupported()
     * @return true
     */
    public boolean isDirectoryRenamingSupported() {
        return true;
    }

    /**
     * @see com.intellij.openapi.vcs.AbstractVcsCapabilities#isTransactionSupported()
     * @return true
     */
    public boolean isTransactionSupported() {
        return true;
    }
}