package com.rezdy.lunch.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.rezdy.lunch.domain.ErrorResponse;

import java.time.format.DateTimeParseException;

import javax.persistence.NoResultException;

@RestControllerAdvice
public class LunchExceptionHandler {

	@ExceptionHandler(DateTimeParseException.class)
	public ResponseEntity<ErrorResponse> handleValidationException(DateTimeParseException exception) {
		return new ResponseEntity<>(new ErrorResponse().setErrorMessage(exception.getMessage()),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(NoResultException.class)
	public ResponseEntity<ErrorResponse> handleNoResultException(NoResultException exception) {
		return new ResponseEntity<>(new ErrorResponse().setErrorMessage(exception.getMessage()), HttpStatus.NOT_FOUND);
	}

}
