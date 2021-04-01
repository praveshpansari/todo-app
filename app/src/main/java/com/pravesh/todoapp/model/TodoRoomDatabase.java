package com.pravesh.todoapp.model;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.pravesh.todoapp.data.TodoDAO;

import java.util.Date;

@Database(entities = {ETodo.class}, version = 1, exportSchema = false)
public abstract class TodoRoomDatabase extends RoomDatabase {
    public abstract TodoDAO mTodoDao();

    public static TodoRoomDatabase INSTANCE;
    public static TodoRoomDatabase getDatabase(Context context){
        if(INSTANCE == null) {
            synchronized (TodoRoomDatabase.class){
                if(INSTANCE ==null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TodoRoomDatabase.class,
                            "todo.db")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .addCallback(sCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static class PopulateDbAsync extends AsyncTask<ETodo,Void,Void> {
        private  TodoDAO mTodoDAO;
        private PopulateDbAsync(TodoRoomDatabase db)
        {
            mTodoDAO=db.mTodoDao();
        }

        @Override
        protected Void doInBackground(ETodo... todos) {
            Date date=new Date();
            ETodo todo= new ETodo("Demo title","Demo Description",date,1,false);
            mTodoDAO.insert(todo);
            return null;
        }
    }

    private static RoomDatabase.Callback sCallback=new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

}
