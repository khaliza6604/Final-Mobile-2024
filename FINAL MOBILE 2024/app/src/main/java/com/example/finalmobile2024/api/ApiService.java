package com.example.finalmobile2024.api;

import com.example.finalmobile2024.model.BookModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    String RAPID_API_KEY = "d888030510msh25d66c429b67cc9p110291jsn5bbac3228665";
    String RAPID_API_HOST = "all-books-api.p.rapidapi.com";

    @Headers({
            "X-RapidAPI-Key: " + RAPID_API_KEY,
            "X-RapidAPI-Host: " + RAPID_API_HOST
    })
    @GET("getBooks")
    Call<List<BookModel>> getBookAll();

    @Headers({
            "X-RapidAPI-Key: " + RAPID_API_KEY,
            "X-RapidAPI-Host: " + RAPID_API_HOST
    })
    @GET("title/{title}")
    Call<BookModel> getBookByTitle(@Path("title") String title);
}
