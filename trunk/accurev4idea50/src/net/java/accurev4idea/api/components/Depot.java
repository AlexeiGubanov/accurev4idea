/*
 * Depot.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on May 28, 2005, 5:21:13 PM
 */
package net.java.accurev4idea.api.components;

/**
 * This is a marker POJO for Depot element in AccuRev.
 *
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @since   1.0
 * @version 1.0
 */
public class Depot {
    private String name;
    private long number;
    private long slice;
    private boolean exclusiveLocking;
    private boolean caseSensitive;
    private boolean hidden;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public long getSlice() {
        return slice;
    }

    public void setSlice(long slice) {
        this.slice = slice;
    }

    public boolean isExclusiveLocking() {
        return exclusiveLocking;
    }

    public void setExclusiveLocking(boolean exclusiveLocking) {
        this.exclusiveLocking = exclusiveLocking;
    }

    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public String toString() {
        return "Depot{" +
               "name='" + name + "'" +
               ", number=" + number +
               ", slice=" + slice +
               ", exclusiveLocking=" + exclusiveLocking +
               ", caseSensitive=" + caseSensitive +
               ", hidden=" + hidden +
               "}";
    }
}
