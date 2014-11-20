/*
 * Copyright (c) 2003-2005, Inversoft, All Rights Reserved
 *
 * This software is distribuable under the GNU Lesser General Public License.
 * For more information visit gnu.org.
 */
package net.java.accurev4idea.api.components;

import org.apache.commons.lang.enums.Enum;

/**
 * <p>
 * This class is an enumeration that contains the differen types of
 * file locking that are supported by Accurev.
 * </p>
 * 
 * @author  Brian Pontarelli
 */
public class Locking extends Enum {
    /**
     * This is a null-safe element representing a default locking
     */
    public static final Locking DEFAULT = new Locking("d");
    /**
     * Strict exclusive locking at the server level. Only one user can modify the files at a time.
     */
    public static final Locking EXCLUSIVE = new Locking("e");

    /**
     * Anchor locking that allows multiple users to edit a file on a Stream, but each one must check
     * out the file each time they edit it.
     */
    public static final Locking ANCHOR = new Locking("a");

    /**
     * @see org.apache.commons.lang.enums.Enum(String)
     */
    private Locking(String s) {
        super(s);
    }
}