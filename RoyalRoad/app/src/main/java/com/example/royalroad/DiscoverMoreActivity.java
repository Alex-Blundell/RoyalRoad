package com.example.royalroad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class DiscoverMoreActivity extends AppCompatActivity
{
    public enum DiscoverMoreType
    {
        LatestUpdates,
        RisingStars,
        PopularThisWeek,
        BestOngoing,
        BestCompleted
    }

    DiscoverMoreType Type;

    Button BackBTN;
    RecyclerView BookRV;
    BookAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover_more);

        Intent RecieveIntent =  getIntent();
        String SelectedType = RecieveIntent.getStringExtra("Type");

        if(SelectedType.equals("Latest Updates"))
        {
            Type = DiscoverMoreType.LatestUpdates;
        }
        else if(SelectedType.equals("Rising Stars"))
        {
            Type = DiscoverMoreType.RisingStars;
        }
        else if(SelectedType.equals("Popular This Week"))
        {
            Type = DiscoverMoreType.PopularThisWeek;
        }
        else if(SelectedType.equals("Best Completed"))
        {
            Type = DiscoverMoreType.BestCompleted;
        }
        else if(SelectedType.equals("Best Ongoing"))
        {
            Type = DiscoverMoreType.BestOngoing;
        }

        BackBTN = findViewById(R.id.BackBTN);
        BookRV = findViewById(R.id.MoreBookRV);

        Book[] NewBooks = new Book[0];
        String URL = "";

        BookRV.setLayoutManager(new LinearLayoutManager(this));
        BookRV.setHasFixedSize(true);

        SharedPreferences Pref = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        boolean IsDarkMode = Pref.getBoolean("AppTheme", false);

        SwitchThemes(IsDarkMode);

        switch(Type)
        {
            case LatestUpdates:
                URL = "https://www.royalroad.com/fictions/latest-updates";
                NewBooks = new Book[20];

                BackBTN.setText("Latest Updates");
                break;

            case RisingStars:
                URL = "https://www.royalroad.com/fictions/rising-stars";
                NewBooks = new Book[50];

                BackBTN.setText("Rising Stars");
                break;

            case PopularThisWeek:
                URL = "https://www.royalroad.com/fictions/weekly-popular";
                NewBooks = new Book[20];

                BackBTN.setText("Popular This Week");
                break;

            case BestOngoing:
                URL = "https://www.royalroad.com/fictions/active-popular";
                NewBooks = new Book[20];

                BackBTN.setText("Best Ongoing");
                break;

            case BestCompleted:
                URL = "https://www.royalroad.com/fictions/complete";
                NewBooks = new Book[20];

                BackBTN.setText("Best Completed");
                break;
        }

        String finalURL = URL;
        Book[] finalNewBooks = NewBooks;

        Thread DocumentThread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    int FictionIndex = 0;

                    Document URLDocument = Jsoup.connect(finalURL).get();
                    Elements FictionElements = URLDocument.getElementsByClass("fiction-list-item row");

                    for(Element Fiction : FictionElements)
                    {
                        int ExternalID = Integer.parseInt(String.valueOf(Fiction.getElementsByClass("fiction-title").get(0)).split("\"")[3].split("/")[2]);
                        finalNewBooks[FictionIndex] = new Book().CreateBook(DiscoverMoreActivity.this, ExternalID, false, false, false);

                        FictionIndex++;
                    }
                }
                catch (IOException e)
                {
                    throw new RuntimeException(e);
                }
                catch (InterruptedException e)
                {
                    throw new RuntimeException(e);
                }
            }
        });

        DocumentThread.start();

        try
        {
            DocumentThread.join();
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }

        Thread BookThread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                for(int i = 0; i < finalNewBooks.length; i++)
                {
                    while(!finalNewBooks[i].IsCompleted || finalNewBooks[i] == null)
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
                }
            }
        });

        BookThread.start();

        try
        {
            BookThread.join();
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }

        adapter = new BookAdapter(Arrays.asList(finalNewBooks));
        BookRV.setAdapter(adapter);

        BackBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
    }

    private void SwitchThemes(boolean DarkMode)
    {
        if(DarkMode)
        {
            getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.DarkOuter));
            BookRV.setBackgroundColor(getResources().getColor(R.color.DarkOuter));
            BackBTN.setTextColor(getResources().getColor(R.color.DarkText));
        }
        else
        {
            getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.LightOuter));
            BookRV.setBackgroundColor(getResources().getColor(R.color.LightOuter));
            BackBTN.setTextColor(getResources().getColor(R.color.black));
        }
    }
}