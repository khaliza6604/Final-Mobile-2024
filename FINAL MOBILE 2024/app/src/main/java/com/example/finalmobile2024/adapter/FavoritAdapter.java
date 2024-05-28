package com.example.finalmobile2024.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalmobile2024.DetailActivity;
import com.example.finalmobile2024.R;
import com.example.finalmobile2024.model.BookModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavoritAdapter extends RecyclerView.Adapter<FavoritAdapter.ViewHolder> {

    private final FragmentManager fragmentManager;
    private List<BookModel> bookModels;
    private Context context;
    private final int userId;

    public FavoritAdapter(FragmentManager fragmentManager, List<BookModel> bookModelList, int userId) {
        this.fragmentManager = fragmentManager;
        this.bookModels = bookModelList;
        this.userId = userId;
    }

    @NonNull
    @Override
    public FavoritAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favotitebook_holder, parent, false);
        return new FavoritAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritAdapter.ViewHolder holder, int position) {
        BookModel bookModel = bookModels.get(position);
        holder.bind(bookModel, userId);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("bookModel", bookModel);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return bookModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_foto;
        TextView tv_title, tv_publisher, tv_author, tv_rank;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_foto = itemView.findViewById(R.id.img_cover);
            tv_title = itemView.findViewById(R.id.tv_titleBookitle);
            tv_publisher = itemView.findViewById(R.id.tv_publisher);
            tv_author = itemView.findViewById(R.id.tv_author);
            tv_rank = itemView.findViewById(R.id.tv_bookRank);
        }

        public void bind(BookModel bookModel, int userId) {
            Picasso.get().load(bookModel.getBookImage()).into(iv_foto);
            tv_title.setText(bookModel.getBookTitle());
            tv_publisher.setText(bookModel.getBookPublisher());
            tv_author.setText(bookModel.getBookAuthor());
            tv_rank.setText(bookModel.getBookRank());
        }
    }
}
