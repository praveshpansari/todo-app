package com.pravesh.todoapp.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.pravesh.todoapp.model.ETodo;
import com.pravesh.todoapp.model.TodoRoomDatabase;

import java.util.List;

public class TodoRepository {
    private TodoDAO mTodoDAO;
    private LiveData<List<ETodo>> allTodoList;

    public TodoRepository(Application application) {
        TodoRoomDatabase database = TodoRoomDatabase.getDatabase(application);
        mTodoDAO = database.mTodoDao();
        allTodoList = mTodoDAO.getAllTodos();
    }

    public TodoDAO getmTodoDAO() {
        return mTodoDAO;
    }

    public void setmTodoDAO(TodoDAO mTodoDAO) {
        this.mTodoDAO = mTodoDAO;
    }

    public LiveData<List<ETodo>> getAllTodoList() {
        return allTodoList;
    }

    public void setAllTodoList(LiveData<List<ETodo>> allTodoList) {
        this.allTodoList = allTodoList;
    }

    public void insert(ETodo eTodo) {
        new insertTodoAysncTask(mTodoDAO).execute(eTodo);
    }

    public void delete(ETodo eTodo) {
        new deleteTodoAysncTask(mTodoDAO).execute(eTodo);
    }

    public void deleteAll() {
        this.mTodoDAO.deleteAll();
    }

    public void deleteCompleted() {
        this.mTodoDAO.deleteCompleted();
    }

    public ETodo getTodoById(int id) {
        return mTodoDAO.getTodoById(id);
    }

    public void update(ETodo eTodo) {
        new updateTodoAysncTask(mTodoDAO).execute(eTodo);
    }

    private static class insertTodoAysncTask extends AsyncTask<ETodo, Void, Void> {
        private TodoDAO mTodoDao;

        private insertTodoAysncTask(TodoDAO todoDAO) {
            mTodoDao = todoDAO;
        }

        @Override
        protected Void doInBackground(ETodo... eTodos) {
            mTodoDao.insert(eTodos[0]);
            return null;
        }
    }

    private static class deleteTodoAysncTask extends AsyncTask<ETodo, Void, Void> {
        private TodoDAO mTodoDao;

        private deleteTodoAysncTask(TodoDAO todoDAO) {
            mTodoDao = todoDAO;
        }

        @Override
        protected Void doInBackground(ETodo... eTodos) {
            mTodoDao.deleteById(eTodos[0]);
            return null;
        }
    }

    private static class updateTodoAysncTask extends AsyncTask<ETodo, Void, Void> {
        private TodoDAO mTodoDao;

        private updateTodoAysncTask(TodoDAO todoDAO) {
            mTodoDao = todoDAO;
        }

        @Override
        protected Void doInBackground(ETodo... eTodos) {
            mTodoDao.update(eTodos[0]);
            return null;
        }
    }
}
