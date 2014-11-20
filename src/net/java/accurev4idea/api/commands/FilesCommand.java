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
import java.io.File;


/**
 * This {@link net.java.accurev4idea.api.exec.Command} implementation corresponds to <tt>accurev pop -O 'filepath'</tt>
 * call.
 *
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @version $Id: FilesCommand.java,v 1.2 2005/11/08 18:39:47 ifedulov Exp $
 * @since 0.1
 */
public class FilesCommand extends BaseAccurevCommand implements Command {
    private final List args = new LinkedList() {{
        add(AccuRevArguments.FILES);
        add(AccuRevArguments.FX);
    }};

    public FilesCommand(String fileDir, Stream stream) {
        this(fileDir);
        args.add(new Argument("-s", stream.getName()));
    }

    public FilesCommand(String fileDir) {
        if (fileDir != null) {
            args.add(new Argument(fileDir));
        }
    }

    public Argument[] getArguments() {
        return (Argument[])args.toArray(new Argument[args.size()]);
    }
}
