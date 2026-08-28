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

    //  GET ALL TASKS (with fallback to user's registered email)
    public List<Taskmodel> getTasks() {
        String sql = "SELECT t.id, t.title, t.description, t.status, t.due_date, t.user_id, " +
                     "COALESCE(t.user_email, u.email) AS user_email " +
                     "FROM tasks t LEFT JOIN users u ON t.user_id = u.id";
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
        String sql = "SELECT t.id, t.title, t.description, t.status, t.due_date, t.user_id, " +
                     "COALESCE(t.user_email, u.email) AS user_email " +
                     "FROM tasks t LEFT JOIN users u ON t.user_id = u.id WHERE t.user_id=?";
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
        String sql = "SELECT t.id, t.title, t.description, t.status, t.due_date, t.user_id, " +
                     "COALESCE(t.user_email, u.email) AS user_email " +
                     "FROM tasks t LEFT JOIN users u ON t.user_id = u.id WHERE t.id=?";
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
        String sql = "UPDATE tasks SET title=?, description=?, status=?, due_date=?, user_email=? WHERE id=?";
        jdbcTemplate.update(sql,
                taskmodel.getTitle(),
                taskmodel.getDescription(),
                taskmodel.getStatus(),
                taskmodel.getDueDate(),
                taskmodel.getUserEmail(),
                id
        );
    }

    //  DELETE TASK
    public void deleteTask(int id) {
        String sql = "DELETE FROM tasks WHERE id=?";
        jdbcTemplate.update(sql, id);
    }
}