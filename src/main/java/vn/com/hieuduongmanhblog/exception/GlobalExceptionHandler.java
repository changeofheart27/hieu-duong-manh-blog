package vn.com.hieuduongmanhblog.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import vn.com.hieuduongmanhblog.dto.ResponseDTO;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseDTO handleResourceNotFoundException(ResourceNotFoundException exception) {
        return new ResponseDTO(HttpStatus.NOT_FOUND, exception.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseDTO handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
        return new ResponseDTO(HttpStatus.BAD_REQUEST, exception.getMessage(), LocalDateTime.now());
    }

    // exception handler for Bean Validation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseDTO handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        return new ResponseDTO(HttpStatus.BAD_REQUEST, exception.getFieldError().getDefaultMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseDTO handleUsernameNotFoundException(UsernameNotFoundException exception) {
        return new ResponseDTO(HttpStatus.UNAUTHORIZED, exception.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseDTO handleBadCredentialsException(BadCredentialsException exception) {
        return new ResponseDTO(HttpStatus.UNAUTHORIZED, exception.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseDTO handleSignatureException(SignatureException exception) {
        return new ResponseDTO(HttpStatus.UNAUTHORIZED, exception.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseDTO handleMalformedJwtExceptionException(MalformedJwtException exception) {
        return new ResponseDTO(HttpStatus.UNAUTHORIZED, exception.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseDTO handleExpiredJwtException(ExpiredJwtException exception) {
        return new ResponseDTO(HttpStatus.UNAUTHORIZED, exception.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseDTO handleAccessDeniedException(AccessDeniedException exception) {
        return new ResponseDTO(HttpStatus.FORBIDDEN, exception.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(Exception.class)
    public ResponseDTO handleInternalServerErrorException(Exception exception) {
        return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), LocalDateTime.now());
    }
}
