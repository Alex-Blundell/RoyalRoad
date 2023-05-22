package com.example.royalroad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

public class ReadActivity extends AppCompatActivity {

    Toolbar BottomToolbar;
    Toolbar TopToolbar;

    boolean ToolbarAppeared;
    public Book ReadBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        Intent ThisIntent = getIntent();
        ReadBook = (Book)ThisIntent.getExtras().getSerializable("Book");
        boolean HasDownloaded = ThisIntent.getBooleanExtra("HasDownloaded", false);

        ToolbarAppeared = true;

        BottomToolbar = (Toolbar) findViewById(R.id.BookBottomToolbar);

        if(HasDownloaded)
        {
            BottomToolbar.inflateMenu(R.menu.bookbottommenudownloaded);
        }
        else
        {
            BottomToolbar.inflateMenu(R.menu.bookbottommenu);
        }

        TopToolbar = (Toolbar) findViewById(R.id.BookTopToolbar);
        TopToolbar.inflateMenu(R.menu.booktoptoolbar);

        FrameLayout FragmentFrame = findViewById(R.id.ReadFragmentFrame);

        FragmentFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ToolbarAppeared && ReadBook.HasRead)
                {
                    BottomToolbar.setVisibility(View.GONE);
                    TopToolbar.setVisibility(View.GONE);

                    ToolbarAppeared = false;
                }
                else
                {
                    BottomToolbar.setVisibility(View.VISIBLE);
                    TopToolbar.setVisibility(View.VISIBLE);

                    ToolbarAppeared = true;
                }
            }
        });

        if(ReadBook.HasRead)
        {
            ReplaceFragment(new ChapterFragment());
        }
        else
        {
            ReplaceFragment(new BookHomeFragment());
        }
    }

    public void ReplaceFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.ReadFragmentFrame, fragment);
        fragmentTransaction.commit();
    }
}