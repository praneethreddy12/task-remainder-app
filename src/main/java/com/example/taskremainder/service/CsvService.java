package com.example.taskremainder.service;

import com.example.taskremainder.model.Taskmodel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CsvService {

    public String exportTasks(List<Taskmodel> tasks) {
        StringBuilder sb = new StringBuilder();
        sb.append("ID,Title,Description,Status,Due Date\n");

        for (Taskmodel task : tasks) {
            sb.append(task.getId()).append(",")
                    .append(task.getTitle()).append(",")
                    .append(task.getDescription()).append(",")
                    .append(task.getStatus()).append(",")
                    .append(task.getDueDate()).append("\n");
        }

        return sb.toString();
    }
}