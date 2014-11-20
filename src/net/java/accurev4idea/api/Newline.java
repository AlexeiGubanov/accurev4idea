package net.java.accurev4idea.api;

import org.apache.commons.lang.enums.Enum;

/**
 * <p>
 * This class is an enumeration of the three types of newline
 * characters or character sequences supported in the world
 * these days.
 * </p>
 * 
 * @author Brian Pontarelli
 * @author Igor Fedulov <mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version $Id: Newline.java,v 1.1 2005/11/05 16:56:15 ifedulov Exp $
 * @since 1.0
 */
public final class Newline extends Enum {
    /**
     * Default line separator for the platform
     */
    public static final Newline DEFAULT = new Newline("d");

    /**
     * Windows line separator (carriage return followed by a newline).
     */
    public static final Newline WINDOWS = new Newline("w");

    /**
     * Unix line separator (newline only).
     */
    public static final Newline UNIX = new Newline("u");

    /**
     * Macintosh line separator (carriage return only).
     */
    public static final Newline MAC = new Newline("m");

    /**
     * @see Enum(String)
     */
    private Newline(String s) {
        super(s);
    }
}