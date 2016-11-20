package se.ju.taun15a16.group5.mjilkmjecipes.backend.rest;

/**
 * Created by kevin on 20.11.2016.
 */

public class HTTP401Exception extends Exception {

    public HTTP401Exception() {
        super();
    }

    public HTTP401Exception(String message) {
        super(message);
    }

    public HTTP401Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public HTTP401Exception(Throwable cause) {
        super(cause);
    }
}
