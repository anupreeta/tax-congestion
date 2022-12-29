package congestion.calculator.controller;

import congestion.calculator.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class TaxControllerAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public final ResponseEntity<Object> handleCustomException(CustomException e) {
        HttpStatus httpStatus = e.getHttpStatus();
        if (null == e.getHttpStatus()) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        List<String> details = new ArrayList<>();
        details.add(e.getMessage());
        return new ResponseEntity<>(details, httpStatus);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleException(Exception e) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        List<String> details = new ArrayList<>();
        details.add(e.getMessage());
        return new ResponseEntity<>(details, httpStatus);
    }
}
