package com.pravesh.todoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pravesh.todoapp.viewModel.TodoViewModel;

public class MainActivity extends AppCompatActivity {

    FragmentManager fragmentManager;
    Fragment fragment;
    FloatingActionButton floatingActionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        fragment = new ListTodoFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.list_activity_container, fragment)
                .commit();

        floatingActionButton = findViewById(R.id.floatingActionButton);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnu_delete_all:
                ShowAlertCancel("Delete all Todo ?", 1);
                break;
            case R.id.mnu_delete_cpmpleted:
                ShowAlertCancel("Delete all completed Todo ?", 2);
                break;
            case R.id.mnu_logout:
                ShowAlertCancel("Do you want to logout?", 3);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    void ShowAlertCancel(String message, int action) {
        TodoViewModel viewModel = new ViewModelProvider(this).get(TodoViewModel.class);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage(message)
                .setTitle(getString(R.string.app_name))
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String toastMsg = "";
                        switch (action) {
                            case 1:
                                viewModel.deleteAll();
                                toastMsg = "Successfully deleted all Todo!";
                                break;
                            case 2:
                                viewModel.deleteCompleted();
                                toastMsg = "Successfully deleted all completed Todo!";
                                break;
                            case 3:
                                SharedPreferences preferences = getApplicationContext().getSharedPreferences("todo_pref", 0);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.clear();
                                editor.commit();
                                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                                startActivity(intent);
                                finish();
                                toastMsg = "Successfully logged out!";
                                break;
                        }

                        Toast.makeText(getApplicationContext(), toastMsg, Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        alertDialog.show();
    }
}