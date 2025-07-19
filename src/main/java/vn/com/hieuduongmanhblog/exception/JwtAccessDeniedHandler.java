package vn.com.hieuduongmanhblog.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import vn.com.hieuduongmanhblog.dto.ResponseDTO;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Custom implementation of Spring Security's
 * {@link org.springframework.security.web.access.AccessDeniedHandler}.
 *
 * <p>
 * It handles cases where an authenticated user tries to access a resource
 * they are not authorized to access (i.e., access denied due to insufficient permissions).
 * </p>
 *
 * <p>
 * When access is denied, this handler sends a JSON response with a 403
 * Forbidden status code and a structured response body containing details.
 * </p>
 *
 */
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;

    @Autowired
    public JwtAccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ResponseDTO responseDTO = new ResponseDTO(HttpStatus.FORBIDDEN.value(), accessDeniedException.getMessage(), LocalDateTime.now());

        // caching headers (for security)
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.getWriter().write(objectMapper.writeValueAsString(responseDTO));
    }
}
