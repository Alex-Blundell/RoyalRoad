package com.example.royalroad;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.DrawableUtils;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.units.qual.Current;

import java.lang.reflect.Field;

public class ReadActivity extends AppCompatActivity {

    Toolbar BottomToolbar;
    Toolbar TopToolbar;

    public ViewPager2 BookPager;
    boolean ToolbarAppeared;
    public Book ReadBook;
    public Button BackBTN;
    public Button ChapterCount;

    public boolean HasDownloaded;

    private VPAdapter vpAdapter;

    public RelativeLayout FontSettingsLayout;
    public RelativeLayout FontSettings;

    Button ArialBTN;
    Button AtkinsonBTN;
    Button CaslonBTN;
    Button ComicSansBTN;
    Button FranklinBTN;
    Button GaramondBTN;
    Button LucidaBTN;
    Button MinionBTN;
    Button DyslexicBTN;
    Button OpenSansBTN;
    Button RobotoBTN;
    Button SansSerifBTN;
    Button UbuntuBTN;
    Button UbuntuCondesnedBTN;
    Button VerdanaBTN;

    public RelativeLayout ParagraphSettings;
    public RelativeLayout BrightnessSettings;

    public TabLayout FontSettingsTabs;
    private boolean IsDarkMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        Intent ThisIntent = getIntent();
        ReadBook = (Book)ThisIntent.getExtras().getSerializable("Book");

        HasDownloaded = ThisIntent.getBooleanExtra("HasDownloaded", false);
        boolean FromNotification = ThisIntent.getBooleanExtra("FromNotification", false);

        BackBTN = findViewById(R.id.BackBTN);
        ChapterCount = findViewById(R.id.ChapterCount);

        ToolbarAppeared = true;

        BottomToolbar = (Toolbar) findViewById(R.id.BookBottomToolbar);

        SharedPreferences Pref = getSharedPreferences("Settings", MODE_PRIVATE);

        IsDarkMode = Pref.getBoolean("ReadingTheme", true);

        FontSettingsLayout = findViewById(R.id.FontSettings);
        FontSettingsTabs = findViewById(R.id.FontTabs);
        FontSettings = findViewById(R.id.FontSettingsLayout);

        ArialBTN = findViewById(R.id.ArialBTN);
        AtkinsonBTN = findViewById(R.id.AtkinsonBTN);
        CaslonBTN = findViewById(R.id.CaslonBTN);
        ComicSansBTN = findViewById(R.id.ComicSansBTN);
        FranklinBTN = findViewById(R.id.FranklinBTN);
        GaramondBTN = findViewById(R.id.GaramondBTN);
        LucidaBTN = findViewById(R.id.LucidaBTN);
        MinionBTN = findViewById(R.id.MinionBTN);
        DyslexicBTN = findViewById(R.id.DyslexicBTN);
        OpenSansBTN = findViewById(R.id.OpenSansBTN);
        RobotoBTN = findViewById(R.id.RobotoBTN);
        SansSerifBTN = findViewById(R.id.SansSerifBTN);
        UbuntuBTN = findViewById(R.id.UbuntuBTN);
        UbuntuCondesnedBTN = findViewById(R.id.UbuntuCondensedBTN);
        VerdanaBTN = findViewById(R.id.VerdanaBTN);

        FontSettings.setVisibility(View.VISIBLE);

        SwitchThemes(IsDarkMode);

        FontSettingsTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                if(tab.getPosition() == 0)
                {
                    if(FontSettings.getVisibility() == View.GONE)
                    {
                        FontSettings.setVisibility(View.VISIBLE);
                    }
                }
                else if(tab.getPosition() == 1)
                {
                    if(FontSettings.getVisibility() == View.VISIBLE)
                    {
                        FontSettings.setVisibility(View.GONE);
                    }
                }
                else if(tab.getPosition() == 2)
                {
                    if(FontSettings.getVisibility() == View.VISIBLE)
                    {
                        FontSettings.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        ArialBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ChangeFont(BaseSettingsFragment.FontStyle.Arial, true);
            }
        });

        AtkinsonBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ChangeFont(BaseSettingsFragment.FontStyle.Atkinson_Hyperlegible, true);
            }
        });

        CaslonBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ChangeFont(BaseSettingsFragment.FontStyle.Caslon, true);
            }
        });

        ComicSansBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ChangeFont(BaseSettingsFragment.FontStyle.Comic_Sans, true);
            }
        });

        FranklinBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ChangeFont(BaseSettingsFragment.FontStyle.Franklin_Gothic, true);
            }
        });

        GaramondBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ChangeFont(BaseSettingsFragment.FontStyle.Garamond, true);
            }
        });

        LucidaBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ChangeFont(BaseSettingsFragment.FontStyle.Lucida, true);
            }
        });

        MinionBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ChangeFont(BaseSettingsFragment.FontStyle.Minion, true);
            }
        });

        DyslexicBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ChangeFont(BaseSettingsFragment.FontStyle.Open_Dyslexic, true);
            }
        });

        OpenSansBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ChangeFont(BaseSettingsFragment.FontStyle.Open_Sans, true);
            }
        });

        RobotoBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ChangeFont(BaseSettingsFragment.FontStyle.Roboto, true);
            }
        });

        SansSerifBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ChangeFont(BaseSettingsFragment.FontStyle.Sans_Serif, true);
            }
        });

        UbuntuBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ChangeFont(BaseSettingsFragment.FontStyle.Ubuntu, true);
            }
        });

        UbuntuCondesnedBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ChangeFont(BaseSettingsFragment.FontStyle.Ubuntu_Condensed, true);
            }
        });

        VerdanaBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ChangeFont(BaseSettingsFragment.FontStyle.Verdanda, true);
            }
        });

        int FontType = Pref.getInt("ReadingFont", BaseSettingsFragment.FontStyle.Open_Sans.ordinal());
        ChangeFont(BaseSettingsFragment.FontStyle.fromOrdinal(FontType), false);

        FontSettingsLayout.setVisibility(View.GONE);

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

                if(FontSettingsLayout.getVisibility() == View.VISIBLE)
                {
                    FontSettingsLayout.setVisibility(View.GONE);
                    getWindow().setLocalFocus(true, true);
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

        if(ReadBook.HasRead && HasDownloaded)
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

            if(HasDownloaded)
            {
                DBHandler SQLiteDB = new DBHandler(this);
                SQLiteDB.UpdateHasRead(ReadBook.GetExternalID());
                SQLiteDB.close();
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        HandleWebsiteLink();
    }

    public void SwitchThemes(boolean DarkMode)
    {
        if(DarkMode)
        {
            ArialBTN.setTextColor(getResources().getColor(R.color.DarkText));
            AtkinsonBTN.setTextColor(getResources().getColor(R.color.DarkText));
            CaslonBTN.setTextColor(getResources().getColor(R.color.DarkText));
            ComicSansBTN.setTextColor(getResources().getColor(R.color.DarkText));
            FranklinBTN.setTextColor(getResources().getColor(R.color.DarkText));
            GaramondBTN.setTextColor(getResources().getColor(R.color.DarkText));
            LucidaBTN.setTextColor(getResources().getColor(R.color.DarkText));
            MinionBTN.setTextColor(getResources().getColor(R.color.DarkText));
            DyslexicBTN.setTextColor(getResources().getColor(R.color.DarkText));
            OpenSansBTN.setTextColor(getResources().getColor(R.color.DarkText));
            RobotoBTN.setTextColor(getResources().getColor(R.color.DarkText));
            SansSerifBTN.setTextColor(getResources().getColor(R.color.DarkText));
            UbuntuBTN.setTextColor(getResources().getColor(R.color.DarkText));
            UbuntuCondesnedBTN.setTextColor(getResources().getColor(R.color.DarkText));
            VerdanaBTN.setTextColor(getResources().getColor(R.color.DarkText));

            BackBTN.setTextColor(getResources().getColor(R.color.ToolbarItem));
            ChapterCount.setTextColor(getResources().getColor(R.color.ToolbarItem));
        }
        else
        {

        }
    }

    public void ChangeFont(BaseSettingsFragment.FontStyle CurrentStyle, boolean AlterFont)
    {
        SharedPreferences Pref = getSharedPreferences("Settings", MODE_PRIVATE);
        SharedPreferences.Editor PrefEditor = Pref.edit();

        ArialBTN.setBackgroundColor(getResources().getColor(R.color.Invis));
        AtkinsonBTN.setBackgroundColor(getResources().getColor(R.color.Invis));
        CaslonBTN.setBackgroundColor(getResources().getColor(R.color.Invis));
        ComicSansBTN.setBackgroundColor(getResources().getColor(R.color.Invis));
        FranklinBTN.setBackgroundColor(getResources().getColor(R.color.Invis));
        GaramondBTN.setBackgroundColor(getResources().getColor(R.color.Invis));
        LucidaBTN.setBackgroundColor(getResources().getColor(R.color.Invis));
        MinionBTN.setBackgroundColor(getResources().getColor(R.color.Invis));
        DyslexicBTN.setBackgroundColor(getResources().getColor(R.color.Invis));
        OpenSansBTN.setBackgroundColor(getResources().getColor(R.color.Invis));
        RobotoBTN.setBackgroundColor(getResources().getColor(R.color.Invis));
        SansSerifBTN.setBackgroundColor(getResources().getColor(R.color.Invis));
        UbuntuBTN.setBackgroundColor(getResources().getColor(R.color.Invis));
        UbuntuCondesnedBTN.setBackgroundColor(getResources().getColor(R.color.Invis));
        VerdanaBTN.setBackgroundColor(getResources().getColor(R.color.Invis));

        switch (CurrentStyle)
        {
            case Arial:
                ArialBTN.setBackgroundColor(getResources().getColor(R.color.ButtonItem));
                break;

            case Atkinson_Hyperlegible:
                AtkinsonBTN.setBackgroundColor(getResources().getColor(R.color.ButtonItem));
                break;

            case Caslon:
                CaslonBTN.setBackgroundColor(getResources().getColor(R.color.ButtonItem));
                break;

            case Comic_Sans:
                ComicSansBTN.setBackgroundColor(getResources().getColor(R.color.ButtonItem));
                break;

            case Franklin_Gothic:
                FranklinBTN.setBackgroundColor(getResources().getColor(R.color.ButtonItem));
                break;

            case Garamond:
                GaramondBTN.setBackgroundColor(getResources().getColor(R.color.ButtonItem));
                break;

            case Lucida:
                LucidaBTN.setBackgroundColor(getResources().getColor(R.color.ButtonItem));
                break;

            case Minion:
                MinionBTN.setBackgroundColor(getResources().getColor(R.color.ButtonItem));
                break;

            case Open_Dyslexic:
                DyslexicBTN.setBackgroundColor(getResources().getColor(R.color.ButtonItem));
                break;

            case Open_Sans:
                OpenSansBTN.setBackgroundColor(getResources().getColor(R.color.ButtonItem));
                break;

            case Roboto:
                RobotoBTN.setBackgroundColor(getResources().getColor(R.color.ButtonItem));
                break;

            case Sans_Serif:
                SansSerifBTN.setBackgroundColor(getResources().getColor(R.color.ButtonItem));
                break;

            case Ubuntu:
                UbuntuBTN.setBackgroundColor(getResources().getColor(R.color.ButtonItem));
                break;

            case Ubuntu_Condensed:
                UbuntuCondesnedBTN.setBackgroundColor(getResources().getColor(R.color.ButtonItem));
                break;

            case Verdanda:
                VerdanaBTN.setBackgroundColor(getResources().getColor(R.color.ButtonItem));
                break;
        }

        if(AlterFont)
        {
            PrefEditor.putInt("ReadingFont", CurrentStyle.ordinal());
            ChapterFragment CurrentChapter = (ChapterFragment)vpAdapter.GetFragment(BookPager.getCurrentItem());
            CurrentChapter.adapter.AlterFont(CurrentStyle);
        }
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
        Intent myIntent = new Intent(Intent.ACTION_SEND);

        myIntent.setType("text/plain");
        String body = ReadBook.Title + "\n" + "https://www.royalroad.com/fiction/" + String.valueOf(ReadBook.ExternalID);
        String sub = "Hi";

        myIntent.putExtra(Intent.EXTRA_SUBJECT,sub);
        myIntent.putExtra(Intent.EXTRA_TEXT,body);

        startActivity(Intent.createChooser(myIntent, "Share Using"));
    }

    public void HideToolbars()
    {
        ChapterFragment CurrentChapter = (ChapterFragment)vpAdapter.GetFragment(BookPager.getCurrentItem());
        CurrentChapter.ShowHideToolbars(ToolbarAppeared);

        getWindow().setLocalFocus(true, true);
    }
}