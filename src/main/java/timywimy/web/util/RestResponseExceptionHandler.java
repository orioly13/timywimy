package timywimy.web.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import timywimy.util.exception.ErrorCode;
import timywimy.util.exception.RestException;
import timywimy.web.dto.common.Response;

import java.time.format.DateTimeParseException;

@ControllerAdvice
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(RestRequestIdInterceptor.class);

    private Integer logWithId(Exception ex, WebRequest request) {
        Integer id = (Integer) request.getAttribute("timywimy.request.id", 0);
        log.error(String.format("[%s] Exception:", id), ex);
        return id;
    }

    //handles anything unexpected
    @ExceptionHandler(value = {Exception.class})
    @ResponseBody
    protected Response<Object> handleGeneral(Exception ex, WebRequest request) {
        return new Response<>(logWithId(ex, request), ErrorCode.INTERNAL_GENERAL);
    }

    @ExceptionHandler(value = {DateTimeParseException.class})
    @ResponseBody
    protected Response<Object> handleDateTimeParse(DateTimeParseException ex, WebRequest request) {
        return new Response<>(logWithId(ex, request), ErrorCode.REQUEST_VALIDATION_INVALID_FIELDS);
    }

    //handles all custom errors
    @ExceptionHandler(value = {RestException.class})
    @ResponseBody
    protected Response<Object> handeRestExcpetions(RestException ex, WebRequest request) {
        ErrorCode errorCode = ex.getErrorCode() == null ? ErrorCode.INTERNAL_GENERAL : ex.getErrorCode();
        String message = ex.getMessage() == null ? errorCode.getMessage() : ex.getMessage();
        return new Response<>(logWithId(ex, request), errorCode, message);
    }

    //400
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ResponseEntity<>(new Response<>(null, ErrorCode.BAD_REQUEST), headers, status);
    }

    //404
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex,
                                                                   HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ResponseEntity<>(new Response<>(null, ErrorCode.NOT_FOUND), headers, status);
    }

    //405
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                         HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ResponseEntity<>(new Response<>(null, ErrorCode.NOT_ALLOWED), headers, status);
    }
}
