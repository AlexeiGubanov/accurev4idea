/*
 * AccuRevFileStatus.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jun 12, 2005, 4:42:28 PM
 */
package net.java.accurev4idea.api.components;

import org.apache.commons.lang.enums.Enum;

/**
 * Enumeration of all possible states a file can have in AccuRev workspace. Note that
 * some of them are not atomic mappings because it's much easier to reference each
 * of those individually rather than as an list of statuses. If more statuses needed just add
 * more elements to the enumeration.
 *
 * @see org.apache.commons.lang.enums.EnumUtils
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @since   1.0
 * @version 1.0
 */
public final class AccuRevFileStatus extends Enum {
    /**
     * Status corresponding to (backed)
     */
    public static final AccuRevFileStatus BACKED = new AccuRevFileStatus("(backed)");
    /**
     * Status corresponding to (missing)
     */
    public static final AccuRevFileStatus MISSING = new AccuRevFileStatus("(missing)");
    /**
     * Status corresponding to (modified)
     */
    public static final AccuRevFileStatus MODIFIED = new AccuRevFileStatus("(modified)");
    /**
     * Status corresponding to (external)
     */
    public static final AccuRevFileStatus EXTERNAL = new AccuRevFileStatus("(external)");
    /**
     * Status corresponding to (member)
     */
    public static final AccuRevFileStatus MEMBER = new AccuRevFileStatus("(member)");
    /**
     * Status corresponding to (kept)(member)
     */
    public static final AccuRevFileStatus KEPT_MEMBER = new AccuRevFileStatus("(kept)(member)");
    /**
     * Status corresponding to (modified)(member)
     */
    public static final AccuRevFileStatus MODIFIED_MEMBER = new AccuRevFileStatus("(modified)(member)");
    /**
     * Status corresponding to (defunct)(kept)(member)
     */
    public static final AccuRevFileStatus DEFUNCT_KEPT_MEMBER = new AccuRevFileStatus("(defunct)(kept)(member)");
    /**
     * Status corresponding to (overlap)(modified)
     */
    public static final AccuRevFileStatus OVERLAP_MODIFIED = new AccuRevFileStatus("(overlap)(modified)");
    /**
     * Status corresponding to (backed)(stale)
     */
    public static final AccuRevFileStatus BACKED_STALE = new AccuRevFileStatus("(backed)(stale)");
    /**
     * Status corresponding to (stale)(backed)
     */
    public static final AccuRevFileStatus STALE_BACKED = new AccuRevFileStatus("(stale)(backed)");
    /**
     * Status corresponding to (no such elem)
     */
    public static final AccuRevFileStatus NO_SUCH_ELEMENT = new AccuRevFileStatus("(no such elem)");

    /**
     * Private, to prevent direct instantiation
     * @see org.apache.commons.lang.enums.Enum(String)
     */
    private AccuRevFileStatus(String s) {
        super(s);
    }
}
