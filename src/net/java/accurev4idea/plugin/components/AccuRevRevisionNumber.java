/**
 * AccuRevRevisionNumber.java
 * Copyright (c) 2005-2006, Igor Fedulov. All Rights Reserved.
 * Created on 
 */
package net.java.accurev4idea.plugin.components;

import com.intellij.openapi.vcs.history.VcsRevisionNumber;
import net.java.accurev4idea.api.components.AccuRevVersion;

/**
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version $Id: AccuRevRevisionNumber.java,v 1.1 2005/11/07 04:20:02 ifedulov Exp $
 * @since 0.1.0
 */
public class AccuRevRevisionNumber implements VcsRevisionNumber {
    private AccuRevVersion version;

    public AccuRevRevisionNumber(AccuRevVersion version) {
        this.version = version;
    }

    public String asString() {
        return version.getReal().getVersionString();
    }

    public int compareTo(final VcsRevisionNumber o) {
        if (o instanceof AccuRevRevisionNumber) {
            return (int) (version.getReal().getVersionId() - ((AccuRevRevisionNumber) o).version.getReal().getVersionId());
        }
        return 0;
    }

    public final AccuRevVersion getVersion() {
        return version;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final AccuRevRevisionNumber that = (AccuRevRevisionNumber) o;

        if (version != null ? !version.equals(that.version) : that.version != null) return false;

        return true;
    }

    public int hashCode() {
        return (version != null ? version.hashCode() : 0);
    }

}
