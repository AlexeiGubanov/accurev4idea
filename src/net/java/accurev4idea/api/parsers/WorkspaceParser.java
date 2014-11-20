/*
 * WorkspaceParser.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jun 26, 2005, 1:32:54 PM
 */
package net.java.accurev4idea.api.parsers;

import net.java.accurev4idea.api.components.AccuRevUser;
import net.java.accurev4idea.api.components.Workspace;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.io.File;

/**
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version $Id: WorkspaceParser.java,v 1.1 2005/11/05 16:56:13 ifedulov Exp $
 * @since 0.1
 */
public class WorkspaceParser {
    /**
     * Log4j audit channel
     */
    private static Logger log = Logger.getLogger(WorkspaceParser.class);

    public static Map extractWorkspaces(final String string) {
        Document document = JDOMUtils.getDocument(string);
        Map results = new HashMap(50);
        List nodes = document.getRootElement().getChildren("Element");
        // Expecting to see response similar to following
        //  <AcResponse
        //    Command="show wspaces">
        //    ...
        //    <Element
        //        Name="scratch-if-appsupport-927_ifedulov"
        //        Storage="c:/tmp/scratch-if-appsupport-927_ifedulov"
        //        Host="hyperion"
        //        Stream="6"
        //        depot="scratch-if"
        //        Target_trans="0"
        //        Trans="0"
        //        Type="17"
        //        EOL="2"
        //        user_id="41"
        //        user_name="ifedulov"/>
        //  </AcResponse>

        Workspace workspace = null;
        for (Iterator i = nodes.iterator(); i.hasNext();) {
            Element e = (Element) i.next();
            workspace = new Workspace();
            workspace.setName(e.getAttributeValue("Name"));
            workspace.setLocation(new File(e.getAttributeValue("Storage")).getAbsolutePath());
            workspace.setHostname(e.getAttributeValue("Host"));
            workspace.setId(Long.parseLong(e.getAttributeValue("Stream")));
            workspace.setUser(new AccuRevUser());
            workspace.getUser().setUsername(e.getAttributeValue("user_name"));
            workspace.getUser().setId(Long.parseLong(e.getAttributeValue("user_id")));
            results.put(workspace.getName(), workspace);
        }
        return results;
    }


}
