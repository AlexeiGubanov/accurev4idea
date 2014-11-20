/*
 * ActionRevert.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jul 3, 2005, 7:06:20 AM
 */
package net.java.accurev4idea.plugin.actions;

import net.java.accurev4idea.api.AccuRev;

import java.io.File;
import java.util.List;

/**
 * Revert local changes to a selected file to "backed" version (i.e. stream version)
 *
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version $Id: ActionRevertToBacked.java,v 1.2 2005/11/07 00:57:38 ifedulov Exp $
 * @since 0.1
 */
public class ActionRevertToBacked extends ActionAbstractRevertTo {
    /**
     * @see ActionAbstractRevertTo#performRevertTo(java.io.File, java.util.List)
     */
    protected void performRevertTo(File file, List listeners) {
        AccuRev.revertToBacked(file, listeners);
    }
}
