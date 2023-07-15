package com.example.royalroad;

import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Arrays;

public class GetBooks extends AsyncTask<Book[], Void, Book[]>
{
    private int[] ExternalIDs;
    private Context context;
    Book[] BookList;

    public GetBooks(int[] IDs, Context CurrentContext)
    {
        this.ExternalIDs = IDs;
        this.context = CurrentContext;
        this.BookList = new Book[IDs.length];
    }

    @Override
    protected Book[] doInBackground(Book[]... arrayLists)
    {
        int[] Index = {0};

        for(int ExternalID : ExternalIDs)
        {
            Thread BookThread = new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        BookList[Index[0]] = new Book().CreateBook(context, ExternalID, false, false, false);
                    }
                    catch (InterruptedException e)
                    {
                        throw new RuntimeException(e);
                    }

                    Index[0]++;
                }
            });

            BookThread.start();
        }

        Thread CheckThread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                for(int i = 0; i < ExternalIDs.length; i++)
                {
                    while(BookList[i] != null && !BookList[i].IsCompleted)
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

        CheckThread.start();
        try
        {
            CheckThread.join();
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }

        return BookList;
    }
}