package com.example.royalroad;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsFragment extends Fragment
{
    EditText KeywordField;
    EditText AuthorField;
    EditText PagesMinField;
    EditText PagesMaxField;
    EditText RatingMinField;
    EditText RatingMaxField;

    Button AdvancedSearchBTN;
    Button SearchBTN;

    Button GenreActionBTN;
    Button GenreAdventureBTN;
    Button GenreComedyBTN;
    Button GenreContemporaryBTN;
    Button GenreDramaBTN;
    Button GenreFantasyBTN;
    Button GenreHistoricalBTN;
    Button GenreHorrorBTN;
    Button GenreMysteryBTN;
    Button GenrePsychologicalBTN;
    Button GenreRomanceBTN;
    Button GenreSatireBTN;
    Button GenreSciFiBTN;
    Button GenreShortStoryBTN;
    Button GenreTragedyBTN;

    Button WarningProfanityBTN;
    Button WarningSexualContentBTN;
    Button WarningGoreBTN;
    Button WarningTraumatisingBTN;

    SeekBar NumberOfPagesSlider;
    SeekBar RatingSlider;

    Spinner IncludeTagsDropdown;
    Spinner ExcludeTagsDropdown;
    Spinner StatusTagsDropdown;
    Spinner OrderByDropdown;
    Spinner OrderByDescendingDropdown;
    Spinner TypeDropdown;

    CheckBox IncludeNotInterested;
    CheckBox ExcludeFollowedFavourited;

    RecyclerView SearchResultsRV;
    BookAdapter SearchAdapter;

    ArrayList<Book> SearchResults;

    boolean IsDarkMode;
    boolean IsAdvancedSearch = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_search_results, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SearchResults = new ArrayList<>();

        SharedPreferences Pref = getActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        IsDarkMode = Pref.getBoolean("AppTheme", false);

        KeywordField = view.findViewById(R.id.KeywordField);
        AuthorField = view.findViewById(R.id.AuthorField);

        GenreActionBTN = view.findViewById(R.id.GenreAction);
        GenreAdventureBTN = view.findViewById(R.id.GenreAdventure);
        GenreComedyBTN = view.findViewById(R.id.GenreComedy);
        GenreContemporaryBTN = view.findViewById(R.id.GenreContemporary);
        GenreDramaBTN = view.findViewById(R.id.GenreDrama);
        GenreFantasyBTN = view.findViewById(R.id.GenreFantasy);
        GenreHistoricalBTN = view.findViewById(R.id.GenreHistorical);
        GenreHorrorBTN = view.findViewById(R.id.GenreHorror);
        GenreMysteryBTN = view.findViewById(R.id.GenreMystery);
        GenrePsychologicalBTN = view.findViewById(R.id.GenrePsychological);
        GenreRomanceBTN = view.findViewById(R.id.GenreRomance);
        GenreSatireBTN = view.findViewById(R.id.GenreSatire);
        GenreSciFiBTN = view.findViewById(R.id.GenreSciFi);
        GenreShortStoryBTN = view.findViewById(R.id.GenreShortStory);
        GenreTragedyBTN = view.findViewById(R.id.GenreTragedy);

        AdvancedSearchBTN = view.findViewById(R.id.AdvancedSearchBTN);

        AdvancedSearchBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(IsAdvancedSearch)
                {
                    IsAdvancedSearch = false;
                }
                else
                {
                    IsAdvancedSearch = true;
                }
            }
        });

        SwitchThemes(IsDarkMode);
    }

    public void SwitchThemes(boolean DarkMode)
    {
        if(DarkMode)
        {

        }
        else
        {

        }
    }

    public void OpenAdvancedSearch()
    {
        if(IsAdvancedSearch)
        {

        }
        else
        {

        }
    }
}