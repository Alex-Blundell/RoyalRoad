package com.example.royalroad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class SearchActivity extends AppCompatActivity
{
    Button BackBTN;
    RecyclerView SearchRV;
    BookAdapter adapter;

    private String BaseURL = "https://www.royalroad.com/fictions/search?";
    private String Title = "";
    private boolean ExpandedSearch = false;

    int FictionCount;

    Book[] SearchBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent RecieveIntent = getIntent();

        ExpandedSearch = RecieveIntent.getBooleanExtra("IsExpandedSearch", false);

        if(ExpandedSearch)
        {

        }
        else
        {
            Title = RecieveIntent.getStringExtra("Title");
        }

        SearchRV = findViewById(R.id.SearchResultsRV);

        SearchRV.setLayoutManager(new LinearLayoutManager(this));
        SearchRV.setHasFixedSize(true);

        BackBTN = findViewById(R.id.BackBTN);
        BackBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

        String URL = "";
        FictionCount = 0;

        if(ExpandedSearch)
        {

        }
        else
        {
            URL = BaseURL + "title=" + Title;
        }

        String finalURL = URL;
        Thread ResultsThread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    Document SearchDocument = Jsoup.connect(finalURL).get();
                    Elements SearchFictions = SearchDocument.getElementsByClass("row fiction-list-item");

                    FictionCount = SearchFictions.size();
                    SearchBooks = new Book[FictionCount];

                    int LoopIndex = 0;

                    for(Element Fiction : SearchFictions)
                    {
                        SearchBooks[LoopIndex] = new Book();

                        int ExternalID = Integer.parseInt(String.valueOf(Fiction.getElementsByClass("fiction-title").get(0)).split("\"")[3].split("/")[2]);
                        SearchBooks[LoopIndex] = new Book().CreateBook(getApplicationContext(), ExternalID, false, false, false);

                        LoopIndex++;
                    }
                }
                catch (IOException | InterruptedException e)
                {
                    throw new RuntimeException(e);
                }
            }
        });

        ResultsThread.start();
        try
        {
            ResultsThread.join();
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }

        Thread ResultsFinished = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                for(int i = 0; i < SearchBooks.length; i++)
                {
                    while(SearchBooks[i] == null || !SearchBooks[i].IsCompleted)
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

        ResultsFinished.start();
        try
        {
            ResultsFinished.join();
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }

        adapter = new BookAdapter(Arrays.asList(SearchBooks));
        SearchRV.setAdapter(adapter);
    }

    public void GetBooks()
    {

    }
}