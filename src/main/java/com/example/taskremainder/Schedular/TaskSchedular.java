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
        try {
            LocalDateTime now = LocalDateTime.now();
            List<Taskmodel> tasks = service.getTasks();
            System.out.println("⏰ [Scheduler] Running check at " + now + " (IST). Total tasks in DB: " + tasks.size());

            for (Taskmodel task : tasks) {
                // skip if no due date
                if (task.getDueDate() == null) {
                    continue;
                }

                // skip if already completed
                if ("COMPLETED".equalsIgnoreCase(task.getStatus())) {
                    continue;
                }

                // skip if reminder already sent for this task
                if (remindedTasks.contains(task.getId())) {
                    continue;
                }

                // send reminder if due within the next 1 hour (or due right now / past up to 5 min)
                LocalDateTime windowStart = now.minusMinutes(5);
                LocalDateTime windowEnd = now.plusHours(1);

                if (task.getDueDate().isAfter(windowStart) && task.getDueDate().isBefore(windowEnd)) {
                    System.out.println("🔔 [Scheduler] Sending reminder for task #" + task.getId() + " '" + task.getTitle() + "' (due: " + task.getDueDate() + ") to: " + task.getUserEmail());
                    
                    emailService.sendReminderEmail(task.getUserEmail(), task.getTitle());

                    // mark this task as reminded so we don't email again
                    remindedTasks.add(task.getId());
                } else if (task.getDueDate().isBefore(windowStart)) {
                    // Task is past due; clean up from reminded set so if edited it can remind again
                    remindedTasks.remove(task.getId());
                }
            }
        } catch (Exception e) {
            System.err.println("❌ [Scheduler] Error during task check: " + e.getMessage());
            e.printStackTrace();
        }
    }
}