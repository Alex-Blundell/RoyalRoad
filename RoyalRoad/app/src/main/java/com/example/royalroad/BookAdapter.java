package com.example.royalroad;

import static androidx.core.content.ContextCompat.getColor;
import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firestore.admin.v1.Index;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.bookviewholder>
{
    List<Book> Data;

    public BookAdapter (List<Book> ThisData)
    {
        this.Data = ThisData;
    }

    @NonNull
    @Override
    public bookviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_item, parent, false);

        return new bookviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull bookviewholder holder, int position) {
        SharedPreferences Pref = holder.itemView.getContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);

        boolean IsDarkMode = Pref.getBoolean("AppTheme", false);

        if(IsDarkMode)
        {
            holder.Title.setTextColor(holder.itemView.getResources().getColor(R.color.white));
            holder.Author.setTextColor(holder.itemView.getResources().getColor(R.color.white));
            holder.Description.setTextColor(holder.itemView.getResources().getColor(R.color.white));
            holder.CreatedDate.setTextColor(holder.itemView.getResources().getColor(R.color.white));
            holder.UpdatedDate.setTextColor(holder.itemView.getResources().getColor(R.color.white));
            holder.Warnings.setTextColor(holder.itemView.getResources().getColor(R.color.white));

            holder.Language.setTextColor(holder.itemView.getResources().getColor(R.color.white));
            holder.PageCount.setTextColor(holder.itemView.getResources().getColor(R.color.white));
            holder.ChapterCount.setTextColor(holder.itemView.getResources().getColor(R.color.white));
            holder.Type.setTextColor(holder.itemView.getResources().getColor(R.color.white));
        }
        else
        {
            holder.Title.setTextColor(holder.itemView.getResources().getColor(R.color.black));
            holder.Author.setTextColor(holder.itemView.getResources().getColor(R.color.black));
            holder.Description.setTextColor(holder.itemView.getResources().getColor(R.color.black));
            holder.CreatedDate.setTextColor(holder.itemView.getResources().getColor(R.color.black));
            holder.UpdatedDate.setTextColor(holder.itemView.getResources().getColor(R.color.black));
            holder.Warnings.setTextColor(holder.itemView.getResources().getColor(R.color.black));

            holder.Language.setTextColor(holder.itemView.getResources().getColor(R.color.black));
            holder.PageCount.setTextColor(holder.itemView.getResources().getColor(R.color.black));
            holder.ChapterCount.setTextColor(holder.itemView.getResources().getColor(R.color.black));
            holder.Type.setTextColor(holder.itemView.getResources().getColor(R.color.black));
        }

        holder.Title.setText(Data.get(position).Title);
        holder.Author.setText(Data.get(position).Author);

        if(Data.get(position).Description.length() > 295)
        {
            String ShortenedDescription = Data.get(position).Description;
            ShortenedDescription = ShortenedDescription.substring(0, 295);
            ShortenedDescription += " ...";

            holder.Description.setText(ShortenedDescription);
        }
        else
        {
            holder.Description.setText(Data.get(position).Description);
        }

        holder.ChapterCount.setText(String.valueOf(Data.get(position).Chapters.size()));
        holder.PageCount.setText(String.valueOf(Data.get(position).PageCount));
        holder.Type.setText(Data.get(position).Type.toString());

        holder.Language.setText("English");

        ConnectivityManager connectivityManager = (ConnectivityManager)holder.itemView.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean IsOnline = (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED);

        if(IsOnline)
        {
            Glide.with(holder.itemView.getContext())
                    .load(Data.get(position).CoverURL)
                    .into(holder.Cover);
        }
        else
        {
            holder.Cover.setImageResource(R.drawable.default_cover);
        }

        if(Data.get(position).HasRead)
        {
            holder.BookProgress.setVisibility(View.VISIBLE);
            holder.BookProgress.setMax(Data.get(position).Chapters.size());
            holder.BookProgress.setProgress(Data.get(position).LastReadChapter);

            if(Data.get(position).LastReadChapter == Data.get(position).Chapters.size())
            {
                holder.BookProgress.setProgressTintList(ColorStateList.valueOf(getColor(holder.itemView.getContext(), R.color.BookComplete)));
            }
            else
            {
                holder.BookProgress.setProgressTintList(ColorStateList.valueOf(getColor(holder.itemView.getContext(), R.color.BookInProgress)));
            }
        }
        else
        {
            holder.BookProgress.setVisibility(View.GONE);
        }

        holder.OpenBookBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent ThisIntent = new Intent(v.getContext(), ReadActivity.class);
                ThisIntent.putExtra("Book", Data.get(position));

                DBHandler SQLiteDB = new DBHandler(holder.itemView.getContext());

                boolean HasDownloaded = SQLiteDB.GetLibraryBook(Data.get(position).ExternalID);
                ThisIntent.putExtra("HasDownloaded", HasDownloaded);

                SQLiteDB.close();

                v.getContext().startActivity(ThisIntent);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return Data.size();
    }


    public class bookviewholder extends RecyclerView.ViewHolder
    {
        ImageView Cover;
        TextView Title,
                 Author,
                 Description,
                 Warnings,
                 CreatedDate,
                 UpdatedDate,
                 PageCount,
                 Type,
                 ChapterCount,
                 Language;

        Button OpenBookBTN;
        SeekBar BookProgress;

        public bookviewholder(@NonNull View itemView) {
            super(itemView);

            Cover = itemView.findViewById(R.id.Cover);
            Title = itemView.findViewById(R.id.Title);
            Author = itemView.findViewById(R.id.Author);
            Description = itemView.findViewById(R.id.Description);
            Warnings = itemView.findViewById(R.id.ContentWarnings);

            CreatedDate = itemView.findViewById(R.id.CreatedDate);
            UpdatedDate = itemView.findViewById(R.id.UpdatedDate);

            OpenBookBTN = itemView.findViewById(R.id.OpenBookBTN);

            PageCount = itemView.findViewById(R.id.PageCount);
            Type = itemView.findViewById(R.id.BookType);
            ChapterCount = itemView.findViewById(R.id.ChapterCount);
            Language = itemView.findViewById(R.id.Language);

            BookProgress = itemView.findViewById(R.id.BookProgress);
        }
    }
}
