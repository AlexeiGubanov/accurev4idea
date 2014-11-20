/*
* Copyright (c) 2003-2005, Inversoft, All Rights Reserved
*
* This software is distribuable under the GNU Lesser General Public License.
* For more information visit gnu.org.
*/
package net.java.accurev4idea.api.commands;

import net.java.accurev4idea.api.exec.Argument;
import net.java.accurev4idea.api.exec.Command;
import net.java.accurev4idea.api.AccuRevArguments;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * This class is a command for Accurev that updates a workspace or reference.
 * accurev update -fx -P -G -r &lt;workspace name&gt;
 * </p>
 * 
 * @author  Brian Pontarelli
 */
public class UpdateCommand extends BaseAccurevCommand implements Command {
    /**
     * List of arguments, defaults to "update -fx"
     */
    private final List args = new LinkedList() {{
        add(AccuRevArguments.UPDATE);
    }};

    /**
     * Constructs a <code>UpdateCommand</code> that when executed will update an Accurev workspace
     * or reference.
     */
    public UpdateCommand() {
        this(false, null, null);
    }

    /**
     * Constructs a <code>UpdateCommand</code> that when executed will update an Accurev workspace or
     * reference.
     *
     * @param   dryRun If true, this update will only produce output and not modify any files.
     * @param   reference (Optional) The name of the reference to be updated rather than a workspace
     * @param   transaction (Optional) The transaction number on the backing stream to update the
     *          workspace or reference to.
     */
    public UpdateCommand(boolean dryRun, String reference, String transaction) {
        if (dryRun) {
            args.add(new Argument("-i"));
        }

        if (reference != null) {
            args.add(new Argument("-r", reference));
        }

        if (transaction != null) {
            args.add(new Argument("-t", transaction));
        }
    }

    /**
     * Returns the Accurev arguments.
     *
     * @return  The args.
     */
    public Argument[] getArguments() {
        return (Argument[]) args.toArray(new Argument[args.size()]);
    }
}