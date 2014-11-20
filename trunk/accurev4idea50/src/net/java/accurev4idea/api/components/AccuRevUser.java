/*
 * AccuRevUser.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jun 23, 2005, 10:19:29 PM
 */
package net.java.accurev4idea.api.components;

/**
 * POJO describing user in AccuRev subsystem.
 *
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version 1.0
 * @since 1.0
 */
public class AccuRevUser {
    private long id;
    private String username;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String toString() {
        final StringBuffer buf = new StringBuffer();
        buf.append("AccuRevUser");
        buf.append("{id=").append(id);
        buf.append(",username=").append(username);
        buf.append('}');
        return buf.toString();
    }
}
