package com.cityplatform.platform.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * 全局异常处理。Controller 不处理异常转换，统一在此完成。
 * 业务异常向上传播由 Application 层抛出，Domain 不感知 HTTP。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ex.getCode(), ex.getMessage()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(UnauthorizedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(ex.getCode(), ex.getMessage()));
    }

    @ExceptionHandler(IllegalStatusTransitionException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStatus(IllegalStatusTransitionException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ex.getCode(), ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return ResponseEntity.badRequest()
                .body(new ErrorResponse("VALIDATION_ERROR", msg));
    }

    /**
     * 请求体不可读（非法JSON/类型不匹配）属于客户端错误，返回400而非500。
     */
    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadable(org.springframework.http.converter.HttpMessageNotReadableException ex) {
        log.warn("请求体解析失败: {}", ex.getMessage());
        return ResponseEntity.badRequest()
                .body(new ErrorResponse("BAD_REQUEST_BODY", "请求体格式错误"));
    }

    @ExceptionHandler(org.springframework.web.servlet.resource.NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFound(org.springframework.web.servlet.resource.NoResourceFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("NOT_FOUND", "资源不存在"));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(BusinessException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ex.getCode(), ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleOther(Exception ex) {
        // 规范第十一章：必须记录异常信息，禁止吞掉栈信息导致无法定位
        log.error("未处理异常: {}", ex.getClass().getName(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("INTERNAL_ERROR", "服务器内部错误"));
    }
}
