package com.example.royalroad;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.collection.LLRBNode;

import java.util.ArrayList;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.DetailViewHolder> {
    public ArrayList<Book.Details> DetailsData;

    public DetailAdapter(ArrayList<Book.Details> Data)
    {
        DetailsData = Data;
    }

    @NonNull
    @Override
    public DetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookgriditem, parent, false);

        return new DetailAdapter.DetailViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull DetailViewHolder holder, int position)
    {
        SharedPreferences Pref = holder.itemView.getContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        boolean IsDarkMode = Pref.getBoolean("AppTheme", false);

        if(IsDarkMode)
        {
            holder.DetailBTN.setBackgroundColor(holder.itemView.getResources().getColor(R.color.DarkOuter));
            holder.DetailBTN.setTextColor(holder.itemView.getResources().getColor(R.color.DarkText));
        }
        else
        {
            holder.DetailBTN.setBackgroundColor(holder.itemView.getResources().getColor(R.color.white));
            holder.DetailBTN.setTextColor(holder.itemView.getResources().getColor(R.color.black));
        }

        holder.DetailBTN.setText(DetailsData.get(position).Text);

        if(DetailsData.get(position).Icon != null)
        {
            holder.DetailBTN.setIcon(DetailsData.get(position).Icon);
            holder.DetailBTN.setIconTint(ColorStateList.valueOf(DetailsData.get(position).IconColor));
        }
        else
        {
            holder.DetailBTN.setIcon(null);
        }

        // Edit Stroke Color.
        holder.DetailBTN.setStrokeColor(ColorStateList.valueOf(DetailsData.get(position).StrokeID));
    }

    @Override
    public int getItemCount()
    {
        return DetailsData.size();
    }

    public class DetailViewHolder extends RecyclerView.ViewHolder
    {
        MaterialButton DetailBTN;

        public DetailViewHolder(@NonNull View itemView)
        {
            super(itemView);

            DetailBTN = itemView.findViewById(R.id.GridBTN);
        }
    }
}