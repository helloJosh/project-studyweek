package com.partimestudy.studyweek.api.exception;

import com.partimestudy.studyweek.api.Response;
import com.partimestudy.studyweek.auth.exception.*;
import com.partimestudy.studyweek.challenge.exception.*;
import com.partimestudy.studyweek.member.exception.DuplicatedLoginIdException;
import com.partimestudy.studyweek.member.exception.MemberNotFoundException;
import com.partimestudy.studyweek.member.exception.PostLoginRequestFormException;
import com.partimestudy.studyweek.member.exception.PostMemberRequestFormException;
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

    /**
     * 400 예외처리
     *
     * @param ex 예외
     * @return Response<ErrorResponse>
     */
    @ExceptionHandler(
            {
                    MaxDepositOverFlowException.class,
                    MinDepositUnderFlowException.class,
                    PostRegistrationRequestFormErrorException.class,
                    PostLoginRequestFormException.class,
                    PostMemberRequestFormException.class
            }
    )
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response<ErrorResponse> badRequestExceptionHandler(Exception ex) {
        return Response.fail(400, ErrorResponse.builder()
                .title(ex.getMessage())
                .status(400)
                .timestamp(LocalDateTime.now())
                .build());
    }

    /**
     * 401 예외처리
     *
     * @param ex 예외
     * @return Response<ErrorResponse>
     */
    @ExceptionHandler(
            {
                    AccessTokenNotValidException.class,
                    AccessTokenNotFoundException.class,
                    RefreshTokenNotValidException.class,
                    RefreshTokenExpireException.class,
                    RefreshTokenNotFoundException.class,
                    AuthenticationInvalidException.class
            }
    )
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response<ErrorResponse> unauthorizedExceptionHandler(Exception ex) {
        return Response.fail(401, ErrorResponse.builder()
                .title(ex.getMessage())
                .status(401)
                .timestamp(LocalDateTime.now())
                .build());
    }


    /**
     * 404 예외처리
     *
     * @param ex 예외
     * @return Response<ErrorResponse>
     */
    @ExceptionHandler(
            {
                    ChallengeNotFoundException.class,
                    RegistrationNotFoundException.class,
                    MemberNotFoundException.class
            }
    )
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response<ErrorResponse> notFoundExceptionHandler(Exception ex) {
        return Response.fail(404, ErrorResponse.builder()
                .title(ex.getMessage())
                .status(404)
                .timestamp(LocalDateTime.now())
                .build());
    }


    /**
     * 409 예외처리
     *
     * @param ex 예외
     * @return Response<ErrorResponse>
     */
    @ExceptionHandler(
            {
                    ChallengeNotActiveException.class,
                    DuplicatedChallengeException.class,
                    DuplicatedLoginIdException.class
            }
    )
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response<ErrorResponse> conflictExceptionHandler(Exception ex) {
        return Response.fail(409, ErrorResponse.builder()
                .title(ex.getMessage())
                .status(409)
                .timestamp(LocalDateTime.now())
                .build());
    }
}
