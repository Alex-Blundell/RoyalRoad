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
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.checkerframework.common.subtyping.qual.Bottom;

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

    ScrollView AlphabetLayout;

    boolean IsRestricted;

    char RestrictedLetter;

    Button BackBTN;
    Button CloseBTN;
    Button AllBTN;
    Button NumberBTN;

    Button ABTN;
    Button BBTN;
    Button CBTN;
    Button DBTN;
    Button EBTN;
    Button FBTN;
    Button GBTN;
    Button HBTN;
    Button IBTN;
    Button JBTN;
    Button KBTN;
    Button LBTN;
    Button MBTN;
    Button NBTN;
    Button OBTN;
    Button PBTN;
    Button QBTN;
    Button RBTN;
    Button SBTN;
    Button TBTN;
    Button UBTN;
    Button VBTN;
    Button WBTN;
    Button XBTN;
    Button YBTN;
    Button ZBTN;

    public enum LibraryType
    {
        History,
        Downloaded,
        Read_Later,
        Folders,
        Download_Manager
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        IsRestricted = false;

        CloseBTN = findViewById(R.id.CloseBTN);
        AllBTN = findViewById(R.id.AllBTN);
        NumberBTN = findViewById(R.id.NumbersBTN);

        AllBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                UnRestrictByLetter();
            }
        });

        NumberBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                RestrictByNumbers();
            }
        });

        ABTN = findViewById(R.id.ABTN);
        BBTN = findViewById(R.id.BBTN);
        CBTN = findViewById(R.id.CBTN);
        DBTN = findViewById(R.id.DBTN);
        EBTN = findViewById(R.id.EBTN);
        FBTN = findViewById(R.id.FBTN);
        GBTN = findViewById(R.id.GBTN);
        HBTN = findViewById(R.id.HBTN);
        IBTN = findViewById(R.id.IBTN);
        JBTN = findViewById(R.id.JBTN);
        KBTN = findViewById(R.id.KBTN);
        LBTN = findViewById(R.id.LBTN);
        MBTN = findViewById(R.id.MBTN);
        NBTN = findViewById(R.id.NBTN);
        OBTN = findViewById(R.id.OBTN);
        PBTN = findViewById(R.id.PBTN);
        QBTN = findViewById(R.id.QBTN);
        RBTN = findViewById(R.id.RBTN);
        SBTN = findViewById(R.id.SBTN);
        TBTN = findViewById(R.id.TBTN);
        UBTN = findViewById(R.id.UBTN);
        VBTN = findViewById(R.id.VBTN);
        WBTN = findViewById(R.id.WBTN);
        XBTN = findViewById(R.id.XBTN);
        YBTN = findViewById(R.id.YBTN);
        ZBTN = findViewById(R.id.ZBTN);

        BackBTN = findViewById(R.id.BackBTN);

        ABTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                RestrictedLetter = 'A';
                RestrictByLetter();
            }
        });

        BBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                RestrictedLetter = 'B';
                RestrictByLetter();
            }
        });

        CBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                RestrictedLetter = 'C';
                RestrictByLetter();
            }
        });

        DBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                RestrictedLetter = 'D';
                RestrictByLetter();
            }
        });

        EBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                RestrictedLetter = 'E';
                RestrictByLetter();
            }
        });

        FBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                RestrictedLetter = 'F';
                RestrictByLetter();
            }
        });

        GBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                RestrictedLetter = 'G';
                RestrictByLetter();
            }
        });

        HBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                RestrictedLetter = 'H';
                RestrictByLetter();
            }
        });

        IBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                RestrictedLetter = 'I';
                RestrictByLetter();
            }
        });

        JBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                RestrictedLetter = 'J';
                RestrictByLetter();
            }
        });

        KBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                RestrictedLetter = 'K';
                RestrictByLetter();
            }
        });

        LBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                RestrictedLetter = 'L';
                RestrictByLetter();
            }
        });

        MBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                RestrictedLetter = 'M';
                RestrictByLetter();
            }
        });

        NBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                RestrictedLetter = 'N';
                RestrictByLetter();
            }
        });

        OBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                RestrictedLetter = 'O';
                RestrictByLetter();
            }
        });

        PBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                RestrictedLetter = 'P';
                RestrictByLetter();
            }
        });

        QBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                RestrictedLetter = 'Q';
                RestrictByLetter();
            }
        });

        RBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                RestrictedLetter = 'R';
                RestrictByLetter();
            }
        });

        SBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                RestrictedLetter = 'S';
                RestrictByLetter();
            }
        });

        TBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                RestrictedLetter = 'T';
                RestrictByLetter();
            }
        });

        UBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                RestrictedLetter = 'U';
                RestrictByLetter();
            }
        });

        VBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                RestrictedLetter = 'V';
                RestrictByLetter();
            }
        });

        WBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                RestrictedLetter = 'W';
                RestrictByLetter();
            }
        });

        XBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                RestrictedLetter = 'X';
                RestrictByLetter();
            }
        });

        YBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                RestrictedLetter = 'Y';
                RestrictByLetter();
            }
        });

        ZBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                RestrictedLetter = 'Z';
                RestrictByLetter();
            }
        });

        CloseBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AlphabetShow();
            }
        });

        BackBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
        LibraryPager.setOffscreenPageLimit(5);
        LibraryPager.setCurrentItem(0);

        LibraryPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback()
        {
            @Override
            public void onPageSelected(int position)
            {
                BottomTabs.selectTab(BottomTabs.getTabAt(position));
            }
        });

        BottomTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                LibraryPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab)
            {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab)
            {

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

        AlphabetLayout = (ScrollView) findViewById(R.id.AlphabeticalLayout);
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
        if(AlphabetLayout.getVisibility() == View.GONE)
        {
            AlphabetLayout.setVisibility(View.VISIBLE);
        }
        else
        {
            AlphabetLayout.setVisibility(View.GONE);
        }
    }

    public void RestrictByLetter()
    {
        if(!IsRestricted)
        {
            IsRestricted = true;
        }

        Log.println(Log.INFO, "Hi", "Restricting By Letter: " + RestrictedLetter);

        // Empty Library.
        // Only Fill Library with Books Begining with the Selected Letter.
    }

    public void RestrictByNumbers()
    {
        if(!IsRestricted)
        {
            IsRestricted = true;
        }

        // Empty Library.
        // Only Fill Library with Books Begining with any numbers.
    }

    public void UnRestrictByLetter()
    {
        if(IsRestricted)
        {
            IsRestricted = false;

            // If it is Already Restricted.
            // Dump Library and Fill it up with all Books.
        }
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

    public void OpenDeletePrompt(boolean Open)
    {
        LibraryFragment CurrentFragment = (LibraryFragment)vpAdapter.GetFragment(LibraryPager.getCurrentItem());
        CurrentFragment.OpenDeleteMenu(Open);
    }
}