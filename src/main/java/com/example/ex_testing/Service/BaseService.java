package com.example.ex_testing.Service;

import java.util.List;

public interface BaseService<T, ID> {
    List<T> getAll();
    T getById(ID id);
    T save(T entity);
    void delete(ID id);
}

