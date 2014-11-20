/*
 * AccuRevTransactionType.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jun 12, 2005, 4:27:55 PM
 */
package net.java.accurev4idea.api.components;

import org.apache.commons.lang.enums.Enum;

/**
 * Enumeration of known transaction types, such as "add", "keep", "promote" etc.
 *
 * @see org.apache.commons.lang.enums.EnumUtils
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @since   1.0
 * @version 1.0
 */
public class AccuRevTransactionType extends Enum {
    /**
     * Transaction type corresponding to "add"
     */
    public static final AccuRevTransactionType ADD = new AccuRevTransactionType("add");
    /**
     * Transaction type corresponding to "keep"
     */
    public static final AccuRevTransactionType KEEP = new AccuRevTransactionType("keep");
    /**
     * Transaction type corresponding to "promote"
     */
    public static final AccuRevTransactionType PROMOTE = new AccuRevTransactionType("promote");
    /**
     * Transaction type corresponding to "defunct"
     */
    public static final AccuRevTransactionType DEFUNCT = new AccuRevTransactionType("defunct");
    /**
     * Transaction type corresponding to "undefunct"
     */
    public static final AccuRevTransactionType UNDEFUNCT = new AccuRevTransactionType("undefunct");
    /**
     * Transaction type corresponding to "undefunct"
     */
    public static final AccuRevTransactionType PURGE = new AccuRevTransactionType("purge");
    /**
     * Transaction type corresponding to "move"
     */
    public static final AccuRevTransactionType MOVE = new AccuRevTransactionType("move");

    /**
     * Private constructor to prevent direct instantiation
     * @see org.apache.commons.lang.enums.Enum(String)
     */ 
    private AccuRevTransactionType(String s) {
        super(s);
    }
}
