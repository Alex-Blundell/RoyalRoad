package com.example.royalroad;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ForumAdapter extends RecyclerView.Adapter<ForumAdapter.forumviewholder>
{
    ArrayList<ForumData> Forums = new ArrayList<>();

    public ForumAdapter(ArrayList<ForumData> SelectedForums)
    {
        this.Forums = SelectedForums;
    }

    @NonNull
    @Override
    public forumviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.forum_item, parent, false);

        return new ForumAdapter.forumviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull forumviewholder holder, int position)
    {
        SharedPreferences Pref = holder.itemView.getContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);

        boolean IsDarkMode = Pref.getBoolean("AppTheme", false);

        if(IsDarkMode)
        {
            holder.Title.setTextColor(holder.itemView.getResources().getColor(R.color.white));
            holder.Description.setTextColor(holder.itemView.getResources().getColor(R.color.white));
            holder.PostsCount.setTextColor(holder.itemView.getResources().getColor(R.color.white));
            holder.TopicsCount.setTextColor(holder.itemView.getResources().getColor(R.color.white));
        }
        else
        {
            holder.Title.setTextColor(holder.itemView.getResources().getColor(R.color.black));
            holder.Description.setTextColor(holder.itemView.getResources().getColor(R.color.black));
            holder.PostsCount.setTextColor(holder.itemView.getResources().getColor(R.color.black));
            holder.TopicsCount.setTextColor(holder.itemView.getResources().getColor(R.color.black));
        }

        holder.Icon.setImageResource(R.drawable.chatbubble);
        holder.Title.setText(Forums.get(position).GetTitle());
        holder.Description.setText(Forums.get(position).GetDescription());
        holder.PostsCount.setText(Forums.get(position).GetPostCount() + " Posts");
        holder.TopicsCount.setText(Forums.get(position).GetTopicsCount() + " Topics");

        if(Forums.get(position).GetDescription().isEmpty())
        {
            holder.Description.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount()
    {
        return Forums.size();
    }

    public class forumviewholder extends RecyclerView.ViewHolder
    {
        ImageView Icon;
        TextView Title,
                 Description,
                 PostsCount,
                 TopicsCount;

        Button OpenForumBTN;

        public forumviewholder(@NonNull View itemView)
        {
            super(itemView);

            Icon = itemView.findViewById(R.id.ForumIcon);
            Title = itemView.findViewById(R.id.ForumTitle);
            Description = itemView.findViewById(R.id.ForumDescription);
            PostsCount = itemView.findViewById(R.id.ForumPosts);
            TopicsCount = itemView.findViewById(R.id.ForumTopics);

            OpenForumBTN = itemView.findViewById(R.id.OpenForumBTN);
        }
    }

}