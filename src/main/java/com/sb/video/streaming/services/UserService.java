package com.sb.video.streaming.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sb.video.streaming.model.User;
import com.sb.video.streaming.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getUserByName(String username){
        return userRepository.findByUsername(username).orElse(null);
    }

}
