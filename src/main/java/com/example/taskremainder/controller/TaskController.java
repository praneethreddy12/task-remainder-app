package com.example.taskremainder.controller;

import com.example.taskremainder.model.Taskmodel;
import com.example.taskremainder.service.TaskService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService service;

    // constructor injection
    public TaskController(TaskService service) {
        this.service = service;
    }

    //  ADD TASK
    @PostMapping("/add")
    public String addTask(@RequestBody Taskmodel taskmodel) {
        service.addTask(taskmodel);
        return "Task added successfully";
    }

    //  GET ALL TASKS
    @GetMapping("/show")
    public List<Taskmodel> getAllTasks() {
        return service.getTasks();
    }

    //  GET TASKS BY USER
    @GetMapping("/user/{userId}")
    public List<Taskmodel> getTasksByUser(@PathVariable int userId) {
        return service.getTasksByUser(userId);
    }

    //  UPDATE TASK
    @PutMapping("/{id}")
    public String updateTask(@PathVariable int id, @RequestBody Taskmodel taskmodel) {
        service.updateTask(id, taskmodel);
        return "Task updated successfully";
    }

    //  DELETE TASK
    @DeleteMapping("/{id}")
    public String deleteTask(@PathVariable int id) {
        service.deleteTask(id);
        return "Task deleted successfully";
    }
}