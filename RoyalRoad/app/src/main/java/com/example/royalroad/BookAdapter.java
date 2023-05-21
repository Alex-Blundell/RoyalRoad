package com.example.royalroad;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        // Glide Image into ViewHolder.

        // Glide.with(holder.itemView.getContext())
        //      .load(Data.get(Position).getDrawableResource()) // Get Cover from SQLite Database.
        //      .into(holder.Cover);

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
        }
        else
        {
            holder.Title.setTextColor(holder.itemView.getResources().getColor(R.color.black));
            holder.Author.setTextColor(holder.itemView.getResources().getColor(R.color.black));
            holder.Description.setTextColor(holder.itemView.getResources().getColor(R.color.black));
            holder.CreatedDate.setTextColor(holder.itemView.getResources().getColor(R.color.black));
            holder.UpdatedDate.setTextColor(holder.itemView.getResources().getColor(R.color.black));
            holder.Warnings.setTextColor(holder.itemView.getResources().getColor(R.color.black));
        }

        holder.Title.setText("");

        holder.OpenBookBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ThisIntent = new Intent(v.getContext(), ReadActivity.class);
                ThisIntent.putExtra("InternalID", 1); // Alter 1 to the Internal ID of the Specific Book.
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
                 UpdatedDate;

        RatingBar Rating;

        Button OpenBookBTN;

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
        }
    }
}
