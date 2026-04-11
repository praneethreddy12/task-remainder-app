package com.example.taskremainder.service;

import com.example.taskremainder.Repository.TaskRepository;
import com.example.taskremainder.model.Taskmodel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    // Add Task
    public void addTask(Taskmodel taskmodel) {
        repository.addTask(taskmodel);
    }

    // Get All Tasks
    public List<Taskmodel> getTasks() {
        return repository.getTasks();
    }

    // Get Tasks by User
    public List<Taskmodel> getTasksByUser(int userId) {
        return repository.getTasksByUser(userId);
    }
    // Get Task By ID
    public Taskmodel getTaskById(int id) {
        return repository.getTaskById(id);
    }

    // Update Task
    public void updateTask(int id, Taskmodel taskmodel) {
        repository.updateTask(id, taskmodel);
    }

    // Delete Task
    public void deleteTask(int id) {
        repository.deleteTask(id);
    }
}