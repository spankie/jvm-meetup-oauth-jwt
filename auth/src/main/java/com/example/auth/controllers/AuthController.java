package com.example.auth.controllers;

import com.example.auth.models.LoginDTO;
import com.example.auth.security.JwtTokenProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

  @Autowired
  AuthenticationManager authenticationManager;
  @Autowired
  JwtTokenProvider tokenProvider;

  @PostMapping("/login")
  public ResponseEntity<LoginDTO> login(@RequestBody LoginDTO loginDTO) {
    LoginDTO  response = new LoginDTO();
    HttpStatus status = HttpStatus.OK;
    try {
      Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));
      response.setToken(tokenProvider.createToken(auth.getName()));
    } catch (Exception e) {
      status = HttpStatus.UNAUTHORIZED;
    }
    return new ResponseEntity<LoginDTO>(response, status);
  }
}