package com.example.royalroad;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ThreadAdapter extends RecyclerView.Adapter<ThreadAdapter.ThreadViewHolder> {

    public ArrayList<ForumThreadData> AllThreads;
    public Activity cm;

    public ThreadAdapter(Activity c, ArrayList<ForumThreadData> threads)
    {
        this.AllThreads = threads;
        this.cm = c;
    }

    @NonNull
    @Override
    public ThreadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.thread_item, parent, false);
        return new ThreadAdapter.ThreadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThreadViewHolder holder, int position)
    {
        SharedPreferences Pref = holder.itemView.getContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        boolean IsDarkMode = Pref.getBoolean("AppTheme", false);

        if(IsDarkMode)
        {
            holder.ThreadTitle.setTextColor(holder.itemView.getResources().getColor(R.color.DarkText));
            holder.ThreadAuthor.setTextColor(holder.itemView.getResources().getColor(R.color.DarkText));
        }
        else
        {
            holder.ThreadTitle.setTextColor(holder.itemView.getResources().getColor(R.color.black));
            holder.ThreadAuthor.setTextColor(holder.itemView.getResources().getColor(R.color.black));
        }

        holder.OpenThreadBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ForumsActivity ActiveActivity = (ForumsActivity) cm;
                ActiveActivity.ReplaceFragment(new ThreadFragment());
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return AllThreads.size();
    }

    public class ThreadViewHolder extends RecyclerView.ViewHolder
    {
        public TextView ThreadTitle,
                        ThreadAuthor;

        public Button OpenThreadBTN;

        public ThreadViewHolder(@NonNull View itemView)
        {
            super(itemView);

            ThreadTitle = itemView.findViewById(R.id.ThreadTitle);
            ThreadAuthor = itemView.findViewById(R.id.ThreadAuthor);

            OpenThreadBTN = itemView.findViewById(R.id.OpenThreadBTN);
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