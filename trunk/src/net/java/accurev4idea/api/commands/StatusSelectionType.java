/*
 * StatusSelectionType.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jul 4, 2005, 1:41:47 PM
 */
package net.java.accurev4idea.api.commands;

import org.apache.commons.lang.enums.Enum;

/**
 * Enumeration to map all possible "stat" selection types
 * <pre>
 * -a      select all elements. You cannot also specify an element list,
 * either on the command line or with a list-file (-l).
 * -d      select the members of the default group
 * -D      select defunct elements
 * -f <format>
 * a: absolute, r: relative, f: filesonly, d: dirs only, l: location only, e: element-ID
 * -i      select stranded elements
 * -k      select kept elements
 * -l <list-file>
 * file containing names of elements to be processed
 * -m      select modified elements
 * -M      display missing files
 * -n      select modified 'non-member' elements. That is, select
 * elements that have been modified but are not yet members of
 * the workspace's default group
 * -o      select 'overlapping' elements.
 * When an overlap occurs, a merge is required to resolve conflicts
 * -O      do not use timestamp optimization, which ignores elements
 * whose timestamps precede the most recent workspace update
 * -p      select both modified elements and kept elements
 * -R      recurse through specified directory(s). This option must
 * be used with -n or -x, and cannot be used with other options.
 * -x      select files/dirs in workspace that are not under version control
 * </pre>
 *
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version $Id: StatusSelectionType.java,v 1.1 2005/11/05 16:56:06 ifedulov Exp $
 * @since 0.1
 */
public class StatusSelectionType extends Enum {
    public static final StatusSelectionType SELECT_STRANDED = new StatusSelectionType("-i");
    public static final StatusSelectionType SELECT_ALL = new StatusSelectionType("-a");
    public static final StatusSelectionType SELECT_MEMBERS = new StatusSelectionType("-d");
    public static final StatusSelectionType SELECT_DEFUNCT = new StatusSelectionType("-D");
    public static final StatusSelectionType SELECT_KEPT = new StatusSelectionType("-k");
    public static final StatusSelectionType SELECT_MODIFIED = new StatusSelectionType("-m");
    public static final StatusSelectionType SELECT_MISSING = new StatusSelectionType("-M");
    public static final StatusSelectionType SELECT_MODIFIED_NON_MEMBER = new StatusSelectionType("-n");
    public static final StatusSelectionType SELECT_OVERLAPPING = new StatusSelectionType("-o");
    public static final StatusSelectionType SELECT_MODIFIED_OR_KEPT = new StatusSelectionType("-p");
    public static final StatusSelectionType SELECT_EXTERNAL = new StatusSelectionType("-x");

    private StatusSelectionType(String s) {
        super(s);
    }
}
