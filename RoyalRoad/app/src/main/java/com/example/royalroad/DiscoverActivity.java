package com.example.royalroad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Debug;
import android.os.SystemClock;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.annotations.concurrent.Background;
import com.google.firebase.firestore.DocumentSnapshot;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DiscoverActivity extends AppCompatActivity {

    ImageView NewsBackground;
    TextView NewsTitle;
    TextView NewsDescription;

    String[] BackgroundURLs;
    String[] Titles;
    String[] Descriptions;

    private BookAdapter LatestUpdateAdapter;
    private BookAdapter RisingStarsAdapter;
    private BookAdapter BestCompletedAdapter;
    private BookAdapter BestOngoingAdapter;

    private RecyclerView LatestUpdatesRV;
    private RecyclerView RisingStarsRV;
    private RecyclerView BestCompletedRV;
    private RecyclerView BestOngoingRV;

    Book[] LatestUpdates;
    Book[] RisingStars;
    Book[] PopularThisWeek;
    Book[] BestCompleted;
    Book[] BestOngoing;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);

        Book DebugBook = new Book();

        DebugBook.SetExternalID(0);
        DebugBook.SetType(Book.BookType.Original);

        DebugBook.SetTitle("Debug Book");
        DebugBook.SetAuthor("Debug Book");
        DebugBook.SetDescription("Debug Book");

        DebugBook.SetCover("https://www.royalroad.com/dist/img/nocover-new-min.png");

        DebugBook.SetAllChapters(new ArrayList<>());

        DebugBook.SetPageCount(100);
        DebugBook.SetFollowerCount(100);
        DebugBook.SetFavouriteCount(100);

        DebugBook.SetWarnings(new ArrayList<>());
        DebugBook.SetGenres(new ArrayList<>());
        DebugBook.SetTags(new ArrayList<>());

        DebugBook.SetRating(2.5);

        DebugBook.SetCreatedDateTime(new GregorianCalendar());
        DebugBook.SetLastUpdatedDateTime(new GregorianCalendar());
        DebugBook.SetDownloadedDateTime(new GregorianCalendar());

        DebugBook.SetHasRead(false);
        DebugBook.SetLastReadChapter(0);

        LatestUpdatesRV = findViewById(R.id.LatestUpdatesList);
        RisingStarsRV = findViewById(R.id.RisingStartsList);
        BestCompletedRV = findViewById(R.id.BestCompletedList);
        BestOngoingRV = findViewById(R.id.BestOngoingList);

        LatestUpdatesRV.setLayoutManager(new LinearLayoutManager(this));
        LatestUpdatesRV.setHasFixedSize(true);

        RisingStarsRV.setLayoutManager(new LinearLayoutManager(this));
        RisingStarsRV.setHasFixedSize(true);

        BestCompletedRV.setLayoutManager(new LinearLayoutManager(this));
        BestCompletedRV.setHasFixedSize(true);

        BestOngoingRV.setLayoutManager(new LinearLayoutManager(this));
        BestOngoingRV.setHasFixedSize(true);

        LatestUpdates = new Book[10];
        RisingStars = new Book[7];
        PopularThisWeek = new Book[10];
        BestCompleted = new Book[10];
        BestOngoing = new Book[10];

        for(int i = 0; i < LatestUpdates.length; i++)
        {
            LatestUpdates[i] = DebugBook;
        }

        for(int i = 0; i < RisingStars.length; i++)
        {
            RisingStars[i] = DebugBook;
        }

        for(int i = 0; i < BestCompleted.length; i++)
        {
            BestCompleted[i] = DebugBook;
        }

        for(int i = 0; i < BestOngoing.length; i++)
        {
            BestOngoing[i] = DebugBook;
        }

        LatestUpdateAdapter = new BookAdapter(Arrays.asList(LatestUpdates));
        LatestUpdatesRV.setAdapter(LatestUpdateAdapter);

        RisingStarsAdapter = new BookAdapter(Arrays.asList(RisingStars));
        RisingStarsRV.setAdapter(RisingStarsAdapter);

        BestCompletedAdapter = new BookAdapter(Arrays.asList(BestCompleted));
        BestCompletedRV.setAdapter(BestCompletedAdapter);

        BestOngoingAdapter = new BookAdapter(Arrays.asList(BestOngoing));
        BestOngoingRV.setAdapter(BestOngoingAdapter);

        BackgroundURLs = null;
        Titles = null;
        Descriptions = null;

        SharedPreferences Pref = getSharedPreferences("Settings", MODE_PRIVATE);
        boolean IsDarkMode = Pref.getBoolean("AppTheme", false);

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean IsOnline = (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED);

        NewsBackground = findViewById(R.id.NewsBackground);
        NewsTitle = findViewById(R.id.NewsTitle);
        NewsDescription = findViewById(R.id.NewsDescription);

        NewsTitle.setTextColor(getColor(R.color.ToolbarItem));
        NewsDescription.setTextColor(getColor(R.color.white));

        SwitchThemes(IsDarkMode);

        if(IsOnline)
        {
            GetHomePage();
        }
        else
        {
            Toast.makeText(this, "Connect to internet.", Toast.LENGTH_SHORT).show();
        }
    }

    void GetHomePage()
    {
        Book[] NewLatestUpdates = new Book[10];
        Book[] NewRisingStars = new Book[7];
        Book[] NewBestCompleted = new Book[10];
        Book[] NewBestOngoing = new Book[10];

        NewLatestUpdates[0] = new Book();
        NewLatestUpdates[1] = new Book();
        NewLatestUpdates[2] = new Book();
        NewLatestUpdates[3] = new Book();
        NewLatestUpdates[4] = new Book();
        NewLatestUpdates[5] = new Book();
        NewLatestUpdates[6] = new Book();
        NewLatestUpdates[7] = new Book();
        NewLatestUpdates[8] = new Book();
        NewLatestUpdates[9] = new Book();

        NewRisingStars[0] = new Book();
        NewRisingStars[1] = new Book();
        NewRisingStars[2] = new Book();
        NewRisingStars[3] = new Book();
        NewRisingStars[4] = new Book();
        NewRisingStars[5] = new Book();
        NewRisingStars[6] = new Book();

        NewBestCompleted[0] = new Book();
        NewBestCompleted[1] = new Book();
        NewBestCompleted[2] = new Book();
        NewBestCompleted[3] = new Book();
        NewBestCompleted[4] = new Book();
        NewBestCompleted[5] = new Book();
        NewBestCompleted[6] = new Book();
        NewBestCompleted[7] = new Book();
        NewBestCompleted[8] = new Book();
        NewBestCompleted[9] = new Book();

        NewBestOngoing[0] = new Book();
        NewBestOngoing[1] = new Book();
        NewBestOngoing[2] = new Book();
        NewBestOngoing[3] = new Book();
        NewBestOngoing[4] = new Book();
        NewBestOngoing[5] = new Book();
        NewBestOngoing[6] = new Book();
        NewBestOngoing[7] = new Book();
        NewBestOngoing[8] = new Book();
        NewBestOngoing[9] = new Book();

        String HomeURL = "https://www.royalroad.com/home";

        try
        {
            Thread DocumentThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Document Doc = null;

                    try
                    {
                        Doc = Jsoup.connect(HomeURL).get();
                    }
                    catch (IOException e)
                    {
                        throw new RuntimeException(e);
                    }

                    Elements NewsElements = Doc.getElementsByClass("promotion");
                    Elements BooksElements = Doc.getElementsByClass("portlet light");

                    // News Carousel.
                    BackgroundURLs = new String[NewsElements.size()];
                    Titles = new String[NewsElements.size()];
                    Descriptions = new String[NewsElements.size()];

                    for (int i = 0; i < NewsElements.size(); i++)
                    {
                        String RawURL = NewsElements.get(i).child(0).child(0).toString();
                        String[] BackgroundSplitter = RawURL.split("\"");
                        String BackgroundURL = BackgroundSplitter[1];

                        BackgroundURLs[i] = BackgroundURL;

                        Titles[i] = BackgroundSplitter[3];

                        Elements SubElements = NewsElements.get(i).getElementsByClass("synopsis font-white col-sm-8 margin-top-10 hidden-xs");
                        Descriptions[i] = SubElements.get(0).text();
                    }

                    NewsElements.clear();
                    for (int i = 0; i < BooksElements.size(); i++)
                    {
                        if (i != 2)
                        {
                            Elements ListElements = BooksElements.get(i).getElementsByClass("mt-list-item no-border inline-block");

                            for (int j = 0; j < ListElements.size(); j++)
                            {
                                int ExternalID = Integer.parseInt(ListElements.get(j).getElementsByClass("font-red").get(0).attr("href").split("/")[2]);

                                if (i == 0)
                                {
                                    try
                                    {
                                        NewLatestUpdates[j] = new Book().CreateBook(DiscoverActivity.this, ExternalID, false, false, false);
                                    }
                                    catch (InterruptedException e)
                                    {
                                        throw new RuntimeException(e);
                                    }
                                }
                                else if (i == 1)
                                {
                                    try
                                    {
                                        NewRisingStars[j] = new Book().CreateBook(DiscoverActivity.this, ExternalID, false, false, false);
                                    }
                                    catch (InterruptedException e)
                                    {
                                        throw new RuntimeException(e);
                                    }
                                }
                                else if (i == 3)
                                {
                                    try
                                    {
                                        NewBestCompleted[j] = new Book().CreateBook(DiscoverActivity.this, ExternalID, false, false, false);
                                    } catch (InterruptedException e)
                                    {
                                        throw new RuntimeException(e);
                                    }
                                }
                                else if (i == 4)
                                {
                                    try
                                    {
                                        NewBestOngoing[j] = new Book().CreateBook(DiscoverActivity.this, ExternalID, false, false, false);
                                    }
                                    catch (InterruptedException e)
                                    {
                                        throw new RuntimeException(e);
                                    }
                                }
                            }
                            ListElements.clear();
                        }
                    }

                    BooksElements.clear();
                }
            });

            DocumentThread.start();
            DocumentThread.join();

            // Set News Display.
            Glide.with(this)
                    .load(BackgroundURLs[0])
                    .into(NewsBackground);

            NewsTitle.setText(Titles[0]);
            NewsDescription.setText(Descriptions[0]);

            Thread UpdateStoriesThread = new Thread(new Runnable() {
                @Override
                public void run()
                {
                    int Index = 0;

                    for(Book ThisBook : NewLatestUpdates)
                    {
                        while(!ThisBook.IsCompleted && LatestUpdateAdapter.getItemCount() == 10)
                        {
                            try
                            {
                                Thread.sleep(50);
                            }
                            catch (InterruptedException e)
                            {
                                throw new RuntimeException(e);
                            }
                        }

                        int finalIndex = Index;
                        LatestUpdatesRV.post(new Runnable() {
                            @Override
                            public void run()
                            {
                                LatestUpdateAdapter.Data.set(finalIndex, ThisBook);
                                LatestUpdateAdapter.notifyItemChanged(finalIndex);
                            }
                        });

                        Index++;
                    }

                    Index = 0;

                    for(Book ThisBook : NewRisingStars)
                    {
                        while(!ThisBook.IsCompleted)
                        {
                            try
                            {
                                Thread.sleep(50);
                            }
                            catch (InterruptedException e)
                            {
                                throw new RuntimeException(e);
                            }
                        }

                        int finalIndex = Index;
                        RisingStarsRV.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                RisingStarsAdapter.Data.set(finalIndex, ThisBook);
                                RisingStarsAdapter.notifyItemChanged(finalIndex);
                            }
                        });

                        Index++;
                    }

                    Index = 0;

                    for(Book ThisBook : NewBestCompleted)
                    {
                        while(!ThisBook.IsCompleted)
                        {
                            try
                            {
                                Thread.sleep(50);
                            }
                            catch (InterruptedException e)
                            {
                                throw new RuntimeException(e);
                            }
                        }

                        BestCompletedAdapter.Data.set(Index, ThisBook);
                        BestCompletedAdapter.notifyItemChanged(Index);

                        Index++;
                    }

                    Index = 0;

                    for(Book ThisBook : NewBestOngoing)
                    {
                        while(!ThisBook.IsCompleted)
                        {
                            try
                            {
                                Thread.sleep(50);
                            }
                            catch (InterruptedException e)
                            {
                                throw new RuntimeException(e);
                            }
                        }

                        BestOngoingAdapter.Data.set(Index, ThisBook);
                        BestOngoingAdapter.notifyItemChanged(Index);

                        Index++;
                    }
                }
            });

            UpdateStoriesThread.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    void SwitchThemes(boolean DarkMode)
    {
        if(DarkMode)
        {
            getWindow().getDecorView().setBackgroundColor(getColor(R.color.DarkOuter));

            LatestUpdatesRV.setBackgroundColor(getColor(R.color.DarkInner));
            RisingStarsRV.setBackgroundColor(getColor(R.color.DarkInner));
            BestCompletedRV.setBackgroundColor(getColor(R.color.DarkInner));
            BestOngoingRV.setBackgroundColor(getColor(R.color.DarkInner));
        }
        else
        {
            getWindow().getDecorView().setBackgroundColor(getColor(R.color.LightOuter));

            LatestUpdatesRV.setBackgroundColor(getColor(R.color.white));
            RisingStarsRV.setBackgroundColor(getColor(R.color.white));
            BestCompletedRV.setBackgroundColor(getColor(R.color.white));
            BestOngoingRV.setBackgroundColor(getColor(R.color.white));
        }
    }
}