package com.example.auth.controllers;

import java.util.List;

import com.example.auth.models.Todo;
import com.example.auth.repositories.TodoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/todo")
public class TodoController {
  
  @Autowired
  private TodoRepository todoRepository;

  @GetMapping
  public List<Todo> getTodos() {
    return todoRepository.findAll();
  }

  @PostMapping
  public Todo createTodo(@RequestBody Todo newTodo) {
    return todoRepository.save(newTodo);
  }

}