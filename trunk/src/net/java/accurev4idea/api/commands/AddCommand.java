/*
 * CatFileCommand.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jun 11, 2005, 4:57:45 PM
 */
package net.java.accurev4idea.api.commands;


import net.java.accurev4idea.api.AccuRevArguments;
import net.java.accurev4idea.api.exec.Argument;
import net.java.accurev4idea.api.exec.Command;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * The add command adds a given file or directory to accurev repository
 *
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @since   1.0
 * @version 1.0
 */
public class AddCommand extends BaseAccurevCommand implements Command {
    private final List args = new LinkedList() {{
        add(AccuRevArguments.ADD);
    }};

    public AddCommand(File file) {
        args.add(new Argument(file.getAbsolutePath()));
    }

    public Argument[] getArguments() {
        return (Argument[])args.toArray(new Argument[args.size()]);
    }
}
