package com.example.royalroad;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ThreadInnerAdapter extends RecyclerView.Adapter<ThreadInnerAdapter.ThreadInnerViewHolder>
{
    ArrayList<ForumInnerData> PostData;

    public ThreadInnerAdapter(ArrayList<ForumInnerData> Posts)
    {
        this.PostData = Posts;
    }

    @NonNull
    @Override
    public ThreadInnerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.thread_inner_item, parent, false);
        return new ThreadInnerAdapter.ThreadInnerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThreadInnerViewHolder holder, int position)
    {
        SharedPreferences Pref = holder.itemView.getContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        boolean IsDarkMode = Pref.getBoolean("AppTheme", false);

        if(IsDarkMode)
        {
            holder.PostTitle.setTextColor(holder.itemView.getResources().getColor(R.color.DarkText));
            holder.PostNum.setTextColor(holder.itemView.getResources().getColor(R.color.DarkText));
            holder.PostAuthor.setTextColor(holder.itemView.getResources().getColor(R.color.DarkText));
            holder.PostBody.setTextColor(holder.itemView.getResources().getColor(R.color.DarkText));
            holder.PostDateTime.setTextColor(holder.itemView.getResources().getColor(R.color.DarkText));

            holder.TitleDivider.setBackgroundColor(holder.itemView.getResources().getColor(R.color.DarkBorder));
            holder.BodyDivider.setBackgroundColor(holder.itemView.getResources().getColor(R.color.DarkBorder));
            holder.EndDivider.setBackgroundColor(holder.itemView.getResources().getColor(R.color.DarkBorder));
        }
        else
        {
            holder.PostTitle.setTextColor(holder.itemView.getResources().getColor(R.color.black));
            holder.PostNum.setTextColor(holder.itemView.getResources().getColor(R.color.black));
            holder.PostAuthor.setTextColor(holder.itemView.getResources().getColor(R.color.black));
            holder.PostBody.setTextColor(holder.itemView.getResources().getColor(R.color.black));
            holder.PostDateTime.setTextColor(holder.itemView.getResources().getColor(R.color.black));
        }

        holder.PostTitle.setText(PostData.get(position).PostTitle);
        holder.PostAuthor.setText(PostData.get(position).PostAuthor);
        holder.PostNum.setText(PostData.get(position).PostNumber);
        holder.PostBody.setText(PostData.get(position).PostBody);
        holder.PostDateTime.setText(PostData.get(position).PostDateTime);
    }


    @Override
    public int getItemCount()
    {
        return PostData.size();
    }

    public class ThreadInnerViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView ProfileIcon;
        public TextView PostTitle,
                        PostNum,
                        PostAuthor,
                        PostBody,
                        PostDateTime;

        public View TitleDivider;
        public View BodyDivider;
        public View EndDivider;

        public ThreadInnerViewHolder(@NonNull View itemView)
        {
            super(itemView);

            ProfileIcon = itemView.findViewById(R.id.ProfileIcon);
            PostTitle = itemView.findViewById(R.id.ThreadName);
            PostAuthor = itemView.findViewById(R.id.ThreadAuthor);
            PostNum = itemView.findViewById(R.id.PostNum);
            PostBody = itemView.findViewById(R.id.ThreadBody);
            PostDateTime = itemView.findViewById(R.id.ThreadDateTime);

            TitleDivider = itemView.findViewById(R.id.ThreadTitleDivider);
            BodyDivider = itemView.findViewById(R.id.BodyDivider);
            EndDivider = itemView.findViewById(R.id.EndDivider);
        }
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public int getItemViewType(int position)
    {
        return position;
    }
}