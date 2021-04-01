package com.pravesh.todoapp.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.pravesh.todoapp.model.ETodo;

import java.util.List;

@Dao
public interface TodoDAO {
    @Insert
    void insert(ETodo todo);

    @Query("DELETE FROM todo_table")
    void deleteAll();

    @Delete
    void deleteById(ETodo todo);

    @Query("SELECT * FROM todo_table WHERE id=:id")
    ETodo getTodoById(int id);

    @Query("DELETE FROM todo_table WHERE is_completed = 1")
    void deleteCompleted();

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(ETodo... todo);

    @Query("SELECT * FROM todo_table ORDER BY todo_date, priority desc")
    LiveData<List<ETodo>> getAllTodos();


}
