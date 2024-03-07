package com.example.royalroad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Debug;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.annotations.concurrent.Background;
import com.google.firebase.firestore.DocumentSnapshot;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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

    Button MoreLatestUpdates;
    Button MoreRisingStars;
    Button MorePopularThisWeek;
    Button MoreBestCompleted;
    Button MoreBestOngoing;
    Button AdvancedSearchBTN;

    SearchView Searchbar;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);

        Searchbar = findViewById(R.id.Searchbar);
        AdvancedSearchBTN = findViewById(R.id.AdvancedSearchBTN);

        AdvancedSearchBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent SearchResultsIntent = new Intent(DiscoverActivity.this, SearchActivity.class);
                SearchResultsIntent.putExtra("IsExpandedSearch", true);

                startActivity(SearchResultsIntent);
            }
        });

        Searchbar.setIconifiedByDefault(false);
        Searchbar.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String s)
            {
                Searchbar.setQuery("", false);
                Searchbar.clearFocus();

                Intent SearchResultsIntent = new Intent(DiscoverActivity.this, SearchActivity.class);

                SearchResultsIntent.putExtra("IsExpandedSearch", false);
                SearchResultsIntent.putExtra("Title", s);

                startActivity(SearchResultsIntent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s)
            {
                return false;
            }
        });

        Book DebugBook = new Book();

        DebugBook.SetType(Book.BookType.Original);

        DebugBook.SetDescription("");

        DebugBook.SetAllChapters(new ArrayList<>());

        DebugBook.SetWarnings(new ArrayList<>());
        DebugBook.SetGenres(new ArrayList<>());
        DebugBook.SetTags(new ArrayList<>());

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

        MoreLatestUpdates = findViewById(R.id.MoreUpdatesBTN);
        MoreLatestUpdates.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent MoreIntent = new Intent(DiscoverActivity.this, DiscoverMoreActivity.class);
                MoreIntent.putExtra("Type", "Latest Updates");
                startActivity(MoreIntent);
            }
        });

        MoreRisingStars = findViewById(R.id.MoreRisingBTN);
        MoreRisingStars.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent MoreIntent = new Intent(DiscoverActivity.this, DiscoverMoreActivity.class);
                MoreIntent.putExtra("Type", "Rising Stars");
                startActivity(MoreIntent);
            }
        });

        /*
        MorePopularThisWeek = findViewById(R.id.MoreUpdatesBTN);
        MorePopularThisWeek.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent MoreIntent = new Intent(DiscoverActivity.this, DiscoverMoreActivity.class);
                MoreIntent.putExtra("Type", "Popular This Week");
                startActivity(MoreIntent);
            }
        });
         */

        MoreBestCompleted = findViewById(R.id.MoreBestCompletedBTN);
        MoreBestCompleted.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent MoreIntent = new Intent(DiscoverActivity.this, DiscoverMoreActivity.class);
                MoreIntent.putExtra("Type", "Best Completed");
                startActivity(MoreIntent);
            }
        });

        MoreBestOngoing = findViewById(R.id.MoreBestOngoingBTN);
        MoreBestOngoing.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent MoreIntent = new Intent(DiscoverActivity.this, DiscoverMoreActivity.class);
                MoreIntent.putExtra("Type", "Best Ongoing");
                startActivity(MoreIntent);
            }
        });

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
                    Elements BooksElements = Doc.getElementsByClass("portlet-body");

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
                    int Index = 0;
                    for (Element CurrentList : BooksElements)
                    {
                        Elements ListElements = BooksElements.get(Index).getElementsByClass("mt-list-item");

                        for (int j = 0; j < ListElements.size(); j++)
                        {
                            int ExternalID = Integer.parseInt(ListElements.get(j).getElementsByAttribute("href").get(1).attr("href").split("/")[2]);

                            if (Index == 0)
                            {
                                try
                                {
                                    NewLatestUpdates[j] = new Book().CreateBook(getApplicationContext(), ExternalID, false, false, false);
                                }
                                catch (InterruptedException e)
                                {
                                    throw new RuntimeException(e);
                                }
                            }
                            else if (Index == 1)
                            {
                                try
                                {
                                    NewRisingStars[j] = new Book().CreateBook(getApplicationContext(), ExternalID, false, false, false);
                                }
                                catch (InterruptedException e)
                                {
                                    throw new RuntimeException(e);
                                }
                            }
                            else if (Index <= 3)
                            {
                                try
                                {
                                    NewBestCompleted[j] = new Book().CreateBook(getApplicationContext(), ExternalID, false, false, false);
                                } catch (InterruptedException e)
                                {
                                    throw new RuntimeException(e);
                                }
                            }
                            else if (Index <= 4)
                            {
                                try
                                {
                                    NewBestOngoing[j] = new Book().CreateBook(getApplicationContext(), ExternalID, false, false, false);
                                }
                                catch (InterruptedException e)
                                {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                        ListElements.clear();

                        Index++;
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
                        while(!ThisBook.IsCompleted && BestCompletedAdapter.getItemCount() == 10)
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
                        BestCompletedRV.post(new Runnable() {
                            @Override
                            public void run()
                            {
                                BestCompletedAdapter.Data.set(finalIndex, ThisBook);
                                BestCompletedAdapter.notifyItemChanged(finalIndex);
                            }
                        });

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