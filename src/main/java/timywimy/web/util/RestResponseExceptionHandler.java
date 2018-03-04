package timywimy.web.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import timywimy.util.exception.ErrorCode;
import timywimy.util.exception.RestException;
import timywimy.web.dto.common.Response;

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

    //handles all custom errors
    @ExceptionHandler(value = {RestException.class})
    @ResponseBody
    protected Response<Object> handeRestExcpetions(RestException ex, WebRequest request) {
        ErrorCode errorCode = ex.getErrorCode() == null ? ErrorCode.INTERNAL_GENERAL : ex.getErrorCode();
        String message = ex.getMessage() == null ? errorCode.getMessage() : ex.getMessage();
        return new Response<>(logWithId(ex, request), errorCode, message);
    }
}
