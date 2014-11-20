package net.java.accurev4idea.api;

import org.apache.commons.lang.enums.Enum;

/**
 * Enumeration of known transaction types, such as "add", "keep", "promote" etc.
 *
 * @see org.apache.commons.lang.enums.EnumUtils
 * @author Igor Fedulov <a href="mailto:igor@fedulov.com>igor@fedulov.com</a>
 * @since   1.0
 * @version $Id: EnumUtils.java,v 1.1 2005/11/05 16:56:15 ifedulov Exp $
 */
public abstract class EnumUtils extends org.apache.commons.lang.enums.EnumUtils {
    /**
     * Method to obtain the "typed" enumeration element from the underlying
     * {@link #getEnum(Class, String)} implementation. It's here to accomplish two tasks:
     * 1. Remove casts on enumeration retrieval
     * 2. Force loading of the enumeration class before looking it up
     *
     * @param clazz a class to lookup enumeration element for
     * @param name the string name of the enumeration element
     * @return enumeration element for given class and element name, null if not found.
     */
    public static Enum getTypedEnum(Class clazz, String name) {
        // force loading the class first, otherwise the EnumUtils might not find it
        // as it wasn't loaded (i.e. nobody used any of the fields prior to looking one up by name
        try {
            Class.forName(clazz.getName());
        } catch (ClassNotFoundException e) {
        }
        return getEnum(clazz,name);
    }
}
