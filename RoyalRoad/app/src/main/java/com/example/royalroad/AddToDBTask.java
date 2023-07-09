package com.example.royalroad;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;

public class AddToDBTask extends AsyncTask<Void, Void, Void>
{
    Book AddBook;
    Context Context;

    public AddToDBTask(Context context, Book NewBook)
    {
        this.Context = context;
        this.AddBook = NewBook;
    }

    @Override
    protected Void doInBackground(Void... voids)
    {
        DBHandler SQLiteDB = new DBHandler(Context);
        SQLiteDB.AddBook(AddBook);
        SQLiteDB.close();

        return null;
    }
}