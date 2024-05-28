package com.example.finalmobile2024;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalmobile2024.sqlite.DbConfig;

public class EditUserActivity extends AppCompatActivity {

    EditText etFullName, etPhone, etAddress;
    RadioButton rbtnFemale, rbtnMale;
    CheckBox cbFantasy, cbRomance, cbHorror;
    Button btnSave;

    DbConfig dbConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        dbConfig = new DbConfig(this);

        etFullName = findViewById(R.id.et_fullname);
        etPhone = findViewById(R.id.et_phone);
        etAddress = findViewById(R.id.et_address);
        rbtnFemale = findViewById(R.id.rbtn_female);
        rbtnMale = findViewById(R.id.rbtn_male);
        cbFantasy = findViewById(R.id.cb_fansty);
        cbRomance = findViewById(R.id.cb_romance);
        cbHorror = findViewById(R.id.cb_horror);
        btnSave = findViewById(R.id.btn_save);

        loadUserData();

        btnSave.setOnClickListener(v -> saveUserData());
    }

    private void loadUserData() {
        SQLiteDatabase db = dbConfig.getReadableDatabase();
        Cursor cursor = db.query(
                DbConfig.TABLE_NAME,
                new String[]{DbConfig.COLUMN_FULLNAME, DbConfig.COLUMN_PHONE, DbConfig.COLUMN_ADDRESS, DbConfig.COLUMN_GENDER, DbConfig.COLUMN_GENRE},
                DbConfig.COLUMN_IS_LOGGED_IN + " = ?",
                new String[]{"1"},
                null, null, null);

        if (cursor.moveToFirst()) {
            String fullName = cursor.getString(cursor.getColumnIndexOrThrow(DbConfig.COLUMN_FULLNAME));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow(DbConfig.COLUMN_PHONE));
            String address = cursor.getString(cursor.getColumnIndexOrThrow(DbConfig.COLUMN_ADDRESS));
            String gender = cursor.getString(cursor.getColumnIndexOrThrow(DbConfig.COLUMN_GENDER));
            String genres = cursor.getString(cursor.getColumnIndexOrThrow(DbConfig.COLUMN_GENRE));

            etFullName.setText(fullName);
            etPhone.setText(phone);
            etAddress.setText(address);

            if (gender.equals("Female")) {
                rbtnFemale.setChecked(true);
            } else {
                rbtnMale.setChecked(true);
            }

            String[] genreArray = genres.split(", ");
            for (String genre : genreArray) {
                switch (genre) {
                    case "Fantasy":
                        cbFantasy.setChecked(true);
                        break;
                    case "Romance":
                        cbRomance.setChecked(true);
                        break;
                    case "Horror":
                        cbHorror.setChecked(true);
                        break;
                }
            }
        }

        cursor.close();
        db.close();
    }

    private void saveUserData() {
        String fullName = etFullName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String gender = rbtnFemale.isChecked() ? "Female" : "Male";

        StringBuilder genres = new StringBuilder();
        if (cbFantasy.isChecked()) {
            genres.append("Fantasy, ");
        }
        if (cbRomance.isChecked()) {
            genres.append("Romance, ");
        }
        if (cbHorror.isChecked()) {
            genres.append("Horror, ");
        }

        String selectedGenres = genres.toString().trim();
        if (selectedGenres.endsWith(", ")) {
            selectedGenres = selectedGenres.substring(0, selectedGenres.length() - 2);
        }

        if (!fullName.isEmpty() && !phone.isEmpty() && !address.isEmpty()) {
            saveUserData(fullName, phone, address, gender, selectedGenres);
        } else {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveUserData(String name, String number, String address, String gender, String selectedGenres) {
        SQLiteDatabase db = dbConfig.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbConfig.COLUMN_FULLNAME, name);
        values.put(DbConfig.COLUMN_PHONE, number);
        values.put(DbConfig.COLUMN_ADDRESS, address);
        values.put(DbConfig.COLUMN_GENDER, gender);
        values.put(DbConfig.COLUMN_GENRE, selectedGenres);

        long result = db.update(DbConfig.TABLE_NAME, values, DbConfig.COLUMN_IS_LOGGED_IN + " = ?", new String[]{"1"});
        db.close();

        if (result != -1) {
            Toast.makeText(this, "User data saved successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to save user data", Toast.LENGTH_SHORT).show();
        }
    }
}
