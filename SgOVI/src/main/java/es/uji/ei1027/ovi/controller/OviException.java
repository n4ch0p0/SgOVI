package es.uji.ei1027.ovi.controller;

public class OviException extends RuntimeException {

    String errorName;
    String message;

    public OviException(String errorName, String message) {
        this.errorName = errorName;
        this.message = message;
    }

    public String getErrorName() {
        return errorName;
    }

    public void setErrorName(String errorName) {
        this.errorName = errorName;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
