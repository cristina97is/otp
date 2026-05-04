package com.promoit.otp.controller;

import com.promoit.otp.model.Role;

record RegisterRequest(String login, String password, Role role) {}
record LoginRequest(String login, String password) {}
record TokenResponse(String token) {}
record OtpConfigRequest(int codeLength, int ttlSeconds) {}
record OtpGenerateRequest(String operationId, String email, String phone, String telegramDestination, boolean sendEmail, boolean sendSms, boolean sendTelegram, boolean saveToFile) {}
record OtpValidateRequest(String operationId, String code) {}
record MessageResponse(String message) {}
