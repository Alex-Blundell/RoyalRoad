package com.example.royalroad;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FolderItem extends RecyclerView.Adapter<FolderItem.FolderHolder>
{


    @NonNull
    @Override
    public FolderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull FolderHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class  FolderHolder extends RecyclerView.ViewHolder
    {
        public FolderHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}