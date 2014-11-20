/*
 * CompositeVersionParser.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jun 26, 2005, 1:16:23 PM
 */
package net.java.accurev4idea.api.parsers;

import org.apache.log4j.Logger;
import net.java.accurev4idea.api.components.CompositeVersion;
import net.java.accurev4idea.api.components.Stream;

import java.util.StringTokenizer;
import java.util.Map;

/**
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version $Id: CompositeVersionParser.java,v 1.1 2005/11/05 16:56:12 ifedulov Exp $
 * @since 0.1
 */
public class CompositeVersionParser {
    /**
     * Log4j audit channel
     */
    private static Logger log = Logger.getLogger(CompositeVersionParser.class);

    public static CompositeVersion extractCompositeVersion(final String versionString) {
        if(versionString == null) {
            return null;
        }
        StringTokenizer st = new StringTokenizer(versionString, "/");
        int count = st.countTokens();
        if(count != 2) {
            // THIS IS ACCUREV BUG!!!!!!! ON WINDOWS PLATFORM SOMETIMES YOU GET "\" INSTEAD OF "/"
            st = new StringTokenizer(versionString, "\\");
            count = st.countTokens();
            if(count != 2) {
                throw new IllegalArgumentException("Unable to parse composite version from ["+versionString+"] string.");
            }
        }
        // at this point it's certain that tokenizer has two tokens, extract them out.
        String streamIdString = st.nextToken();
        String versionIdString = st.nextToken();

        // convert the strings over into longs
        long streamId = Long.MIN_VALUE;
        String streamName = null;
        try {
            streamId = Long.parseLong(streamIdString);
        } catch (NumberFormatException e) {
            // assume this is a named version, get the streamName set instead
            streamName = streamIdString;
        }
        long versionId = Long.parseLong(versionIdString);

        CompositeVersion version = new CompositeVersion();
        version.setVersionString(versionString);
        version.setVersionId(versionId);
        version.setStreamId(streamId);
        version.setStreamName(streamName);

        // return result
        return version;
    }

    public static CompositeVersion extractCompositeVersion(final String versionString, final Map streamsMap) {
        if(versionString == null)
            return null;

        final CompositeVersion realVersion = extractCompositeVersion(versionString);
        final Stream stream = (Stream) streamsMap.get(String.valueOf(realVersion.getStreamId()));
        realVersion.setStream(stream);
        return realVersion;
    }


}
