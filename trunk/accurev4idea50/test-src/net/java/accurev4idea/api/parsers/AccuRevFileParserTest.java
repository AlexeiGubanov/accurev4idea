/*
 * AccuRevFileParserTest.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jun 29, 2005, 8:00:48 PM
 */
package net.java.accurev4idea.api.parsers;

import junit.framework.TestCase;
import net.java.accurev4idea.api.components.AccuRevFile;
import net.java.accurev4idea.api.components.AccuRevFileStatus;
import net.java.accurev4idea.api.components.AccuRevFileType;
import org.apache.commons.lang.SystemUtils;

import java.io.File;
import java.util.List;

/**
 * JUnit test for {@link net.java.accurev4idea.api.parsers.AccuRevFileParser}
 *
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version $Id: AccuRevFileParserTest.java,v 1.1 2006/02/21 22:45:38 ifedulov Exp $
 * @since 0.1
 */
public class AccuRevFileParserTest extends TestCase {

    /**
     * Unit test for {@link net.java.accurev4idea.api.parsers.AccuRevFileParser#extractAccuRevFile(String,File)}
     *
     * @see net.java.accurev4idea.api.parsers.AccuRevFileParser#extractAccuRevFile(String,File)
     */
    public void testExtractAccuRevFile() throws Exception {
        File file = new File(SystemUtils.JAVA_IO_TMPDIR);
        String example = "<AcResponse\n" +
                         "    Command=\"stat\"\n" +
                         "    Directory=\"C:/data/work/projects/accurev-tools-home/gui\">\n" +
                         "  <element\n" +
                         "      location=\"\\.\\gui\\acgui-debug.sh\"\n" +
                         "      dir=\"no\"\n" +
                         "      id=\"5\"\n" +
                         "      elemType=\"text\"\n" +
                         "      Virtual=\"1\\1\"\n" +
                         "      namedVersion=\"accurev-tools\\1\"\n" +
                         "      Real=\"2\\1\"\n" +
                         "      status=\"(backed)\"/>\n" +
                         "</AcResponse>";
        AccuRevFile accuRevFile = AccuRevFileParser.extractAccuRevFile(example, file);
        assertNotNull(accuRevFile);
        assertNotNull(accuRevFile.getStatus());
        assertFalse(accuRevFile.isDirectory());
        assertNotNull(accuRevFile.getAbsolutePath());
        assertNotNull(accuRevFile.getType());
        assertTrue(accuRevFile.getFileId() == 5);
        assertTrue(AccuRevFileStatus.BACKED.equals(accuRevFile.getStatus()));
        assertTrue(AccuRevFileType.TEXT.equals(accuRevFile.getType()));
    }

    /**
     * Unit test for {@link net.java.accurev4idea.api.parsers.AccuRevFileParser#extractAccuRevFiles(String,File)}
     *
     * @see net.java.accurev4idea.api.parsers.AccuRevFileParser#extractAccuRevFiles(String,File)
     */
    public void testExtractAccuRevFiles() throws Exception {
        File file = new File(SystemUtils.JAVA_IO_TMPDIR);
        String example1 = "<AcResponse\n" +
                         "    Command=\"stat\"\n" +
                         "    Directory=\"C:/data/work/projects/accurev-tools-home\">\n" +
                         "  <element\n" +
                         "      location=\".\\admin\\init-depot.sh\"\n" +
                         "      dir=\"no\"\n" +
                         "      id=\"3\"\n" +
                         "      elemType=\"text\"\n" +
                         "      Virtual=\"1\\6\"\n" +
                         "      namedVersion=\"accurev-tools\\6\"\n" +
                         "      Real=\"2\\6\"\n" +
                         "      status=\"(modified)\"/>\n" +
                         "  <element\n" +
                         "      location=\".\\gui\\acgui-debug.sh\"\n" +
                         "      dir=\"no\"\n" +
                         "      id=\"5\"\n" +
                         "      elemType=\"text\"\n" +
                         "      Virtual=\"1\\1\"\n" +
                         "      namedVersion=\"accurev-tools\\1\"\n" +
                         "      Real=\"2\\1\"\n" +
                         "      status=\"(modified)\"/>\n" +
                         "</AcResponse>";
        String example2 = "<AcResponse\n" +
                          "    Command=\"stat\"\n" +
                          "    Directory=\"C:/data/work/projects/accurev-tools-home\">\n" +
                          "  <element\n" +
                          "      location=\".\\blah\"\n" +
                          "      dir=\"no\"\n" +
                          "      status=\"(external)\"/>\n" +
                          "  <element\n" +
                          "      location=\".\\blah1\"\n" +
                          "      dir=\"no\"\n" +
                          "      status=\"(external)\"/>\n" +
                          "  <element\n" +
                          "      location=\".\\foo\"\n" +
                          "      dir=\"no\"\n" +
                          "      status=\"(external)\"/>\n" +
                          "  <element\n" +
                          "      location=\".\\foo1\"\n" +
                          "      dir=\"no\"\n" +
                          "      status=\"(external)\"/>\n" +
                          "</AcResponse>";

        List files = AccuRevFileParser.extractAccuRevFiles(example1, file);
        assertNotNull(files);
        assertFalse(files.isEmpty());
        files = AccuRevFileParser.extractAccuRevFiles(example2, file);
        assertNotNull(files);
        assertFalse(files.isEmpty());
    }

    public void testDirectory() throws Exception {
        String directoryString = "C:/data/work/projects/accurev-idea-dev-home/idea-plugin/java/com/orbitz";
        String fileLocationString = "\\.\\idea-plugin\\java\\com\\orbitz\\build.xml";
        File file = new File(fileLocationString);
        File directory = new File(directoryString);
        System.out.println(file.getParentFile().getCanonicalFile().toURL());
        System.out.println(directory.getCanonicalPath());
        

    }
}