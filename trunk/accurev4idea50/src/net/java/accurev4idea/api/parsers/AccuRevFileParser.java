/*
 * AccuRevFileParser.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jun 26, 2005, 1:07:36 PM
 */
package net.java.accurev4idea.api.parsers;

import net.java.accurev4idea.api.components.AccuRevFile;
import net.java.accurev4idea.api.components.AccuRevFileStatus;
import net.java.accurev4idea.api.components.AccuRevFileType;
import net.java.accurev4idea.api.components.AccuRevVersion;
import net.java.accurev4idea.api.exceptions.ParseException;
import net.java.accurev4idea.api.EnumUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * This parser handles extract operations on AccuRev response XML related to {@link AccuRevFile}
 *
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version 1.1
 * @since 1.1
 */
public class AccuRevFileParser {
    /**
     * Log4j audit channel
     */
    private static Logger log = Logger.getLogger(AccuRevFileParser.class);

    /**
     * Extract {@link AccuRevFile} from given XML string and given local {@link java.io.File} reference.
     * XML is expected to look similar to following example:
     * <pre>
     * &lt;AcResponse
     *    Command="stat"
     *    Directory="C:/data/work/projects/accurev-idea-dev-home/idea-plugin"&gt;
     *  &lt;element
     *      location="\.\idea-plugin\build.xml"
     *      dir="no"
     *      id="11"
     *      elemType="text"
     *      Virtual="5\2"
     *      namedVersion="accurev-idea-dev\2"
     *      Real="8\1"
     *      status="(missing)"/&gt;
     * &lt;/AcResponse&gt;
     *
     * @param string the XML string received from AccuRev server
     * @param workspaceDir reference to workspaceDir on a local workspaceDir system
     * @return AccuRevFile instance corresponding to given local workspaceDir and xml string
     */
    public static AccuRevFile extractAccuRevFile(String string, File workspaceDir) {
        // create actual DOM reference and get the root node out
        Document document = JDOMUtils.getDocument(string);
        Element root = document.getRootElement();
        log.assertLog(JDOMUtils.hasChildren(root), "Root node in given xml string ["+string+"] doesn't have child nodes.");
        // root element will look similar to following:
        // NOTE: The version delimiters are broken on Windows platform thus you use 8\1 instead of 8/1
        Element element = root.getChild("element");
        if (element != null) {
            AccuRevFile accuRevFile = extractAccuRevFile(workspaceDir, element);
            return accuRevFile;
        }
        // element is "null", throw parse exception
        throw new ParseException("Unable to obtain <element> node from [" + document.toString() + "]");
    }

    /**
     * This method is used to extract multiple AccuRevFile objects from given xml string, this is
     * usually used by commands that run "stat" related methods.
     *
     * @param string
     * @param workspaceDir - If it is a Stream, null should be provided
     * @return
     */
    public static List extractAccuRevFiles(String string, File workspaceDir) {
        Document document = JDOMUtils.getDocument(string);
        Element root = document.getRootElement();
        // make sure that there are children available
        if(!JDOMUtils.hasChildren(root)) {
            return Collections.EMPTY_LIST;
        }
        List elements = root.getChildren("element");
        // check to make sure that elements list is not null and is not empty
        if(elements == null || elements.isEmpty()) {
            throw new ParseException("Unable to obtain <element> nodes from ["+document.toString()+"]");
        }

        List results = new LinkedList();
        for (Iterator i = elements.iterator(); i.hasNext();) {
            Element element = (Element) i.next();
            results.add(extractAccuRevFile(workspaceDir, element));
        }
        return results;
    }

    /**
     * Helper method to extract {@link AccuRevFile} from given xml element. Xml element is
     * expected to look like this:<p>
     * <pre>
     * &lt;element
     *      location="\.\idea-plugin\build.xml"
     *      dir="no"
     *      id="11"
     *      elemType="text"
     *      Virtual="5\2"
     *      namedVersion="accurev-idea-dev\2"
     *      Real="8\1"
     *      status="(missing)"/&gt;
     * </pre>
     * </p>
     *
     * @param workspaceDir reference to workspace directory this file is located in
     * @param element xml node containing information about AccuRevFile being extracted
     * @return AccuRevFile instance and never null
     */
    private static AccuRevFile extractAccuRevFile(File workspaceDir, Element element) {
        AccuRevFile accuRevFile = new AccuRevFile();
        // get the location of the file, but make sure that the first "." is removed
        // because path normalizer doesn't remove those and during path comparasion
        // equals might return negative for exactly the same file
        final String location = StringUtils.replaceOnce(element.getAttributeValue("location"), ".", StringUtils.EMPTY);
        // get the locationof the workspace dir (append extra separator to make sure
        // that directory name doesn't get merged with actual file name without one)
        if (workspaceDir != null) {
            try {
                final String workspaceDirLocation = workspaceDir.getCanonicalPath()+File.separator;
                accuRevFile.setAbsolutePath(new File(workspaceDirLocation, location).getCanonicalPath());
            } catch (IOException ioe) {
                log.debug("Unable to resolve the canonical path of the workspace directory provided...", ioe);
                final String workspaceDirLocation = workspaceDir.getAbsolutePath()+File.separator;
                accuRevFile.setAbsolutePath(new File(workspaceDirLocation, location).getAbsolutePath());
            }
        } else {
            // This is most likely a file belonging to a Stream, thus has no mapping to a local file system
            accuRevFile.setAbsolutePath(location);
        }
        accuRevFile.setDirectory("yes".equals(element.getAttributeValue("dir")));
        accuRevFile.setStatus((AccuRevFileStatus) EnumUtils.getTypedEnum(AccuRevFileStatus.class, element.getAttributeValue("status")));
        // only continue parsing other nodes if workspaceDir status is not "external"
        if (!AccuRevFileStatus.EXTERNAL.equals(accuRevFile.getStatus())) {
            accuRevFile.setFileId(Long.parseLong(element.getAttributeValue("id")));
            accuRevFile.setType((AccuRevFileType) EnumUtils.getTypedEnum(AccuRevFileType.class, element.getAttributeValue("elemType")));
            accuRevFile.setVersion(new AccuRevVersion());
            accuRevFile.getVersion().setReal(CompositeVersionParser.extractCompositeVersion(element.getAttributeValue("Real")));
            accuRevFile.getVersion().setVirtual(CompositeVersionParser.extractCompositeVersion(element.getAttributeValue("Virtual")));
            accuRevFile.getVersion().setNamed(CompositeVersionParser.extractCompositeVersion(element.getAttributeValue("namedVersion")));
        }
        return accuRevFile;
    }
}
