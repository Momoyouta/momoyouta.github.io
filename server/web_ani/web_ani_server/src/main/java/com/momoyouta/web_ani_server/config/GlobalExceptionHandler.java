package com.momoyouta.web_ani_server.config;

import com.momoyouta.web_ani_common.enumm.ResponseCode;
import com.momoyouta.web_ani_common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AccessDeniedException.class)
    public Result<String> handleAccessDeniedException(AccessDeniedException e) {
        return Result.error(ResponseCode.TOKEN_INVALID,"无权限访问");
    }
}
