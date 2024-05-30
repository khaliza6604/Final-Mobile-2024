package com.example.finalmobile2024.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.finalmobile2024.EditUserActivity;
import com.example.finalmobile2024.LoginActivity;
import com.example.finalmobile2024.R;
import com.example.finalmobile2024.sqlite.DbConfig;

public class ProfileFragment extends Fragment {

    private TextView tv_fullname, tv_gender, tv_phone, tv_address, tv_genre, tv_username;
    private Button btn_logout, btn_edit;
    private ImageView iv_delete;
    private DbConfig dbConfig;
    private int recordId;

    private final ActivityResultLauncher<Intent> editUserLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == getActivity().RESULT_OK) {
                    // Update user data
                    loadUserData();
                }
            }
    );

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout untuk fragment ini
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbConfig = new DbConfig(getContext());


        tv_username = view.findViewById(R.id.tv_username);
        tv_fullname = view.findViewById(R.id.tv_fullname);
        tv_gender = view.findViewById(R.id.tv_gender);
        tv_phone = view.findViewById(R.id.tv_phone);
        tv_address = view.findViewById(R.id.tv_address);
        tv_genre = view.findViewById(R.id.tv_genre);

        btn_logout = view.findViewById(R.id.btn_logout);
        btn_edit = view.findViewById(R.id.btn_edit);
        iv_delete = view.findViewById(R.id.btn_delete);

        loadUserData();

        btn_edit.setOnClickListener(v -> editUserLauncher.launch(new Intent(getActivity(), EditUserActivity.class)));

        btn_logout.setOnClickListener(v -> logoutUser());

        iv_delete.setOnClickListener(v -> showDeleteConfirmationDialog());
    }

    @Override
    public void onResume() {
        super.onResume();
        // Memuat ulang data pengguna setiap kali fragment ini aktif kembali
        loadUserData();
    }

    private void loadUserData() {
        try (SQLiteDatabase db = dbConfig.getReadableDatabase();
             Cursor cursor = db.query(
                     DbConfig.TABLE_NAME,
                     new String[]{DbConfig.COLUMN_ID, DbConfig.COLUMN_USERNSME, DbConfig.COLUMN_FULLNAME, DbConfig.COLUMN_GENDER, DbConfig.COLUMN_PHONE, DbConfig.COLUMN_ADDRESS, DbConfig.COLUMN_GENRE},
                     DbConfig.COLUMN_IS_LOGGED_IN + " = ?",
                     new String[]{"1"},
                     null, null, null)) {

            if (cursor.moveToFirst() && isAdded()) {
                recordId = cursor.getInt(cursor.getColumnIndexOrThrow(DbConfig.COLUMN_ID));
                String username = cursor.getString(cursor.getColumnIndexOrThrow(DbConfig.COLUMN_USERNSME));
                String fullname = cursor.getString(cursor.getColumnIndexOrThrow(DbConfig.COLUMN_FULLNAME));
                String gender = cursor.getString(cursor.getColumnIndexOrThrow(DbConfig.COLUMN_GENDER));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow(DbConfig.COLUMN_PHONE));
                String address = cursor.getString(cursor.getColumnIndexOrThrow(DbConfig.COLUMN_ADDRESS));
                String genre = cursor.getString(cursor.getColumnIndexOrThrow(DbConfig.COLUMN_GENRE));

                tv_username.setText(username);
                tv_fullname.setText(fullname);
                tv_gender.setText(gender);
                tv_phone.setText(phone);
                tv_address.setText(address);
                tv_genre.setText(genre);
            }
        }
    }

    private void logoutUser() {
        try (SQLiteDatabase db = dbConfig.getWritableDatabase()) {
            dbConfig.updateRecord(recordId, 0); // Mengubah status pengguna menjadi logged out
        }

        if (isAdded()) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        }
    }

    private void showDeleteConfirmationDialog() {
        if (!isAdded()) return;

        new AlertDialog.Builder(getActivity())
                .setTitle("Hapus Akun")
                .setMessage("Apakah anda yakin ingin menghapus akun ini?")
                .setPositiveButton("Ya", (dialog, which) -> {
                    dbConfig.deleteRecord(recordId);
                    logoutUser();
                })
                .setNegativeButton("Tidak", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }
}
