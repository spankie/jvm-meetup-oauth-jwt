package com.example.auth.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * {JwtTokenFilter} We should use OncePerRequestFilter since we are doing a
 * database call, there is no point in doing this more than once
 */
public class JwtTokenFilter extends OncePerRequestFilter {
  private final JwtTokenProvider jwtTokenProvider;

  public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
    this.jwtTokenProvider = jwtTokenProvider;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String token = jwtTokenProvider.resolveToken(request);
    try {
      if (token != null && jwtTokenProvider.validateToken(token)) {
        Authentication auth = jwtTokenProvider.getAuthentication(token); // this throws exception
        SecurityContextHolder.getContext().setAuthentication(auth);
      } else {
        throw new Exception("UNAUTHORIZED");
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
      // this is very important, since it guarantees the user is not authenticated at all
      SecurityContextHolder.clearContext();
      HttpStatus status = HttpStatus.UNAUTHORIZED;
      String error = "UNAUTHORIZED";
      //set the response headers
      response.setStatus(status.value());
      response.setContentType("application/json");

      PrintWriter out = response.getWriter();
      out.print(error);
      out.flush();
      return;
    }

    filterChain.doFilter(request, response);
  }

}