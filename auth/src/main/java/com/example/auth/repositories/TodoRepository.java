package com.example.auth.repositories;

import com.example.auth.models.Todo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Integer> {
  
}