/*
 * StreamType.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jun 18, 2005, 4:25:15 PM
 */
package net.java.accurev4idea.api.components;

import org.apache.commons.lang.enums.Enum;

/**
 * Type of {@link Stream} that is used by AccuRev. Know types are "normal" which is referring
 * to a regular stream, "workspace" that is a workspace stream and "snapshot" (read-only) stream
 * 
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version 1.0
 * @since 1.0
 */
public class StreamType extends Enum {
    public static final StreamType NORMAL = new StreamType("normal");
    public static final StreamType WORKSPACE = new StreamType("workspace");
    public static final StreamType SNAPSHOT = new StreamType("snapshot");

    private StreamType(String s) {
        super(s);
    }
}
