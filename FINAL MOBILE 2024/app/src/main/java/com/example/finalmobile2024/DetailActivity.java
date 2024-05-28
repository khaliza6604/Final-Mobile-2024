package com.example.finalmobile2024;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
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
    private boolean isFavorite;
    RecyclerView recomdRc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        dbConfig = new DbConfig(this);

        TextView tvTitle = findViewById(R.id.tv_titleBook);
        TextView tvDescription = findViewById(R.id.tv_detailSummary);
        TextView tvPublisher = findViewById(R.id.tv_bookPublisher);
        TextView tvAuthor = findViewById(R.id.tv_bookAuthor);
        TextView tvRank = findViewById(R.id.tv_bookRank);
        ImageView ivImage = findViewById(R.id.img_poster);
        ImageView ivLove = findViewById(R.id.btn_favorite);
        recomdRc = findViewById(R.id.recommendationView);

        BookModel bookModel = getIntent().getParcelableExtra("bookModel");

        if (bookModel != null) {
            tvTitle.setText(bookModel.getBookTitle());
            tvDescription.setText(bookModel.getBookDescription());
            tvPublisher.setText(bookModel.getBookPublisher());
            tvAuthor.setText(bookModel.getBookAuthor());
            tvRank.setText(bookModel.getBookRank());
            Picasso.get().load(bookModel.getBookImage()).into(ivImage);

            int loggedInUserId = getLoggedInUserId();
            isFavorite = dbConfig.isFavorite(loggedInUserId, bookModel.getBookIsbn());

            if (isFavorite) {
                ivLove.setEnabled(false);
                ivLove.setImageResource(R.drawable.love);
            }

            ivLove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isFavorite) {
                        int loggedInUserId = getLoggedInUserId();
                        String bookId = String.valueOf(bookModel.getBookIsbn());
                        saveFavoriteBook(loggedInUserId, bookId);
                        ivLove.setEnabled(false);
                        ivLove.setImageResource(R.drawable.love);
                        Toast.makeText(DetailActivity.this, "Buku ditambahkan ke favorit", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DetailActivity.this, "Buku sudah ada di favorit", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        loadBooks();
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

    private int getLoggedInUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", MODE_PRIVATE);
        return sharedPreferences.getInt("userId", -1);
    }

    private void saveFavoriteBook(int userId, String bookId) {
        dbConfig.insertFavorite(userId, bookId);
    }
}
