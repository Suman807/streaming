package com.sb.video.streaming.filter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sb.video.streaming.exception.JwtExceptionHandler;
import com.sb.video.streaming.utils.JwtUtils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private JwtExceptionHandler jwtExceptionHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String authHeader = request.getHeader("Authorization");
            String username = null;
            String jwt = null;

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                jwt = authHeader.substring("Bearer ".length());
                username = jwtUtils.extractUsername(jwt);
            }

            if (username != null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (jwtUtils.validateToken(jwt)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
                            null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            log.error("JWT token has expired: {}", e.getMessage());
            writeResponse(response, jwtExceptionHandler.handleExpiredJwtException(e));
            return; // Stop further processing
        }catch(SignatureException e){
            log.error("JWT token signature is invalid: {}", e.getMessage());
            writeResponse(response, jwtExceptionHandler.handleSignatureException(e));
            return; // Stop further processing
        }
        catch (JwtException e) {
            log.error("JWT token is invalid: {}", e.getMessage());
            writeResponse(response, jwtExceptionHandler.handleJwtException(e));
            return; // Stop further processing
        }
        catch (Exception e) {
            // Handle any other exceptions that may occur   
            ResponseEntity<?> entity = ResponseEntity  
                .status(500)  
                .body(  
                    Map.of(  
                        "error", "INTERNAL_SERVER_ERROR",  
                        "message", "An unexpected error occurred",  
                        "timestamp", LocalDateTime.now()  
                    )  
                ); 
            writeResponse(response, entity);
            return; // Stop further processing
        }
    }

    private void writeResponse(HttpServletResponse response,ResponseEntity<?> entity) throws IOException {
        response.setStatus(
            entity.getStatusCode().value()
        );

        response.setContentType("application/json");

        new ObjectMapper()
            .writeValue(
                response.getWriter(),
                entity.getBody()
            );
    }

}
