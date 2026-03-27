package com.example.taskremainder.controller;

import com.example.taskremainder.model.Taskmodel;
import com.example.taskremainder.service.TaskManager;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

   // private TaskManager service= new TaskManager();
private final TaskManager service;

//constructor injection

    public TaskController(TaskManager service){
        this.service = service;
    }
    //add task

    @PostMapping("/add")
    public String addTask(@RequestBody Taskmodel taskmodel){
        service.addTask(taskmodel);

        return "Task added successfully";
    }

    //get all task

    @GetMapping("/show")
    public List<Taskmodel> getAllTasks(){
        return service.getTasks();
    }

    //update task

    @PutMapping("/{id}")
    public String updateTask(@PathVariable int id, @RequestBody Taskmodel taskmodel){
        service.updateTask(id, taskmodel);
        return "Task updated successfully";
    }

    //delete task

    @DeleteMapping("/{id}")
    public String deleteTask(@PathVariable int id){
        service.deleteTask(id);
        return "Task deleted successfully";
    }
}
