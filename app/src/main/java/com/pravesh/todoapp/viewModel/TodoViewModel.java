package com.pravesh.todoapp.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.pravesh.todoapp.data.TodoRepository;
import com.pravesh.todoapp.model.ETodo;

import java.util.List;

public class TodoViewModel extends AndroidViewModel {
    private TodoRepository mTodoRepository;
    private LiveData<List<ETodo>> allTodos;

    public TodoViewModel(@NonNull Application application) {
        super(application);
        mTodoRepository = new TodoRepository(application);
        allTodos = mTodoRepository.getAllTodoList();
    }

    public LiveData<List<ETodo>> getAllTodos() {
        return allTodos;
    }

    public void insert(ETodo todo) {
        mTodoRepository.insert(todo);
    }

    public void deleteById(ETodo todo) {
        mTodoRepository.delete(todo);
    }

    public void deleteAll() {
        mTodoRepository.deleteAll();
    }

    public void deleteCompleted() {
        mTodoRepository.deleteCompleted();
    }

    public ETodo getTodoById(int id) {
        return mTodoRepository.getTodoById(id);
    }

    public void update(ETodo eTodo) {
        mTodoRepository.update(eTodo);
    }
}
