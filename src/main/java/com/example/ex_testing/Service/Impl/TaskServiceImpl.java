package com.example.ex_testing.Service.Impl;


import com.example.ex_testing.Model.Task;
import com.example.ex_testing.Repositories.TaskRepository;
import com.example.ex_testing.Repositories.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl extends BaseServiceImpl<Task, Long> {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;


    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository, TaskRepository taskRepository1) {
        super(taskRepository);
        this.userRepository = userRepository;
        this.taskRepository = taskRepository1;
    }
    public List<Task> findByAssignedUserId(Long id) {
        return taskRepository.findByAssignedUserId(id);
    }
}
