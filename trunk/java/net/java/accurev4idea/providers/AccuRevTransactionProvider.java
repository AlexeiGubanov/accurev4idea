/*
 * AccuRevTransactionProvider.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jun 19, 2005, 2:08:53 PM
 */
package net.java.accurev4idea.providers;

import org.apache.log4j.Logger;
import com.intellij.openapi.vcs.TransactionProvider;
import com.intellij.openapi.vcs.VcsException;
import net.java.savant.plugin.scm.accurev.exceptions.AccuRevRuntimeException;

/**
 * AccuRev plugin implementation for {@link TransactionProvider}. This is needed to
 * facilitate support for changesets because otherwise generic VCS model will need to
 * be followed where each file is individually commited.
 *
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version $Id: AccuRevTransactionProvider.java,v 1.2 2005/07/12 00:44:53 ifedulov Exp $
 * @since 0.1
 */
public class AccuRevTransactionProvider implements TransactionProvider {
    /**
     * Log4j audit channel
     */
    private static Logger log = Logger.getLogger(AccuRevTransactionProvider.class);

    /**
     * @see TransactionProvider#startTransaction(Object)
     */
    public void startTransaction(Object o) throws VcsException {
        log.debug("Transaction start with [" + o + "]");
        try {
            if (o != null) {
                // create changepallet
            }
        } catch (AccuRevRuntimeException e) {
            throw new VcsException(e);
        }
    }

    /**
     * @see TransactionProvider#commitTransaction(Object)
     */
    public void commitTransaction(Object o) throws VcsException {
        log.debug("Transaction commit with [" + o + "]");
    }

    /**
     * @see TransactionProvider#rollbackTransaction(Object)
     */ 
    public void rollbackTransaction(Object o) {
        log.debug("Transaction rollback with [" + o + "]");
    }
}
