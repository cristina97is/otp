package com.promoit.otp.controller;
import com.promoit.otp.service.AuthService;import org.springframework.web.bind.annotation.*;
@RestController@RequestMapping("/api/auth")
public class AuthController{private final AuthService auth;public AuthController(AuthService auth){this.auth=auth;}@PostMapping("/register")public MessageResponse register(@RequestBody RegisterRequest r){auth.register(r.login(),r.password(),r.role());return new MessageResponse("registered");}@PostMapping("/login")public TokenResponse login(@RequestBody LoginRequest r){return new TokenResponse(auth.login(r.login(),r.password()));}}
