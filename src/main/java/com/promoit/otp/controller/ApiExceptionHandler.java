package com.promoit.otp.controller;
import org.slf4j.*;import org.springframework.http.*;import org.springframework.web.bind.annotation.*;import java.util.*;
@RestControllerAdvice
public class ApiExceptionHandler{private static final Logger log=LoggerFactory.getLogger(ApiExceptionHandler.class);@ExceptionHandler(Exception.class)public ResponseEntity<Map<String,String>> handle(Exception e){log.error("API error: {}",e.getMessage(),e);return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));}}
