package com.example.finalmobile2024.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalmobile2024.R;
import com.example.finalmobile2024.adapter.FavoritAdapter;
import com.example.finalmobile2024.api.ApiConfig;
import com.example.finalmobile2024.api.ApiService;
import com.example.finalmobile2024.model.BookModel;
import com.example.finalmobile2024.sqlite.DbConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteFragment extends Fragment {

    private RecyclerView recyclerView;
    private FavoritAdapter favoritAdapter;
    private View favoritLoadingView;
    private DbConfig dbConfig;
    private ApiService service;
    private SharedPreferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        favoritLoadingView = view.findViewById(R.id.home_loading_view);
        recyclerView = view.findViewById(R.id.tc_result);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        service = ApiConfig.getClient().create(ApiService.class);

        dbConfig = new DbConfig(requireActivity());
        preferences = requireActivity().getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        int userId = preferences.getInt("user_id", 0);

        Log.d("FavoriteFragment", String.valueOf(userId));

        Cursor cursor = dbConfig.getFavoriteBooksByUserId(userId);
        Log.d("FavoriteFragment", cursor.moveToFirst() ? "Cursor move to first" : "Cursot not move");
        ArrayList<String> favoritesBookIsbn = new ArrayList<>();
        if (cursor.moveToFirst()) {
            Log.d("FavoriteFragment", cursor.moveToFirst() ? "Cursor move to first" : "Cursot not move");
            do {
                String bookIsbn = cursor.getString(cursor.getColumnIndexOrThrow(DbConfig.COLUMN_BOOK_ID));
                Log.d("FavoriteFragment", bookIsbn);
                favoritesBookIsbn.add(bookIsbn);
            } while (cursor.moveToNext());
        }

        favoritLoadingView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {
                Call<List<BookModel>> call = service.getBookAll();
                call.enqueue(new Callback<List<BookModel>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<BookModel>> call, @NonNull Response<List<BookModel>> response) {
                        if (response.isSuccessful()) {
                            List<BookModel> bookModels = response.body();
                            List<BookModel> favoriteBooks = new ArrayList<>();
                            for (BookModel book : bookModels) {
                                if (favoritesBookIsbn.contains(book.getBookIsbn())) {
//                                    Log.d("FavoriteFragment", book.getBookTitle());
                                    favoriteBooks.add(book);
                                }
                            }
                            favoritAdapter = new FavoritAdapter(getParentFragmentManager(), favoriteBooks, userId);
                            recyclerView.setAdapter(favoritAdapter);

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    favoritLoadingView.setVisibility(View.GONE);
                                    if (favoriteBooks.isEmpty()) {
                                        recyclerView.setVisibility(View.GONE);
                                    } else {
                                        recyclerView.setVisibility(View.VISIBLE);
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<BookModel>> call, @NonNull Throwable t) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                favoritLoadingView.setVisibility(View.GONE);
                            }
                        });
                    }
                });
            }
        });
    }
}

