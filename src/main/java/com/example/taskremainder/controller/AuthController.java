package com.example.taskremainder.controller;

import com.example.taskremainder.model.UserDTO;
import com.example.taskremainder.model.Taskmodel;
import com.example.taskremainder.entity.User;
import com.example.taskremainder.service.UserService;
import com.example.taskremainder.service.TaskService;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AuthController {

    private final UserService userService;
    private final TaskService taskService;

    public AuthController(UserService userService, TaskService taskService) {
        this.userService = userService;
        this.taskService = taskService;
    }

    // REGISTER
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(User user, Model model) {

        if (!user.getPassword().equals(user.getConfirmPassword())) {
            model.addAttribute("error", "Passwords do not match!");
            return "register";
        }

        String result = userService.register(user);

        if (!result.equals("SUCCESS")) {
            model.addAttribute("error", result);
            return "register";
        }

        model.addAttribute("msg", "Check your email to verify account!");
        return "login";
    }

    // VERIFY EMAIL
    @GetMapping("/verify")
    public String verify(@RequestParam String token, Model model) {
        boolean result = userService.verifyUser(token);
        if (result) {
            model.addAttribute("msg", "Account verified successfully!");
        } else {
            model.addAttribute("error", "Invalid or expired token!");
        }
        return "login";
    }

    // LOGIN
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        Model model,
                        HttpSession session) {
        try {
            UserDTO user = userService.login(email, password);
            session.setAttribute("loggedUser", user);
            return "redirect:/dashboard";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }

    // DASHBOARD
    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        UserDTO user = (UserDTO) session.getAttribute("loggedUser");
        if (user == null) return "redirect:/login";

        List<Taskmodel> tasks = taskService.getTasksByUser(user.getId());

        model.addAttribute("tasks", tasks);
        model.addAttribute("user", user);
        model.addAttribute("totalTasks",     tasks.size());
        model.addAttribute("pendingCount",   tasks.stream().filter(t -> "pending".equalsIgnoreCase(t.getStatus())).count());
        model.addAttribute("completedCount", tasks.stream().filter(t -> "completed".equalsIgnoreCase(t.getStatus())).count());

        return "dashboard";
    }

    // ADD TASK
    @PostMapping("/add-task")
    public String addTask(Taskmodel task, HttpSession session) {
        UserDTO user = (UserDTO) session.getAttribute("loggedUser");
        if (user == null) return "redirect:/login";
        task.setUserId(user.getId());
        task.setUserEmail(user.getEmail());
        taskService.addTask(task);
        return "redirect:/dashboard";
    }

    // DELETE TASK
    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable int id) {
        taskService.deleteTask(id);
        return "redirect:/dashboard";
    }

    // EDIT TASK
    @GetMapping("/edit/{id}")
    public String editTask(@PathVariable int id, Model model) {
        Taskmodel task = taskService.getTaskById(id);
        model.addAttribute("task", task);
        return "edit-task";
    }

    // UPDATE TASK
    @PostMapping("/update-task")
    public String updateTask(Taskmodel task) {
        taskService.updateTask(task.getId(), task);
        return "redirect:/dashboard";
    }

    // SEND OTP
    @PostMapping("/send-otp")
    public String sendOtp(@RequestParam String email, Model model) {
        String result = userService.sendOtp(email);
        if (result.equals("EMAIL_NOT_FOUND")) {
            model.addAttribute("error", "No account found with that email.");
            return "forgot-password";
        }
        model.addAttribute("otpSent", true);
        model.addAttribute("email", email);
        return "forgot-password";
    }

    // FORGOT PASSWORD
    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "forgot-password";
    }

    // RESET PASSWORD
    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String email,
                                @RequestParam String otp,
                                @RequestParam String newPassword,
                                @RequestParam String confirmPassword,
                                Model model) {

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match.");
            model.addAttribute("otpSent", true);
            model.addAttribute("email", email);
            return "forgot-password";
        }

        String result = userService.resetPassword(email, otp, newPassword);

        if (!result.equals("SUCCESS")) {
            model.addAttribute("error", result);
            model.addAttribute("otpSent", true);
            model.addAttribute("email", email);
            return "forgot-password";
        }

        return "redirect:/login";
    }

    // LOGOUT
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}