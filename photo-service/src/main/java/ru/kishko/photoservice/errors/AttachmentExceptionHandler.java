package ru.kishko.photoservice.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AttachmentExceptionHandler {

    @ExceptionHandler(value = {AttachmentNotFoundException.class})
    public ResponseEntity<Object> handleUserNotFoundException(AttachmentNotFoundException attachmentNotFoundException) {

        AttachmentException attachmentException = new AttachmentException(
                attachmentNotFoundException.getMessage(),
                HttpStatus.NOT_FOUND
        );

        return new ResponseEntity<>(attachmentException, attachmentException.getHttpStatus());

    }

}


