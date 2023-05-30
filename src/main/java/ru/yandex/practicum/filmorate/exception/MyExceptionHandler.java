package ru.yandex.practicum.filmorate.exception;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class MyExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Map<String, List<String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
        log.error(getErrorsMap(errors).toString());
        return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.valueOf(500));
    }

    @ExceptionHandler({ValidationException.class})
    public ResponseEntity<Map<String, List<String>>> handleValidationErrors(ValidationException ex) {
        log.error(ex.getMessage());
        List<String> err = new ArrayList<>();
        err.add(ex.getMessage());
        return new ResponseEntity<>(getErrorsMap(err), new HttpHeaders(), HttpStatus.valueOf(500));
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<Map<String, List<String>>> handleValidationErrors(NotFoundException ex) {
        log.error(ex.getMessage());
        List<String> err = new ArrayList<>();
        err.add(ex.getMessage());
        return new ResponseEntity<>(getErrorsMap(err), new HttpHeaders(), HttpStatus.valueOf(404));
    }

    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("Validation errors", errors);
        return errorResponse;
    }

}