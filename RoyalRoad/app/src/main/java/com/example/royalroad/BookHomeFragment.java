package com.example.royalroad;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class BookHomeFragment extends Fragment {

    boolean IsDarkMode;

    RecyclerView ReviewRV;
    ReviewAdapter reviewAdapter;

    TextView TitleTXT;
    TextView AuthorTXT;
    TextView Description;
    TextView Tags;
    TextView DateLastUpdated;
    TextView DateCreated;
    RatingBar Rating;
    ImageView Cover;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        Book ReadBook = ((ReadActivity)getActivity()).ReadBook;

        TitleTXT = view.findViewById(R.id.BookTitle);
        AuthorTXT = view.findViewById(R.id.BookAuthor);
        Description = view.findViewById(R.id.BookDescription);
        Rating = view.findViewById(R.id.BookRating);
        Cover = view.findViewById(R.id.BookCover);

        Tags = view.findViewById(R.id.BookTags);
        DateLastUpdated = view.findViewById(R.id.DateUpdatedTXT);
        DateCreated = view.findViewById(R.id.DateCreatedTXT);

        TitleTXT.setText(ReadBook.Title);
        AuthorTXT.setText(ReadBook.Author);
        Description.setText(ReadBook.Description);
        Rating.setRating((float) ReadBook.Rating);

        ReviewRV = view.findViewById(R.id.ReviewRecyclerView);

        ReviewRV.setLayoutManager(new LinearLayoutManager(getContext()));
        ReviewRV.setHasFixedSize(true);

        Glide.with(getContext())
                .load(ReadBook.CoverURL)
                .into(Cover);

        SharedPreferences Pref = getActivity().getSharedPreferences("Settings", MODE_PRIVATE);
        IsDarkMode = Pref.getBoolean("AppTheme", false);

        SwitchThemes(IsDarkMode);

        // Check if Online.
        ConnectivityManager connectivityManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean IsOnline = (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED);

        if(IsOnline)
        {
            GetReviews(ReadBook.GetExternalID());
        }

    }

    private void GetReviews(int ExternalID)
    {
        ArrayList<Book.Review> AllReviews = new ArrayList<>();

        Thread ReviewThread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    String URL = "https://www.royalroad.com/fiction/" + ExternalID;
                    Document StoryDoc = Jsoup.connect(URL).get();

                    Elements ReviewHeader = StoryDoc.getElementsByClass("portlet light reviews");
                    Elements Reviews = ReviewHeader.get(0).getElementsByTag("script");

                    for(Element Review : Reviews)
                    {
                        Book.Review NewReview = new Book.Review();

                        String ReviewString = Review.toString();
                        String[] ItemSplitter = ReviewString.split("\\{");

                        for(int i = 1; i < ItemSplitter.length; i++)
                        {
                            Log.println(Log.INFO, "Hi", ItemSplitter[i]);

                            String[] IndividualSplitter = ItemSplitter[i].split(":");

                            for(int j = 0; j < IndividualSplitter.length; j++)
                            {
                                if(i == 1 && j == 4)
                                {
                                    // Review Name.
                                    NewReview.Title = IndividualSplitter[j].split("\"")[1];
                                }

                                if(i == 2 && j == 2)
                                {
                                    NewReview.ReviewAuthor = IndividualSplitter[j].split("\"")[1];
                                }

                                if(i == 2 && j == 3)
                                {
                                    NewReview.DateTime = IndividualSplitter[j].split("\"")[1];
                                }

                                if(i == 4 && j == 5)
                                {
                                    String Description = "";
                                    int Index = 0;

                                    if(!IndividualSplitter[j].contains("reviewRating"))
                                    {
                                        for(int k = j; k < IndividualSplitter.length; k++)
                                        {
                                            if(IndividualSplitter[k].contains("reviewRating"))
                                            {
                                                Index++;
                                                break;
                                            }
                                            else
                                            {
                                                Index++;
                                            }
                                        }

                                        for(int k = 0; k < Index; k++)
                                        {
                                            Description += IndividualSplitter[j + k];
                                        }
                                    }
                                    else
                                    {
                                        Description = IndividualSplitter[j];
                                    }

                                    Description = Description.substring(1);
                                    Description = Description.replace("<p>", "");
                                    Description = Description.replace("</p>", "");
                                    Description = Description.replace("<em>", "");
                                    Description = Description.replace("</em>", "");
                                    Description = Description.replace("<span>", "");
                                    Description = Description.replace("</span>", "");
                                    Description = Description.replace("<del>", "");
                                    Description = Description.replace("</del>", "");
                                    Description = Description.replace("<br>", "");
                                    Description = Description.replace("</br>", "");
                                    Description = Description.replace("&nbsp;", "");
                                    Description = Description.replace("\",\"reviewRating\"", "");

                                    if(Description.contains("\\n"))
                                    {
                                        String[] Splitter = Description.split("\\\\n");
                                        NewReview.ReviewDescription = new String[Splitter.length];

                                        for(int k = 0; k < Splitter.length; k++)
                                        {
                                            if(Splitter[k].contains("\\n"))
                                            {
                                                Splitter[k] = Splitter[k].replace("\\n", "");
                                            }

                                            NewReview.ReviewDescription[k] = Splitter[k];
                                        }
                                    }
                                    else
                                    {
                                        Log.println(Log.INFO, "Hi", "Does not Contain BackslashN");

                                        NewReview.ReviewDescription = new String[1];
                                        NewReview.ReviewDescription[0] = Description;
                                    }
                                }

                                if(i == 5 && j == 3)
                                {
                                    NewReview.OverallScore = Double.parseDouble(IndividualSplitter[j].split(",")[0]);

                                    Log.println(Log.INFO, "Hi", String.valueOf(NewReview.OverallScore));
                                }
                            }
                        }

                        AllReviews.add(NewReview);
                    }

                    StoryDoc = null;
                }
                catch (IOException e)
                {
                    throw new RuntimeException(e);
                }
            }
        });

        ReviewThread.start();

        try
        {
            ReviewThread.join();
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }

        reviewAdapter = new ReviewAdapter(AllReviews);
        ReviewRV.setAdapter(reviewAdapter);
    }

    @SuppressLint("ResourceType")
    public void SwitchThemes(boolean DarkMode)
    {
        if(DarkMode)
        {
            getActivity().getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.DarkOuter));
            ReviewRV.setBackgroundColor(getResources().getColor(R.color.DarkOuter));

            TitleTXT.setTextColor(getResources().getColor(R.color.DarkText));
            AuthorTXT.setTextColor(getResources().getColor(R.color.DarkText));
            Description.setTextColor(getResources().getColor(R.color.DarkText));

            Tags.setTextColor(getResources().getColor(R.color.DarkText));
            DateCreated.setTextColor(getResources().getColor(R.color.DarkText));
            DateLastUpdated.setTextColor(getResources().getColor(R.color.DarkText));
        }
        else
        {
            getActivity().getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.LightOuter));
            ReviewRV.setBackgroundColor(getResources().getColor(R.color.LightOuter));

            TitleTXT.setTextColor(getResources().getColor(R.color.black));
            AuthorTXT.setTextColor(getResources().getColor(R.color.black));
            Description.setTextColor(getResources().getColor(R.color.black));

            Tags.setTextColor(getResources().getColor(R.color.black));
            DateCreated.setTextColor(getResources().getColor(R.color.black));
            DateLastUpdated.setTextColor(getResources().getColor(R.color.black));
        }
    }
}