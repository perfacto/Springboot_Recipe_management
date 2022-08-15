package com.recipe.recipemanagement.exception;

import java.time.LocalDateTime;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author ShubhamKumar
 *  Global exceptions handler- this class separate the  exceptions code from business logic
 */

@RestControllerAdvice
public class GlobalExceptionHandler {
	private static final Logger LOGGER = LogManager.getLogger();

	/**
	 * This method handles EntityNotFoundException
	 *
	 * @param request HttpServletRequest
	 * @param ex      EntityNotFoundException
	 * @return Exception response
	 */
	@ExceptionHandler({ EntityNotFoundException.class })
	public ResponseEntity<ExceptionResponse> handleNotFoundException(HttpServletRequest request,
			EntityNotFoundException ex) {
		LOGGER.error("EntityNotFoundException {} {}", request.getMethod(), request.getRequestURI());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponse(HttpStatus.NOT_FOUND.value(),
				request.getRequestURI(), HttpStatus.NOT_FOUND.getReasonPhrase(), LocalDateTime.now(), ex.getMessage()));

	}

	/**
	 * This method handles EmptyResultDataAccessException
	 *
	 * @param request HttpServletRequest
	 * @param ex      EmptyResultDataAccessException
	 * @return Exception response
	 */
	@ExceptionHandler({ EmptyResultDataAccessException.class })
	public ResponseEntity<ExceptionResponse> emptyResultDataAccessException(HttpServletRequest request,
			EmptyResultDataAccessException ex) {
		LOGGER.error("EmptyResultDataAccessException {} {}", request.getMethod(), request.getRequestURI());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponse(HttpStatus.NOT_FOUND.value(),
				request.getRequestURI(), HttpStatus.NOT_FOUND.getReasonPhrase(), LocalDateTime.now(), ex.getMessage()));

	}

	/**
	 * This method handles PersistenceException
	 *
	 * @param request HttpServletRequest
	 * @param ex      PersistenceException
	 * @return Exception response
	 */
	@ExceptionHandler({ PersistenceException.class })
	public ResponseEntity<ExceptionResponse> persistenceException(HttpServletRequest request, PersistenceException ex) {
		LOGGER.error("PersistenceException {} {}", request.getMethod(), request.getRequestURI());

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(ExceptionResponse.builder().statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
						.URI(request.getRequestURI()).message(ex.getMessage()).timestamp(LocalDateTime.now())
						.details(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()).build());

	}

	/**
	 * This method handles ConstraintViolationException
	 *
	 * @param request HttpServletRequest
	 * @param ex      PersistenceException
	 * @return Exception response
	 */
	@ExceptionHandler({ ConstraintViolationException.class })
	public ResponseEntity<ExceptionResponse> constraintViolationException(HttpServletRequest request,
			PersistenceException ex) {
		LOGGER.error("ConstraintViolationException {} {}", request.getMethod(), request.getRequestURI());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(ExceptionResponse.builder()
						.statusCode(HttpStatus.BAD_REQUEST.value())
						.URI(request.getRequestURI())
						.message(ex.getMessage())
						.timestamp(LocalDateTime.now())
						.details(HttpStatus.BAD_REQUEST.getReasonPhrase()).build());

	}
}
