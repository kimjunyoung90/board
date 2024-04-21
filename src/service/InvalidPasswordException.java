package service;

public class InvalidPasswordException extends ServiceException {
    public InvalidPasswordException(String message) {
        super(message);
    }
}
