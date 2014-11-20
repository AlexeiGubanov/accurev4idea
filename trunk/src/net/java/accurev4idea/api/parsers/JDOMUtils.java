/*
 * JDOMUtils.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jun 26, 2005, 1:19:18 PM
 */
package net.java.accurev4idea.api.parsers;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;

/**
 * This utility class aids in {@link org.jdom.Document} creation tasks by wrapping calls to
 * proper methods provided by JDOM xml parser implmentation.
 *
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version $Id: JDOMUtils.java,v 1.1 2005/11/05 16:56:13 ifedulov Exp $
 * @since 0.1
 */
public class JDOMUtils {
    /**
     * Log4j audit channel
     */
    private static Logger log = Logger.getLogger(JDOMUtils.class);
    /**
     * Create {@link org.jdom.input.SAXBuilder} instance at field level as it's an expensive operation
     * and should be only done once.
     */
    private static final SAXBuilder saxBuilder = new SAXBuilder() {{
            // because accurev responses do not contain DTD reference ensure
            // that entity resolver is set to NullEntityResolver to eliminate the need
            // to validate the document
            setEntityResolver(new NullEntityResolver());
    }};

    /**
     * Obtain {@link org.jdom.Document} from given String <tt>xml</tt>. If given <tt>xml</tt> can't
     * be parsed into <tt>Document</tt> the {@link IllegalArgumentException} will be thrown.
     *
     * @param xml string to parse into Document
     * @return {@link org.jdom.Document} instance if xml was valid
     */
    public static Document getDocument(String xml) {
        Document document = null;
        StringReader stringReader = null;
        try {
            stringReader = new StringReader(xml);
            document = saxBuilder.build(stringReader);
        } catch (IOException e) {
            // Log the error and full exception stack trace
            log.error(e.getLocalizedMessage(), e);
            // rethrow as IllegalArgumentException to indicate failure of
            // proper input xml string
            throw new IllegalArgumentException(e.getLocalizedMessage());
        } catch (JDOMException e) {
            // Log the error and full exception stack trace
            log.error(e.getLocalizedMessage(), e);
            // rethrow as IllegalArgumentException to indicate failure of
            // proper input xml string
            throw new IllegalArgumentException(e.getLocalizedMessage());
        } finally {
            // dispose the reader if it was created
            if(stringReader != null) {
                stringReader.close();
            }
        }
        return document;
    }

    /**
     * Returns <tt>true</tt> if given element has children nodes, <tt>false</tt> otherwise
     *
     * @param element the {@link org.jdom.Element} instance to check, can be null
     * @return <tt>true</tt> if given element has children, <tt>false</tt> otherwise
     */
    public static boolean hasChildren(Element element) {
        return (element != null && element.getChildren() != null && !element.getChildren().isEmpty());
    }

    /**
     * Null {@link org.xml.sax.EntityResolver} used to set the {@link org.jdom.input.SAXBuilder#setEntityResolver(org.xml.sax.EntityResolver)}
     * @see org.xml.sax.EntityResolver
     */
    private static class NullEntityResolver implements EntityResolver {
        public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
            return new InputSource(StringUtils.EMPTY);
        }
    }



}
