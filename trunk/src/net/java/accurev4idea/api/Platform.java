/*
 * Platform.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on May 27, 2005, 5:12:48 PM
 */
package net.java.accurev4idea.api;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.enums.Enum;
import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.List;

/**
 * Enumeration of supported platforms. During class load the current platform
 * will be determined by figuring out the proper mapping based on "os.name" system
 * property.
 *
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @since   1.0
 * @version $Id: Platform.java,v 1.1 2005/11/05 16:56:15 ifedulov Exp $
 */
public class Platform extends Enum {
    /**
     * Log4j audit channel
     */
    private static Logger log = Logger.getLogger(Platform.class);

    /**
     * Platform referencing Windows (any version)
     */
    public static final Platform WINDOWS = new Platform("Windows");
    /**
     * Platform referencing Linux (any version)
     */
    public static final Platform LINUX = new Platform("Linux");
    /**
     * Platform referencing Mac (any version)
     */
    public static final Platform MAC = new Platform("Mac");
    /**
     * Platform referencing SUN Solaris (any version)
     */
    public static final Platform SOLARIS = new Platform("Solaris");

    /**
     * Internal reference to current platform the VM is running on
     */
    private static final Platform currentPlatform;

    // Initialize the accurev location and proper name for the executable
    // FIXME: Move this to PlatformDeterminator class
    static {
        // obtain system property "os.name" and if it's contains "Windows"
        // string assume microsoft windows and set executable to
        // "accurevw.exe"; Otherwise assume "accurev"
        String osName = System.getProperty("os.name");
        if(osName == null) {
            throw new IllegalStateException("Unable to access system property [os.name] to determine the platform.");
        }
        if(log.isDebugEnabled()) {
            log.debug("Read [os.name] from system properties as ["+osName+"]");
        }
        // declare local platform reference
        Platform currentPlatformLocal = null;
        // doing indexOf try to lookup each known platform name
        List enumerationElements = getEnumList(Platform.class);
        for (Iterator i = enumerationElements.iterator(); i.hasNext();) {
            Platform platform = (Platform) i.next();
            // if osName contains the name of the platform, get the platform reference
            // assigned to internal variable and bail out from the loop
            if(StringUtils.contains(osName, platform.getName())) {
                currentPlatformLocal = platform;
                break;
            }
        }
        // check if platform was determined in above block, and if not
        // log the warning and default platform to "Linux"
        if(currentPlatformLocal == null) {
            log.warn("Unable to map Platform from os.name: ["+osName+"], defaulting to Platform.LINUX");
            currentPlatformLocal = Platform.LINUX;
        }
        // copy the current platform reference into class field
        currentPlatform = currentPlatformLocal;

        if(log.isDebugEnabled()) {
            log.debug("Current platform is ["+currentPlatform.getName()+"]");
        }
    }

    /**
     * Default constructor for this enumeration. See {@link org.apache.commons.lang.enums.Enum(String)} for
     * more details.
     *
     * @param s the descriptor for the enumeration
     */
    private Platform(String s) {
        super(s);
    }

    /**
     * Obtain the current {@link net.java.accurev4idea.api.Platform} the VM is running on. The reference
     * returned by this method is determined statically during class load.
     *
     * @return current {@link net.java.accurev4idea.api.Platform} the VM is running in
     */
    public static final Platform getCurrentPlatform() {
        return currentPlatform;
    }

    /**
     * Return <tt>true</tt> if given {@link net.java.accurev4idea.api.Platform} enumeration element is
     * the "currentPlatform"
     *
     * @return true if {@link #getCurrentPlatform()} matches given instance this method is invoked on
     */
    public final boolean isCurrentPlatform() {
        return this.equals(currentPlatform);
    }

}
