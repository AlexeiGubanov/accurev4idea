/*
 * Copyright (c) 2003-2005, Inversoft, All Rights Reserved
 *
 * This software is distribuable under the GNU Lesser General Public License.
 * For more information visit gnu.org.
 */
package net.java.accurev4idea.api.exec;

/**
 * <p>
 * This class defines a command line argument.
 * </p>
 * 
 * @author  Brian Pontarelli
 */
public class Argument {
    private String name;
    private String value;

    /**
     * Constructs a new <code>Argument</code> that is a single value argument, meaning that the
     * argument doesn't take additional information.  For example:
     *
     * <pre>
     * ps -e -f -w -w
     * </pre>
     *
     * <p>
     * Here the arguments -e -f -w -w are all single arguments.
     * </p>
     *
     * @param   name The name of the argument (which is also the value of the argument).
     *
     */
    public Argument(String name) {
        this.name = name;
    }

    /**
     * Constructs a new <code>Argument</code> that is a valued argument, meaning that the argument
     * takes one additional parameter that is the value of the argument.  For example:
     *
     * <pre>
     * java -cp /usr/lib/my_jar.jar
     * </pre>
     *
     * <p>
     * Here the argument -cp takes an additional value of <code>/usr/lib/my_jar.jar</code>.
     * </p>
     *
     * @param   name The name of the argument.
     * @param   value The value of the argument.
     *
     */
    public Argument(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}