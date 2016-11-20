package se.ju.taun15a16.group5.mjilkmjecipes.backend.rest;

/**
 * Created by kevin on 20.11.2016.
 */

public class HTTP400Exception extends Exception {

    public HTTP400Exception() {
        super();
    }

    public HTTP400Exception(String message) {
        super(message);
    }

    public HTTP400Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public HTTP400Exception(Throwable cause) {
        super(cause);
    }
}
