package com.pravesh.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    EditText username;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.getSupportActionBar().hide();
        btnLogin = findViewById(R.id.login_activity_btn_login);
        username = findViewById(R.id.userName);
        password = findViewById(R.id.password);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (username.getText().toString().equals("") || username.getText().toString().equals("")) {
                    Toast toast = Toast.makeText(LoginActivity.this, "Please check your details and try again!", Toast.LENGTH_SHORT);
                    toast.show();
                } else {

                    SharedPreferences preference = getApplicationContext().getSharedPreferences("todo_pref", 0);
                    SharedPreferences.Editor editor = preference.edit();
                    editor.putBoolean("authentication", true);
                    editor.commit();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}