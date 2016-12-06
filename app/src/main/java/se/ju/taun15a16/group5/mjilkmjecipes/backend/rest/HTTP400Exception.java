package se.ju.taun15a16.group5.mjilkmjecipes.backend.rest;

/**
 * Created by kevin on 20.11.2016.
 */

public class HTTP400Exception extends Exception {

    private RESTErrorCodes[] errorCodes;

    public HTTP400Exception() {
        super();
    }

    public HTTP400Exception(String message, RESTErrorCodes errorCode) {
        super(message);
        this.errorCodes = new RESTErrorCodes[]{errorCode};
    }

    public HTTP400Exception(String message, Throwable cause, RESTErrorCodes errorCode) {
        super(message, cause);
        this.errorCodes = new RESTErrorCodes[]{errorCode};
    }

    public HTTP400Exception(Throwable cause, RESTErrorCodes errorCode) {
        super(cause);
        this.errorCodes = new RESTErrorCodes[]{errorCode};
    }

    public HTTP400Exception(String message, RESTErrorCodes[] errorCodes) {
        super(message);
        this.errorCodes = errorCodes;
    }

    public HTTP400Exception(String message, Throwable cause, RESTErrorCodes[] errorCodes) {
        super(message, cause);
        this.errorCodes = errorCodes;
    }

    public HTTP400Exception(Throwable cause, RESTErrorCodes[] errorCodes) {
        super(cause);
        this.errorCodes = errorCodes;
    }

    public RESTErrorCodes getFirstErrorCode() {
        if(isSingleErrorCode()){
            return errorCodes[0];
        }
        return null;
    }

    public RESTErrorCodes[] getErrorCodes() {
        return errorCodes;
    }

    public boolean isSingleErrorCode(){
        return errorCodes.length == 1 ? true : false;
    }

    public String errorCodesToString(){
        String code = "";
        for(RESTErrorCodes c : errorCodes){
            code += c.getDescription() + " ";
        }
        return code;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
