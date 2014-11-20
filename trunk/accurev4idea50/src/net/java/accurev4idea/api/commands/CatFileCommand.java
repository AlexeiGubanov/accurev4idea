/*
 * CatFileCommand.java
 * Copyright (c) 2005, Igor Fedulov. All Rights Reserved.
 * Created on Jun 11, 2005, 4:57:45 PM
 */
package net.java.accurev4idea.api.commands;


import net.java.accurev4idea.api.exec.Argument;
import net.java.accurev4idea.api.exec.Command;
import net.java.accurev4idea.api.AccuRevArguments;
import net.java.accurev4idea.api.components.AccuRevVersion;
import net.java.accurev4idea.api.components.AccuRevFile;

import java.io.File;
import java.util.List;
import java.util.LinkedList;

/**
 * The cat command extracts contents of the file from accurev server by given file name
 *
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @since   1.0
 * @version 1.0
 */
public class CatFileCommand extends BaseAccurevCommand implements Command {
    /**
     * List of Cat command arguments populated in constructor
     */
    private final List args = new LinkedList();

    /**
     * Construct CatFileCommand with given {@link AccuRevFile} argument. It's assumed that
     * given file is not a directory.
     *
     * @param file
     */
    public CatFileCommand(AccuRevFile file) {
        args.add(AccuRevArguments.CAT);
        args.add(new Argument("-e", String.valueOf(file.getFileId())));
    }

    /**
     * Construct CatFileCommand with given {@link AccuRevFile} argument and given {@link AccuRevVersion}. This
     * constructor will execute following accurev command:
     * <p>
     * <pre>accurev cat -v {@link AccuRevVersion#getReal()} -e {@link AccuRevFile#getFileId()}</pre>
     * </p>
     *
     * @param file
     * @param version
     */
    public CatFileCommand(AccuRevFile file, AccuRevVersion version) {
        this(file);
        args.add(new Argument("-v", version.getReal().getVersionString()));
    }

    /**
     * Returns the Accurev arguments for "cat" command
     *
     * @return  The args.
     */
    public Argument[] getArguments() {
        return (Argument[]) args.toArray(new Argument[args.size()]);
    }
}
