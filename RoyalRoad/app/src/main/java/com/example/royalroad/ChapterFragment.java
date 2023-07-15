package com.example.royalroad;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayoutManager;

import org.checkerframework.checker.units.qual.Current;

import java.util.ArrayList;

public class ChapterFragment extends Fragment
{
    RecyclerView StoryRV;
    ParagraphLineAdapter adapter;

    ReadActivity readActivity;
    boolean IsDarkMode;
    boolean ToolbarShow;

    int ChapterID;

    boolean FontDetails;

    public ChapterFragment(int chapterID)
    {
        this.ChapterID = chapterID;
    }

    public ChapterFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chapter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences Pref = getActivity().getSharedPreferences("Settings", MODE_PRIVATE);
        IsDarkMode = Pref.getBoolean("ReadingTheme", false);
        FontDetails = false;

        readActivity = (ReadActivity)getActivity();

        ToolbarShow = readActivity.ToolbarAppeared;

        int BookID = readActivity.ReadBook.GetInternalID();

        DBHandler SQLiteDB = new DBHandler(getActivity());
        Book.Chapter CurrentChapter = SQLiteDB.GetChapter(BookID, ChapterID);
        SQLiteDB.close();

        StoryRV = (RecyclerView) view.findViewById(R.id.ChapterContentRV);

        readActivity.TopToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                if(item.getOrder() == 1)
                {
                    ShowFontDetails();
                }

                return true;
            }
        });

        for(Book.Paragraph ThisParagraph : CurrentChapter.Content)
        {

            if(!ThisParagraph.Content.isEmpty() && ThisParagraph.Content.length() > 1)
            {
                boolean IsAllSpaces = false;

                for(int i = 0; i < ThisParagraph.Content.length(); i++)
                {
                    if(ThisParagraph.Content.charAt(i) == ' ' || Character.isWhitespace(ThisParagraph.Content.charAt(i)))
                    {
                        IsAllSpaces = true;
                    }
                    else
                    {
                        IsAllSpaces = false;
                        break;
                    }
                }

                if(!IsAllSpaces)
                {
                    StringBuilder AlteredString = new StringBuilder(ThisParagraph.Content);

                    for(int i = ThisParagraph.Content.length() - 1; i > 0; i--)
                    {
                        if(ThisParagraph.Content.charAt(i) == ' ')
                        {
                            AlteredString.deleteCharAt(i);
                        }
                        else
                        {
                            break;
                        }
                    }


                    ThisParagraph.Content = AlteredString.toString();
                }
            }
        }

        LinearLayoutManager LayoutManager = new LinearLayoutManager(getContext());

        StoryRV.setLayoutManager(LayoutManager);
        StoryRV.setHasFixedSize(true);

        adapter = new ParagraphLineAdapter(CurrentChapter.Content);
        StoryRV.setAdapter(adapter);

        Log.println(Log.INFO, "Hi", String.valueOf(CurrentChapter.ChapterProgress));
        LayoutManager.scrollToPosition(CurrentChapter.ChapterProgress);

        StoryRV.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState)
            {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);

                if(CurrentChapter.ChapterProgress != ((LinearLayoutManager)StoryRV.getLayoutManager()).findFirstVisibleItemPosition())
                {
                    CurrentChapter.ChapterProgress = ((LinearLayoutManager)StoryRV.getLayoutManager()).findFirstVisibleItemPosition();

                    // Update SQLite Database.
                    DBHandler SQLiteDB = new DBHandler(getActivity());
                    SQLiteDB.UpdateChapterProgress(CurrentChapter, readActivity.ReadBook.InternalID);
                    SQLiteDB.close();
                }
            }
        });

        StoryRV.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ShowHideToolbars(ToolbarShow);
            }
        });

        SwitchThemes(IsDarkMode);
    }

    public void SwitchThemes(boolean DarkMode)
    {
        if(DarkMode)
         {
            StoryRV.setBackgroundColor(getResources().getColor(R.color.DarkInner));
        }
        else
        {
            StoryRV.setBackgroundColor(getResources().getColor(R.color.LightOuter));
        }
    }

    public void ShowHideToolbars(boolean Show)
    {
        if(Show)
        {
            readActivity.TopToolbar.setVisibility(View.GONE);
            readActivity.BottomToolbar.setVisibility(View.GONE);
            readActivity.BackBTN.setVisibility(View.GONE);
            readActivity.ChapterCount.setVisibility(View.GONE);

            readActivity.ToolbarAppeared = false;
        }
        else
        {
            readActivity.TopToolbar.setVisibility(View.VISIBLE);
            readActivity.BottomToolbar.setVisibility(View.VISIBLE);
            readActivity.BackBTN.setVisibility(View.VISIBLE);
            readActivity.ChapterCount.setVisibility(View.VISIBLE);

            readActivity.ToolbarAppeared = true;

        }
    }

    public void ShowFontDetails()
    {
        if(FontDetails)
        {
            Log.println(Log.INFO, "Hi", "Font Details Close");
            FontDetails = false;
        }
        else
        {
            Log.println(Log.INFO, "Hi", "Font Details Open");
            FontDetails = true;
        }
    }
}