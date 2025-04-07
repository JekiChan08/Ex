package com.example.ex_testing.Service.Impl;

import com.example.ex_testing.Model.Project;
import com.example.ex_testing.Repositories.ProjectRepository;
import com.example.ex_testing.Repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl extends BaseServiceImpl<Project, Long> {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository, UserRepository userRepository) {
        super(projectRepository);
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }
    public List<Project> findByUserId(Long userId) {
        return projectRepository.findByUserId(userId);
    }

}
