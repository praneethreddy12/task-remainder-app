package com.example.taskremainder.controller;

import com.example.taskremainder.model.Taskmodel;
import com.example.taskremainder.service.CsvService;
import com.example.taskremainder.service.TaskService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.taskremainder.model.UserDTO;
import java.io.IOException;
import java.util.List;

@Controller
public class CSVController {

    private final CsvService csvService;
    private final TaskService taskService;

    public CSVController(CsvService csvService, TaskService taskService) {
        this.csvService = csvService;
        this.taskService = taskService;
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response, HttpSession session) throws IOException {

        UserDTO user = (UserDTO) session.getAttribute("loggedUser");
        if (user == null) {
            response.sendRedirect("/login");
            return;
        }

        List<Taskmodel> tasks = taskService.getTasksByUser(user.getId());
        String csv = csvService.exportTasks(tasks);

        // browser to download this as a file
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"tasks.csv\"");
        response.getWriter().write(csv);
    }
}