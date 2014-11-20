/*
 * ListDepotsCommand.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on May 27, 2005
 */
package net.java.accurev4idea.api.commands;

import net.java.accurev4idea.api.AccuRevArguments;
import net.java.accurev4idea.api.exec.Argument;
import net.java.accurev4idea.api.exec.Command;

/**
 * This {@link Command} implementation allows for listing of depots on AccuRev server.
 *
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @since   1.0
 * @version 1.0
 */
public class ListDepotsCommand extends BaseAccurevCommand implements Command {
    /**
     * Initialized once because this set of arguments never change for this command;
     * contains 3 arguments "show", "-fx" and "depots"
     */
    private static final Argument[] args = new Argument[] {
        AccuRevArguments.SHOW,
        AccuRevArguments.FX,
        AccuRevArguments.DEPOTS
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