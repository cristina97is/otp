package com.promoit.otp.service;

import com.promoit.otp.dao.UserDao;
import com.promoit.otp.model.*;
import com.promoit.otp.security.JwtService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserDao users; private final JwtService jwt; private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    public AuthService(UserDao users, JwtService jwt) { this.users = users; this.jwt = jwt; }
    public User register(String login, String password, Role role) {
        if (role == Role.ADMIN && users.adminExists()) throw new RuntimeException("Administrator already exists");
        if (users.findByLogin(login).isPresent()) throw new RuntimeException("Login already exists");
        return users.create(login, encoder.encode(password), role == null ? Role.USER : role);
    }
    public String login(String login, String password) {
        User u = users.findByLogin(login).orElseThrow(() -> new RuntimeException("User not found"));
        if (!encoder.matches(password, u.passwordHash())) throw new RuntimeException("Wrong password");
        return jwt.generate(new AuthUser(u.id(), u.login(), u.role()));
    }
}
