package com.sb.video.streaming.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;

import com.sb.video.streaming.model.User;

@SpringBootTest
@ActiveProfiles("test")
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void cleanUp() {
        userRepository.delete(userRepository.findByUsername("testuser").orElseThrow(()-> new UsernameNotFoundException("User is not in db")));
    }


    @Test
    void shouldFindUserByUsername() {
        User user = new User();
        user.setUsername("testuser");
        user.setUser_id(UUID.randomUUID().toString());
        userRepository.save(user);

        Optional<User> result = userRepository.findByUsername("testuser");

        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());
    }
}
