package com.example.royalroad;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.type.DateTime;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>
{
    ArrayList<Book.Review> ReviewList = new ArrayList<>();

    public ReviewAdapter(ArrayList<Book.Review> Reviews)
    {
        this.ReviewList = Reviews;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);

        return new ReviewAdapter.ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position)
    {
        SharedPreferences Pref = holder.itemView.getContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        boolean IsDarkMode = Pref.getBoolean("AppTheme", false);

        if(IsDarkMode)
        {
            holder.ReviewTitle.setTextColor(holder.itemView.getResources().getColor(R.color.DarkText));
            holder.ReviewAuthor.setTextColor(holder.itemView.getResources().getColor(R.color.DarkText));
            holder.ReviewedAt.setTextColor(holder.itemView.getResources().getColor(R.color.DarkText));
            holder.ReviewDate.setTextColor(holder.itemView.getResources().getColor(R.color.DarkText));
            holder.ReviewDescription.setTextColor(holder.itemView.getResources().getColor(R.color.DarkText));

            holder.OverallRatingTXT.setTextColor(holder.itemView.getResources().getColor(R.color.DarkText));
            holder.StyleRatingTXT.setTextColor(holder.itemView.getResources().getColor(R.color.DarkText));
            holder.StoryRatingTXT.setTextColor(holder.itemView.getResources().getColor(R.color.DarkText));
            holder.GrammerRatingTXT.setTextColor(holder.itemView.getResources().getColor(R.color.DarkText));
            holder.CharacterRatingTXT.setTextColor(holder.itemView.getResources().getColor(R.color.DarkText));
        }
        else
        {
            holder.ReviewTitle.setTextColor(holder.itemView.getResources().getColor(R.color.black));
            holder.ReviewAuthor.setTextColor(holder.itemView.getResources().getColor(R.color.black));
            holder.ReviewDescription.setTextColor(holder.itemView.getResources().getColor(R.color.black));
            holder.ReviewedAt.setTextColor(holder.itemView.getResources().getColor(R.color.black));
            holder.ReviewDate.setTextColor(holder.itemView.getResources().getColor(R.color.black));

            holder.OverallRatingTXT.setTextColor(holder.itemView.getResources().getColor(R.color.black));
            holder.StyleRatingTXT.setTextColor(holder.itemView.getResources().getColor(R.color.black));
            holder.StoryRatingTXT.setTextColor(holder.itemView.getResources().getColor(R.color.black));
            holder.GrammerRatingTXT.setTextColor(holder.itemView.getResources().getColor(R.color.black));
            holder.CharacterRatingTXT.setTextColor(holder.itemView.getResources().getColor(R.color.black));
        }

        holder.ReviewTitle.setText(ReviewList.get(position).Title);
        holder.ReviewAuthor.setText(ReviewList.get(position).ReviewAuthor);

        String Description = "";

        if(ReviewList.get(position).ReviewDescription.length > 1)
        {
            for(int i = 0; i < ReviewList.get(position).ReviewDescription.length; i++)
            {
                if(i != ReviewList.get(position).ReviewDescription.length - 1)
                {
                    Description += ReviewList.get(position).ReviewDescription[i] + "\n\n";
                }
                else
                {
                    Description += ReviewList.get(position).ReviewDescription[i];
                }
            }
        }
        else
        {
            Description = ReviewList.get(position).ReviewDescription[0];
        }

        holder.ReviewDescription.setText(Description);
        holder.OverallRating.setRating((float)ReviewList.get(position).OverallScore);

        if(!ReviewList.get(position).IsAdvancedReview)
        {
            holder.StyleRating.setVisibility(View.GONE);
            holder.StoryRating.setVisibility(View.GONE);
            holder.GrammerRating.setVisibility(View.GONE);
            holder.CharacterRating.setVisibility(View.GONE);

            holder.StyleRatingTXT.setVisibility(View.GONE);
            holder.StoryRatingTXT.setVisibility(View.GONE);
            holder.GrammerRatingTXT.setVisibility(View.GONE);
            holder.CharacterRatingTXT.setVisibility(View.GONE);
        }
        else
        {
            holder.StyleRating.setRating((float) ReviewList.get(position).StyleScore);
            holder.StoryRating.setRating((float) ReviewList.get(position).StoryScore);
            holder.GrammerRating.setRating((float) ReviewList.get(position).GrammerScore);
            holder.CharacterRating.setRating((float) ReviewList.get(position).CharacterScore);
        }

        String[] DateSplitter = ReviewList.get(position).DateTime.split("-");
        String Year = DateSplitter[0];
        String Month = DateSplitter[1];
        String Day = DateSplitter[2];

        String FormattedDateTime = Day + "-" + Month + "-" + Year;

        holder.ReviewDate.setText(FormattedDateTime);
    }

    @Override
    public int getItemCount()
    {
        return ReviewList.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder
    {
        ImageView ProfileImage;
        TextView ReviewTitle,
                 ReviewedAt,
                 ReviewAuthor,
                 ReviewDate,
                 ReviewDescription,
                 OverallRatingTXT,
                 StyleRatingTXT,
                 StoryRatingTXT,
                 GrammerRatingTXT,
                 CharacterRatingTXT;

        RatingBar OverallRating,
                  StyleRating,
                  StoryRating,
                  GrammerRating,
                  CharacterRating;

        public ReviewViewHolder(@NonNull View itemView)
        {
            super(itemView);

            ProfileImage = itemView.findViewById(R.id.ProfileImage);

            ReviewTitle = itemView.findViewById(R.id.ReviewTitle);
            ReviewedAt = itemView.findViewById(R.id.ReviewedAtChapter);
            ReviewAuthor = itemView.findViewById(R.id.ReviewAuthor);
            ReviewDate = itemView.findViewById(R.id.ReviewDate);
            ReviewDescription = itemView.findViewById(R.id.ReviewDescription);

            OverallRatingTXT = itemView.findViewById(R.id.OverallRatingTXT);
            StyleRatingTXT = itemView.findViewById(R.id.StyleRatingTXT);
            StoryRatingTXT = itemView.findViewById(R.id.StoryRatingTXT);
            GrammerRatingTXT = itemView.findViewById(R.id.GrammerRatingTXT);
            CharacterRatingTXT = itemView.findViewById(R.id.CharacterRatingTXT);

            OverallRating = itemView.findViewById(R.id.OverallRating);
            StyleRating = itemView.findViewById(R.id.StyleRating);
            StoryRating = itemView.findViewById(R.id.StoryRating);
            GrammerRating = itemView.findViewById(R.id.GrammerRating);
            CharacterRating = itemView.findViewById(R.id.CharacterRating);
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