package com.example.auth.security;

import java.util.Base64;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenProvider {
  // @Value("${api.security.jwt.token.secret-key:secret-key}")
  private String secretKey = "secretKeyToGenerateJWTs";

  // @Value("${api.security.jwt.token.expire-length:86400000}")
  private final long validityInMilliseconds = 86400000;

  @Autowired
  private UserDetailsService myUserDetails;

  @PostConstruct
  public void init() {
    secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
  }

  protected String generateToken(String subject, Long validityPeriod) {
    Claims claims = Jwts.claims().setSubject(subject);
    Date now = new Date();
    Date validity = new Date(now.getTime() + validityPeriod);

    return Jwts.builder().setClaims(claims).setIssuedAt(now).setExpiration(validity)
        .signWith(SignatureAlgorithm.HS256, secretKey).compact();
  }

  public String createToken(String username) {
    return generateToken(username, validityInMilliseconds);
  }

  public String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }

  public boolean validateToken(String token) {
    // this also checks if the token is expired
    Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
    return true;
  }

  public Authentication getAuthentication(String token) {
    UserDetails userDetails = myUserDetails.loadUserByUsername(getUsername(token));
    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  public String getUsername(String token) {
    return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
  }

  private Claims getAllClaims(String token) {
    return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
  }

  public Boolean isTokenExpired(String token) {
    return getAllClaims(token).getExpiration().before(new Date());
  }
}