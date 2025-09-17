package com.example.main.service;

import com.example.main.model.RoleType;
import com.example.main.model.User;
import com.example.main.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(User user) throws Exception {
       user.setPassword(passwordEncoder.encode(user.getPassword()));
       user.setUsername(user.getUsername());
       user.setEmail(user.getEmail());
       user.getRoles().add(RoleType.USER);
       userRepository.save(user);

    }

    public void saveUser(User user) {
         userRepository.save(user);
    }

   public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}
