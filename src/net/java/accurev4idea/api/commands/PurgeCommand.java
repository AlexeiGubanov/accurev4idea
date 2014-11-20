/*
 * PurgeCommand.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jul 10, 2005, 2:28:00 PM
 */
package net.java.accurev4idea.api.commands;

import net.java.accurev4idea.api.exec.Argument;
import net.java.accurev4idea.api.exec.Command;
import net.java.accurev4idea.api.AccuRevArguments;

import java.util.List;
import java.util.LinkedList;
import java.io.File;


/**
 * This {@link Command} implementation corresponds to <tt>accurev purge 'filepath'</tt>
 * call.
 *
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version $Id: PurgeCommand.java,v 1.1 2005/11/05 16:56:06 ifedulov Exp $
 * @since 0.1
 */
public class PurgeCommand extends BaseAccurevCommand implements Command {
    private final List args = new LinkedList() {{
        add(AccuRevArguments.PURGE);
    }};

    public PurgeCommand(File file) {
        args.add(new Argument(file.getName()));
    }

    public Argument[] getArguments() {
        return (Argument[])args.toArray(new Argument[args.size()]);
    }
}
