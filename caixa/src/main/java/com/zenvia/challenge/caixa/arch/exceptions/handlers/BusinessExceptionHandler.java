package com.zenvia.challenge.caixa.arch.exceptions.handlers;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Order(2)
public class BusinessExceptionHandler {
	
	@ExceptionHandler(value = BusinessException.class)
	public ResponseEntity handleResponse(BusinessException businessException) {
		return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(businessException.getMessage());
	}
}
