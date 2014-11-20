package net.java.accurev4idea.plugin.factories;

import com.intellij.openapi.vcs.VcsException;
//import com.intellij.openapi.vcs.checkin.changeListBasedCheckin.TreeElement;
//import com.intellij.openapi.vcs.versions.AbstractRevisions;
import com.intellij.openapi.vfs.LocalFileSystem;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Iterator;
import java.io.File;

import net.java.accurev4idea.plugin.AccuRevVcs;
import net.java.accurev4idea.plugin.revisions.AccuRevRevisions;
import net.java.accurev4idea.api.AccuRev;
import net.java.accurev4idea.api.components.AccuRevFile;
import net.java.accurev4idea.api.commands.StatusSelectionType;
import org.apache.log4j.Logger;

/**
 * $Id: AccuRevRevisionsFactory.java,v 1.2 2005/11/10 23:25:05 ifedulov Exp $
 * User: aantonov
 * Date: Nov 4, 2005
 * Time: 1:56:55 PM
 */
public class AccuRevRevisionsFactory extends AbstractAccuRevRevisionsFactory {

    private static final Logger log = Logger.getLogger(AccuRevRevisionsFactory.class);

    private AccuRevVcs vcs;

    public AccuRevRevisionsFactory(AccuRevVcs vcs) {
        this.vcs = vcs;
    }

    /**
     * Checks if the file is a directory or a file.
     * @param changeData
     * @return true if file, false if directory.
     */
    protected boolean isFile(Object changeData) {
        if (changeData == null) {
            return false;
        }

        AccuRevFile file = (AccuRevFile) changeData;
        return !file.isDirectory();
    }

    protected Map createFileToChangeMap(String[] paths) throws VcsException {
        // <File, SVNStatus>
        final Map result = new HashMap();

        LocalFileSystem lfs = LocalFileSystem.getInstance();

        for (int i = 0; i < paths.length; i++) {
            String path = paths[i];

            List files = AccuRev.getAccuRevFilesInFolderByStatus(path, StatusSelectionType.SELECT_ALL, vcs.getCommandExecListeners());
            for (Iterator j = files.iterator(); j.hasNext();) {
                AccuRevFile accuRevFile = (AccuRevFile) j.next();
                final File file = new File(accuRevFile.getAbsolutePath());
                result.put(file, accuRevFile);
            }
        }

        return result;
    }

//TODO    protected AbstractRevisions createRevisions(File file) {
//        return new AccuRevRevisions(
//                (TreeElement) myFileToTreeElementMap.get(file),
//                this);
//    }
}
