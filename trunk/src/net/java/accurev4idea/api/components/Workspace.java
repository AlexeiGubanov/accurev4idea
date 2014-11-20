/*
 * Workspace.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jun 23, 2005, 8:10:45 AM
 */
package net.java.accurev4idea.api.components;

import net.java.accurev4idea.api.Newline;

/**
 * This sublcass of {@link Stream} adds couple fields that are unique to the "workspace" notion
 * in AccuRev terminology. Fields such as direcory location on the file system, current host name
 * this workspace belogs to, settings for the {@link Locking} and
 * {@link Newline}.
 *
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version 1.0
 * @since 1.0
 */
public class Workspace extends Stream {
    private String location;
    private String hostname;
    private AccuRevUser user;
    private Locking locking;
    private Newline newline;

    public Workspace() {
        setType(StreamType.WORKSPACE);
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Locking getLocking() {
        return locking;
    }

    public void setLocking(Locking locking) {
        this.locking = locking;
    }

    public Newline getNewline() {
        return newline;
    }

    public void setNewline(Newline newline) {
        this.newline = newline;
    }

    public AccuRevUser getUser() {
        return user;
    }

    public void setUser(AccuRevUser user) {
        this.user = user;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String toString() {
        final StringBuffer buf = new StringBuffer();
        buf.append(super.toString()).append(" ::: ");
        buf.append("Workspace");
        buf.append("{location=").append(location);
        buf.append(",hostname=").append(hostname);
        buf.append(",user=").append(user);
        buf.append(",locking=").append(locking);
        buf.append(",newline=").append(newline);
        buf.append('}');
        return buf.toString();
    }
}
