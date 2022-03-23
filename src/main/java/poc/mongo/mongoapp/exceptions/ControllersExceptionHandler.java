package poc.mongo.mongoapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ControllerAdvice
public class ControllersExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public Issue constraintViolationExceptionHandler(final ConstraintViolationException constraintViolationException) {

        final List<String> errors = new ArrayList<>();

        constraintViolationException.getConstraintViolations().forEach(constraintViolation ->
                errors.add(constraintViolation.getMessage())
        );

        return new Issue(HttpStatus.BAD_REQUEST.value(), errors);

    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public Issue badRequestExceptionHandler(final BadRequestException badRequestException) {

        return new Issue(HttpStatus.BAD_REQUEST.value(), Collections.singletonList(badRequestException.getExceptionMessage()));
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingRequestHeaderException.class)
    public Issue missingRequestHeaderExceptionHandler(final MissingRequestHeaderException missingRequestHeaderException) {

        return new Issue(HttpStatus.BAD_REQUEST.value(), Collections.singletonList(missingRequestHeaderException.getMessage()));
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AlreadyExistentException.class)
    public Issue alreadyExistentExceptionHandler(final AlreadyExistentException alreadyExistentException) {

        return new Issue(HttpStatus.BAD_REQUEST.value(), Collections.singletonList(alreadyExistentException.getExceptionMessage()));
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public NotFoundResponse notFoundExceptionHandler(final NotFoundException notFoundException) {

        return new NotFoundResponse(notFoundException.getExceptionMessage());
    }


}
