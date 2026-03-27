package com.example.taskremainder.Schedular;

import com.example.taskremainder.service.EmailService;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Component;
import com.example.taskremainder.model.Taskmodel;
import com.example.taskremainder.service.TaskManager;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class TaskSchedular {

    private final TaskManager service;
    private final EmailService emailService;

    public TaskSchedular(TaskManager service, EmailService emailService) {
        this.service = service;
        this.emailService = emailService;
    }

    //constructor injection
    //
    //public TaskSchedular(TaskManager service) {
    //    this.service;
    // }

    @Scheduled(fixedRate = 60000)
    public void checkTask() {
        System.out.println("Scheduling Running.........check task for remainders.....");

        List<Taskmodel> tasks = service.getTasks();

        LocalDateTime now = LocalDateTime.now();

        for (Taskmodel task : tasks) {


            if (task.getDueDate() !=null){

                //check if task is within one hour

                if (task.getDueDate().isBefore(now.plusHours(1))&& task.getDueDate().isAfter(now))

                    System.out.println("Remainder for task: " + task.getTitle());

                    //send email
                    emailService.sendEmail("user@gmail.com",
                "Task Remainder",
                "Remainder" + task.getTitle() + "is due soon!");
            }
            //        System.out.println("Task: " + task.getTitle());
            //       System.out.println("Due Data: " + task.getDueDate());
        }
    }
}
