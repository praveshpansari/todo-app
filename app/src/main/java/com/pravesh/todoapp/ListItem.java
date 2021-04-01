package com.pravesh.todoapp;

public abstract class ListItem {
    public static final int TYPE_GROUP = 0;
    public static final int TYPE_TODO = 1;

    abstract public int getType();
}

