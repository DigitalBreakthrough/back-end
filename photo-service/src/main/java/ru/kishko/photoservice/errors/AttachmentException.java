package ru.kishko.photoservice.errors;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class AttachmentException {

    private final String message;

    private final HttpStatus httpStatus;

}
