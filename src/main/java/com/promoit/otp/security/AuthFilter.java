package com.promoit.otp.security;

import com.promoit.otp.model.AuthUser;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.slf4j.*;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class AuthFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(AuthFilter.class);
    private final JwtService jwt;
    public AuthFilter(JwtService jwt) { this.jwt = jwt; }
    @Override public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest r = (HttpServletRequest) req; HttpServletResponse w = (HttpServletResponse) res;
        log.info("API request: {} {}", r.getMethod(), r.getRequestURI());
        String path = r.getRequestURI();
        if (path.startsWith("/api/auth") || path.equals("/error")) { chain.doFilter(req,res); return; }
        try {
            String h = r.getHeader("Authorization");
            if (h == null || !h.startsWith("Bearer ")) throw new RuntimeException("No token");
            AuthUser user = jwt.parse(h.substring(7));
            r.setAttribute("authUser", user);
            if (path.startsWith("/api/admin") && user.role() != com.promoit.otp.model.Role.ADMIN) { w.sendError(403, "Admin only"); return; }
            if (path.startsWith("/api/user") && user.role() != com.promoit.otp.model.Role.USER) { w.sendError(403, "User only"); return; }
            chain.doFilter(req,res);
        } catch (Exception e) { log.warn("Unauthorized request: {}", e.getMessage()); w.sendError(401, e.getMessage()); }
    }
}
