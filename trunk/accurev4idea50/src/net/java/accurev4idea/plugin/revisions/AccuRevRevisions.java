package net.java.accurev4idea.plugin.revisions;

import com.intellij.openapi.vcs.checkin.changeListBasedCheckin.ChangeListBasedRevisions;
import com.intellij.openapi.vcs.checkin.changeListBasedCheckin.TreeElement;
import com.intellij.openapi.vcs.checkin.changeListBasedCheckin.ChangeListBasedRevisionsFactory;
import com.intellij.openapi.vcs.checkin.Revisions;
import com.intellij.openapi.vcs.checkin.DifferenceType;
import com.intellij.openapi.vcs.versions.AbstractRevisions;
import net.java.accurev4idea.api.components.AccuRevFileStatus;
import net.java.accurev4idea.api.components.AccuRevFile;

import java.util.List;

/**
 * $Id: AccuRevRevisions.java,v 1.2 2006/06/20 19:58:21 ifedulov Exp $
 * User: aantonov
 * Date: Nov 4, 2005
 * Time: 4:26:47 PM
 */
public class AccuRevRevisions extends ChangeListBasedRevisions implements Revisions {

    public AccuRevRevisions(TreeElement treeElement, ChangeListBasedRevisionsFactory changeListBasedRevisionsFactory) {
        super(treeElement, changeListBasedRevisionsFactory);
    }

    public DifferenceType getDifferenceType(Object status) {
        if (status == null) {
            return DifferenceType.NOT_CHANGED;
        }
        AccuRevFile file = (AccuRevFile) status;
        if (file.getStatus() == AccuRevFileStatus.MISSING) {
            return DifferenceType.NOT_CHANGED;
        } else if (file.getStatus() == AccuRevFileStatus.EXTERNAL) {
            return DifferenceType.INSERTED;
        } else if (file.getStatus() == AccuRevFileStatus.DEFUNCT_KEPT_MEMBER) {
            return DifferenceType.DELETED;
        } else if (file.getStatus() == AccuRevFileStatus.BACKED ||
                file.getStatus() == AccuRevFileStatus.BACKED_STALE) {
            return DifferenceType.NOT_CHANGED;
        } else {
            return DifferenceType.MODIFIED;
        }
    }

    public List getDirectories() {
        return super.getDirectories();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public AbstractRevisions createChild(TreeElement child) {
        return null;
    }
}
