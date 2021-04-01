package com.pravesh.todoapp;

public class GroupItem extends ListItem {

    private String group;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    @Override
    public int getType() {
        return TYPE_GROUP;
    }
}
