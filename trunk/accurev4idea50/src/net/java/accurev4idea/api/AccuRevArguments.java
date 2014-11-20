/*
 * AccuRevArguments.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on May 29, 2005, 2:29:51 PM
 */
package net.java.accurev4idea.api;

import net.java.accurev4idea.api.exec.Argument;

/**
 * List of arguments that can be supplied to accurev executable. The interface declaration approach
 * is used to avoid typos and to facilitate management of the arguments into single location.
 *
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @since   1.0
 * @version 1.0
 */
public final class AccuRevArguments {
    public static final Argument SHOW = new Argument("show");
    public static final Argument FX = new Argument("-fx");
    public static final Argument STREAMS = new Argument("streams");
    public static final Argument WSPACES = new Argument("wspaces");
    public static final Argument CAT = new Argument("cat");
    public static final Argument KEEP = new Argument("keep");
    public static final Argument DEPOTS = new Argument("depots");
    public static final Argument INFO = new Argument("info");
    public static final Argument HIST = new Argument("hist");
    public static final Argument STAT = new Argument("stat");
    public static final Argument START = new Argument("start");
    public static final Argument UPDATE = new Argument("update");
    public static final Argument PURGE = new Argument("purge");
    public static final Argument POP = new Argument("pop");
    public static final Argument FILES = new Argument("files");
    public static final Argument MOVE = new Argument("move");
    public static final Argument ADD = new Argument("add");
    public static final Argument DEFUNCT = new Argument("defunct");

    private AccuRevArguments() {}
}
