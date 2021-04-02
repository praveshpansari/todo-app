package com.pravesh.todoapp.model;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.pravesh.todoapp.data.TodoDAO;

import java.util.Calendar;
import java.util.Date;

@Database(entities = {ETodo.class}, version = 1, exportSchema = false)
public abstract class TodoRoomDatabase extends RoomDatabase {
    public abstract TodoDAO mTodoDao();

    public static TodoRoomDatabase INSTANCE;

    public static TodoRoomDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (TodoRoomDatabase.class) {
                if (INSTANCE == null) {
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

    private static class PopulateDbAsync extends AsyncTask<ETodo, Void, Void> {
        private TodoDAO mTodoDAO;

        private PopulateDbAsync(TodoRoomDatabase db) {
            mTodoDAO = db.mTodoDao();
        }

        @Override
        protected Void doInBackground(ETodo... todos) {
            Date date = new Date();
            ETodo todo = new ETodo("Get Milk", "Take milk out from fridge", date, 2, true);
            mTodoDAO.insert(todo);

            todo = new ETodo("MAD Assessment", "Complete MAD component 2", date, 1, false);
            mTodoDAO.insert(todo);

            todo = new ETodo("IS Demo", "Prepare for demo", date, 1, true);
            mTodoDAO.insert(todo);

            todo = new ETodo("Make a Call", "Call the company", date, 3, false);
            mTodoDAO.insert(todo);

            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.DATE, 1);  // number of days to add
            date = c.getTime();
            todo = new ETodo("Grocery", "Buy grocery from market", date, 2, false);
            mTodoDAO.insert(todo);

            todo = new ETodo("Meet John", "Meet John in the shop", date, 3, false);
            mTodoDAO.insert(todo);

            c.setTime(new Date());
            c.add(Calendar.DATE, 5);  // number of days to add
            date = c.getTime();
            todo = new ETodo("IS Exam", "Lab exam of IS", date, 1, false);
            mTodoDAO.insert(todo);

            todo = new ETodo("Cheese", "Buy cheese from store", date, 2, true);
            mTodoDAO.insert(todo);

            todo = new ETodo("MAD Exam", "Prepare for lab exam", date, 1, false);
            mTodoDAO.insert(todo);

            c.setTime(new Date());
            c.add(Calendar.DATE, -6);  // number of days to add
            date = c.getTime();
            todo = new ETodo("MAD Component 1", "Complete Quiz App", date, 3, true);
            mTodoDAO.insert(todo);

            todo = new ETodo("MAD Component 2", "Prepare for assessment", date, 2, false);
            mTodoDAO.insert(todo);

            todo = new ETodo("Milk", "Bring milk from store", date, 2, false);
            mTodoDAO.insert(todo);

            todo = new ETodo("Get Grocery", "Buy grocery from market", date, 1, true);
            mTodoDAO.insert(todo);



            return null;
        }
    }

    private static RoomDatabase.Callback sCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

}
