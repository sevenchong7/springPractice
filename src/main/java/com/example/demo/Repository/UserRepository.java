package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    // This interface will automatically provide CRUD operations for User entity
    // No additional code is needed here as JpaRepository provides all necessary
    // methods
}
