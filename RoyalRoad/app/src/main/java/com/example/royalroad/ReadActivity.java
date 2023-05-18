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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        Intent ReadIntent = getIntent();

        ToolbarAppeared = true;

        BottomToolbar = (Toolbar) findViewById(R.id.BookBottomToolbar);
        BottomToolbar.inflateMenu(R.menu.bookbottommenu);

        TopToolbar = (Toolbar) findViewById(R.id.BookTopToolbar);
        TopToolbar.inflateMenu(R.menu.booktoptoolbar);

        FrameLayout FragmentFrame = findViewById(R.id.ReadFragmentFrame);

        boolean HasRead = ReadIntent.getBooleanExtra("HasRead", false);
        int CurrentChapter = 0;

        if(HasRead)
        {
            CurrentChapter = ReadIntent.getIntExtra("LastReadChapter", 0);
        }

        int finalCurrentChapter = CurrentChapter;

        FragmentFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ToolbarAppeared && finalCurrentChapter != 0)
                {
                    BottomToolbar.setVisibility(View.GONE);
                    TopToolbar.setVisibility(View.GONE);
                }
                else
                {
                    BottomToolbar.setVisibility(View.VISIBLE);
                    TopToolbar.setVisibility(View.VISIBLE);
                }
            }
        });

        if(CurrentChapter == 0) // Chapter 0 is BookHome.
        {
            ReplaceFragment(new BookHomeFragment());
        }
        else // Show Chapter.
        {
            ReplaceFragment(new ChapterFragment());
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