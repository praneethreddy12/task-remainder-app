package com.example.taskremainder.Repository;

import com.example.taskremainder.model.Taskmodel;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TaskRepository {

    private  final JdbcTemplate jdbcTemplate;

    public TaskRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //add data

    public void addTask(Taskmodel taskmodel){
        String sql ="INSERT INTO taskmodel(id,title,description,status) VALUES (?,?,?,?)";
        jdbcTemplate.update(sql,
                taskmodel.getId(),
                taskmodel.getTitle(),
                taskmodel.getDescription(),
                taskmodel.getStatus());
    }


    //get data
    public List<Taskmodel> getTask(){
        String sql = "SELECT * FROM taskmodel";

        return jdbcTemplate.query(sql,(rs,rowNums)-> {
            Taskmodel task = new Taskmodel();

            task.setId(rs.getInt("id"));
            task.setTitle(rs.getString("title"));
            task.setDescription(rs.getString("description"));
            task.setStatus(rs.getString("status"));

            return task;
        });
    }

    //update task

    public void updateTask(int id,Taskmodel taskmodel){
        String sql= "UPDATE taskmodel SET title=?,description=?,status=? WHERE id=?";
        jdbcTemplate.update(sql,
                taskmodel.getTitle(),
                taskmodel.getDescription(),
                taskmodel.getStatus(),id);
    }

    //delete data
    public void deleteTask(int id){
        String sql = "DELETE FROM taskmodel WHERE id =?";
        jdbcTemplate.update(sql,id);
    }
}
