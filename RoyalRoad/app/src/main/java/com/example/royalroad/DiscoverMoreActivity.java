package com.example.royalroad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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

    Button FirstBTN;
    Button PreviousBTN;
    Button OneBTN;
    Button TwoBTN;
    Button ThreeBTN;
    Button FourBTN;
    Button FiveBTN;
    Button NextBTN;
    Button LastBTN;

    ScrollView Scroller;
    RelativeLayout PagesRL;

    int PageCount;
    int PageNum = 1;

    private boolean HasPages = false;

    Book[] NewBooks;
    String URL = "";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover_more);

        Intent RecieveIntent =  getIntent();
        String SelectedType = RecieveIntent.getStringExtra("Type");

        PagesRL = findViewById(R.id.PagesLayout);

        FirstBTN = findViewById(R.id.FirstBTN);
        PreviousBTN = findViewById(R.id.PreviousBTN);

        OneBTN = findViewById(R.id.OneBTN);
        TwoBTN = findViewById(R.id.TwoBTN);
        ThreeBTN = findViewById(R.id.ThreeBTN);
        FourBTN = findViewById(R.id.FourBTN);
        FiveBTN = findViewById(R.id.FiveBTN);

        NextBTN = findViewById(R.id.NextBTN);
        LastBTN = findViewById(R.id.LastBTN);

        Scroller = findViewById(R.id.Scoller);

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

                PagesRL.setVisibility(View.GONE);
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

        GetBooks(URL);

        BackBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

        FirstBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Scroller.smoothScrollTo(0,0);

                PageNum = 1;
                DrawPages();

                String CurrentURL = URL + "?page=" + PageNum;
                GetBooks(CurrentURL);
            }
        });

        PreviousBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Previous();
            }
        });

        OneBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Scroller.smoothScrollTo(0,0);

                PageNum = Integer.parseInt((String) OneBTN.getText());
                DrawPages();

                String CurrentURL = URL + "?page=" + PageNum;
                GetBooks(CurrentURL);
            }
        });
        TwoBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Scroller.smoothScrollTo(0,0);

                PageNum = Integer.parseInt((String) TwoBTN.getText());
                DrawPages();

                String CurrentURL = URL + "?page=" + PageNum;
                GetBooks(CurrentURL);
            }
        });
        ThreeBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Scroller.smoothScrollTo(0,0);

                PageNum = Integer.parseInt((String) ThreeBTN.getText());
                DrawPages();

                String CurrentURL = URL + "?page=" + PageNum;
                GetBooks(CurrentURL);
            }
        });
        FourBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Scroller.smoothScrollTo(0,0);

                PageNum = Integer.parseInt((String) FourBTN.getText());
                DrawPages();

                String CurrentURL = URL + "?page=" + PageNum;
                GetBooks(CurrentURL);
            }
        });
        FiveBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Scroller.smoothScrollTo(0,0);

                PageNum = Integer.parseInt((String) FiveBTN.getText());
                DrawPages();

                String CurrentURL = URL + "?page=" + PageNum;
                GetBooks(CurrentURL);
            }
        });

        NextBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Next();
            }
        });

        LastBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Scroller.smoothScrollTo(0,0);

                PageNum = PageCount;
                DrawPages();

                String CurrentURL = URL + "?page=" + PageNum;
                GetBooks(CurrentURL);
            }
        });

        DrawPages();
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

    private void GetBooks(String ThisURL)
    {
        Log.println(Log.INFO, "Hi", ThisURL);

        String finalURL = ThisURL;
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
                    Elements Pages = URLDocument.getElementsByClass("pagination");

                    if(Pages.size() > 0)
                    {
                        HasPages = true;
                        PageCount = Integer.parseInt(Pages.first().children().last().children().first().attributes().get("data-page"));
                    }

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
    }

    public void Next()
    {
        Scroller.smoothScrollTo(0,0);

        PageNum++;
        DrawPages();

        String CurrentURL = URL + "?page=" + PageNum;
        GetBooks(CurrentURL);
    }

    public void Previous()
    {
        Scroller.smoothScrollTo(0,0);

        PageNum--;
        DrawPages();

        String CurrentURL = URL + "?page=" + PageNum;
        GetBooks(CurrentURL);
    }

    void DrawPages()
    {
        if(PageNum == 1)
        {
            FirstBTN.setVisibility(View.GONE);
            PreviousBTN.setVisibility(View.GONE);

            OneBTN.setBackgroundColor(getColor(R.color.ButtonInner));
            TwoBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));
            ThreeBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));
            FourBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));
            FiveBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));

            NextBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));
            LastBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));

            NextBTN.setVisibility(View.VISIBLE);
            LastBTN.setVisibility(View.VISIBLE);

            OneBTN.setText(String.valueOf(PageNum));
            TwoBTN.setText(String.valueOf(PageNum + 1));
            ThreeBTN.setText(String.valueOf(PageNum + 2));
            FourBTN.setText(String.valueOf(PageNum + 3));
            FiveBTN.setText(String.valueOf(PageNum + 4));
        }
        else if(PageNum == 2)
        {
            FirstBTN.setVisibility(View.VISIBLE);
            PreviousBTN.setVisibility(View.VISIBLE);

            FirstBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));
            PreviousBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));

            OneBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));
            TwoBTN.setBackgroundColor(getColor(R.color.ButtonInner));
            ThreeBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));
            FourBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));
            FiveBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));

            NextBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));
            LastBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));

            NextBTN.setVisibility(View.VISIBLE);
            LastBTN.setVisibility(View.VISIBLE);

            OneBTN.setText(String.valueOf(PageNum - 1));
            TwoBTN.setText(String.valueOf(PageNum));
            ThreeBTN.setText(String.valueOf(PageNum + 1));
            FourBTN.setText(String.valueOf(PageNum + 2));
            FiveBTN.setText(String.valueOf(PageNum + 3));
        }
        else if(PageNum == PageCount - 1)
        {
            FirstBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));
            PreviousBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));

            FirstBTN.setVisibility(View.VISIBLE);
            PreviousBTN.setVisibility(View.VISIBLE);

            OneBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));
            TwoBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));
            ThreeBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));
            FourBTN.setBackgroundColor(getColor(R.color.ButtonInner));
            FiveBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));

            NextBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));
            LastBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));

            NextBTN.setVisibility(View.VISIBLE);
            LastBTN.setVisibility(View.VISIBLE);

            OneBTN.setText(String.valueOf(PageNum - 3));
            TwoBTN.setText(String.valueOf(PageNum - 2));
            ThreeBTN.setText(String.valueOf(PageNum - 1));
            FourBTN.setText(String.valueOf(PageNum));
            FiveBTN.setText(String.valueOf(PageNum + 1));
        }
        else if(PageNum == PageCount)
        {
            FirstBTN.setVisibility(View.VISIBLE);
            PreviousBTN.setVisibility(View.VISIBLE);

            FirstBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));
            PreviousBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));

            OneBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));
            TwoBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));
            ThreeBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));
            FourBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));
            FiveBTN.setBackgroundColor(getColor(R.color.ButtonInner));

            NextBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));
            LastBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));

            NextBTN.setVisibility(View.GONE);
            LastBTN.setVisibility(View.GONE);

            OneBTN.setText(String.valueOf(PageNum - 4));
            TwoBTN.setText(String.valueOf(PageNum - 3));
            ThreeBTN.setText(String.valueOf(PageNum - 2));
            FourBTN.setText(String.valueOf(PageNum - 1));
            FiveBTN.setText(String.valueOf(PageNum));
        }
        else
        {
            FirstBTN.setVisibility(View.VISIBLE);
            PreviousBTN.setVisibility(View.VISIBLE);

            FirstBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));
            PreviousBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));

            OneBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));
            TwoBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));
            ThreeBTN.setBackgroundColor(getColor(R.color.ButtonInner));
            FourBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));
            FiveBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));

            NextBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));
            LastBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));

            NextBTN.setVisibility(View.VISIBLE);
            LastBTN.setVisibility(View.VISIBLE);

            OneBTN.setText(String.valueOf(PageNum - 2));
            TwoBTN.setText(String.valueOf(PageNum - 1));
            ThreeBTN.setText(String.valueOf(PageNum));
            FourBTN.setText(String.valueOf(PageNum + 1));
            FiveBTN.setText(String.valueOf(PageNum + 2));
        }
    }
}