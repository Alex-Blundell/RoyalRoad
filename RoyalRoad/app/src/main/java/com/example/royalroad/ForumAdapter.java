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
            holder.Title.setTextColor(holder.itemView.getResources().getColor(R.color.DarkText));
            holder.Description.setTextColor(holder.itemView.getResources().getColor(R.color.DarkText));
            holder.PostsCount.setTextColor(holder.itemView.getResources().getColor(R.color.DarkText));
            holder.TopicsCount.setTextColor(holder.itemView.getResources().getColor(R.color.DarkText));

            holder.LastUser.setTextColor(holder.itemView.getResources().getColor(R.color.DarkText));
            holder.LastUserTitle.setTextColor(holder.itemView.getResources().getColor(R.color.DarkText));
            holder.LastUserDate.setTextColor(holder.itemView.getResources().getColor(R.color.DarkText));
        }
        else
        {
            holder.Title.setTextColor(holder.itemView.getResources().getColor(R.color.black));
            holder.Description.setTextColor(holder.itemView.getResources().getColor(R.color.black));
            holder.PostsCount.setTextColor(holder.itemView.getResources().getColor(R.color.black));
            holder.TopicsCount.setTextColor(holder.itemView.getResources().getColor(R.color.black));

            holder.LastUser.setTextColor(holder.itemView.getResources().getColor(R.color.black));
            holder.LastUserTitle.setTextColor(holder.itemView.getResources().getColor(R.color.black));
            holder.LastUserDate.setTextColor(holder.itemView.getResources().getColor(R.color.black));
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
        ImageView Icon,
                  UserIcon;
        TextView Title,
                 Description,
                 PostsCount,
                 TopicsCount,
                 LastUserTitle,
                 LastUser,
                 LastUserDate;

        Button OpenForumBTN;

        public forumviewholder(@NonNull View itemView)
        {
            super(itemView);

            Icon = itemView.findViewById(R.id.ForumIcon);
            Title = itemView.findViewById(R.id.ForumTitle);
            Description = itemView.findViewById(R.id.ForumDescription);
            PostsCount = itemView.findViewById(R.id.ForumPosts);
            TopicsCount = itemView.findViewById(R.id.ForumTopics);

            UserIcon = itemView.findViewById(R.id.LastUserIcon);
            LastUserTitle = itemView.findViewById(R.id.LastUserTitle);
            LastUser = itemView.findViewById(R.id.LastUser);
            LastUserDate = itemView.findViewById(R.id.LastUserDate);

            OpenForumBTN = itemView.findViewById(R.id.OpenForumBTN);
        }
    }

}