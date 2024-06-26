package com.example.finalmobile2024;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalmobile2024.sqlite.DbConfig;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText et_username;
    TextInputEditText et_password;
    Button btn_login;
    TextView tv_register;
    DbConfig dbConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbConfig = new DbConfig(this);

        et_username = findViewById(R.id.username);
        et_password = findViewById(R.id.password);
        btn_login = findViewById(R.id.btnLogin);
        tv_register = findViewById(R.id.register);

        tv_register.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        btn_login.setOnClickListener(v -> {
            String nim = et_username.getText().toString().trim();
            String password = et_password.getText().toString().trim();

            if (nim.isEmpty()) {
                et_username.setError("Please enter your Username");
            } else if (password.isEmpty()) {
                et_password.setError("Please enter your password");
            } else {
                login(nim, password);
            }
        });
    }

    private void login(String username, String password) {
        try (SQLiteDatabase db = dbConfig.getReadableDatabase();
             Cursor cursor = db.query(
                     DbConfig.TABLE_NAME,
                     new String[]{DbConfig.COLUMN_ID},
                     DbConfig.COLUMN_USERNSME + "=? AND " + DbConfig.COLUMN_PASSWORD + "=?",
                     new String[]{username, password},
                     null, null, null)) {

            if (cursor != null && cursor.moveToFirst()) {
                int idColumnIndex = cursor.getColumnIndex(DbConfig.COLUMN_ID);
                if (idColumnIndex != -1) {
                    int userId = cursor.getInt(idColumnIndex);
                    loginSuccess(userId); // Menyimpan ID pengguna yang berhasil login
                    updateLoginStatus(username, true);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }
            } else {
                Toast.makeText(this, "Incorrect Username or Password", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateLoginStatus(String nim, boolean isLoggedIn) {
        try (SQLiteDatabase db = dbConfig.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(DbConfig.COLUMN_IS_LOGGED_IN, isLoggedIn ? 1 : 0);
            db.update(DbConfig.TABLE_NAME, values, DbConfig.COLUMN_USERNSME + " = ?", new String[]{nim});
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkLoginStatus();
    }

    private void checkLoginStatus() {
        try (SQLiteDatabase db = dbConfig.getReadableDatabase();
             Cursor cursor = db.query(
                     DbConfig.TABLE_NAME,
                     new String[]{DbConfig.COLUMN_ID},
                     DbConfig.COLUMN_IS_LOGGED_IN + " = ?",
                     new String[]{"1"},
                     null, null, null)) {

            if (cursor.getCount() > 0) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        }
    }

    // Metode untuk menyimpan ID pengguna yang berhasil login ke SharedPreferences
    private void loginSuccess(int userId) {
        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("user_id", userId);
        editor.apply();
    }
}
