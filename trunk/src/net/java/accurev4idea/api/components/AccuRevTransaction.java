/*
 * AccuRevTransaction.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jun 12, 2005, 4:25:03 PM
 */
package net.java.accurev4idea.api.components;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * POJO Corresponding to a "transaction" of a given "File" in that file's revision
 * history.
 *  
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @since   1.0
 * @version 1.0
 */
public class AccuRevTransaction {
    private AccuRevFile file;
    private AccuRevTransactionType type;
    private long transactionId;
    private String userName;
    private String comment;
    private AccuRevVersion version;
    private Date date;

    public AccuRevFile getFile() {
        return file;
    }

    public void setFile(AccuRevFile file) {
        this.file = file;
    }

    public AccuRevTransactionType getType() {
        return type;
    }

    public void setType(AccuRevTransactionType type) {
        this.type = type;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public AccuRevVersion getVersion() {
        return version;
    }

    public void setVersion(AccuRevVersion version) {
        this.version = version;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    public String toString() { 
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return "AccuRevTransaction{" +
               "file=" + file +
               ", type=" + type +
               ", transactionId=" + transactionId +
               ", userName='" + userName + "'" +
               ", comment='" + comment + "'" +
               ", version=" + version +
               ", date=" + (date == null ? null : sdf.format(date)) +
               "}";
    }
}
