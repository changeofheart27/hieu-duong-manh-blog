package vn.com.hieuduongmanhblog.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import vn.com.hieuduongmanhblog.dto.ResponseDTO;

import java.io.IOException;
import java.time.LocalDateTime;


/**
 * Custom implementation of Spring Security's
 * {@link org.springframework.security.web.AuthenticationEntryPoint}.
 *
 * <p>
 * This class is used to handle unauthorized access attempts in applications
 * secured with JWT (JSON Web Token). It is triggered whenever a user tries to
 * access a protected resource without proper authentication.
 * </p>
 *
 * <p>
 * When authentication fails, this entry point sends a JSON response with a 401
 * Unauthorized status code and a structured response body containing details.
 * </p>
 *
 */
@Component("jwtAuthenticationEntryPoint")
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Autowired
    public JwtAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ResponseDTO responseDTO = new ResponseDTO(HttpStatus.UNAUTHORIZED.value(), authException.getMessage(), LocalDateTime.now());

        // caching headers (for security)
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(objectMapper.writeValueAsString(responseDTO));

    }
}
