package com.example.royalroad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

public class ReadActivity extends AppCompatActivity {

    Toolbar BottomToolbar;
    Toolbar TopToolbar;

    private int ChapterNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        BottomToolbar = (Toolbar) findViewById(R.id.BookBottomToolbar);
        BottomToolbar.inflateMenu(R.menu.bookbottommenu);

        TopToolbar = (Toolbar) findViewById(R.id.BookTopToolbar);
        TopToolbar.inflateMenu(R.menu.booktoptoolbar);
    }

    public void OpenBook(Book SelectedBook)
    {

    }
}