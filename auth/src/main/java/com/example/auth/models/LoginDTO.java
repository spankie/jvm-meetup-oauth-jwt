package com.example.auth.models;

import lombok.Data;

@Data
public class LoginDTO extends User {
  private String token;
}
