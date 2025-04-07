package com.example.ex_testing.controller;

import com.example.ex_testing.DTO.ProjectResponse;
import com.example.ex_testing.Model.Project;
import com.example.ex_testing.Model.User;
import com.example.ex_testing.Repositories.RoleRepository;
import com.example.ex_testing.Service.Impl.ProjectServiceImpl;
import com.example.ex_testing.Service.Impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final UserServiceImpl userService;
    private final ProjectServiceImpl projectService;
    private final RoleRepository roleRepository;

    @PostMapping(value = "/admin/create", consumes = {"multipart/form-data"})
    public ResponseEntity<Project> createProject(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("user_id") Long userId) throws IOException {

        Project project = new Project();
        project.setName(name);
        project.setDescription(description);
        project.setUserId(userService.getById(userId).getId());

        project.setCreatedAt(LocalDateTime.now());
        if (userService.getById(userId) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        projectService.save(project);
        Project savedProject = projectService.getById(project.getId());
        return ResponseEntity.ok(savedProject);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getById(
            @PathVariable Long id
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = userService.findByLogin(currentPrincipalName);
        if (!projectService.getById(id).getUserId().equals(user.getId()) ) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Project project = projectService.getById(id);

        if (project == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        ProjectResponse response = new ProjectResponse(project);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/")
    public ResponseEntity<List<Project>> getAllProjects() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = userService.findByLogin(currentPrincipalName);
        if (user.getRoles().contains(roleRepository.findById(2L).get())) {
            List<Project> projects = projectService.getAll();
            if (projects.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(projects);
        } else {
            List<Project> projects = projectService.findByUserId(user.getId());
            if (projects.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(projects);
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponse> editProject(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam("description") String description
    ) {
        Project project = projectService.getById(id);
        if (name != null) {
            project.setName(name);
        }
        if (description != null) {
            project.setDescription(description);
        }

        if (project == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        project.setUpdatedAt(LocalDateTime.now());
        projectService.save(project);
        ProjectResponse response = new ProjectResponse(project);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = userService.findByLogin(currentPrincipalName);

        if (user.getRoles().contains(roleRepository.findById(2L).get()) || projectService.getById(id).getUserId().equals(user.getId())) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = userService.findByLogin(currentPrincipalName);
        if (user.getRoles().contains(roleRepository.findById(2L).get()) || projectService.getById(id).getUserId().equals(user.getId())) {
            projectService.delete(id);
            return ResponseEntity.noContent().build();
        }else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
