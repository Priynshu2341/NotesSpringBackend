package com.example.main.service;

import com.example.main.model.User;
import com.example.main.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {


    private final UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       User user = userRepository.findByUsername(username)
               .orElseThrow(() -> new UsernameNotFoundException("User not found"));

       return org.springframework.security.core.userdetails.User.builder()
               .username(user.getUsername())
               .password(user.getPassword())
               .authorities(user.getAuthorities())
               .build();

    }
}
