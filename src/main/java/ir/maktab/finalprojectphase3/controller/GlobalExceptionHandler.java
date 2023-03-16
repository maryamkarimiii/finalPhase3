package ir.maktab.finalprojectphase3.controller;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import ir.maktab.finalprojectphase3.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.net.BindException;
import java.sql.SQLException;
import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> notFoundExceptionHandler(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> illegalArgumentExceptionHandler(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> noSuchElementExceptionHandler(NoSuchElementException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<String> bindExceptionHandler(BindException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(DuplicationException.class)
    public ResponseEntity<String> duplicationExceptionHandler(DuplicationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(FileException.class)
    public ResponseEntity<String> fileExceptionHandler(FileException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(NotAllowedToAccess.class)
    public ResponseEntity<String> notAllowedToAccessHandler(NotAllowedToAccess e) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(e.getMessage());
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<String> sQLExceptionHandler(SQLException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(UnexpectedRollbackException.class)
    ResponseEntity<String> unexpectedRollbackExceptionHandler(UnexpectedRollbackException e) {
        return ResponseEntity.internalServerError().body(e.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    ResponseEntity<String> nullPointerExceptionHandler(NullPointerException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    // input not mapping to target definition
    @ExceptionHandler(MismatchedInputException.class)
    ResponseEntity<String> mismatchedInputExceptionHandler(MismatchedInputException e) {
        return ResponseEntity.internalServerError().body(e.getMessage());
    }

    // wrong mapping HTTP request, for example method is GET but request is POST
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    ResponseEntity<String> httpRequestMethodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    // if media is not json, for example it is xml or text
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    ResponseEntity<String> httpMediaTypeNotSupportedExceptionHandler(HttpMediaTypeNotSupportedException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<String> handleException(HttpMessageNotReadableException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    ResponseEntity<String> handleException(NoHandlerFoundException e) {
        return ResponseEntity.internalServerError().body(e.getMessage());
    }

    // miss RequestAttribute
    @ExceptionHandler(ServletRequestBindingException.class)
    ResponseEntity<String> handleException(ServletRequestBindingException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(NotCorrectInputException.class)
    public ResponseEntity<String> notCorrectInputExceptionHandler(NotCorrectInputException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> validationExceptionHandler(ValidationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

}
