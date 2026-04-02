package com.sb.video.streaming.exception;

import java.time.Instant;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;

@Component
public class JwtExceptionHandler {

    public ResponseEntity<?> handleExpiredJwtException(ExpiredJwtException ex) {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(
                    Map.of(
                            "error", "TOKEN_EXPIRED",
                            "message", "JWT token has expired",
                            "timestamp", Instant.now().toString()
                    )
            );
    }

    public ResponseEntity<?> handleSignatureException(SignatureException ex) {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(
                    Map.of(
                            "error", "INVALID_SIGNATURE",
                            "message", "JWT token signature is invalid",
                            "timestamp", Instant.now().toString()
                    )
            );
    }

    public ResponseEntity<?> handleJwtException(JwtException ex) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                    Map.of(
                            "error", "INVALID_TOKEN",
                            "message", "JWT token is invalid",
                            "timestamp", Instant.now().toString()
                    )
            );
    }

}
