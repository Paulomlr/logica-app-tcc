package com.tcc.logica.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

/**
 * Single place handling every exception type the API can throw, so every error
 * response has the same {@link ApiError} shape. This also sidesteps a real bug:
 * Spring's default handling for an uncaught exception calls response.sendError(),
 * which makes Tomcat internally redispatch to "/error" — a second pass through
 * the servlet/security filter chain that leaks a full stack trace by default and
 * can even get blocked by Spring Security, masking the real status code with 403.
 * @ExceptionHandler methods write the response directly and never trigger that.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiError> handleResponseStatusException(ResponseStatusException ex) {
        String message = ex.getReason() != null ? ex.getReason() : ex.getStatusCode().toString();
        return ResponseEntity.status(ex.getStatusCode()).body(new ApiError(ex.getStatusCode().value(), message));
    }

    @ExceptionHandler(FormulaSyntaxException.class)
    public ResponseEntity<ApiError> handleFormulaSyntaxException(FormulaSyntaxException ex) {
        return ResponseEntity.badRequest()
                .body(new ApiError(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), ex.getPosition()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getDefaultMessage())
                .filter(msg -> msg != null && !msg.isBlank())
                .findFirst()
                .orElse("Dados inválidos.");
        return ResponseEntity.badRequest().body(new ApiError(HttpStatus.BAD_REQUEST.value(), message));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleMalformedJson(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest()
                .body(new ApiError(HttpStatus.BAD_REQUEST.value(), "Corpo da requisição inválido ou malformado."));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnexpected(Exception ex) {
        log.error("Erro não tratado", ex);
        return ResponseEntity.internalServerError()
                .body(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro interno do servidor."));
    }
}
