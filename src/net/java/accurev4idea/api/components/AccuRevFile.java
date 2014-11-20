/*
 * AccuRevFile.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jun 12, 2005, 4:19:43 PM
 */
package net.java.accurev4idea.api.components;


/**
 * POJO representing a "File" in AccuRev subsystem.
 * 
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @since   1.0
 * @version 1.0
 */
public class AccuRevFile {
    private String absolutePath;
    private boolean isDirectory;
    private long fileId;
    private AccuRevFileType type;
    private AccuRevVersion version;
    private AccuRevFileStatus status;

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean directory) {
        isDirectory = directory;
    }

    public long getFileId() {
        return fileId;
    }

    public void setFileId(long fileId) {
        this.fileId = fileId;
    }

    public AccuRevFileType getType() {
        return type;
    }

    public void setType(AccuRevFileType type) {
        this.type = type;
    }

    public AccuRevVersion getVersion() {
        return version;
    }

    public void setVersion(AccuRevVersion version) {
        this.version = version;
    }

    public AccuRevFileStatus getStatus() {
        return status;
    }

    public void setStatus(AccuRevFileStatus status) {
        this.status = status;
    }

    public String toString() {
        return "AccuRevFile{" +
               "absolutePath='" + absolutePath + "'" +
               ", isDirectory=" + isDirectory +
               ", fileId=" + fileId +
               ", type=" + type +
               ", version=" + version +
               ", status=" + status +
               "}";
    }
}
