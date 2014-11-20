/*
 * AccuRevExecutable.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jun 14, 2005, 11:43:09 PM
 */
package net.java.accurev4idea.api;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.File;

/**
 * This utility class is used to encapsulate determination and storage
 * of "accurev" executable location on current platform. Methods provided
 * to specify custom location as well as ability to specify the location as
 * system parameter via "-Daccurev.executable"
 *
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version 1.1
 * @since 1.0
 */
public abstract class AccuRevExecutable {
    /**
     * Log4j audit channel
     */
    private static final Logger log = Logger.getLogger(AccuRevExecutable.class);
    /**
     * Absolute path to the "accurev" executable which is platform dependent
     */
    private static volatile String absolutePath = StringUtils.EMPTY;

    // Initialize the accurev location and proper name for the executable
    static {
        // read system property "accurev.executable" if it's specified,
        // use it as path to the accurev, otherwise set it based on
        // platform
        final String accurevExecutable = System.getProperty("accurev.executable");
        String accurevLocal = null;
        // CHECK: Verify the paths for the Solaris and Mac Platforms
        if (StringUtils.isNotBlank(accurevExecutable)) {
            accurevLocal = accurevExecutable;
        } else if (Platform.WINDOWS.isCurrentPlatform()) {
            accurevLocal = "C:\\Program Files\\AccuRev\\bin\\accurevw.exe";
        } else if (Platform.LINUX.isCurrentPlatform()) {
            accurevLocal = "/usr/local/bin/accurev";
        } else if (Platform.SOLARIS.isCurrentPlatform()) {
            accurevLocal = "/usr/local/bin/accurev";
        } else if (Platform.MAC.isCurrentPlatform()) {
            accurevLocal = "/usr/local/bin/accurev";
        } else {
            // by default set to Linux default location
            accurevLocal = "/usr/local/bin/accurev";
        }
        // set accurev executable path. Following setter
        // will throw IllegalArgumentException if binary
        // can't be located, catch it and allow to proceed
        try {
            setAbsolutePath(accurevLocal);
        } catch (IllegalArgumentException e) {
            // this is static initializer, don't propagate this exception out
            // otherwise this class will never can be loaded
            log.info(e.getLocalizedMessage(), e);
        }
    }

    /**
     * Obtain the current location to accurev executable
     *
     * @return current location to accurev executable, never null
     */
    public static String getAbsolutePath() {
        return absolutePath;
    }

    /**
     * Set the path to accurev executable to custom location. This method will
     * verify given parameter validity.
     *
     * @param accurev new location for accurev executable
     */
    // NOTE: I don't see this method benefiting from synchronization at all
    public static void setAbsolutePath(String accurev) {
        verifyAccuRevExecutablePath(accurev);
        absolutePath = accurev;
    }

    /**
     * Do a quick sanity check, first check if the given parameter is not null or
     * blank and next by creating a {@link java.io.File} instance with given argument and
     * verifying that {@link java.io.File#canRead()} method returns true.
     *
     * @param accurevLocal location of the accurev executable to verify
     */
    private static void verifyAccuRevExecutablePath(String accurevLocal) {
        if(StringUtils.isBlank(accurevLocal)) {
            throw new IllegalArgumentException("AccuRev executable path can't be null or empty.");
        }
        final File accurevFile = new File(accurevLocal);
        if (!accurevFile.exists()) {
            throw new IllegalArgumentException("AccuRev binary doesn't exist at specified location [" + accurevLocal + "]");
        }
        if(!accurevFile.canRead()) {
            throw new IllegalArgumentException("AccuRev binary can't be read, check permissions.");
        }
    }
}
