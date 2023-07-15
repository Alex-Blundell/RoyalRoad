package com.example.royalroad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Field;

public class ReadActivity extends AppCompatActivity {

    Toolbar BottomToolbar;
    Toolbar TopToolbar;

    private ViewPager2 BookPager;
    boolean ToolbarAppeared;
    public Book ReadBook;
    public Button BackBTN;
    public Button ChapterCount;

    private VPAdapter vpAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        Intent ThisIntent = getIntent();
        ReadBook = (Book)ThisIntent.getExtras().getSerializable("Book");
        boolean HasDownloaded = ThisIntent.getBooleanExtra("HasDownloaded", false);
        boolean FromNotification = ThisIntent.getBooleanExtra("FromNotification", false);

        BackBTN = findViewById(R.id.BackBTN);
        ChapterCount = findViewById(R.id.ChapterCount);

        ToolbarAppeared = true;

        BottomToolbar = (Toolbar) findViewById(R.id.BookBottomToolbar);

        if(FromNotification)
        {
            DBHandler SQLiteDB = new DBHandler(this);

            ReadBook.InternalID = SQLiteDB.GetBookID(ReadBook.ExternalID);

            SQLiteDB.close();
        }

        if(HasDownloaded)
        {
            BottomToolbar.inflateMenu(R.menu.bookbottommenudownloaded);
        }
        else
        {
            BottomToolbar.inflateMenu(R.menu.bookbottommenu);
        }

        TopToolbar = (Toolbar) findViewById(R.id.BookTopToolbar);
        TopToolbar.inflateMenu(R.menu.chaptertoptoolbar);

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
        vpAdapter = new VPAdapter(getSupportFragmentManager(), getLifecycle());

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
                DBHandler SQLiteDB = new DBHandler(ReadActivity.this);
                SQLiteDB.UpdateLastReadChapter(ReadBook.GetExternalID(), position);
                SQLiteDB.close();

                if(position == 0)
                {
                    ChapterCount.setText("Home / " + ReadBook.Chapters.size());
                    TopToolbar.getMenu().getItem(0).setVisible(false);
                }
                else
                {
                    ChapterCount.setText(position + " / " + ReadBook.Chapters.size());

                    if(!TopToolbar.getMenu().getItem(0).isVisible())
                    {
                        TopToolbar.getMenu().getItem(0).setVisible(true);
                    }
                }
            }
        });

        ReduceDragSensitivity(3);

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

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        HandleWebsiteLink();
    }

    private void HandleWebsiteLink()
    {
        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
    }

    private void ReduceDragSensitivity(int Sensitivity)
    {
        try
        {
            Field FF = ViewPager2.class.getDeclaredField("mRecyclerView");
            FF.setAccessible(true);

            RecyclerView recyclerView = (RecyclerView) FF.get(BookPager);
            Field TouchSlopField = RecyclerView.class.getDeclaredField("mTouchSlop");

            TouchSlopField.setAccessible(true);
            int TouchSlop = (int)TouchSlopField.get(recyclerView);
            TouchSlopField.set(recyclerView, TouchSlop * Sensitivity);
        }
        catch (NoSuchFieldException e)
        {
            throw new RuntimeException(e);
        }
        catch (IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);

        if(hasFocus)
        {
            if(!ToolbarAppeared)
            {
                getWindow().getDecorView().setSystemUiVisibility(HideSystemBars());
            }
            else
            {
                getWindow().getDecorView().setSystemUiVisibility(0);
            }
        }
        else
        {
            Log.println(Log.INFO, "Hi", "Focus Changed");
        }
    }

    private int HideSystemBars()
    {
        return View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
    }

    public void DownloadStory()
    {
        try
        {
            Book NewBook = new Book().CreateBook(getApplicationContext(), ReadBook.GetExternalID(), true, true, true);
            BottomToolbar.getMenu().getItem(0).setVisible(false);
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void OpenProfile()
    {
        Intent ProfileIntent = new Intent(ReadActivity.this, ProfileActivity.class);
        ProfileIntent.putExtra("ProfileID", ReadBook.ProfileID);

        startActivity(ProfileIntent);
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

    public void HideToolbars()
    {
        ChapterFragment CurrentChapter = (ChapterFragment)vpAdapter.GetFragment(BookPager.getCurrentItem());
        CurrentChapter.ShowHideToolbars(ToolbarAppeared);

        getWindow().setLocalFocus(true, true);
    }
}