package com.example.finalmobile2024.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.finalmobile2024.R;
import com.example.finalmobile2024.adapter.BookAdapter;
import com.example.finalmobile2024.adapter.CategoryAdapter;
import com.example.finalmobile2024.adapter.SliderAdapters;
import com.example.finalmobile2024.api.ApiConfig;
import com.example.finalmobile2024.api.ApiService;
import com.example.finalmobile2024.model.BookModel;
import com.example.finalmobile2024.model.SliderItems;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView, recyclerView2;
    private BookAdapter bookAdapter;
    private View homeLoadingView2, homeLoadingView3;
    private ViewPager2 viewPager2;
    private Handler slideHandler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.rc_view1);
        recyclerView2 = view.findViewById(R.id.rc_view2);
        viewPager2 = view.findViewById(R.id.viewpagerSlider);
        homeLoadingView2 = view.findViewById(R.id.home_loading_view2);
        homeLoadingView3 = view.findViewById(R.id.home_loading_view3);

        loadBooks();
        loadBooks2();
    }


    private void loadBooks() {
        homeLoadingView2.setVisibility(View.VISIBLE);
        ApiService apiService = ApiConfig.getClient().create(ApiService.class);
        Call<List<BookModel>> call = apiService.getBookAll();
        call.enqueue(new Callback<List<BookModel>>() {
            @Override
            public void onResponse(Call<List<BookModel>> call, Response<List<BookModel>> response) {
                homeLoadingView2.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    List<BookModel> bookModels = response.body();
                    List<BookModel> firstFiveBooks = bookModels.size() > 5 ? bookModels.subList(0, 5) : bookModels;
                    bookAdapter = new BookAdapter(firstFiveBooks, getContext());
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                    recyclerView.setAdapter(bookAdapter);
                    setupBanners();
                } else {
                    Toast.makeText(getContext(), "Failed to load books", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<BookModel>> call, Throwable t) {
                homeLoadingView2.setVisibility(View.GONE);
                Toast.makeText(getContext(), "An error occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadBooks2() {
        homeLoadingView3.setVisibility(View.VISIBLE);
        ApiService apiService = ApiConfig.getClient().create(ApiService.class);
        Call<List<BookModel>> call = apiService.getBookAll();
        call.enqueue(new Callback<List<BookModel>>() {
            @Override
            public void onResponse(Call<List<BookModel>> call, Response<List<BookModel>> response) {
                homeLoadingView3.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    List<BookModel> bookModels = response.body();
                    bookAdapter = new BookAdapter(bookModels, getContext());
                    recyclerView2.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                    recyclerView2.setAdapter(bookAdapter);
                } else {
                    Toast.makeText(getContext(), "Failed to load books", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<BookModel>> call, Throwable t) {
                homeLoadingView3.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setupBanners() {
        List<SliderItems> sliderItems = new ArrayList<>();
        sliderItems.add(new SliderItems(R.drawable.book1));
        sliderItems.add(new SliderItems(R.drawable.book2));
        sliderItems.add(new SliderItems(R.drawable.book3));

        viewPager2.setAdapter(new SliderAdapters(sliderItems, viewPager2));
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });

        viewPager2.setPageTransformer(compositePageTransformer);
        viewPager2.setCurrentItem(1);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                slideHandler.removeCallbacks(sliderRunnable);
                slideHandler.postDelayed(sliderRunnable, 3000);
            }
        });
    }

    private final Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        slideHandler.removeCallbacks(sliderRunnable);
    }
}
