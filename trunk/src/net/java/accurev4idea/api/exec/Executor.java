package net.java.accurev4idea.api.exec;

import net.java.accurev4idea.api.exceptions.ExecutionException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * <p/>
 * This class executes CLI programs using the Java runtime
 * fork exec model.
 * </p>
 *
 * @author Brian Pontarelli
 * @author Igor Fedulov
 */
public class Executor {
    /**
     * Log4j audit channel
     */
    private static final Logger log = Logger.getLogger(Executor.class);

    /**
     * A very naive command executor. This uses the Java Runtime class to spawn off a new process
     * to perform a command. This waits for the called command to exit before returning and bundles
     * the results in a {@link CommandResult}.
     *
     * @param command The {@link Command} to execute.
     * @param dir     The working directory to perform the execution from.
     * @return A CommandResult and never null.
     * @throws ExecutionException If the command could not be executed.
     */
    public CommandResult execute(Command command, File dir) throws ExecutionException {
        Runtime rt = Runtime.getRuntime();
        List args = new ArrayList();
        args.add(command.getCommand());

        Argument[] cmdArgs = command.getArguments();
        if (cmdArgs != null) {
            for (int i = 0; i < cmdArgs.length; i++) {
                Argument cmdArg = cmdArgs[i];
                if (cmdArg != null) {
                    args.add(cmdArg.getName());
                    if (cmdArg.getValue() != null) {
                        args.add(cmdArg.getValue());
                    }
                }
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("Executing command [" + args.toString() + "]");
        }

        // declare both stdin and stderr readers outside of try/catch to dispose/close
        // in finally
        Reader inReader = null;
        Reader errReader = null;
        try {
            String[] strArgs = (String[]) args.toArray(new String[args.size()]);
            Process p = rt.exec(strArgs, null, dir);

            StringBuffer out = new StringBuffer();
            inReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            char[] cb = new char[1024];
            int read;
            do {
                read = inReader.read(cb);
                if (read > 0) {
                    out.append(cb, 0, read);
                }
            } while (read != -1);

            StringBuffer err = new StringBuffer();
            errReader = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            do {
                read = errReader.read(cb);
                if (read > 0) {
                    err.append(cb, 0, read);
                }
            } while (read != -1);

            // NOTE: The "p.waitFor()" should be invoked AFTER the error and out streams
            // are read, "...because some native platforms only provide
            // limited buffer size for standard input and output streams, failure
            // to promptly write the input stream or read the output stream of
            // the subprocess may cause the subprocess to block, and even deadlock."
            int code = p.waitFor();

            return new CommandResult(strArgs, code, StringUtils.trim(out.toString()), StringUtils.trim(err.toString().trim()));
        } catch (Throwable t) {
            final String message = "Unable to execute command [" + command.toString() + "]";
            // log as error and rethrow as execution exception for visibility
            log.error(message, t);
            throw new ExecutionException(message, t);
        } finally {
            // clean up readers for both stdIn and stdErr streams
            if(inReader != null) {
                try {
                    inReader.close();
                } catch (IOException e) {
                    // ignore, as it's irrelevant
                }
            }
            if(errReader != null) {
                try {
                    errReader.close();
                } catch (IOException e) {
                    // ignore as it's irrelevant
                }
            }
        }
    }
}