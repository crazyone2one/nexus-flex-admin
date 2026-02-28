package com.nexus.flex.common.exception;

import com.nexus.flex.common.IResultCode;
import com.nexus.flex.common.result.HttpResultCode;
import com.nexus.flex.common.result.ResultHolder;
import com.nexus.flex.utils.ServiceUtils;
import com.nexus.flex.utils.Translator;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : 11's papa
 * @since : 2026/2/25, 星期三
 **/
@NullMarked
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final Translator translator;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultHolder handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            assert errorMessage != null;
            errors.put(fieldName, errorMessage);
        });
        return ResultHolder.error(HttpResultCode.VALIDATE_FAILED.getCode(), translator.get(HttpResultCode.VALIDATE_FAILED.getMessage()), errors);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResultHolder handleHttpRequestMethodNotSupportedException(HttpServletResponse response, Exception exception) {
        response.setStatus(HttpStatus.METHOD_NOT_ALLOWED.value());
        return ResultHolder.error(HttpStatus.METHOD_NOT_ALLOWED.value(), exception.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResultHolder handleBadCredentials(BadCredentialsException e) {
        return ResultHolder.error(HttpResultCode.UNAUTHORIZED.getCode(), translator.get(HttpResultCode.UNAUTHORIZED.getMessage()), e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResultHolder handleAccessDenied(AccessDeniedException e) {
        return ResultHolder.error(HttpResultCode.FORBIDDEN.getCode(), translator.get(HttpResultCode.FORBIDDEN.getMessage()), e.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ResultHolder> handlerMSException(BusinessException e) {
        IResultCode errorCode = e.getErrorCode();
        if (errorCode == null) {
            // 如果抛出异常没有设置状态码，则返回错误 message
            return ResponseEntity.internalServerError()
                    .body(ResultHolder.error(HttpResultCode.FAILED.getCode(), e.getMessage()));
        }

        int code = errorCode.getCode();
        String message = errorCode.getMessage();
        message = translator.get(message, message);

        if (errorCode instanceof HttpResultCode) {
            // 如果是 HttpResultCode，则设置响应的状态码，取状态码的后三位
            if (errorCode.equals(HttpResultCode.NOT_FOUND)) {
                message = getNotFoundMessage(message);
            }
            return ResponseEntity.status(code % 1000)
                    .body(ResultHolder.error(code, message, e.getMessage()));
        } else {
            // 响应码返回 500，设置业务状态码
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResultHolder.error(code, translator.get(message, message), e.getMessage()));
        }
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ResultHolder> handleException(Exception e) {
        return ResponseEntity.internalServerError()
                .body(ResultHolder.error(HttpResultCode.FAILED.getCode(),
                        e.getMessage(), getStackTraceAsString(e)));
    }

    private String getStackTraceAsString(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw, true));
        return sw.toString();
    }

    private String getNotFoundMessage(String message) {
        String resourceName = ServiceUtils.getResourceName();
        if (StringUtils.isNotBlank(resourceName)) {
            message = String.format(message, translator.get(resourceName, resourceName));
        } else {
            message = String.format(message, translator.get("resource.name"));
        }
        ServiceUtils.clearResourceName();
        return message;
    }
}
