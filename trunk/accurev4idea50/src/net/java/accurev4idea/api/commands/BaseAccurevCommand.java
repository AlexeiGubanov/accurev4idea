package net.java.accurev4idea.api.commands;

import net.java.accurev4idea.api.Newline;
import net.java.accurev4idea.api.AccuRevExecutable;
import net.java.accurev4idea.api.components.Locking;
import net.java.accurev4idea.api.components.AccuRevFileType;

/**
 * <p>
 * This class is a base class for Accurev commands that provides
 * standard functionality.
 * </p>
 * 
 * @author Brian Pontarelli
 * @author Igor Fedulov
 */
public class BaseAccurevCommand {
    /**
     * Returns full path to the "accurev" executable platform dependent, i.e. on
     * Windows platform this method will return full path to the "accurevw.exe", when on
     * any other platform expect to receive just "accurev"
     *
     * @return full path to the accurev executable platform dependent
     */
    public String getCommand() {
        return AccuRevExecutable.getAbsolutePath();
    }

    /**
     * Given a Newline instance, this determines the Accurev specific option that corresponds to the
     * given Newline.
     *
     * @param   newline The newline to determine the option for.
     * @return  The option and never null.
     * @throws  IllegalArgumentException If the Newline is {@link Newline#MAC} because Accurev
     *          does not support Mac newlines.
     */
    protected String determineOption(Newline newline) {
        if(Newline.MAC == newline) {
            throw new IllegalArgumentException("Mac line separator not supported by accurev");
        }

        return newline.getName();
    }

    /**
     * Given a Locking instance, this determines the Accurev specific option that corresponds to the
     * given Locking type.
     *
     * @param   locking The locking to determine the option for.
     * @return  The option and never null.
     */
    protected String determineOption(Locking locking) {
        return locking.getName();
    }

    /**
     * Given an {@link AccuRevFileType} instance, this determines the Accurev specific option that corresponds to
     * the given ElementType.
     *
     * @param   type The element type.
     * @return  The option and never null.
     */
    protected String determineOption(AccuRevFileType type) {
        return type.getName();
    }
}