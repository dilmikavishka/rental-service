package lk.ijse.rentalservice.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lk.ijse.rentalservice.exception.BookServiceException;
import lk.ijse.rentalservice.exception.RentalNotFoundException;
import lk.ijse.rentalservice.exception.UserServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.Instant;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
@Order(-1)
public class GlobalExceptionHandler {

    @ExceptionHandler(RentalNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleRentalNotFound(
            RentalNotFoundException ex, HttpServletRequest request) {
        log.warn("Rental not found: {}", ex.getMessage());
        ProblemDetail problem = buildProblemDetail(
                HttpStatus.NOT_FOUND, "Rental Not Found", ex.getMessage(), request.getRequestURI());
        return problemResponse(HttpStatus.NOT_FOUND, problem);
    }

    @ExceptionHandler(UserServiceException.class)
    public ResponseEntity<ProblemDetail> handleUserServiceException(
            UserServiceException ex, HttpServletRequest request) {
        log.error("User service error: {}", ex.getMessage(), ex);
        ProblemDetail problem = buildProblemDetail(
                HttpStatus.BAD_GATEWAY,
                "User Service Error",
                "Unable to retrieve user details. Please try again later.",
                request.getRequestURI());
        return problemResponse(HttpStatus.BAD_GATEWAY, problem);
    }

    @ExceptionHandler(BookServiceException.class)
    public ResponseEntity<ProblemDetail> handleBookServiceException(
            BookServiceException ex, HttpServletRequest request) {
        log.error("Book service error: {}", ex.getMessage(), ex);
        ProblemDetail problem = buildProblemDetail(
                HttpStatus.BAD_GATEWAY,
                "Book Service Error",
                "Unable to retrieve book details. Please try again later.",
                request.getRequestURI());
        return problemResponse(HttpStatus.BAD_GATEWAY, problem);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ProblemDetail> handleBindException(
            BindException ex, HttpServletRequest request) {

        String detail = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining("; "));

        log.warn("Validation failed for [{}]: {}", request.getRequestURI(), detail);

        ProblemDetail problem = buildProblemDetail(
                HttpStatus.UNPROCESSABLE_ENTITY, "Validation Error", detail, request.getRequestURI());

        ex.getBindingResult().getFieldErrors()
                .forEach(fe -> problem.setProperty(fe.getField(), fe.getDefaultMessage()));

        return problemResponse(HttpStatus.UNPROCESSABLE_ENTITY, problem);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> handleConstraintViolation(
            ConstraintViolationException ex, HttpServletRequest request) {

        String detail = ex.getConstraintViolations()
                .stream()
                .map(cv -> {
                    String path = cv.getPropertyPath().toString();
                    String field = path.contains(".") ? path.substring(path.lastIndexOf('.') + 1) : path;
                    return field + ": " + cv.getMessage();
                })
                .collect(Collectors.joining("; "));

        log.warn("Constraint violation for [{}]: {}", request.getRequestURI(), detail);

        ProblemDetail problem = buildProblemDetail(
                HttpStatus.UNPROCESSABLE_ENTITY, "Validation Error", detail, request.getRequestURI());

        ex.getConstraintViolations().forEach(cv -> {
            String path = cv.getPropertyPath().toString();
            String field = path.contains(".") ? path.substring(path.lastIndexOf('.') + 1) : path;
            problem.setProperty(field, cv.getMessage());
        });

        return problemResponse(HttpStatus.UNPROCESSABLE_ENTITY, problem);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGeneric(
            Exception ex, HttpServletRequest request) {
        log.error("Unhandled exception on [{}]: {}", request.getRequestURI(), ex.getMessage(), ex);
        ProblemDetail problem = buildProblemDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                "An unexpected error occurred. Please try again later.",
                request.getRequestURI());
        return problemResponse(HttpStatus.INTERNAL_SERVER_ERROR, problem);
    }

    private ProblemDetail buildProblemDetail(
            HttpStatus status, String title, String detail, String instance) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
        problem.setTitle(title);
        problem.setInstance(URI.create(instance));
        problem.setProperty("timestamp", Instant.now().toString());
        return problem;
    }

    private ResponseEntity<ProblemDetail> problemResponse(HttpStatus status, ProblemDetail problem) {
        return ResponseEntity.status(status)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(problem);
    }
}