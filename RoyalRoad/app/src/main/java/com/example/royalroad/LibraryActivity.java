package com.example.royalroad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

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
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;


public class LibraryActivity extends AppCompatActivity {

    public TabLayout BottomTabs;
    Toolbar TopToolbar;
    RecyclerView LibraryRecyclerview;
    TextView NoBookTXT;

    public int HistoryCount;
    public int DownloadedCount;
    public int ReadLaterCount;
    public int DownloadManagerCount;

    private ViewPager2 LibraryPager;
    private boolean IsDarkMode;
    VPAdapter vpAdapter;

    public enum LibraryType
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

        BottomTabs = (TabLayout) findViewById(R.id.TabLayout);

        LibraryPager = findViewById(R.id.LibraryPager);
        vpAdapter = new VPAdapter(getSupportFragmentManager(), getLifecycle());

        vpAdapter.AddFragment(new LibraryFragment(LibraryType.History));
        vpAdapter.AddFragment(new LibraryFragment(LibraryType.Downloaded));
        vpAdapter.AddFragment(new LibraryFragment(LibraryType.Read_Later));
        vpAdapter.AddFragment(new LibraryFragment(LibraryType.Folders));
        vpAdapter.AddFragment(new LibraryFragment(LibraryType.Download_Manager));

        LibraryPager.setAdapter(vpAdapter);
        LibraryPager.setCurrentItem(0);
        LibraryPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                BottomTabs.selectTab(BottomTabs.getTabAt(position));
            }
        });
        BottomTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                LibraryPager.setCurrentItem(tab.getPosition());
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
    }

    public void SyncLibrary()
    {
        DBHandler SQLiteDB = new DBHandler(this);
        int LibraryCount = SQLiteDB.GetLibraryCount();

        for(int i = 0; i < LibraryCount; i++)
        {
            Book ThisBook = SQLiteDB.GetBook(i);

            try
            {
                SQLiteDB.CheckUpdate(this, ThisBook);
            }
            catch (InterruptedException e)
            {
                throw new RuntimeException(e);
            }
        }

        SQLiteDB.close();

        Toast.makeText(this, "Sync Completed", Toast.LENGTH_SHORT).show();
    }

    public void Sort()
    {

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
}