/*
 * AccuRevInfoParser.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jun 26, 2005, 1:07:59 PM
 */
package net.java.accurev4idea.api.parsers;

import net.java.accurev4idea.api.components.AccuRevInfo;
import net.java.accurev4idea.api.exceptions.AccuRevRuntimeException;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * This parser handles data comming from "accurev info" call.
 *
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version $Id: AccuRevInfoParser.java,v 1.1 2005/11/05 16:56:12 ifedulov Exp $
 * @since 0.1
 */
public class AccuRevInfoParser {
    /**
     * Log4j audit channel
     */
    private static Logger log = Logger.getLogger(AccuRevInfoParser.class);

    /**
     * Extract {@link AccuRevInfo} from given
     * {@link net.java.accurev4idea.api.exec.CommandResult} object, where the
     * {@link net.java.accurev4idea.api.exec.CommandResult#getOut()} should contain the output from
     * "accurev info" command that will be parsed in this method into AccuRevInfo instance.
     *
     * @param string
     * @return {@link AccuRevInfo}
     */
    public static AccuRevInfo extractAccuRevInfo(String string) {
        BufferedReader reader = new BufferedReader(new StringReader(string));
        Map info = new HashMap();
        AccuRevInfo accuRevInfo = new AccuRevInfo();
        try {
            String line = null;
            while (reader.ready() && (line = reader.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, "\\\t");
                while (st.hasMoreTokens() && st.countTokens() == 2) {
                    info.put(st.nextToken().toLowerCase(), st.nextToken());
                }
            }
        } catch (IOException e) {
            // Log the error and full exception stack trace
            log.error(e.getLocalizedMessage(), e);
            // throw runtime exception
            throw new AccuRevRuntimeException(e.getLocalizedMessage(),e);
        } finally {
            // close reader
            try {
                if(reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
            }
        }
        accuRevInfo.setPrincipal((String) info.get("principal:"));
        accuRevInfo.setServerName((String) info.get("server_name:"));
        accuRevInfo.setHost((String) info.get("host:"));
        accuRevInfo.setPort((String) info.get("port:"));
        accuRevInfo.setAccuRevBin((String) info.get("accurev_bin:"));

        final Date clientTime = extractDateByTokenName(info, "client_time:");
        final Date serverTime = extractDateByTokenName(info, "server_time:");

        accuRevInfo.setServerTime(serverTime);
        accuRevInfo.setClientTime(clientTime);

        // debug output current info
        log.debug(accuRevInfo);

        return accuRevInfo;
    }

    /**
     * Extract {@link java.util.Date} by given token name <tt>s</tt> from Map of tokens from info command output
     *
     * @param info {@link java.util.Map} of tokens from info command output
     * @param s string matching the date token name
     * @return date extracted and converted to {@link java.util.Date}
     */
    private static Date extractDateByTokenName(Map info, String s) {
        final String timeString = (String) info.get(s);
        final String secondsString = timeString.substring(timeString.indexOf("(")+1, timeString.indexOf(")"));
        return new Date(Long.parseLong(secondsString)*DateUtils.MILLIS_PER_SECOND);
    }
}
                                                           
