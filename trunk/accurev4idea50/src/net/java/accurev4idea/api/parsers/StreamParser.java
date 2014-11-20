/*
 * StreamParser.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jun 26, 2005, 1:28:40 PM
 */
package net.java.accurev4idea.api.parsers;

import net.java.accurev4idea.api.components.Stream;
import net.java.accurev4idea.api.components.StreamType;
import net.java.accurev4idea.api.EnumUtils;
import org.apache.log4j.Logger;
import org.apache.commons.lang.time.DateUtils;
import org.jdom.Document;
import org.jdom.Element;

import java.util.*;

/**
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version $Id: StreamParser.java,v 1.2 2005/11/07 02:35:59 ifedulov Exp $
 * @since 0.1
 */
public class StreamParser {
    /**
     * Log4j audit channel
     */
    private static Logger log = Logger.getLogger(StreamParser.class);

    /**
     *
     * @param string the not null xml string to use to parse out streams
     * @return root Stream element that is a "root" for streams tree
     */
    public static Stream extractStreamsTree(final String string) {
        Document document = JDOMUtils.getDocument(string);
        Element root = document.getRootElement();
        List nodes = root.getChildren("stream");
        // response looks similar to following:
        //<streams>
        //  <stream
        //      name="accurev-tools"
        //      depotName="accurev-tools"
        //      streamNumber="1"
        //      isDynamic="true"
        //      type="normal"
        //      startTime="1116595551"/>
        //  <stream
        //      name="accurev-idea-dev"
        //      basis="accurev-tools"
        //      basisStreamNumber="1"
        //      depotName="accurev-tools"
        //      streamNumber="5"
        //      isDynamic="true"
        //      type="normal"
        //      startTime="1117047097"
        //      has_members="true"/>
        //  <stream
        //      name="accurev-idea-dev_ifedulov"
        //      basis="accurev-idea-dev"
        //      basisStreamNumber="5"
        //      depotName="accurev-tools"
        //      streamNumber="6"
        //      isDynamic="false"
        //      type="workspace"
        //      startTime="1117049369"
        //      has_members="true"/>
        //  <stream
        //      name="accurev-tools_ifedulov"
        //      basis="accurev-tools"
        //      basisStreamNumber="1"
        //      depotName="accurev-tools"
        //      streamNumber="7"
        //      isDynamic="false"
        //      type="workspace"
        //      startTime="1117050852"/>
        //  <stream
        //      name="accurev-idea-dev-home_ifedulov"
        //      basis="accurev-idea-dev"
        //      basisStreamNumber="5"
        //      depotName="accurev-tools"
        //      streamNumber="8"
        //      isDynamic="false"
        //      type="workspace"
        //      startTime="1117223704"
        //      has_members="true"/>
        //</streams>

        // in order to construct the tree of the streams from linear representation
        // first pass extract all <stream/> elements in Stream representation POJO and
        // store them in the map, also saving the parent stream (the one that doesn't have
        // basisStreamNumber defined). Once all elements parsed
        Map childrenStreamsCache = new HashMap(nodes.size() * 2);
        // use linked hash map to maintain order
        // CHECK: this might not be necessary
        Map streamsLookupCache = new LinkedHashMap(nodes.size() * 2);
        Stream rootStream = null;
        Stream stream;
        for (Iterator i = nodes.iterator(); i.hasNext();) {
            Element e = (Element) i.next();
            stream = new Stream();
            stream.setName(e.getAttributeValue("name"));
            stream.setId(Long.parseLong(e.getAttributeValue("streamNumber")));
            final String basisStreamNumber = e.getAttributeValue("basisStreamNumber");
            if(basisStreamNumber != null) {
                stream.setParentStreamId(Long.parseLong(basisStreamNumber));
            } else {
                // found root stream, save the reference
                log.assertLog(rootStream == null, "Expected null rootStream, but it's already set to ["+rootStream+"]");
                rootStream = stream;
            }
            stream.setType((StreamType) EnumUtils.getTypedEnum(StreamType.class, e.getAttributeValue("type")));
            stream.setCreationDate(new Date(Long.parseLong(e.getAttributeValue("startTime"))* DateUtils.MILLIS_PER_SECOND));

            List childrenForParentStream = (List) childrenStreamsCache.get(String.valueOf(stream.getParentStreamId()));
            if(childrenForParentStream == null) {
                childrenForParentStream = new LinkedList();
                childrenStreamsCache.put(String.valueOf(stream.getParentStreamId()), childrenForParentStream);
            }
            childrenForParentStream.add(stream);

            // add stream to the lookup by id cache
            streamsLookupCache.put(String.valueOf(stream.getId()), stream);

        }

        log.assertLog(rootStream != null, "Expected root stream to be not null!");
        // iterate over results list and for each stream element populate the list of children
        // and assign parent
        for (Iterator i = streamsLookupCache.values().iterator(); i.hasNext();) {
            Stream next = (Stream) i.next();
            final Collection childrenStreams = (Collection) childrenStreamsCache.get(String.valueOf(next.getId()));
            if (childrenStreams != null) {
                // TODO: Check if parent reference even needed in Stream. It's doesn't cost
                //       that much anyway to process but still
                for (Iterator j = childrenStreams.iterator(); j.hasNext();) {
                    Stream child = (Stream) j.next();
                    child.setParent(next);
                    next.getChildren().add(child);
                }
            }
        }
        if(log.isDebugEnabled()) {
            log.debug("Read rootStream as ["+rootStream+"]");
        }
        return rootStream;
    }


}
