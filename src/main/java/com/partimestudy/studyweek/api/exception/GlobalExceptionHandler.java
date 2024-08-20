package com.partimestudy.studyweek.api.exception;

import com.partimestudy.studyweek.api.Response;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;


/**
 * 전역 예외 처리 핸들러.
 *
 * @author 김병우
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 런타임 예외 처리.
     *
     * @param ex 예외
     * @return Response<ErrorResponse>
     */
    @ExceptionHandler(
            RuntimeException.class
    )
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response<ErrorResponse> runtimeExceptionHandler(Exception ex) {
        return Response.fail(500, ErrorResponse.builder()
                .title(ex.getMessage())
                .status(500)
                .timestamp(LocalDateTime.now())
                .build());
    }
}
