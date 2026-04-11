package com.example.taskremainder.Repository;

import com.example.taskremainder.model.Taskmodel;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class TaskRepository {

    private final JdbcTemplate jdbcTemplate;

    public TaskRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // ADD TASK
    public void addTask(Taskmodel taskmodel) {
        String sql = "INSERT INTO tasks(title, description, status, user_id, user_email, due_date) VALUES (?,?,?,?,?,?)";
        jdbcTemplate.update(sql,
                taskmodel.getTitle(),
                taskmodel.getDescription(),
                taskmodel.getStatus(),
                taskmodel.getUserId(),
                taskmodel.getUserEmail(),
                taskmodel.getDueDate()
        );
    }

    //  GET ALL TASKS
    public List<Taskmodel> getTasks() {
        String sql = "SELECT * FROM tasks";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Taskmodel task = new Taskmodel();
            task.setId(rs.getInt("id"));
            task.setTitle(rs.getString("title"));
            task.setDescription(rs.getString("description"));
            task.setStatus(rs.getString("status"));
            task.setUserId(rs.getInt("user_id"));
            task.setUserEmail(rs.getString("user_email"));
            task.setDueDate(rs.getObject("due_date", LocalDateTime.class));
            return task;
        });
    }

    //  GET TASKS BY USER
    public List<Taskmodel> getTasksByUser(int userId) {
        String sql = "SELECT * FROM tasks WHERE user_id=?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Taskmodel task = new Taskmodel();
            task.setId(rs.getInt("id"));
            task.setTitle(rs.getString("title"));
            task.setDescription(rs.getString("description"));
            task.setStatus(rs.getString("status"));
            task.setUserId(rs.getInt("user_id"));
            task.setUserEmail(rs.getString("user_email"));
            task.setDueDate(rs.getObject("due_date", LocalDateTime.class));
            return task;
        }, userId);
    }

    //  GET TASK BY ID
    public Taskmodel getTaskById(int id) {
        String sql = "SELECT * FROM tasks WHERE id=?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            Taskmodel task = new Taskmodel();
            task.setId(rs.getInt("id"));
            task.setTitle(rs.getString("title"));
            task.setDescription(rs.getString("description"));
            task.setStatus(rs.getString("status"));
            task.setUserId(rs.getInt("user_id"));
            task.setUserEmail(rs.getString("user_email"));
            task.setDueDate(rs.getObject("due_date", LocalDateTime.class));
            return task;
        }, id);
    }

    //  UPDATE TASK
    public void updateTask(int id, Taskmodel taskmodel) {
        String sql = "UPDATE tasks SET title=?, description=?, status=? WHERE id=?";
        jdbcTemplate.update(sql,
                taskmodel.getTitle(),
                taskmodel.getDescription(),
                taskmodel.getStatus(),
                id
        );
    }

    //  DELETE TASK
    public void deleteTask(int id) {
        String sql = "DELETE FROM tasks WHERE id=?";
        jdbcTemplate.update(sql, id);
    }
}