package co.empathy.academy.search.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ExcepcionController extends ResponseEntityExceptionHandler {

    /**
     * Method that throws a ResponseEntity with the parameter message and a 404 not found status
     *
     * @param exception that throws this ResponseEntity
     * @return ResponseEntity with the message and the not found status
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Method that throws a ResponseEntity with the parameter message and a 409 conflict status
     *
     * @param exception that throws this ResponseEntity
     * @return ResponseEntity with the message and the conflict status
     */
    @ExceptionHandler(RepeatedUserException.class)
    public ResponseEntity<String> handleRepeatedUserException(RepeatedUserException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }
}
