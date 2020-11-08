package com.friendship41.memberserver.common

import com.friendship41.memberserver.data.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import java.lang.Exception
import java.lang.RuntimeException
import java.net.BindException

@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        logger().error("[handleMethodArgumentNotValidException]", e)
        return ResponseEntity(
                ErrorResponse(
                        CommonErrorCode.INVALID_INPUT_VALUE.message,
                        CommonErrorCode.INVALID_INPUT_VALUE.status,
                        CommonErrorCode.INVALID_INPUT_VALUE.code),
                HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(BindException::class)
    fun handleBindException(e: BindException): ResponseEntity<ErrorResponse> {
        logger().error("[handleBindException]", e)
        return ResponseEntity(
                ErrorResponse(
                        CommonErrorCode.INVALID_INPUT_VALUE.message,
                        CommonErrorCode.INVALID_INPUT_VALUE.status,
                        CommonErrorCode.INVALID_INPUT_VALUE.code),
                HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleMethodArgumentTypeMismatchException(e: MethodArgumentTypeMismatchException): ResponseEntity<ErrorResponse> {
        logger().error("[handleMethodArgumentTypeMismatchException]", e)
        return ResponseEntity(
                ErrorResponse(
                        CommonErrorCode.INVALID_INPUT_VALUE.message,
                        CommonErrorCode.INVALID_INPUT_VALUE.status,
                        CommonErrorCode.INVALID_INPUT_VALUE.code),
                HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleHttpRequestMethodNotSupportedException(e: HttpRequestMethodNotSupportedException): ResponseEntity<ErrorResponse> {
        logger().error("[handleHttpRequestMethodNotSupportedException]", e)
        return ResponseEntity(
                ErrorResponse(
                        CommonErrorCode.METHOD_NOT_ALLOWED.message,
                        CommonErrorCode.METHOD_NOT_ALLOWED.status,
                        CommonErrorCode.METHOD_NOT_ALLOWED.code),
                HttpStatus.METHOD_NOT_ALLOWED)
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException::class)
    fun handleAccessDeniedException(e: org.springframework.security.access.AccessDeniedException): ResponseEntity<ErrorResponse> {
        logger().error("[handleAccessDeniedException]", e)
        return ResponseEntity(
                ErrorResponse(
                        CommonErrorCode.UNAUTHORIZED.message,
                        CommonErrorCode.UNAUTHORIZED.status,
                        CommonErrorCode.UNAUTHORIZED.code),
                HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(e: BusinessException): ResponseEntity<ErrorResponse> {
        logger().error("[handleBusinessException]", e)
        return ResponseEntity(
                e.errorResponse,
                HttpStatus.valueOf(e.errorResponse.status))
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ErrorResponse> {
        logger().error("[handleException]", e)
        return ResponseEntity(
                ErrorResponse(
                        e.message ?: CommonErrorCode.INTERNAL_SERVER_ERROR.message,
                        CommonErrorCode.INTERNAL_SERVER_ERROR.status,
                        CommonErrorCode.INTERNAL_SERVER_ERROR.code),
                HttpStatus.INTERNAL_SERVER_ERROR)
    }
}

enum class CommonErrorCode(val status: Int, val code: String, val message: String) {
    INVALID_INPUT_VALUE(400, "CE001", "Invalid Input Value"),
    METHOD_NOT_ALLOWED(405, "CE002", "Not Allowed Request"),
    UNAUTHORIZED(401, "CE003", "Unauthroized, Access is Denied"),
    INTERNAL_SERVER_ERROR(500, "CE004", "Internal Server Error")
}

data class BusinessException(val errorResponse: ErrorResponse): RuntimeException()
