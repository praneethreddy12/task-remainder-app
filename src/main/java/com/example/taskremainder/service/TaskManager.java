package com.example.taskremainder.service;

import com.example.taskremainder.Repository.TaskRepository;
import com.example.taskremainder.model.Taskmodel;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Service;

//import java.util.ArrayList;
import java.util.List;


@Service
public class TaskManager {
    private final TaskRepository repository;

    //private List<Taskmodel> tasks=new ArrayList<>();


    //constructor injection
    public TaskManager(TaskRepository repository) {
        this.repository = repository;
    }

    //add task
    public void addTask(Taskmodel taskmodel){
        repository.addTask(taskmodel);
    }
    //this service is used for post to add task

    //public void addTask(Taskmodel taskmodel){
     //   tasks.add(taskmodel);
     //   System.out.println("Task added successfully");
    //}


    //get all task

    public List<Taskmodel> getTasks(){
        return repository.getTask();
    }

    //this is used for get all task to show
    //public List<Taskmodel> getAllTaskmodel(){
    //   return tasks;
    //}


     //update task

    public void updateTask(int id,Taskmodel taskmodel){
        repository.updateTask(id,taskmodel);
    }
    //this is used for delete any task
    //
    //public void deleteTask(int id){
    //tasks.removeIf(taskmodel -> taskmodel.getId()==id);
    //System.out.println("Task delete successfully");
    //}


    //delete  task

    public void deleteTask(int id){
        repository.deleteTask(id);
    }

    //this is used for update any task
    //
    //public void updateTask(int id,Taskmodel updatedTask){
      //
      //   for(Taskmodel task:tasks){
      //
      //      if(task.getId()==id){
      //
      //          task.setTitle(updatedTask.getTitle());
      //task.setDescription(updatedTask.getDescription());
      //          task.setDueDate(updatedTask.getDueDate());
      //task.setStatus(updatedTask.getStatus());

       //         System.out.println("Task updated successfully");

        //        return;
      //      }
      //  }

     //   System.out.println("Task not found");
    //}
}
