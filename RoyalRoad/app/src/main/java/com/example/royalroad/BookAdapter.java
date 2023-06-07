package com.example.royalroad;

import static androidx.core.content.ContextCompat.getColor;
import static androidx.core.content.ContextCompat.getSystemService;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
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
    public void onBindViewHolder(@NonNull bookviewholder holder, @SuppressLint("RecyclerView") int Position) {
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

            holder.FollowCount.setTextColor(holder.itemView.getResources().getColor(R.color.white));
            holder.FavouriteCount.setTextColor(holder.itemView.getResources().getColor(R.color.white));

            holder.TagOne.setTextColor(holder.itemView.getResources().getColor(R.color.white));
            holder.TagTwo.setTextColor(holder.itemView.getResources().getColor(R.color.white));
            holder.TagThree.setTextColor(holder.itemView.getResources().getColor(R.color.white));
            holder.TagFour.setTextColor(holder.itemView.getResources().getColor(R.color.white));

            holder.Complete.setTextColor(holder.itemView.getResources().getColor(R.color.white));
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

            holder.FollowCount.setTextColor(holder.itemView.getResources().getColor(R.color.black));
            holder.FavouriteCount.setTextColor(holder.itemView.getResources().getColor(R.color.black));

            holder.TagOne.setTextColor(holder.itemView.getResources().getColor(R.color.black));
            holder.TagTwo.setTextColor(holder.itemView.getResources().getColor(R.color.black));
            holder.TagThree.setTextColor(holder.itemView.getResources().getColor(R.color.black));
            holder.TagFour.setTextColor(holder.itemView.getResources().getColor(R.color.black));

            holder.Complete.setTextColor(holder.itemView.getResources().getColor(R.color.black));
        }

        holder.Title.setText(Data.get(Position).GetTitle());
        holder.Author.setText(Data.get(Position).GetAuthor());

        if(Data.get(Position).GetDescription().length() > 275)
        {
            String ShortenedDescription = Data.get(Position).GetDescription();
            ShortenedDescription = ShortenedDescription.substring(0, 275);
            ShortenedDescription += " ...";

            holder.Description.setText(ShortenedDescription);
        }
        else
        {
            holder.Description.setText(Data.get(Position).GetDescription());
        }

        holder.ChapterCount.setText(String.valueOf(Data.get(Position).GetAllChapters().size()));
        holder.PageCount.setText(String.valueOf(Data.get(Position).GetPageCount()));
        holder.Type.setText(Data.get(Position).GetType().toString());

        holder.Language.setText("English");

        ConnectivityManager connectivityManager = (ConnectivityManager)holder.itemView.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean IsOnline = (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED);

        if(IsOnline)
        {
            Glide.with(holder.itemView.getContext())
                    .load(Data.get(Position).GetCover())
                    .into(holder.Cover);
        }
        else
        {
            holder.Cover.setImageResource(R.drawable.default_cover);
        }

        if(Data.get(Position).GetHasRead())
        {
            holder.BookProgress.setVisibility(View.VISIBLE);
            holder.BookProgress.setMax(Data.get(Position).GetAllChapters().size());
            holder.BookProgress.setProgress(Data.get(Position).GetLastReadChapter());

            if(Data.get(Position).GetLastReadChapter() == Data.get(Position).GetAllChapters().size())
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
                ThisIntent.putExtra("Book", Data.get(Position));

                DBHandler SQLiteDB = new DBHandler(holder.itemView.getContext());

                boolean HasDownloaded = SQLiteDB.GetLibraryBook(Data.get(Position).GetExternalID());
                ThisIntent.putExtra("HasDownloaded", HasDownloaded);

                SQLiteDB.close();

                v.getContext().startActivity(ThisIntent);
            }
        });

        holder.OpenBookBTN.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(holder.DarkCover.getVisibility() == View.GONE)
                {
                    holder.DarkCover.setVisibility(View.VISIBLE);
                }
                return false;
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
        ImageView Cover,
                  DarkCover;
        TextView Title,
                 Author,
                 Description,
                 Warnings,
                 Language,
                 Type,
                 PageCount,
                 ChapterCount,
                 FollowCount,
                 FavouriteCount,
                 CreatedDate,
                 UpdatedDate,
                 TagOne,
                 TagTwo,
                 TagThree,
                 TagFour,
                 Complete;

        Button OpenBookBTN;
        SeekBar BookProgress;
        GridLayout DetailsGridLayout;

        public bookviewholder(@NonNull View itemView)
        {
            super(itemView);

            Cover = itemView.findViewById(R.id.Cover);
            Title = itemView.findViewById(R.id.Title);
            Author = itemView.findViewById(R.id.Author);
            Description = itemView.findViewById(R.id.Description);

            Warnings = itemView.findViewById(R.id.ContentWarnings);
            Language = itemView.findViewById(R.id.Language);
            Type = itemView.findViewById(R.id.BookType);

            PageCount = itemView.findViewById(R.id.PageCount);
            ChapterCount = itemView.findViewById(R.id.ChapterCount);
            FollowCount = itemView.findViewById(R.id.FollowerCount);
            FavouriteCount = itemView.findViewById(R.id.FavouriteCount);

            CreatedDate = itemView.findViewById(R.id.CreatedDate);
            UpdatedDate = itemView.findViewById(R.id.UpdatedDate);

            TagOne = itemView.findViewById(R.id.Tag1);
            TagTwo = itemView.findViewById(R.id.Tag2);
            TagThree = itemView.findViewById(R.id.Tag3);
            TagFour = itemView.findViewById(R.id.Tag4);

            Complete = itemView.findViewById(R.id.Complete);

            BookProgress = itemView.findViewById(R.id.BookProgress);

            DetailsGridLayout = itemView.findViewById(R.id.DetailsGridLayout);
            OpenBookBTN = itemView.findViewById(R.id.OpenBookBTN);
            DarkCover = itemView.findViewById(R.id.DarkCover);
        }
    }
}
