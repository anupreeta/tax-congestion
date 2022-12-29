package congestion.calculator.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends Exception{

    private String message;
    private HttpStatus httpStatus;

    public CustomException(String message) {
        this.message = message;
    }

    public CustomException(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
