package com.example.taskremainder.Schedular;

import com.example.taskremainder.service.EmailService;
import org.springframework.stereotype.Component;
import com.example.taskremainder.model.Taskmodel;
import com.example.taskremainder.service.TaskService;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class TaskSchedular {

    private final TaskService service;
    private final EmailService emailService;


    private final Set<Integer> remindedTasks = new HashSet<>();

    public TaskSchedular(TaskService service, EmailService emailService) {
        this.service = service;
        this.emailService = emailService;
    }

    @Scheduled(fixedRate = 60000)
    public void checkTasks() {
        System.out.println("Scheduler running... checking tasks for reminders.....");

        List<Taskmodel> tasks = service.getTasks();
        LocalDateTime now = LocalDateTime.now();

        for (Taskmodel task : tasks) {

            // skip if no due date
            if (task.getDueDate() == null) continue;

            // skip if already completed
            if ("COMPLETED".equals(task.getStatus())) continue;

            // skip if reminder already sent for this task
            if (remindedTasks.contains(task.getId())) continue;

            // send reminder if due within next 1 hour
            LocalDateTime oneHourLater = now.plusHours(1);

            if (task.getDueDate().isAfter(now) && task.getDueDate().isBefore(oneHourLater)) {
                emailService.sendReminderEmail(task.getUserEmail(), task.getTitle());

                // mark this task as reminded so we don't email again
                remindedTasks.add(task.getId());

                System.out.println("Reminder sent for task: " + task.getTitle());
            }

            // clean up: if task is past due, remove from reminded set
            // so if user resets the task it can be reminded again
            if (task.getDueDate().isBefore(now)) {
                remindedTasks.remove(task.getId());
            }
        }
    }
}