package com.promoit.otp.model;
public record User(Long id, String login, String passwordHash, Role role) {}
