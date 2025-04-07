package com.example.ex_testing.DTO;

import com.example.ex_testing.Model.Task;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TaskResponse {
    private Task task;
}
