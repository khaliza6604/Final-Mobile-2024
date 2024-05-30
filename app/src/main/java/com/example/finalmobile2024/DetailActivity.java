package com.example.finalmobile2024;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalmobile2024.adapter.BookAdapter;
import com.example.finalmobile2024.api.ApiConfig;
import com.example.finalmobile2024.api.ApiService;
import com.example.finalmobile2024.model.BookModel;
import com.example.finalmobile2024.sqlite.DbConfig;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    private DbConfig dbConfig;
    private BookAdapter bookAdapter;
    private ImageView btn_back;
    private Button btn_roadMore;
    private ImageView ivLove;
    private boolean isFavorite;
    private RecyclerView recomdRc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        dbConfig = new DbConfig(this);

        String link = getIntent().getStringExtra("link");

        TextView tvTitle = findViewById(R.id.tv_titleBook);
        TextView tvDescription = findViewById(R.id.tv_detailSummary);
        TextView tvPublisher = findViewById(R.id.tv_bookPublisher);
        TextView tvAuthor = findViewById(R.id.tv_bookAuthor);
        TextView tvRank = findViewById(R.id.tv_bookRank);
        ImageView ivImage = findViewById(R.id.img_poster);
        ivLove = findViewById(R.id.btn_favorite);
        btn_back = findViewById(R.id.btn_back);
        recomdRc = findViewById(R.id.recommendationView);
        btn_roadMore = findViewById(R.id.btn_readMore);


        btn_back.setOnClickListener(v -> {
            finish();
        });

        ivLove.setOnClickListener(v -> {
            toggleFavorite();
        });

        btn_roadMore.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(link));
            startActivity(intent);
        });

        BookModel bookModel = getIntent().getParcelableExtra("bookModel");
        if (bookModel != null) {
            displayBookDetails(bookModel);
            int loggedInUserId = getLoggedInUserId();
            isFavorite = isBookFavorite(loggedInUserId, bookModel.getBookIsbn());
            updateFavoriteIcon();
        }

        loadBooks();
    }

    private void displayBookDetails(BookModel bookModel) {
        TextView tvTitle = findViewById(R.id.tv_titleBook);
        TextView tvDescription = findViewById(R.id.tv_detailSummary);
        TextView tvPublisher = findViewById(R.id.tv_bookPublisher);
        TextView tvAuthor = findViewById(R.id.tv_bookAuthor);
        TextView tvRank = findViewById(R.id.tv_bookRank);
        ImageView ivImage = findViewById(R.id.img_poster);

        tvTitle.setText(bookModel.getBookTitle());
        tvDescription.setText(bookModel.getBookDescription());
        tvPublisher.setText(bookModel.getBookPublisher());
        tvAuthor.setText(bookModel.getBookAuthor());
        tvRank.setText(bookModel.getBookRank());
        Picasso.get().load(bookModel.getBookImage()).into(ivImage);
    }

    private void toggleFavorite() {
        int loggedInUserId = getLoggedInUserId();
        BookModel bookModel = getIntent().getParcelableExtra("bookModel");

        if (bookModel != null) {
            if (isFavorite) {
                dbConfig.deleteFavorite(loggedInUserId, bookModel.getBookIsbn());
                ivLove.setImageResource(R.drawable.fav);
                Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show();
            } else {
                dbConfig.insertFavorite(loggedInUserId, bookModel.getBookIsbn());
                ivLove.setImageResource(R.drawable.love);
                Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show();
            }
            isFavorite = !isFavorite;
        }
    }

    private void loadBooks() {
        ApiService apiService = ApiConfig.getClient().create(ApiService.class);
        Call<List<BookModel>> call = apiService.getBookAll();
        call.enqueue(new Callback<List<BookModel>>() {
            @Override
            public void onResponse(Call<List<BookModel>> call, Response<List<BookModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<BookModel> bookModels = response.body();
                    bookAdapter = new BookAdapter(bookModels, DetailActivity.this);
                    recomdRc.setLayoutManager(new LinearLayoutManager(DetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
                    recomdRc.setAdapter(bookAdapter);
                } else {
                    Toast.makeText(DetailActivity.this, "Failed to load books", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<BookModel>> call, Throwable t) {
                Toast.makeText(DetailActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateFavoriteIcon() {
        if (isFavorite) {
            ivLove.setImageResource(R.drawable.love);
        } else {
            ivLove.setImageResource(R.drawable.fav);
        }
    }

    private boolean isBookFavorite(int userId, String bookIsbn) {
        Cursor cursor = dbConfig.getFavoriteBooksByUserId(userId);
        boolean isFavorite = false;
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String isbn = cursor.getString(cursor.getColumnIndexOrThrow(DbConfig.COLUMN_BOOK_ID));
                if (isbn.equals(bookIsbn)) {
                    isFavorite = true;
                    break;
                }
            }
            cursor.close();
        }
        return isFavorite;
    }

    private int getLoggedInUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", MODE_PRIVATE);
        return sharedPreferences.getInt("user_id", -1);
    }
}
