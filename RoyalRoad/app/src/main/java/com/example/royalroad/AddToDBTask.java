package com.example.royalroad;

import android.content.Context;
import android.os.AsyncTask;

public class AddToDBTask extends AsyncTask<Void, Void, Void>
{
    Context ThisContext;
    Book AddBook;

    public AddToDBTask(Context context, Book NewBook)
    {
        this.ThisContext = context;
        this.AddBook = NewBook;
    }

    @Override
    protected Void doInBackground(Void... voids)
    {
        DBHandler SQLiteDB = new DBHandler(ThisContext);
        SQLiteDB.AddBook(AddBook);
        SQLiteDB.close();

        return null;
    }
}