package com.example.royalroad;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.checkerframework.checker.units.qual.Current;

import java.util.ArrayList;

public class ChapterFragment extends Fragment
{
    ListView StoryList;
    ReadActivity readActivity;
    boolean IsDarkMode;
    boolean ToolbarShow;

    int ChapterID;

    public ChapterFragment(int chapterID)
    {
        this.ChapterID = chapterID;
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

        readActivity = (ReadActivity)getActivity();

        ToolbarShow = readActivity.ToolbarAppeared;

        int BookID = readActivity.ReadBook.GetInternalID();

        DBHandler SQLiteDB = new DBHandler(getActivity().getApplicationContext());

        Book.Chapter CurrentChapter = SQLiteDB.GetChapter(BookID, ChapterID);
        SQLiteDB.close();

        StoryList = (ListView) view.findViewById(R.id.ChapterContentList);
        ArrayList<String> Paragraphs = new ArrayList<>();

        for(Book.Paragraph ThisParagraph : CurrentChapter.Content)
        {
            if(!ThisParagraph.Content.isEmpty())
            {
                boolean AllSpaces = false;

                for(int i = 0; i < ThisParagraph.Content.length(); i++)
                {
                    if(ThisParagraph.Content.charAt(i) == ' ')
                    {
                        AllSpaces = true;
                    }
                    else
                    {
                        AllSpaces = false;
                    }
                }

                if(!AllSpaces)
                {
                    if(ThisParagraph.Content.length() > 1)
                    {
                        Paragraphs.add(ThisParagraph.Content);
                    }
                }
            }
        }
        ArrayAdapter<String> ParagarphItems = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, Paragraphs){
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                View view = super.getView(position, convertView, parent);

                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                if(IsDarkMode)
                {
                    textView.setTextColor(getResources().getColor(R.color.DarkText));
                }

                return view;
            }
        };

        StoryList.setAdapter(ParagarphItems);
        StoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ShowHideToolbars(ToolbarShow);
            }
        });

        SwitchThemes(IsDarkMode);
    }

    public void SwitchThemes(boolean DarkMode)
    {
        if(DarkMode)
        {
            StoryList.setBackgroundColor(getResources().getColor(R.color.DarkOuter));
        }
        else
        {
            StoryList.setBackgroundColor(getResources().getColor(R.color.white));
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

            ToolbarShow = false;
        }
        else
        {
            readActivity.TopToolbar.setVisibility(View.VISIBLE);
            readActivity.BottomToolbar.setVisibility(View.VISIBLE);
            readActivity.BackBTN.setVisibility(View.VISIBLE);
            readActivity.ChapterCount.setVisibility(View.VISIBLE);

            ToolbarShow = true;
        }
    }
}