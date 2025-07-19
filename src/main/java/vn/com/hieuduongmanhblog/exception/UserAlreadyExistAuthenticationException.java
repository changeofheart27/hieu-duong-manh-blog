package vn.com.hieuduongmanhblog.exception;

public class UserAlreadyExistAuthenticationException extends RuntimeException {

    public UserAlreadyExistAuthenticationException(String msg) {
        super(msg);
    }

    public UserAlreadyExistAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserAlreadyExistAuthenticationException(Throwable cause) {
        super(cause);
    }
}
