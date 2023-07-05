package org.example.gc.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ExceptionResponse {
    private Integer httpStatus;
    private String errorCode;
    private String errorMessage;
}
