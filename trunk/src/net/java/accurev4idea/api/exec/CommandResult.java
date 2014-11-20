package net.java.accurev4idea.api.exec;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * <p/>
 * This class encapsulates the result from a command execution.
 * </p>
 *
 * @author Brian Pontarelli
 * @author Igor Fedulov
 */
public class CommandResult {
    /**
     * Null-safe instance, should be returned everywhere command result instance is needed instead of null
     */
    public static final CommandResult EMPTY_COMMAND_RESULT = new CommandResult(new String[]{StringUtils.EMPTY},
                                                                               Integer.MAX_VALUE, StringUtils.EMPTY,
                                                                               StringUtils.EMPTY);
    /**
     * Command and arguments as were passed to the {@link Runtime#exec(String[], String[], java.io.File)}
     */
    private String[] command;
    /**
     * Exit code of the command
     */
    private int code;
    /**
     * Out stream copy
     */
    private String out;
    /**
     * Error stream copy
     */
    private String err;

    /**
     * Construct command result instance with given arguments
     *
     * @param command
     * @param code
     * @param out
     * @param err
     */
    public CommandResult(String[] command, int code, String out, String err) {
        this.command = command;
        this.code = code;
        this.out = out;
        this.err = err;
    }

    /**
     * Obtain the command and arguments as were passed to {@link Runtime#exec(String[], String[], java.io.File)}
     *
     * @return command and arguments
     */
    public String[] getCommand() {
        return command;
    }

    public int getCode() {
        return code;
    }

    public String getOut() {
        return out;
    }

    public String getErr() {
        return err;
    }

    public boolean isSuccess() {
        return code == 0;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}