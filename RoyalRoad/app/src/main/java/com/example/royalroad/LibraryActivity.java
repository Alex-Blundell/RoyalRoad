package com.example.royalroad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;


public class LibraryActivity extends AppCompatActivity {

    TabLayout BottomTabs;
    Toolbar TopToolbar;
    RecyclerView LibraryRecyclerview;
    TextView NoBookTXT;

    private int HistoryCount;
    private int DownloadedCount;
    private int ReadLaterCount;
    private int DownloadManagerCount;

    private boolean IsDarkMode;

    private BookAdapter bookAdapter;
    private List<Book> BookList;

    private enum Library
    {
        History,
        Downloaded,
        Read_Later,
        Folders,
        Download_Manager
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        SharedPreferences Pref = getSharedPreferences("Settings", MODE_PRIVATE);
        IsDarkMode = Pref.getBoolean("AppTheme", false);

        NoBookTXT = findViewById(R.id.NoBooksTXT);

        BottomTabs = (TabLayout) findViewById(R.id.TabLayout);
        BottomTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab == BottomTabs.getTabAt(0))
                {
                    SwitchLibraries(Library.History);
                }
                else if(tab == BottomTabs.getTabAt(1))
                {
                    SwitchLibraries(Library.Downloaded);
                }
                else if(tab == BottomTabs.getTabAt(2))
                {
                    SwitchLibraries(Library.Read_Later);
                }
                else if(tab == BottomTabs.getTabAt(3))
                {
                    SwitchLibraries(Library.Folders);
                }
                else if(tab == BottomTabs.getTabAt(4))
                {
                    SwitchLibraries(Library.Download_Manager);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        TopToolbar = (Toolbar) findViewById(R.id.TopToolbar);
        TopToolbar.inflateMenu(R.menu.librarytoptoolbar);
        TopToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getOrder())
                {
                    case 1:
                        SyncLibrary();
                        break;

                    case 2:
                        Sort();
                        break;

                    case 3:
                        AlphabetShow();
                        break;

                    case 4:
                        ShowSearch();
                        break;

                    case 5:
                        SendToSettings();
                        break;
                }

                return false;
            }
        });

        LibraryRecyclerview = findViewById(R.id.LibraryRecyclerview);
        SwitchTheme(IsDarkMode);

        InitializeLibrary();

        SwitchLibraries(Library.History);
    }

    private void InitializeLibrary()
    {
        BookList = new ArrayList<>();

        LibraryRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        LibraryRecyclerview.setHasFixedSize(true);

        DBHandler SQLiteDB = new DBHandler(this);

        int LibraryCount = SQLiteDB.GetLibraryCount();

        if(LibraryCount > 0)
        {
            NoBookTXT.setVisibility(View.GONE);

            for(int i = 0; i < LibraryCount; i++)
            {
                Book NewBook = SQLiteDB.GetBook(i + 1);
                BookList.add(NewBook);
            }
        }
        else
        {
            NoBookTXT.setVisibility(View.VISIBLE);
            // Display Library Empty Message.
        }

        bookAdapter = new BookAdapter(BookList);
        LibraryRecyclerview.setAdapter(bookAdapter);

        //Check Which Library we are on.
        HistoryCount = BookList.size();
        DownloadedCount = BookList.size();
        ReadLaterCount = BookList.size();
        DownloadManagerCount = BookList.size();

        if(HistoryCount > 0)
            BottomTabs.getTabAt(0).setText(BottomTabs.getTabAt(0).getText() + " ( " + HistoryCount + " )");

        if(DownloadedCount > 0)
            BottomTabs.getTabAt(1).setText(BottomTabs.getTabAt(1).getText() + " ( " + DownloadedCount + " )");

        if(ReadLaterCount > 0)
            BottomTabs.getTabAt(2).setText(BottomTabs.getTabAt(2).getText() + " ( " + ReadLaterCount + " )");

        if(DownloadManagerCount > 0)
            BottomTabs.getTabAt(4).setText(BottomTabs.getTabAt(4).getText() + " ( " + DownloadManagerCount + " )");
    }

    public void SyncLibrary()
    {
        Toast.makeText(this, "Syncing ...", Toast.LENGTH_SHORT).show();

        DBHandler SQLiteDB = new DBHandler(this);

        SQLiteDB.close();
    }

    public void Sort()
    {
        Log.println(Log.INFO, "LG", "Sort");
    }

    public void AlphabetShow()
    {
        Log.println(Log.INFO, "LG", "Alphabet");
    }

    public void ShowSearch()
    {
        Log.println(Log.INFO, "LG", "Search");
    }

    public void SendToSettings()
    {
        Intent SettingsIntent = new Intent(LibraryActivity.this, SettingsActivity.class);
        startActivity(SettingsIntent);
    }

    public void SwitchLibraries(Library SelectedLibrary)
    {
        switch (SelectedLibrary)
        {
            case History:
                break;

            case Downloaded:
                break;

            case Read_Later:
                break;

            case Folders:
                break;

            case Download_Manager:
                break;
        }
    }

    public void SwitchTheme(boolean DarkMode)
    {
        if(DarkMode)
        {
            LibraryRecyclerview.setBackgroundColor(getColor(R.color.DarkOuter));
            BottomTabs.setTabTextColors(ColorStateList.valueOf(getColor(R.color.ToolbarItem)));
            BottomTabs.setSelectedTabIndicatorColor(getColor(R.color.ToolbarItem));

            NoBookTXT.setTextColor(getColor(R.color.ToolbarItem));
        }
        else
        {
            LibraryRecyclerview.setBackgroundColor(getColor(R.color.white));
            BottomTabs.setTabTextColors(ColorStateList.valueOf(getColor(R.color.black)));
            BottomTabs.setSelectedTabIndicatorColor(getColor(R.color.black));

            NoBookTXT.setTextColor(getColor(R.color.black));
        }
    }
}