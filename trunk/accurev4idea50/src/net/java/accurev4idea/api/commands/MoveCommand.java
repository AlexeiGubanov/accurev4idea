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
 * This {@link Command} implementation corresponds to <tt>accurev move file1 file2</tt>
 * call.
 *
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version $Id: MoveCommand.java,v 1.2 2006/02/08 22:40:28 ifedulov Exp $
 * @since 0.1
 */
public class MoveCommand extends BaseAccurevCommand implements Command {
    private final List args = new LinkedList() {{
        add(AccuRevArguments.MOVE);
    }};

    public MoveCommand(File source, File destination) {
        args.add(new Argument(source.getAbsolutePath()));
        args.add(new Argument(destination.getAbsolutePath()));
    }

    public Argument[] getArguments() {
        return (Argument[])args.toArray(new Argument[args.size()]);
    }
}
