package exceptions;

public class ClienteException extends Exception {
    private Exception _exception;

    public ClienteException(Exception e) {
        this._exception = e;
    } 

    public String toString() {
        return this._exception.toString();
    }

}
