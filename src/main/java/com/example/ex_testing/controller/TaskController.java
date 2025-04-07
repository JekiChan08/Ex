package com.example.ex_testing.controller;

import com.example.ex_testing.DTO.TaskResponse;
import com.example.ex_testing.Model.Task;
import com.example.ex_testing.Model.User;
import com.example.ex_testing.Repositories.RoleRepository;
import com.example.ex_testing.Service.Impl.TaskServiceImpl;
import com.example.ex_testing.Service.Impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskServiceImpl taskService;
    private final UserServiceImpl userService;
    private final RoleRepository roleRepository;

    @PostMapping
    public ResponseEntity<Task> createTask(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("status") String status,
            @RequestParam("project_id") Long projectId,
            @RequestParam(value = "assigned_user_id", required = false) Long assignedUserId
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User user = userService.findByLogin(currentUserName);

        if (!status.equals("TODO") && !status.equals("IN_PROGRESS") && !status.equals("DONE")) {
            return ResponseEntity.badRequest().build();
        }

        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setStatus(status);
        task.setCreatedAt(LocalDateTime.now());
        task.setProjectId(projectId);

        taskService.save(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User user = userService.findByLogin(currentUserName);

        Task task = taskService.getById(id);
        if (task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (!user.getRoles().contains(roleRepository.findById(2L).get()) && !task.getAssignedUserId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        TaskResponse response = new TaskResponse(task);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/")
    public ResponseEntity<List<Task>> getAllTasks() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User user = userService.findByLogin(currentUserName);

        List<Task> tasks;
        if (user.getRoles().contains(roleRepository.findById(2L).get())) {
            tasks = taskService.getAll();
        } else {
            tasks = taskService.findByAssignedUserId(user.getId());
        }

        if (tasks.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(tasks);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long id,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("status") String status
    ) {
        Task task = taskService.getById(id);
        if (task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (!status.equals("TODO") && !status.equals("IN_PROGRESS") && !status.equals("DONE")) {
            return ResponseEntity.badRequest().build();
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User user = userService.findByLogin(currentUserName);

        if (!user.getRoles().contains(roleRepository.findById(2L).get()) && !task.getAssignedUserId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        task.setTitle(title);
        task.setDescription(description);
        task.setStatus(status);
        task.setUpdatedAt(LocalDateTime.now());

        taskService.save(task);
        return ResponseEntity.ok(new TaskResponse(task));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        Task task = taskService.getById(id);
        if (task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User user = userService.findByLogin(currentUserName);

        if (!user.getRoles().contains(roleRepository.findById(2L).get()) && !task.getAssignedUserId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
