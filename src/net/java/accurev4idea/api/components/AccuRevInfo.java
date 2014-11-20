/*
 * AccuRevInfo.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on May 29, 2005, 1:45:44 PM
 */
package net.java.accurev4idea.api.components;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.Date;

/**
 * This POJO represents information about AccuRev subsystem such as current host, server name, port etc.
 *
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @since   1.0
 * @version 1.0
 */
public class AccuRevInfo {
    private String principal;
    private String host;
    private String serverName;
    private String port;
    private String accuRevBin;
    private Date clientTime;
    private Date serverTime;

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getAccuRevBin() {
        return accuRevBin;
    }

    public void setAccuRevBin(String accuRevBin) {
        this.accuRevBin = accuRevBin;
    }

    public Date getClientTime() {
        return clientTime;
    }

    public void setClientTime(Date clientTime) {
        this.clientTime = clientTime;
    }

    public Date getServerTime() {
        return serverTime;
    }

    public void setServerTime(Date serverTime) {
        this.serverTime = serverTime;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
