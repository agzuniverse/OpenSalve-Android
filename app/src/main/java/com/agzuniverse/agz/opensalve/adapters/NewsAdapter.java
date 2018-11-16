package com.agzuniverse.agz.opensalve.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.agzuniverse.agz.opensalve.Modals.News;
import com.agzuniverse.agz.opensalve.R;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<News> data;
    private LayoutInflater inflater;
    private int[] colors;

    public NewsAdapter(Context context, List<News> data, int[] colors) {
        this.data = data;
        this.colors = colors;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.news_card, viewGroup, false);
        NewsViewHolder holder = new NewsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder viewHolder, int i) {
        News current = data.get(i);
        viewHolder.body.setText(current.getBody());
        viewHolder.author.setText(current.getAuthor());
        viewHolder.main.setCardBackgroundColor(colors[(int) (Math.random() * colors.length)]);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView body, author;
        CardView main;

        private NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            main = itemView.findViewById(R.id.card_background);
            body = itemView.findViewById(R.id.news_body);
            author = itemView.findViewById(R.id.news_author);
        }
    }
}
