/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright by The HDF Group.                                               *
 * All rights reserved.                                                      *
 *                                                                           *
 * This file is part of HDF5.  The full HDF5 copyright notice, including     *
 * terms governing use, modification, and redistribution, is contained in    *
 * the COPYING file, which can be found at the root of the source code       *
 * distribution tree, or in https://www.hdfgroup.org/licenses.               *
 * If you do not have access to either file, you may request a copy from     *
 * help@hdfgroup.org.                                                        *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package org.hdfgroup.javahdf5.exceptions;

/**
 * @page ERRORS Errors and Exceptions
 * The class HDF5Exception returns errors from the Java HDF5 Interface.
 *
 * Two sub-classes of HDF5Exception are defined:
 * <ol>
 * <li>
 * HDF5LibraryException -- errors raised by the HDF5 library code
 * <li>
 * HDF5JavaException -- errors raised by the HDF5 Java wrapper code
 * </ol>
 *
 * These exceptions are sub-classed to represent specific error conditions, as
 * needed. In particular, HDF5LibraryException has a sub-class for each major
 * error code returned by the HDF5 library.
 *
 * @defgroup JERR HDF5 Library Exception Interface
 *
 */
public class HDF5Exception extends RuntimeException {
    /**
     *  the specified detail message of this exception
     */
    protected String detailMessage;

    /**
     * @ingroup JERR
     *
     * Constructs an <code>HDF5Exception</code> with no specified detail
     * message.
     */
    public HDF5Exception() { super(); }

    /**
     * @ingroup JERR
     *
     * Constructs an <code>HDF5Exception</code> with the specified detail
     * message.
     *
     * @param message
     *            the detail message.
     */
    public HDF5Exception(String message)
    {
        super();
        detailMessage = message;
    }

    /**
     * @ingroup JERR
     *
     * Returns the detail message of this exception
     *
     * @return the detail message or <code>null</code> if this object does not
     *         have a detail message.
     */
    @Override
    public String getMessage()
    {
        return detailMessage;
    }

    /*
     *  defineHDF5LibraryException()  returns the name of the sub-class
     *  which goes with an HDF5 error code.
     */
    public String defineHDF5LibraryException(long maj_num)
    {
        if (H5E_ARGS() == maj_num)
            return "org/hdfgroup/javahdf5/exceptions/HDF5FunctionArgumentException";
        else if (H5E_RESOURCE() == maj_num)
            return "org/hdfgroup/javahdf5/exceptions/HDF5ResourceUnavailableException";
        else if (H5E_INTERNAL() == maj_num)
            return "org/hdfgroup/javahdf5/exceptions/HDF5InternalErrorException";
        else if (H5E_FILE() == maj_num)
            return "org/hdfgroup/javahdf5/exceptions/HDF5FileInterfaceException";
        else if (H5E_IO() == maj_num)
            return "org/hdfgroup/javahdf5/exceptions/HDF5LowLevelIOException";
        else if (H5E_FUNC() == maj_num)
            return "org/hdfgroup/javahdf5/exceptions/HDF5FunctionEntryExitException";
        else if (H5E_ID() == maj_num)
            return "org/hdfgroup/javahdf5/exceptions/HDF5IdException";
        else if (H5E_CACHE() == maj_num)
            return "org/hdfgroup/javahdf5/exceptions/HDF5MetaDataCacheException";
        else if (H5E_BTREE() == maj_num)
            return "org/hdfgroup/javahdf5/exceptions/HDF5BtreeException";
        else if (H5E_SYM() == maj_num)
            return "org/hdfgroup/javahdf5/exceptions/HDF5SymbolTableException";
        else if (H5E_HEAP() == maj_num)
            return "org/hdfgroup/javahdf5/exceptions/HDF5HeapException";
        else if (H5E_OHDR() == maj_num)
            return "org/hdfgroup/javahdf5/exceptions/HDF5ObjectHeaderException";
        else if (H5E_DATATYPE() == maj_num)
            return "org/hdfgroup/javahdf5/exceptions/HDF5DatatypeInterfaceException";
        else if (H5E_DATASPACE() == maj_num)
            return "org/hdfgroup/javahdf5/exceptions/HDF5DataspaceInterfaceException";
        else if (H5E_DATASET() == maj_num)
            return "org/hdfgroup/javahdf5/exceptions/HDF5DatasetInterfaceException";
        else if (H5E_STORAGE() == maj_num)
            return "org/hdfgroup/javahdf5/exceptions/HDF5DataStorageException";
        else if (H5E_PLIST() == maj_num)
            return "org/hdfgroup/javahdf5/exceptions/HDF5PropertyListInterfaceException";
        else if (H5E_ATTR() == maj_num)
            return "org/hdfgroup/javahdf5/exceptions/HDF5AttributeException";
        else if (H5E_PLINE() == maj_num)
            return "org/hdfgroup/javahdf5/exceptions/HDF5DataFiltersException";
        else if (H5E_EFL() == maj_num)
            return "org/hdfgroup/javahdf5/exceptions/HDF5ExternalFileListException";
        else if (H5E_REFERENCE() == maj_num)
            return "org/hdfgroup/javahdf5/exceptions/HDF5ReferenceException";

        return "org/hdfgroup/javahdf5/exceptions/HDF5LibraryException";
    } /* end  defineHDF5LibraryException() */

