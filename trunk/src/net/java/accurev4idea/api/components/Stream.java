/*
 * Stream.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on May 28, 2005, 5:21:19 PM
 */
package net.java.accurev4idea.api.components;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

/**
 * This is a marker POJO for Stream element in AccuRev.
 * 
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @since   1.0
 * @version 1.0
 */
public class Stream {
    private String name;
    private StreamType type;
    private long id;
    private long parentStreamId;
    private Stream parent;
    private Date creationDate;
    /**
     * Create a list of children with guesstimate of potential 50 elements
     */
    private final List children = new ArrayList(50);

    public StreamType getType() {
        return type;
    }

    public void setType(StreamType type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Stream getParent() {
        return parent;
    }

    public void setParent(Stream parent) {
        this.parent = parent;
    }

    public List getChildren() {
        return children;
    }

    public long getParentStreamId() {
        return parentStreamId;
    }

    public void setParentStreamId(long parentStreamId) {
        this.parentStreamId = parentStreamId;
    }

    public boolean hasChildren() {
        return !children.isEmpty();
    }

    public boolean isWorkspace() {
        return type == StreamType.WORKSPACE;
    }

    public boolean isNormalStream() {
        return type == StreamType.NORMAL;
    }

    public boolean isSnapshot() {
        return type == StreamType.SNAPSHOT;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
