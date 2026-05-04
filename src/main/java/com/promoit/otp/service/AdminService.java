package com.promoit.otp.service;

import com.promoit.otp.dao.*;
import com.promoit.otp.model.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AdminService {
    private final UserDao users; private final OtpConfigDao config;
    public AdminService(UserDao users, OtpConfigDao config) { this.users = users; this.config = config; }
    public void updateConfig(int len, int ttl) { if (len < 4 || len > 10 || ttl < 30) throw new RuntimeException("Bad OTP config"); config.update(len, ttl); }
    public OtpConfig getConfig() { return config.get(); }
    public List<User> nonAdmins() { return users.findNonAdmins(); }
    public void deleteUser(long id) { users.deleteUser(id); }
}
