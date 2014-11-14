package com.compomics.mascotdatfile.util.exception;

/**
 * Created by IntelliJ IDEA.
 * User: kennyhelsens
 * Date: Oct 12, 2010
 * Time: 2:41:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class MascotDatfileException extends RuntimeException {

    /**
     * This error is thrown upon breaking assumptions within MascotDatfile.
     * @param message - String detailing the error.
     */
    public MascotDatfileException(String message) {
        super(message);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
