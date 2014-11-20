/*
 * DepotParser.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jun 26, 2005, 1:31:42 PM
 */
package net.java.accurev4idea.api.parsers;

import net.java.accurev4idea.api.components.Depot;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version $Id: DepotParser.java,v 1.1 2005/11/05 16:56:13 ifedulov Exp $
 * @since 0.1
 */
public class DepotParser {
    /**
     * Log4j audit channel
     */
    private static Logger log = Logger.getLogger(DepotParser.class);
    /**
     * Obtain list of {@link Depot}s from given command result.
     *
     * @param string the xml to parse depots from
     * @return List of {@link Depot}s for given command result
     */
    public static List extractDepots(final String string) {
        Document document = JDOMUtils.getDocument(string);
        List results = new LinkedList();
        List nodes = document.getRootElement().getChildren("Element");
        Depot depot = null;
        for (Iterator i = nodes.iterator(); i.hasNext();) {
            Element e = (Element) i.next();
            depot = new Depot();
            depot.setName(e.getAttribute("Name").getValue());
            // TODO: Extract all other attributes
            results.add(depot);
        }

        return results;
    }

}
