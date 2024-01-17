package com.reflect.reflect.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reflect.reflect.R;
import com.reflect.reflect.databinding.LytNewsBinding;
import com.reflect.reflect.model.Article;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private Context context;

    public void setArticles(List<Article> articles) {
        this.articles = articles;
        notifyDataSetChanged();
    }

    private List<Article> articles=new ArrayList<>();

    public NewsAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        LytNewsBinding binding = LytNewsBinding.inflate(layoutInflater, parent, false);
        return new NewsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        Article article = articles.get(position);
        holder.bind(article);
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    class NewsViewHolder extends RecyclerView.ViewHolder {

        private LytNewsBinding binding;

        NewsViewHolder(LytNewsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Open URL in an external browser when an item is clicked
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Article article = articles.get(position);
                        openUrlInBrowser(article.getUrl());
                    }
                }
            });
        }

        void bind(Article article) {

            binding.txtDate.setText(formatDate(article.getPublishedAt()));
            binding.txtName.setText(article.getTitle());
            binding.txtDescription.setText(article.getDescription());
            if(article.getUrlToImage()!=null)
            {
                binding.imageView.setVisibility(View.VISIBLE);
                Picasso.get().load(article.getUrlToImage()).into(binding.imageView);
            }
            else
            {
                binding.imageView.setVisibility(View.GONE);
            }
        }
    }


    String formatDate(String date)
    {

        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());

            Date parse = inputFormat.parse(date);
            String formattedDate = new SimpleDateFormat("d' 'MMMM',' yyyy",Locale.ENGLISH).format(parse);
//            formattedDate = addDaySuffix(parse.getDay()) + " " + formattedDate;
            return formattedDate;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
    private static String addDaySuffix(int day) {
        if (day >= 11 && day <= 13) {
            return day + "th";
        }
        switch (day % 10) {
            case 1:
                return day + "st";
            case 2:
                return day + "nd";
            case 3:
                return day + "rd";
            default:
                return day + "th";
        }
    }

    private void openUrlInBrowser(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(browserIntent);
    }
}
