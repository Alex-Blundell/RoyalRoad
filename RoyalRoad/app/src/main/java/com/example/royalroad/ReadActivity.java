package com.example.royalroad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.google.firebase.firestore.FirebaseFirestore;

public class ReadActivity extends AppCompatActivity {

    Toolbar BottomToolbar;
    Toolbar TopToolbar;

    private ViewPager2 BookPager;
    boolean ToolbarAppeared;
    public Book ReadBook;
    public Button BackBTN;
    public Button ChapterCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        Intent ThisIntent = getIntent();
        ReadBook = (Book)ThisIntent.getExtras().getSerializable("Book");
        boolean HasDownloaded = ThisIntent.getBooleanExtra("HasDownloaded", false);

        BackBTN = findViewById(R.id.BackBTN);
        ChapterCount = findViewById(R.id.ChapterCount);

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

        BottomToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                switch (item.getOrder())
                {
                    case 1:
                        DownloadStory();
                        break;

                    case 2:
                        OpenProfile();
                        break;

                    case 3:
                        WriteReview();
                        break;

                    case 4:
                        FavouriteBook();
                        break;

                    case 5:
                        FollowBook();
                        break;

                    case 6:
                        ShareBook();
                        break;
                }

                return false;
            }
        });

        BookPager = findViewById(R.id.ReadPager);
        VPAdapter vpAdapter = new VPAdapter(getSupportFragmentManager(), getLifecycle());

        vpAdapter.AddFragment(new BookHomeFragment());

        for(int i = 0; i < ReadBook.Chapters.size(); i++)
        {
            vpAdapter.AddFragment(new ChapterFragment(i));
        }

        BookPager.setAdapter(vpAdapter);
        BookPager.setCurrentItem(0);

        BookPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position)
            {
                if(position == 0)
                {
                    ChapterCount.setText("Home / " + ReadBook.Chapters.size());
                }
                else
                {
                    DBHandler SQLiteDB = new DBHandler(getApplicationContext());
                    SQLiteDB.UpdateLastReadChapter(ReadBook.GetExternalID(), position);
                    SQLiteDB.close();

                    ChapterCount.setText(position + " / " + ReadBook.Chapters.size());
                }
            }
        });

        BackBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

        BackBTN.setText(ReadBook.Title);

        if(ReadBook.HasRead)
        {
            if(ReadBook.LastReadChapter >= 0)
            {
                BookPager.setCurrentItem(ReadBook.LastReadChapter);
            }
            else
            {
                // Has Read but Last Chapter is Home Page.
                BookPager.setCurrentItem(0);
            }
        }
        else
        {
            BookPager.setCurrentItem(0);

            DBHandler SQLiteDB = new DBHandler(this);
            SQLiteDB.UpdateHasRead(ReadBook.GetExternalID());
            SQLiteDB.close();
        }
    }

    public void DownloadStory()
    {
        try
        {
            Book NewBook = new Book().CreateBook(this, ReadBook.GetExternalID(), true, true, true);
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void OpenProfile()
    {

    }

    public void WriteReview()
    {

    }

    public void FavouriteBook()
    {

    }

    public void FollowBook()
    {

    }

    public void ShareBook()
    {

    }
}