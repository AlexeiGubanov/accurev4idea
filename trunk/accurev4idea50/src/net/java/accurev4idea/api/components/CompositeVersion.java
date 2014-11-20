/*
 * CompositeVersion.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jun 18, 2005, 3:27:54 PM
 */
package net.java.accurev4idea.api.components;


/**
 * This POJO class represents a "composite" version notion as used by AccuRev, i.e. versions
 * that look like 8/3 or "my-depot-stream/12"
 *
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version 1.0
 * @since 1.0
 */
public class CompositeVersion {
    private String versionString;
    private Stream stream;
    private long streamId;
    private String streamName;
    private long versionId;

    public String getVersionString() {
        return versionString;
    }

    public void setVersionString(String versionString) {
        this.versionString = versionString;
    }

    public long getVersionId() {
        return versionId;
    }

    public void setVersionId(long versionId) {
        this.versionId = versionId;
    }

    public Stream getStream() {
        return stream;
    }

    public void setStream(Stream stream) {
        this.stream = stream;
    }

    public long getStreamId() {
        return streamId;
    }

    public void setStreamId(long streamId) {
        this.streamId = streamId;
    }

    public String getStreamName() {
        return streamName;
    }

    public void setStreamName(String streamName) {
        this.streamName = streamName;
    }

    public String toString() {
        return "CompositeVersion{" +
               "versionString='" + versionString + "'" +
               ", stream=" + stream +
               ", streamId=" + streamId +
               ", streamName='" + streamName + "'" +
               ", versionId=" + versionId +
               "}";
    }
}
