package com.example.royalroad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.google.android.material.tabs.TabLayout;


public class LibraryActivity extends AppCompatActivity {

    TabLayout BottomTabs;
    Toolbar TopToolbar;

    private int HistoryCount;
    private int DownloadedCount;
    private int ReadLaterCount;
    private int DownloadManagerCount;

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

        if(HistoryCount > 0)
            BottomTabs.getTabAt(0).setText(BottomTabs.getTabAt(0).getText() + " ( " + HistoryCount + " )");

        if(DownloadedCount > 0)
            BottomTabs.getTabAt(1).setText(BottomTabs.getTabAt(1).getText() + " ( " + DownloadedCount + " )");

        if(ReadLaterCount > 0)
            BottomTabs.getTabAt(2).setText(BottomTabs.getTabAt(2).getText() + " ( " + ReadLaterCount + " )");

        if(DownloadManagerCount > 0)
            BottomTabs.getTabAt(4).setText(BottomTabs.getTabAt(4).getText() + " ( " + DownloadManagerCount + " )");

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

        SwitchLibraries(Library.History);
    }

    public void SyncLibrary()
    {
        Log.println(Log.INFO, "LG", "Sync");
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
}