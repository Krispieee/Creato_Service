package com.creato.common;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e){

		Map<String, String> respMsg = new HashMap<String, String>();
		respMsg.put("message", "Oops!! Something went wrong");
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(respMsg);

    }
	
	@ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException e){

		Map<String, String> respMsg = new HashMap<String, String>();
		respMsg.put("message", "Sorry not an authorized request");
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respMsg);
    }



}