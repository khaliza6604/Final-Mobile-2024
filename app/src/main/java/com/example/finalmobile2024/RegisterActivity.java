package com.example.finalmobile2024;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalmobile2024.sqlite.DbConfig;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText et_username;
    TextInputEditText et_password;
    Button btn_register;
    DbConfig dbConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbConfig = new DbConfig(this);

        et_username = findViewById(R.id.username2);
        et_password = findViewById(R.id.password2);
        btn_register = findViewById(R.id.btnRegister);

        btn_register.setOnClickListener(v -> {
            String username = et_username.getText().toString().trim();
            String password = et_password.getText().toString().trim();

            if (!username.isEmpty() && !password.isEmpty()) {
                if (dbConfig.isUsernameExists(username)) {
                    et_username.setError("Username already exists");
                    Toast.makeText(RegisterActivity.this, "Username already exists. Please use a different Username.", Toast.LENGTH_SHORT).show();
                } else {
                    dbConfig.insertUserData(username, password);
                    Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } else {
                Toast.makeText(RegisterActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
