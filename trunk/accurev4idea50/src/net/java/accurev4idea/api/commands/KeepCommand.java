/*
 * PurgeCommand.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jul 10, 2005, 2:28:00 PM
 */
package net.java.accurev4idea.api.commands;

import net.java.accurev4idea.api.exec.Argument;
import net.java.accurev4idea.api.exec.Command;
import net.java.accurev4idea.api.AccuRevArguments;
import net.java.accurev4idea.api.components.Stream;

import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;
import java.io.File;


/**
 * This {@link net.java.accurev4idea.api.exec.Command} implementation corresponds to <tt>accurev pop -O 'filepath'</tt>
 * call.
 *
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version $Id: KeepCommand.java,v 1.1 2005/11/10 22:22:59 ifedulov Exp $
 * @since 0.1
 */
public class KeepCommand extends BaseAccurevCommand implements Command {
    private final List args = new LinkedList() {{
        add(AccuRevArguments.KEEP);
    }};

    public KeepCommand(String comment, String fileLocationRelativeToWorkspaceRoot) {
    }

    public KeepCommand(String comment, List fileLocationsRelativeToWorkspaceRoot) {
        args.add(new Argument("-c", comment));
        for (Iterator i = fileLocationsRelativeToWorkspaceRoot.iterator(); i.hasNext();) {
            String fileLocation = (String) i.next();
            args.add(new Argument(fileLocation));
        }
    }

    public Argument[] getArguments() {
        return (Argument[])args.toArray(new Argument[args.size()]);
    }
}
