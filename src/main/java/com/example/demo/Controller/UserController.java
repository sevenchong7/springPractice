package com.example.demo.Controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Repository.UserRepository;
import com.example.demo.entity.Post;
import com.example.demo.entity.User;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public User createUser(@RequestBody User user) {
        if (user.getPosts() != null) {
            for (Post post : user.getPosts()) {
                post.setUser(user); // 👈 Important: establish the relationship
            }
        }
        return userRepository.save(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody User updatedUserData) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        User existingUser = optionalUser.get();

        // Update user fields
        existingUser.setName(updatedUserData.getName());
        existingUser.setEmail(updatedUserData.getEmail());

        // Clear existing posts (optional - if you're replacing them)
        existingUser.getPosts().clear();

        // Add new/updated posts
        if (updatedUserData.getPosts() != null) {
            for (Post post : updatedUserData.getPosts()) {
                post.setUser(existingUser); // Link post to user
                existingUser.getPosts().add(post); // Add to list
            }
        }

        userRepository.save(existingUser);

        return ResponseEntity.ok(existingUser);
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return "User not found";
        }
        userRepository.deleteById(id);
        return "User deleted successfully";
    }

    // Uncomment the following code if you want to use an in-memory list instead of
    // a database
    // private List<User> users = new ArrayList<>();
    // private Long nextId = 1L;

    // @GetMapping
    // public List<User> getAllUsers() {
    // return users;

    // @GetMapping("/{id}")
    // public User getUserById(@PathVariable Long id) {
    // return users.stream()
    // .filter(user -> user.getId().equals(id))
    // .findFirst()
    // .orElse(null);
    //

    // @PostMapping
    // public User createUser(@Valid @RequestBody User user) {
    // user.setId(nextId++);
    // users.add(user);
    // return user;
    //

    // @PutMapping("/{id}")
    // public User updateUser(@PathVariable Long id, @RequestBody User user) {
    // for (int i = 0; i < users.size(); i++) {
    // if (users.get(i).getId().equals(id)) {
    // user.setId(id);
    // users.set(i, user);
    // return user;
    //
    //
    // return null; // User not found
    //

    // @DeleteMapping("/{id}")
    // public String deleteUser(@PathVariable Long id) {
    // users.removeIf(user -> user.getId().equals(id));
    // return "User deleted successfully";
    //

}
