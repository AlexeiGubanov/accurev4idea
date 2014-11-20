/*
 * ListWorkspacesCommand.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jun 23, 2005, 10:05:23 PM
 */
package net.java.accurev4idea.api.commands;

import net.java.accurev4idea.api.AccuRevArguments;
import net.java.accurev4idea.api.exec.Argument;
import net.java.accurev4idea.api.exec.Command;

/**
 * This {@link Command} implementation is equal to running <code>accurev show -fx wspaces</code>
 * from cli.
 *
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version 1.0
 * @since 1.0
 */
public class ListWorkspacesCommand extends BaseAccurevCommand implements Command {
    /**
     * Initialized once because this set of arguments never change for this command;
     * contains 3 arguments "show", "-fx" and "wspaces"
     */
    private static final Argument[] args = new Argument[] {
        AccuRevArguments.SHOW,
        AccuRevArguments.FX,
        AccuRevArguments.WSPACES
    };

    /**
     * Returns the Accurev arguments for show depots command
     *
     * @return  The args.
     */
    public Argument[] getArguments() {
        return args;
    }
}
