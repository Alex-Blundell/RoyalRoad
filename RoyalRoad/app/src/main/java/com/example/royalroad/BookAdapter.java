package com.example.royalroad;

import static androidx.core.content.ContextCompat.getColor;
import static androidx.core.content.ContextCompat.getSystemService;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
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
    private boolean CanDestroy = false;

    public BookAdapter (List<Book> ThisData)
    {
        this.Data = ThisData;
    }

    @NonNull
    @Override
    public bookviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item, parent, false);

        return new bookviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull bookviewholder holder, @SuppressLint("RecyclerView") int Position)
    {
        ArrayList<Book.Details> AllDetails = new ArrayList<>();

        SharedPreferences Pref = holder.itemView.getContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        boolean IsDarkMode = Pref.getBoolean("AppTheme", false);

        if(IsDarkMode)
        {
            holder.Title.setTextColor(holder.itemView.getResources().getColor(R.color.DarkText));
            holder.Author.setTextColor(holder.itemView.getResources().getColor(R.color.DarkText));
            holder.Description.setTextColor(holder.itemView.getResources().getColor(R.color.DarkText));

            holder.BookDivider.setBackgroundColor(holder.itemView.getResources().getColor(R.color.DarkBorder));
        }
        else
        {
            holder.Title.setTextColor(holder.itemView.getResources().getColor(R.color.black));
            holder.Author.setTextColor(holder.itemView.getResources().getColor(R.color.black));
            holder.Description.setTextColor(holder.itemView.getResources().getColor(R.color.black));
        }

        holder.UpdatedImage.setVisibility(View.GONE);
        holder.DeleteBTN.setVisibility(View.GONE);

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

        // Warnings First.
        if(Data.get(Position).ContentWarnings != null)
        {
            if(Data.get(Position).ContentWarnings.size() > 0)
            {
                // Add Warnings.
                String WarningsText = "";

                for(Book.Warnings ThisWarning : Data.get(Position).ContentWarnings)
                {
                    WarningsText += ThisWarning.name();
                }

                Book.Details WarningsDetail = new Book.Details(WarningsText, holder.itemView.getResources().getColor(R.color.BlueBorder));
                AllDetails.add(WarningsDetail);
            }
        }

        // Language Next.
        Book.Details LanguageDetail = new Book.Details("English", holder.itemView.getResources().getDrawable(R.drawable.earth),
                                          holder.itemView.getResources().getColor(R.color.BlueBorder), holder.itemView.getResources().getColor(R.color.DarkText));
        AllDetails.add(LanguageDetail);

        // Type Next.
        Book.Details TypeDetail = new Book.Details(Data.get(Position).GetType().toString(), holder.itemView.getResources().getColor(R.color.BlueBorder));
        AllDetails.add(TypeDetail);

        // Genres Next.


        // Counts Next.
        Book.Details ChapterCountDetail = new Book.Details(String.valueOf(Data.get(Position).GetAllChapters().size()), holder.itemView.getResources().getDrawable(R.drawable.chapters),
                                              holder.itemView.getResources().getColor(R.color.LengthBorder), holder.itemView.getResources().getColor(R.color.DarkText));
        AllDetails.add(ChapterCountDetail);

        Book.Details PageCountDetail = new Book.Details(String.valueOf(Data.get(Position).GetPageCount()), holder.itemView.getResources().getDrawable(R.drawable.pages),
                                           holder.itemView.getResources().getColor(R.color.LengthBorder), holder.itemView.getResources().getColor(R.color.DarkText));
        AllDetails.add(PageCountDetail);

        Book.Details FollowCountDetail = new Book.Details(String.valueOf(Data.get(Position).GetFollowerCount()), holder.itemView.getResources().getDrawable(R.drawable.follow),
                                             holder.itemView.getResources().getColor(R.color.LengthBorder), holder.itemView.getResources().getColor(R.color.Orange));
        AllDetails.add(FollowCountDetail);

        Book.Details FavouriteCountDetail = new Book.Details(String.valueOf(Data.get(Position).GetFavouriteCount()), holder.itemView.getResources().getDrawable(R.drawable.favourite),
                                                holder.itemView.getResources().getColor(R.color.LengthBorder), holder.itemView.getResources().getColor(R.color.Red));
        AllDetails.add(FavouriteCountDetail);

        // Date Times Next.
        Book.Details LastUpdateDateTimeDetail = new Book.Details("06/04", holder.itemView.getResources().getDrawable(R.drawable.update),
                                                    holder.itemView.getResources().getColor(R.color.TimeBorder), holder.itemView.getResources().getColor(R.color.DarkText));
        AllDetails.add(LastUpdateDateTimeDetail);

        Book.Details CreatedDateTimeDetail = new Book.Details("01/01/01", holder.itemView.getResources().getDrawable(R.drawable.empty_clock),
                                                 holder.itemView.getResources().getColor(R.color.TimeBorder), holder.itemView.getResources().getColor(R.color.DarkText));
        AllDetails.add(CreatedDateTimeDetail);

        // Tags Next.
        if(Data.get(Position).TagsList != null)
        {
            // Has At least One tag.
            if(Data.get(Position).TagsList.size() > 0)
            {
                for(int i = 0; i < 4; i++)
                {
                    if(i == 0)
                    {
                        Book.Details TagDetail = new Book.Details(Data.get(Position).TagsList.get(i).toString(), holder.itemView.getResources().getColor(R.color.BlueBorder));
                        AllDetails.add(TagDetail);
                    }

                    // Has Two Tags.
                    if(Data.get(Position).TagsList.size() > 1)
                    {
                        if(i == 1)
                        {
                            Book.Details TagDetail = new Book.Details(Data.get(Position).TagsList.get(i).toString(), holder.itemView.getResources().getColor(R.color.BlueBorder));
                            AllDetails.add(TagDetail);
                        }

                        // Has Three Tags.
                        if(Data.get(Position).TagsList.size() > 2)
                        {
                            if(i == 2)
                            {
                                Book.Details TagDetail = new Book.Details(Data.get(Position).TagsList.get(i).toString(), holder.itemView.getResources().getColor(R.color.BlueBorder));
                                AllDetails.add(TagDetail);
                            }

                            // Has Four Tags.
                            if(Data.get(Position).TagsList.size() > 3)
                            {
                                if(i == 1)
                                {
                                    Book.Details TagDetail = new Book.Details(Data.get(Position).TagsList.get(i).toString(), holder.itemView.getResources().getColor(R.color.BlueBorder));
                                    AllDetails.add(TagDetail);
                                }
                            }
                        }
                    }
                }
            }
        }

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
            public void onClick(View v)
            {
                if(!CanDestroy)
                {
                    Intent ThisIntent = new Intent(v.getContext(), ReadActivity.class);

                    ThisIntent.putExtra("Book", Data.get(Position));

                    DBHandler SQLiteDB = new DBHandler((Activity)holder.itemView.getContext());

                    boolean HasDownloaded = SQLiteDB.GetLibraryBook(Data.get(Position).GetExternalID());
                    ThisIntent.putExtra("HasDownloaded", HasDownloaded);

                    SQLiteDB.close();

                    v.getContext().startActivity(ThisIntent);
                }
                else
                {
                   if(holder.DeleteBTN.getVisibility() == View.GONE)
                   {
                       holder.OpenBookBTN.setBackgroundColor(holder.itemView.getResources().getColor(R.color.DarkBackground));
                       holder.OpenBookBTN.setBackgroundTintList(ColorStateList.valueOf(holder.itemView.getResources().getColor(R.color.DarkBackground)));

                       holder.DeleteBTN.setVisibility(View.VISIBLE);
                       holder.DeleteBTN.setChecked(true);
                   }
                   else
                   {
                       holder.OpenBookBTN.setBackgroundColor(holder.itemView.getResources().getColor(R.color.Invis));
                       holder.OpenBookBTN.setBackgroundTintList(ColorStateList.valueOf(holder.itemView.getResources().getColor(R.color.Invis)));

                       holder.DeleteBTN.setVisibility(View.GONE);
                       holder.DeleteBTN.setChecked(false);
                   }
                }
            }
        });

        if((holder.itemView.getContext() instanceof  LibraryActivity))
        {
            holder.OpenBookBTN.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view)
                {
                    if(CanDestroy)
                    {
                        holder.OpenBookBTN.setBackgroundColor(holder.itemView.getResources().getColor(R.color.Invis));
                        holder.OpenBookBTN.setBackgroundTintList(ColorStateList.valueOf(holder.itemView.getResources().getColor(R.color.Invis)));

                        holder.DeleteBTN.setVisibility(View.GONE);
                        holder.DeleteBTN.setChecked(false);

                        CanDestroy = false;

                    }
                    else
                    {
                        holder.OpenBookBTN.setBackgroundColor(holder.itemView.getResources().getColor(R.color.DarkBackground));
                        holder.OpenBookBTN.setBackgroundTintList(ColorStateList.valueOf(holder.itemView.getResources().getColor(R.color.DarkBackground)));

                        holder.DeleteBTN.setVisibility(View.VISIBLE);
                        holder.DeleteBTN.setChecked(true);

                        CanDestroy = true;
                    }

                    ((LibraryActivity)holder.itemView.getContext()).OpenDeletePrompt(CanDestroy);

                    return true;
                }
            });
        }

        DetailAdapter adpater = new DetailAdapter(AllDetails);
        holder.DetailsRV.setAdapter(adpater);
    }

    @Override
    public int getItemCount()
    {
        return Data.size();
    }

    public class bookviewholder extends RecyclerView.ViewHolder
    {
        ImageView Cover,
                  UpdatedImage;
        TextView Title,
                 Author,
                 Description;

        Button OpenBookBTN;
        RadioButton DeleteBTN;
        SeekBar BookProgress;
        RecyclerView DetailsRV;

        View BookDivider;

        public bookviewholder(@NonNull View itemView)
        {
            super(itemView);

            Cover = itemView.findViewById(R.id.Cover);
            UpdatedImage = itemView.findViewById(R.id.UpdatedImage);

            Title = itemView.findViewById(R.id.Title);
            Author = itemView.findViewById(R.id.Author);
            Description = itemView.findViewById(R.id.Description);

            OpenBookBTN = itemView.findViewById(R.id.OpenBookBTN);

            BookProgress = itemView.findViewById(R.id.BookProgress);

            BookDivider = itemView.findViewById(R.id.Divider);

            DetailsRV = itemView.findViewById(R.id.DetailsRV);

            DeleteBTN = itemView.findViewById(R.id.DeleteSelectBTN);

            CanDestroy = false;
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
