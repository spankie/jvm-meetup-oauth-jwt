package com.example.auth.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class TodoUserDetailsService implements UserDetailsService {

  @Autowired
  PasswordEncoder passwordEncoder;


  @Override
  public UserDetails loadUserByUsername(String arg0) throws UsernameNotFoundException {
    return User.withUsername("spankie").password(passwordEncoder.encode("password")).authorities(Arrays.asList()).build();
  }
  
}