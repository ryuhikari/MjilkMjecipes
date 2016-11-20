package se.ju.taun15a16.group5.mjilkmjecipes.backend.rest;

/**
 * Created by kevin on 20.11.2016.
 */

public class HTTP404Exception extends Exception {

    public HTTP404Exception() {
        super();
    }

    public HTTP404Exception(String message) {
        super(message);
    }

    public HTTP404Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public HTTP404Exception(Throwable cause) {
        super(cause);
    }
}
