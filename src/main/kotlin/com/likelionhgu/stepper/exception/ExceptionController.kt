package com.likelionhgu.stepper.exception

import org.springframework.boot.context.properties.bind.BindException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionController {

    @ExceptionHandler(BindException::class)
    fun handleBindException(e: BindException, result: BindingResult): ResponseEntity<Http400ErrorResponse> {
        result.fieldErrors
        return ResponseEntity.badRequest().body(
            Http400ErrorResponse(
                result.allErrors.map {
                    ErrorField(it.objectName, it.defaultMessage)
                }
            )
        )
    }

    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeException(e: RuntimeException): ResponseEntity<Http500ErrorResponse> {
        return ResponseEntity.internalServerError().body(
            Http500ErrorResponse(e.message)
        )
    }

    open class HttpStatusErrorResponse(
        val statusCode: Int,
        val message: String
    )

    data class Http500ErrorResponse(
        val description: String?
    ) : HttpStatusErrorResponse(500, HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase)

    data class Http400ErrorResponse(
        val errors: List<ErrorField>
    ) : HttpStatusErrorResponse(400, HttpStatus.BAD_REQUEST.reasonPhrase)

    data class ErrorField(
        val field: String,
        val description: String?
    )
}