/*
 * RevisionHistoryParser.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jun 26, 2005, 1:23:52 PM
 */
package net.java.accurev4idea.api.parsers;

import net.java.accurev4idea.api.components.AccuRevFile;
import net.java.accurev4idea.api.components.AccuRevTransaction;
import net.java.accurev4idea.api.components.AccuRevTransactionType;
import net.java.accurev4idea.api.components.AccuRevVersion;
import net.java.accurev4idea.api.components.Stream;
import net.java.accurev4idea.api.components.StreamType;
import net.java.accurev4idea.api.EnumUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version $Id: RevisionHistoryParser.java,v 1.1 2005/11/05 16:56:13 ifedulov Exp $
 * @since 0.1
 */
public class RevisionHistoryParser {
    /**
     * Log4j audit channel
     */
    private static Logger log = Logger.getLogger(RevisionHistoryParser.class);

    public static List extractRevisionHistory(final String string, final AccuRevFile file) {
        Document document = JDOMUtils.getDocument(string);
        List results = new LinkedList();
        Element root = document.getRootElement();
        // extract "streams" this file is appearing in
        Element streamsElement = root.getChild("streams");
        List streams = streamsElement.getChildren("stream");
        Map streamsMap = new HashMap(streams.size() * 2);
        for (Iterator i = streams.iterator(); i.hasNext();) {
            Element e = (Element) i.next();
            final Stream stream = new Stream();
            stream.setName(e.getAttributeValue("name"));
            stream.setId(Long.parseLong(e.getAttributeValue("id")));
            stream.setType((StreamType) EnumUtils.getTypedEnum(StreamType.class, e.getAttributeValue("type")));
            streamsMap.put(String.valueOf(stream.getId()), stream);
            log.debug(stream);
        }

        Element element = root.getChild("element");
        List transactions = element.getChildren("transaction");
        // each <transaction> element looks similar to following:
        // <transaction
        //     id="15"
        //     type="add"
        //     time="1117047034"
        //     user="ifedulov">
        //     <comment>initial checkin of the future accurev-idea plugin</comment>
        //     <version virtual="3/1" real="3/1" ancestor="2/1" elem_type="text"/>
        // </transaction>


        AccuRevTransaction transaction = null;
        for (Iterator i = transactions.iterator(); i.hasNext();) {
            Element e = (Element) i.next();
            transaction = new AccuRevTransaction();
            transaction.setFile(file);
            transaction.setTransactionId(Long.parseLong(e.getAttributeValue("id")));
            final String typeString = e.getAttributeValue("type");
            final AccuRevTransactionType type = (AccuRevTransactionType) EnumUtils.getTypedEnum(AccuRevTransactionType.class, typeString);
            log.assertLog(type != null, "AccuRevTransaction type is null for [" + typeString + "]");
            transaction.setType(type);
            transaction.setUserName(e.getAttributeValue("user"));
            transaction.setComment(StringUtils.defaultString(e.getChildText("comment"), StringUtils.EMPTY));
            final long time = Long.parseLong(e.getAttributeValue("time"));
            final long date = time * DateUtils.MILLIS_PER_SECOND;
            transaction.setDate(new Date(date));


            Element version = e.getChild("version");
            transaction.setVersion(new AccuRevVersion());
            transaction.getVersion().setReal(CompositeVersionParser.extractCompositeVersion(version.getAttributeValue("real"), streamsMap));
            transaction.getVersion().setVirtual(CompositeVersionParser.extractCompositeVersion(version.getAttributeValue("virtual"), streamsMap));
            transaction.getVersion().setAncestor(CompositeVersionParser.extractCompositeVersion(version.getAttributeValue("ancestor"), streamsMap));

            log.debug(transaction);

            results.add(transaction);
        }

        return results;
    }

}
