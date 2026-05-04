package com.promoit.otp.security;

import com.promoit.otp.model.AuthUser;
import com.promoit.otp.model.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

@Service
public class JwtService {
    @Value("${app.jwt.secret}") private String secret;
    @Value("${app.jwt.ttl-minutes}") private long ttlMinutes;
    public String generate(AuthUser u) {
        long exp = Instant.now().plusSeconds(ttlMinutes * 60).getEpochSecond();
        String payload = u.id()+":"+u.login()+":"+u.role()+":"+exp;
        String body = b64(payload); return body + "." + sign(body);
    }
    public AuthUser parse(String token) {
        String[] p = token.split("\\.");
        if (p.length != 2 || !sign(p[0]).equals(p[1])) throw new RuntimeException("Invalid token");
        String[] v = new String(Base64.getUrlDecoder().decode(p[0]), StandardCharsets.UTF_8).split(":");
        if (Instant.now().getEpochSecond() > Long.parseLong(v[3])) throw new RuntimeException("Token expired");
        return new AuthUser(Long.parseLong(v[0]), v[1], Role.valueOf(v[2]));
    }
    private String b64(String s) { return Base64.getUrlEncoder().withoutPadding().encodeToString(s.getBytes(StandardCharsets.UTF_8)); }
    private String sign(String body) {
        try { Mac mac = Mac.getInstance("HmacSHA256"); mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256")); return Base64.getUrlEncoder().withoutPadding().encodeToString(mac.doFinal(body.getBytes(StandardCharsets.UTF_8))); }
        catch (Exception e) { throw new RuntimeException(e); }
    }
}