    public int checkException(int status)
    {
        long stk_id             = H5I_INVALID_HID();
        long[] exceptionNumbers = {0, 0};

        try (Arena arena = Arena.openConfined()) {
            MemorySegment exceptionNumbersSegment = arena.ofArray(exceptionNumbers);
            /* Save current stack contents for future use */
            if ((stk_id = H5Eget_current_stack()) >= 0)
                MemorySegment walk_error_callback = H5E_walk2_t.allocate(this::walk_error_callback, arena);
            /* This will clear current stack */
            if (H5Ewalk2(stk_id, H5E_WALK_DOWNWARD(), walk_error_callback, exceptionNumbersSegment) < 0)
                return status;
            exceptionNumbers = exceptionNumbersSegment.toArray();
        }
        /*
         * No error detected in HDF5 error stack.
         */
        if (!exceptionNumbers[0] && !exceptionNumbers[1])
            return status;

        String exception = defineHDF5LibraryException(exceptionNumbers[0]);
        long msg_size    = 0;
        String msg_str   = null;

        /* get the length of the name */
        if ((msg_size = H5Eget_msg(exceptionNumbers[1], null, null, 0)) < 0)
            return status;

        /*        if (msg_size > 0) {
                    if (NULL == (msg_str = (char *)calloc((size_t)msg_size + 1, sizeof(char))))
                        H5_OUT_OF_MEMORY_ERROR(ENVONLY, "h5libraryerror: failed to allocate buffer for error
           message");

                    if ((msg_size = H5Eget_msg(min_num, &error_msg_type, msg_str, (size_t)msg_size + 1)) < 0)
                        goto done;
                    msg_str[msg_size] = '\0';

                    if (NULL == (str = ENVPTR->NewStringUTF(ENVONLY, msg_str)))
                        CHECK_JNI_EXCEPTION(ENVONLY, JNI_FALSE);
                }
                else
                    str = NULL;

                if (stk_id >= 0)
                    H5Eset_current_stack(stk_id);

                args[0] = (char *)str;
                args[1] = 0;

                THROWEXCEPTION(exception, args);
        */
        return status;
    }

    /* get the major and minor error numbers on the top of the error stack */
    public static int walk_error_callback(int n, MemorySegment err_desc, MemorySegment _err_nums)
    {
        H5E_num_t *err_nums = (H5E_num_t *)_err_nums;

        if (err_desc) {
            err_nums -> maj_num = H5E_error2_t.maj_num(err_desc);
            err_nums -> min_num = H5E_error2_t.min_num(err_desc);
        } /* end if */

        return 0;
    }
}
