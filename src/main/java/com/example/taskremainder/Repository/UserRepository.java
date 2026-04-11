package com.example.taskremainder.Repository;

import com.example.taskremainder.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

   User findByEmail(String email);

   User findByVerificationToken(String token);
}