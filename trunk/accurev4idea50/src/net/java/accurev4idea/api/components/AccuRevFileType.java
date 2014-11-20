/*
 * AccuRevFileType.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jun 12, 2005, 4:39:15 PM
 */
package net.java.accurev4idea.api.components;

import org.apache.commons.lang.enums.Enum;

/**
 * File type of the "File" in AccuRev subsystem. Known types are "text" and "binary"
 *
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version 1.0
 * @see org.apache.commons.lang.enums.EnumUtils
 * @since   1.0
 */
public final class AccuRevFileType extends Enum {
    /**
     * File type corresponding to "text"
     */
    public static final AccuRevFileType TEXT = new AccuRevFileType("text");
    /**
     * File type corresponding to "binary"
     */
    public static final AccuRevFileType BINARY = new AccuRevFileType("binary");

    /**
     * Private constructor to disallow direct instantiation
     * @see org.apache.commons.lang.enums.Enum(String)
     */ 
    private AccuRevFileType(String s) {
        super(s);
    }
}
