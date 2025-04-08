package com.example.quiz13.exception;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler({MethodArgumentNotValidException.class})
	public ResponseEntity<Map<String ,Object>>paramExceptionHandler(MethodArgumentNotValidException e){
		
		Map<String , Object>erroMap = new HashMap<>();
		erroMap.put("code", HttpStatus.BAD_REQUEST.value());
		erroMap.put("message",e.getAllErrors().get(0).getDefaultMessage());
		return ResponseEntity.badRequest().body(erroMap);
	}

	@ExceptionHandler({SQLException.class})
	public ResponseEntity<Map<String ,Object>>handleSQLException(SQLException e){
		
		Map<String , Object>erroMap = new HashMap<>();
		erroMap.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
		erroMap.put("message",e.getMessage());
		return ResponseEntity.internalServerError().body(erroMap);
	}





}
