package com.pravesh.todoapp;

import com.pravesh.todoapp.model.ETodo;

public class TodoItem extends ListItem {

    private ETodo eTodo;

    public ETodo getEtodo() {
        return eTodo;
    }

    public void seteTodo(ETodo eTodo) {
        this.eTodo = eTodo;
    }

    @Override
    public int getType() {
        return TYPE_TODO;
    }
}
