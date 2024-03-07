package com.example.royalroad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.Spinner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

public class SearchActivity extends AppCompatActivity
{
    public enum TagData
    {
        Default,
        Include,
        Exclude
    }

    Button BackBTN;
    RecyclerView SearchRV;
    BookAdapter adapter;

    ScrollView Scroller;

    Button FirstBTN;
    Button PreviousBTN;
    Button OneBTN;
    Button TwoBTN;
    Button ThreeBTN;
    Button FourBTN;
    Button FiveBTN;
    Button NextBTN;
    Button LastBTN;
    Button SearchBTN;

    Spinner TypeDropdown;
    Spinner OrderByDropdown;
    Spinner OrderByAscendingDropdown;

    // Genre Buttons
    Button ActionGenreBTN;
    Button AdventureGenreBTN;
    Button ComedyGenreBTN;
    Button ContemporaryGenreBTN;
    Button DramaGenreBTN;
    Button FantasyGenreBTN;
    Button HistoricalGenreBTN;
    Button HorrorGenreBTN;
    Button MysteryGenreBTN;
    Button PsychologicalGenreBTN;
    Button RomanceGenreBTN;
    Button SatireGenreBTN;
    Button Sci_FiGenreBTN;
    Button ShortStoryGenreBTN;
    Button TragedyGenreBTN;

    // Warnings
    Button ProfanityBTN;
    Button SexualContentBTN;
    Button GraphicViolenceBTN;
    Button SensitiveContentBTN;
    Button AI_AssistedContentBTN;
    Button AI_GeneratedContentBTN;

    Button AdvancedSearchBTN;

    SearchView SearchViewer;

    RelativeLayout AdvancedSearch;
    RelativeLayout SearchPagesLayout;
    EditText KeywordField;
    EditText AuthorField;

    // Genre TagData
    TagData ActionGenre = TagData.Default;
    TagData AdventureGenre = TagData.Default;
    TagData ComedyGenre = TagData.Default;
    TagData ContemporaryGenre = TagData.Default;
    TagData DramaGenre = TagData.Default;
    TagData FantasyGenre = TagData.Default;
    TagData HistoricalGenre = TagData.Default;
    TagData HorrorGenre = TagData.Default;
    TagData MysteryGenre = TagData.Default;
    TagData PsychologicalGenre = TagData.Default;
    TagData RomanceGenre = TagData.Default;
    TagData SatireGenre = TagData.Default;
    TagData Sci_FiGenre = TagData.Default;
    TagData ShortStoryGenre = TagData.Default;
    TagData TragedyGenre = TagData.Default;

    // Warning TagData
    TagData Profanity = TagData.Default;
    TagData SexualContent = TagData.Default;
    TagData GraphicViolence = TagData.Default;
    TagData SensitiveContent = TagData.Default;
    TagData AI_AssistedContent = TagData.Default;
    TagData AI_GeneratedContent = TagData.Default;

    // Page Counter
    int PageMin = 0;
    int PageMax = 20000;

    // Rating Counter
    float RatingMin = 0.0f;
    float RatingMax = 5.0f;

    private String BaseURL = "https://www.royalroad.com/fictions/search?";
    private String Title = "";
    private boolean ExpandedSearch = false;
    private boolean HasPages = false;

    int PageCount;
    int PageNum = 1;

    int FictionCount;

    Book[] SearchBooks;

    Boolean IsDarkMode;

    private SharedPreferences Pref;
    private SharedPreferences.Editor PrefEditor;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent RecieveIntent = getIntent();

        boolean ExpandedSearchFirstTime = false;

        ExpandedSearch = RecieveIntent.getBooleanExtra("IsExpandedSearch", false);
        SearchViewer = findViewById(R.id.Searchbar);

        Scroller = findViewById(R.id.Scoller);

        SearchPagesLayout = findViewById(R.id.SearchPagesLayout);

        if(ExpandedSearch)
        {
            ExpandedSearchFirstTime = true;

            // Genres.
            ActionGenre = TagData.Default;
            AdventureGenre = TagData.Default;
            ComedyGenre = TagData.Default;
            ContemporaryGenre = TagData.Default;
            DramaGenre = TagData.Default;
            FantasyGenre = TagData.Default;
            HistoricalGenre = TagData.Default;
            HorrorGenre = TagData.Default;
            MysteryGenre = TagData.Default;
            PsychologicalGenre = TagData.Default;
            RomanceGenre = TagData.Default;
            SatireGenre = TagData.Default;
            Sci_FiGenre = TagData.Default;
            ShortStoryGenre = TagData.Default;
            TragedyGenre = TagData.Default;

            // Warnings.
            Profanity = TagData.Default;
            SexualContent = TagData.Default;
            GraphicViolence = TagData.Default;
            SensitiveContent = TagData.Default;
            AI_AssistedContent = TagData.Default;
            AI_GeneratedContent = TagData.Default;
        }
        else
        {
            Title = RecieveIntent.getStringExtra("Title");
            SearchViewer.setQuery(Title, false);
        }

        SearchBTN = findViewById(R.id.SearchBTN);

        TypeDropdown = findViewById(R.id.TypeDropdown);

        OrderByDropdown = findViewById(R.id.OrderByDropdown);
        OrderByAscendingDropdown = findViewById(R.id.AscendingDropdown);

        OrderByAscendingDropdown.setSelection(1);

        AdvancedSearchBTN = findViewById(R.id.AdvancedSearchBTN);
        AdvancedSearch = findViewById(R.id.ExpandedSearchOptions);

        KeywordField = findViewById(R.id.KeywordField);
        AuthorField = findViewById(R.id.AuthorField);

        // Page Buttons.
        FirstBTN = findViewById(R.id.FirstBTN);
        PreviousBTN = findViewById(R.id.PreviousBTN);

        OneBTN = findViewById(R.id.OneBTN);
        TwoBTN = findViewById(R.id.TwoBTN);
        ThreeBTN = findViewById(R.id.ThreeBTN);
        FourBTN = findViewById(R.id.FourBTN);
        FiveBTN = findViewById(R.id.FiveBTN);

        NextBTN = findViewById(R.id.NextBTN);
        LastBTN = findViewById(R.id.LastBTN);

        SearchRV = findViewById(R.id.SearchResultsRV);

        // Genre Buttons.
        ActionGenreBTN = findViewById(R.id.GenreAction);
        AdventureGenreBTN = findViewById(R.id.GenreAdventure);
        ComedyGenreBTN = findViewById(R.id.GenreComedy);
        ContemporaryGenreBTN = findViewById(R.id.GenreContemporary);
        DramaGenreBTN = findViewById(R.id.GenreDrama);
        FantasyGenreBTN = findViewById(R.id.GenreFantasy);
        HistoricalGenreBTN = findViewById(R.id.GenreHistorical);
        HorrorGenreBTN = findViewById(R.id.GenreHorror);
        MysteryGenreBTN = findViewById(R.id.GenreMystery);
        PsychologicalGenreBTN = findViewById(R.id.GenrePsychological);
        RomanceGenreBTN = findViewById(R.id.GenreRomance);
        SatireGenreBTN = findViewById(R.id.GenreSatire);
        Sci_FiGenreBTN = findViewById(R.id.GenreSciFi);
        ShortStoryGenreBTN = findViewById(R.id.GenreShortStory);
        TragedyGenreBTN = findViewById(R.id.GenreTragedy);

        // Warning Buttons.
        ProfanityBTN = findViewById(R.id.ProfanityWarning);
        SexualContentBTN = findViewById(R.id.SexualWarning);
        GraphicViolenceBTN = findViewById(R.id.ViolenceWarning);
        SensitiveContentBTN = findViewById(R.id.SensitiveWarning);
        AI_AssistedContentBTN = findViewById(R.id.AI_AssistedWaring);
        AI_GeneratedContentBTN = findViewById(R.id.AI_GeneratedWaring);

        AdvancedSearchBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(ExpandedSearch)
                {
                    ExpandedSearch = false;
                }
                else
                {
                    ExpandedSearch = true;
                }

                SetAdvancedSearch();
            }
        });

        // Genre Button Listeners.
        ActionGenreBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ActionGenre == TagData.Default)
                {
                    ActionGenre = TagData.Include;
                    ActionGenreBTN.setBackgroundColor(getColor(R.color.GenreInclude));

                }
                else if(ActionGenre == TagData.Include)
                {
                    ActionGenre = TagData.Exclude;
                    ActionGenreBTN.setBackgroundColor(getColor(R.color.GenreExclude));
                }
                else if(ActionGenre == TagData.Exclude)
                {
                    ActionGenre = TagData.Default;
                    ActionGenreBTN.setBackgroundColor(getColor(R.color.ButtonInner));
                }
            }
        });

        AdventureGenreBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                if(AdventureGenre == TagData.Default)
                {
                    AdventureGenre = TagData.Include;
                    AdventureGenreBTN.setBackgroundColor(getColor(R.color.GenreInclude));

                    AdventureGenreBTN.setCompoundDrawables(getDrawable(R.drawable.genre_include), null, null, null);
                }
                else if(AdventureGenre == TagData.Include)
                {
                    AdventureGenre = TagData.Exclude;
                    AdventureGenreBTN.setBackgroundColor(getColor(R.color.GenreExclude));
                }
                else if(AdventureGenre == TagData.Exclude)
                {
                    AdventureGenre = TagData.Default;
                    AdventureGenreBTN.setBackgroundColor(getColor(R.color.ButtonInner));
                }
            }
        });

        ComedyGenreBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                if(ComedyGenre == TagData.Default)
                {
                    ComedyGenre = TagData.Include;
                    ComedyGenreBTN.setBackgroundColor(getColor(R.color.GenreInclude));

                    ComedyGenreBTN.setCompoundDrawables(getDrawable(R.drawable.genre_include), null, null, null);
                }
                else if(ComedyGenre == TagData.Include)
                {
                    ComedyGenre = TagData.Exclude;
                    ComedyGenreBTN.setBackgroundColor(getColor(R.color.GenreExclude));
                }
                else if(ComedyGenre == TagData.Exclude)
                {
                    ComedyGenre = TagData.Default;
                    ComedyGenreBTN.setBackgroundColor(getColor(R.color.ButtonInner));
                }
            }
        });

        ContemporaryGenreBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                if(ContemporaryGenre == TagData.Default)
                {
                    ContemporaryGenre = TagData.Include;
                    ContemporaryGenreBTN.setBackgroundColor(getColor(R.color.GenreInclude));

                    ContemporaryGenreBTN.setCompoundDrawables(getDrawable(R.drawable.genre_include), null, null, null);
                }
                else if(ContemporaryGenre == TagData.Include)
                {
                    ContemporaryGenre = TagData.Exclude;
                    ContemporaryGenreBTN.setBackgroundColor(getColor(R.color.GenreExclude));
                }
                else if(ContemporaryGenre == TagData.Exclude)
                {
                    ContemporaryGenre = TagData.Default;
                    ContemporaryGenreBTN.setBackgroundColor(getColor(R.color.ButtonInner));
                }
            }
        });

        DramaGenreBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                if(DramaGenre == TagData.Default)
                {
                    DramaGenre = TagData.Include;
                    DramaGenreBTN.setBackgroundColor(getColor(R.color.GenreInclude));

                    DramaGenreBTN.setCompoundDrawables(getDrawable(R.drawable.genre_include), null, null, null);
                }
                else if(DramaGenre == TagData.Include)
                {
                    DramaGenre = TagData.Exclude;
                    DramaGenreBTN.setBackgroundColor(getColor(R.color.GenreExclude));
                }
                else if(DramaGenre == TagData.Exclude)
                {
                    DramaGenre = TagData.Default;
                    DramaGenreBTN.setBackgroundColor(getColor(R.color.ButtonInner));
                }
            }
        });

        FantasyGenreBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                if(FantasyGenre == TagData.Default)
                {
                    FantasyGenre = TagData.Include;
                    FantasyGenreBTN.setBackgroundColor(getColor(R.color.GenreInclude));

                    FantasyGenreBTN.setCompoundDrawables(getDrawable(R.drawable.genre_include), null, null, null);
                }
                else if(FantasyGenre == TagData.Include)
                {
                    FantasyGenre = TagData.Exclude;
                    FantasyGenreBTN.setBackgroundColor(getColor(R.color.GenreExclude));
                }
                else if(FantasyGenre == TagData.Exclude)
                {
                    FantasyGenre = TagData.Default;
                    FantasyGenreBTN.setBackgroundColor(getColor(R.color.ButtonInner));
                }
            }
        });

        HistoricalGenreBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                if(HistoricalGenre == TagData.Default)
                {
                    HistoricalGenre = TagData.Include;
                    HistoricalGenreBTN.setBackgroundColor(getColor(R.color.GenreInclude));

                    HistoricalGenreBTN.setCompoundDrawables(getDrawable(R.drawable.genre_include), null, null, null);
                }
                else if(HistoricalGenre == TagData.Include)
                {
                    HistoricalGenre = TagData.Exclude;
                    HistoricalGenreBTN.setBackgroundColor(getColor(R.color.GenreExclude));
                }
                else if(HistoricalGenre == TagData.Exclude)
                {
                    HistoricalGenre = TagData.Default;
                    HistoricalGenreBTN.setBackgroundColor(getColor(R.color.ButtonInner));
                }
            }
        });

        HorrorGenreBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                if(HorrorGenre == TagData.Default)
                {
                    HorrorGenre = TagData.Include;
                    HorrorGenreBTN.setBackgroundColor(getColor(R.color.GenreInclude));

                    HorrorGenreBTN.setCompoundDrawables(getDrawable(R.drawable.genre_include), null, null, null);
                }
                else if(HorrorGenre == TagData.Include)
                {
                    HorrorGenre = TagData.Exclude;
                    HorrorGenreBTN.setBackgroundColor(getColor(R.color.GenreExclude));
                }
                else if(HorrorGenre == TagData.Exclude)
                {
                    HorrorGenre = TagData.Default;
                    HorrorGenreBTN.setBackgroundColor(getColor(R.color.ButtonInner));
                }
            }
        });

        MysteryGenreBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                if(MysteryGenre == TagData.Default)
                {
                    MysteryGenre = TagData.Include;
                    MysteryGenreBTN.setBackgroundColor(getColor(R.color.GenreInclude));

                    MysteryGenreBTN.setCompoundDrawables(getDrawable(R.drawable.genre_include), null, null, null);
                }
                else if(MysteryGenre == TagData.Include)
                {
                    MysteryGenre = TagData.Exclude;
                    MysteryGenreBTN.setBackgroundColor(getColor(R.color.GenreExclude));
                }
                else if(MysteryGenre == TagData.Exclude)
                {
                    MysteryGenre = TagData.Default;
                    MysteryGenreBTN.setBackgroundColor(getColor(R.color.ButtonInner));
                }
            }
        });

        PsychologicalGenreBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                if(PsychologicalGenre == TagData.Default)
                {
                    PsychologicalGenre = TagData.Include;
                    PsychologicalGenreBTN.setBackgroundColor(getColor(R.color.GenreInclude));

                    PsychologicalGenreBTN.setCompoundDrawables(getDrawable(R.drawable.genre_include), null, null, null);
                }
                else if(PsychologicalGenre == TagData.Include)
                {
                    PsychologicalGenre = TagData.Exclude;
                    PsychologicalGenreBTN.setBackgroundColor(getColor(R.color.GenreExclude));
                }
                else if(PsychologicalGenre == TagData.Exclude)
                {
                    PsychologicalGenre = TagData.Default;
                    PsychologicalGenreBTN.setBackgroundColor(getColor(R.color.ButtonInner));
                }
            }
        });

        RomanceGenreBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                if(RomanceGenre == TagData.Default)
                {
                    RomanceGenre = TagData.Include;
                    RomanceGenreBTN.setBackgroundColor(getColor(R.color.GenreInclude));

                    RomanceGenreBTN.setCompoundDrawables(getDrawable(R.drawable.genre_include), null, null, null);
                }
                else if(RomanceGenre == TagData.Include)
                {
                    RomanceGenre = TagData.Exclude;
                    RomanceGenreBTN.setBackgroundColor(getColor(R.color.GenreExclude));
                }
                else if(RomanceGenre == TagData.Exclude)
                {
                    RomanceGenre = TagData.Default;
                    RomanceGenreBTN.setBackgroundColor(getColor(R.color.ButtonInner));
                }
            }
        });

        SatireGenreBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                if(SatireGenre == TagData.Default)
                {
                    SatireGenre = TagData.Include;
                    SatireGenreBTN.setBackgroundColor(getColor(R.color.GenreInclude));

                    SatireGenreBTN.setCompoundDrawables(getDrawable(R.drawable.genre_include), null, null, null);
                }
                else if(SatireGenre == TagData.Include)
                {
                    SatireGenre = TagData.Exclude;
                    SatireGenreBTN.setBackgroundColor(getColor(R.color.GenreExclude));
                }
                else if(SatireGenre == TagData.Exclude)
                {
                    SatireGenre = TagData.Default;
                    SatireGenreBTN.setBackgroundColor(getColor(R.color.ButtonInner));
                }
            }
        });

        Sci_FiGenreBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                if(Sci_FiGenre == TagData.Default)
                {
                    Sci_FiGenre = TagData.Include;
                    Sci_FiGenreBTN.setBackgroundColor(getColor(R.color.GenreInclude));

                    Sci_FiGenreBTN.setCompoundDrawables(getDrawable(R.drawable.genre_include), null, null, null);
                }
                else if(Sci_FiGenre == TagData.Include)
                {
                    Sci_FiGenre = TagData.Exclude;
                    Sci_FiGenreBTN.setBackgroundColor(getColor(R.color.GenreExclude));
                }
                else if(Sci_FiGenre == TagData.Exclude)
                {
                    Sci_FiGenre = TagData.Default;
                    Sci_FiGenreBTN.setBackgroundColor(getColor(R.color.ButtonInner));
                }
            }
        });

        ShortStoryGenreBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                if(ShortStoryGenre == TagData.Default)
                {
                    ShortStoryGenre = TagData.Include;
                    ShortStoryGenreBTN.setBackgroundColor(getColor(R.color.GenreInclude));

                    ShortStoryGenreBTN.setCompoundDrawables(getDrawable(R.drawable.genre_include), null, null, null);
                }
                else if(ShortStoryGenre == TagData.Include)
                {
                    ShortStoryGenre = TagData.Exclude;
                    ShortStoryGenreBTN.setBackgroundColor(getColor(R.color.GenreExclude));
                }
                else if(ShortStoryGenre == TagData.Exclude)
                {
                    ShortStoryGenre = TagData.Default;
                    ShortStoryGenreBTN.setBackgroundColor(getColor(R.color.ButtonInner));
                }
            }
        });

        TragedyGenreBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                if(TragedyGenre == TagData.Default)
                {
                    TragedyGenre = TagData.Include;
                    TragedyGenreBTN.setBackgroundColor(getColor(R.color.GenreInclude));

                    TragedyGenreBTN.setCompoundDrawables(getDrawable(R.drawable.genre_include), null, null, null);
                }
                else if(TragedyGenre == TagData.Include)
                {
                    TragedyGenre = TagData.Exclude;
                    TragedyGenreBTN.setBackgroundColor(getColor(R.color.GenreExclude));
                }
                else if(TragedyGenre == TagData.Exclude)
                {
                    TragedyGenre = TagData.Default;
                    TragedyGenreBTN.setBackgroundColor(getColor(R.color.ButtonInner));
                }
            }
        });

        // Warning Button Listeners
        ProfanityBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                if(Profanity == TagData.Default)
                {
                    Profanity = TagData.Include;
                    ProfanityBTN.setBackgroundColor(getColor(R.color.GenreInclude));

                    ProfanityBTN.setCompoundDrawables(getDrawable(R.drawable.genre_include), null, null, null);
                }
                else if(Profanity == TagData.Include)
                {
                    Profanity = TagData.Exclude;
                    ProfanityBTN.setBackgroundColor(getColor(R.color.GenreExclude));
                }
                else if(Profanity == TagData.Exclude)
                {
                    Profanity = TagData.Default;
                    ProfanityBTN.setBackgroundColor(getColor(R.color.ButtonInner));
                }
            }
        });

        SexualContentBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                if(SexualContent == TagData.Default)
                {
                    SexualContent = TagData.Include;
                    SexualContentBTN.setBackgroundColor(getColor(R.color.GenreInclude));

                    SexualContentBTN.setCompoundDrawables(getDrawable(R.drawable.genre_include), null, null, null);
                }
                else if(SexualContent == TagData.Include)
                {
                    SexualContent = TagData.Exclude;
                    SexualContentBTN.setBackgroundColor(getColor(R.color.GenreExclude));
                }
                else if(SexualContent == TagData.Exclude)
                {
                    SexualContent = TagData.Default;
                    SexualContentBTN.setBackgroundColor(getColor(R.color.ButtonInner));
                }
            }
        });

        GraphicViolenceBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                if(GraphicViolence == TagData.Default)
                {
                    GraphicViolence = TagData.Include;
                    GraphicViolenceBTN.setBackgroundColor(getColor(R.color.GenreInclude));

                    GraphicViolenceBTN.setCompoundDrawables(getDrawable(R.drawable.genre_include), null, null, null);
                }
                else if(GraphicViolence == TagData.Include)
                {
                    GraphicViolence = TagData.Exclude;
                    GraphicViolenceBTN.setBackgroundColor(getColor(R.color.GenreExclude));
                }
                else if(GraphicViolence == TagData.Exclude)
                {
                    GraphicViolence = TagData.Default;
                    GraphicViolenceBTN.setBackgroundColor(getColor(R.color.ButtonInner));
                }
            }
        });

        SensitiveContentBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                if(SensitiveContent == TagData.Default)
                {
                    SensitiveContent = TagData.Include;
                    SensitiveContentBTN.setBackgroundColor(getColor(R.color.GenreInclude));

                    SensitiveContentBTN.setCompoundDrawables(getDrawable(R.drawable.genre_include), null, null, null);
                }
                else if(SensitiveContent == TagData.Include)
                {
                    SensitiveContent = TagData.Exclude;
                    SensitiveContentBTN.setBackgroundColor(getColor(R.color.GenreExclude));
                }
                else if(SensitiveContent == TagData.Exclude)
                {
                    SensitiveContent = TagData.Default;
                    SensitiveContentBTN.setBackgroundColor(getColor(R.color.ButtonInner));
                }
            }
        });

        AI_AssistedContentBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                if(AI_AssistedContent == TagData.Default)
                {
                    AI_AssistedContent = TagData.Include;
                    AI_AssistedContentBTN.setBackgroundColor(getColor(R.color.GenreInclude));

                    AI_AssistedContentBTN.setCompoundDrawables(getDrawable(R.drawable.genre_include), null, null, null);
                }
                else if(AI_AssistedContent == TagData.Include)
                {
                    AI_AssistedContent = TagData.Exclude;
                    AI_AssistedContentBTN.setBackgroundColor(getColor(R.color.GenreExclude));
                }
                else if(AI_AssistedContent == TagData.Exclude)
                {
                    AI_AssistedContent = TagData.Default;
                    AI_AssistedContentBTN.setBackgroundColor(getColor(R.color.ButtonInner));
                }
            }
        });

        AI_GeneratedContentBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                if(AI_GeneratedContent == TagData.Default)
                {
                    AI_GeneratedContent = TagData.Include;
                    AI_GeneratedContentBTN.setBackgroundColor(getColor(R.color.GenreInclude));

                    AI_GeneratedContentBTN.setCompoundDrawables(getDrawable(R.drawable.genre_include), null, null, null);
                }
                else if(AI_GeneratedContent == TagData.Include)
                {
                    AI_GeneratedContent = TagData.Exclude;
                    AI_GeneratedContentBTN.setBackgroundColor(getColor(R.color.GenreExclude));
                }
                else if(AI_GeneratedContent == TagData.Exclude)
                {
                    AI_GeneratedContent = TagData.Default;
                    AI_GeneratedContentBTN.setBackgroundColor(getColor(R.color.ButtonInner));
                }
            }
        });


        SearchBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                HasPages = false;

                PageNum = 1;
                String URL = GetURL();

                GetBooks(URL);
                DrawPages();
            }
        });

        SearchViewer.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String s)
            {
                HasPages = false;

                Scroller.smoothScrollTo(0,0);
                Title = SearchViewer.getQuery().toString();

                String URL = GetURL();
                PageNum = 1;

                GetBooks(URL);
                DrawPages();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        FirstBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Scroller.smoothScrollTo(0,0);

                PageNum = 1;
                DrawPages();

                String URL = GetURL();
                GetBooks(URL);
            }
        });

        PreviousBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Previous();
            }
        });

        OneBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Scroller.smoothScrollTo(0,0);

                PageNum = Integer.parseInt((String) OneBTN.getText());
                DrawPages();

                String URL = GetURL();
                GetBooks(URL);
            }
        });
        TwoBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Scroller.smoothScrollTo(0,0);

                PageNum = Integer.parseInt((String) TwoBTN.getText());
                DrawPages();

                String URL = GetURL();
                GetBooks(URL);
            }
        });
        ThreeBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Scroller.smoothScrollTo(0,0);

                PageNum = Integer.parseInt((String) ThreeBTN.getText());
                DrawPages();

                String URL = GetURL();
                GetBooks(URL);
            }
        });
        FourBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Scroller.smoothScrollTo(0,0);

                PageNum = Integer.parseInt((String) FourBTN.getText());
                DrawPages();

                String URL = GetURL();
                GetBooks(URL);
            }
        });
        FiveBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Scroller.smoothScrollTo(0,0);

                PageNum = Integer.parseInt((String) FiveBTN.getText());
                DrawPages();

                String URL = GetURL();
                GetBooks(URL);
            }
        });

        NextBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Next();
            }
        });

        LastBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Scroller.smoothScrollTo(0,0);

                PageNum = PageCount;
                DrawPages();

                String URL = GetURL();
                GetBooks(URL);
            }
        });

        SearchRV.setLayoutManager(new LinearLayoutManager(this));
        SearchRV.setHasFixedSize(true);

        BackBTN = findViewById(R.id.BackBTN);
        BackBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

        FictionCount = 0;

        String URL = GetURL();

        Pref = getSharedPreferences("Settings", MODE_PRIVATE);
        PrefEditor = Pref.edit();

        IsDarkMode = Pref.getBoolean("AppTheme", false);

        SwitchThemes(IsDarkMode);

        GetBooks(URL);
        DrawPages();
        SetAdvancedSearch();
    }

    public void GetBooks(String URL)
    {
        String finalURL = URL;
        Thread ResultsThread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    Document SearchDocument = Jsoup.connect(finalURL).get();
                    Elements SearchFictions = SearchDocument.getElementsByClass("row fiction-list-item");
                    Elements SearchPages = SearchDocument.getElementsByClass("pagination");

                    if(SearchPages.size() > 0)
                    {
                        HasPages = true;
                        PageCount = Integer.parseInt(SearchPages.first().children().last().children().first().attributes().get("data-page"));
                    }

                    FictionCount = SearchFictions.size();
                    SearchBooks = new Book[FictionCount];

                    int LoopIndex = 0;

                    for(Element Fiction : SearchFictions)
                    {
                        SearchBooks[LoopIndex] = new Book();

                        int ExternalID = Integer.parseInt(String.valueOf(Fiction.getElementsByClass("fiction-title").get(0)).split("\"")[3].split("/")[2]);
                        SearchBooks[LoopIndex] = new Book().CreateBook(getApplicationContext(), ExternalID, false, false, false);

                        LoopIndex++;
                    }
                }
                catch (IOException | InterruptedException e)
                {
                    throw new RuntimeException(e);
                }
            }
        });

        ResultsThread.start();
        try
        {
            ResultsThread.join();
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }

        Thread ResultsFinished = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                for(int i = 0; i < SearchBooks.length; i++)
                {
                    while(SearchBooks[i] == null || !SearchBooks[i].IsCompleted)
                    {
                        try
                        {
                            Thread.sleep(50);
                        }
                        catch (InterruptedException e)
                        {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        });

        ResultsFinished.start();
        try
        {
            ResultsFinished.join();
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }

        adapter = new BookAdapter(Arrays.asList(SearchBooks));
        SearchRV.setAdapter(adapter);
    }

    public void Next()
    {
        Scroller.smoothScrollTo(0,0);

        PageNum++;
        DrawPages();

        String URL = GetURL();
        GetBooks(URL);
    }

    public void Previous()
    {
        Scroller.smoothScrollTo(0,0);

        PageNum--;
        DrawPages();

        String URL = GetURL();
        GetBooks(URL);
    }

    public void SetAdvancedSearch()
    {
        if(ExpandedSearch)
        {
            AdvancedSearch.setVisibility(View.VISIBLE);
        }
        else
        {
            AdvancedSearch.setVisibility(View.GONE);
        }
    }

    void DrawPages()
    {
        if(PageNum == 1)
        {
            FirstBTN.setVisibility(View.GONE);
            PreviousBTN.setVisibility(View.GONE);

            OneBTN.setBackgroundColor(getColor(R.color.ButtonInner));
            TwoBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));
            ThreeBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));
            FourBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));
            FiveBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));

            NextBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));
            LastBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));

            NextBTN.setVisibility(View.VISIBLE);
            LastBTN.setVisibility(View.VISIBLE);

            OneBTN.setText(String.valueOf(PageNum));
            TwoBTN.setText(String.valueOf(PageNum + 1));
            ThreeBTN.setText(String.valueOf(PageNum + 2));
            FourBTN.setText(String.valueOf(PageNum + 3));
            FiveBTN.setText(String.valueOf(PageNum + 4));
        }
        else if(PageNum == 2)
        {
            FirstBTN.setVisibility(View.VISIBLE);
            PreviousBTN.setVisibility(View.VISIBLE);

            FirstBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));
            PreviousBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));

            OneBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));
            TwoBTN.setBackgroundColor(getColor(R.color.ButtonInner));
            ThreeBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));
            FourBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));
            FiveBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));

            NextBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));
            LastBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));

            NextBTN.setVisibility(View.VISIBLE);
            LastBTN.setVisibility(View.VISIBLE);

            OneBTN.setText(String.valueOf(PageNum - 1));
            TwoBTN.setText(String.valueOf(PageNum));
            ThreeBTN.setText(String.valueOf(PageNum + 1));
            FourBTN.setText(String.valueOf(PageNum + 2));
            FiveBTN.setText(String.valueOf(PageNum + 3));
        }
        else if(PageNum == PageCount - 1)
        {
            FirstBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));
            PreviousBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));

            FirstBTN.setVisibility(View.VISIBLE);
            PreviousBTN.setVisibility(View.VISIBLE);

            OneBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));
            TwoBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));
            ThreeBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));
            FourBTN.setBackgroundColor(getColor(R.color.ButtonInner));
            FiveBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));

            NextBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));
            LastBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));

            NextBTN.setVisibility(View.VISIBLE);
            LastBTN.setVisibility(View.VISIBLE);

            OneBTN.setText(String.valueOf(PageNum - 3));
            TwoBTN.setText(String.valueOf(PageNum - 2));
            ThreeBTN.setText(String.valueOf(PageNum - 1));
            FourBTN.setText(String.valueOf(PageNum));
            FiveBTN.setText(String.valueOf(PageNum + 1));
        }
        else if(PageNum == PageCount)
        {
            FirstBTN.setVisibility(View.VISIBLE);
            PreviousBTN.setVisibility(View.VISIBLE);

            FirstBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));
            PreviousBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));

            OneBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));
            TwoBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));
            ThreeBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));
            FourBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));
            FiveBTN.setBackgroundColor(getColor(R.color.ButtonInner));

            NextBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));
            LastBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));

            NextBTN.setVisibility(View.GONE);
            LastBTN.setVisibility(View.GONE);

            OneBTN.setText(String.valueOf(PageNum - 4));
            TwoBTN.setText(String.valueOf(PageNum - 3));
            ThreeBTN.setText(String.valueOf(PageNum - 2));
            FourBTN.setText(String.valueOf(PageNum - 1));
            FiveBTN.setText(String.valueOf(PageNum));
        }
        else
        {
            FirstBTN.setVisibility(View.VISIBLE);
            PreviousBTN.setVisibility(View.VISIBLE);

            FirstBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));
            PreviousBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));

            OneBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));
            TwoBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));
            ThreeBTN.setBackgroundColor(getColor(R.color.ButtonInner));
            FourBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));
            FiveBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));

            NextBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));
            LastBTN.setBackgroundColor(getColor(R.color.ToolbarBlue));

            NextBTN.setVisibility(View.VISIBLE);
            LastBTN.setVisibility(View.VISIBLE);

            OneBTN.setText(String.valueOf(PageNum - 2));
            TwoBTN.setText(String.valueOf(PageNum - 1));
            ThreeBTN.setText(String.valueOf(PageNum));
            FourBTN.setText(String.valueOf(PageNum + 1));
            FiveBTN.setText(String.valueOf(PageNum + 2));
        }

        if(!HasPages)
        {
            SearchPagesLayout.setVisibility(View.GONE);
        }
        else
        {
            SearchPagesLayout.setVisibility(View.VISIBLE);
        }
    }

    void SwitchThemes(boolean DarkMode)
    {

    }

    public String GetURL()
    {
        String URL = BaseURL;

        if(Title.length() > 0)
            URL += "&title=" + Title;

        if(ExpandedSearch)
        {
            if(KeywordField.getText().length() > 0)
                URL += "&keyword=" + KeywordField.getText().toString();

            if(AuthorField.getText().length() > 0)
                URL += "&author=" + AuthorField.getText().toString();

            if(ActionGenre != TagData.Default)
            {
                if(ActionGenre == TagData.Include)
                    URL += "&tagsAdd=action";
                else if(ActionGenre == TagData.Exclude)
                    URL += "&tagsRemove=action";
            }

            if(AdventureGenre != TagData.Default)
            {
                if(AdventureGenre == TagData.Include)
                    URL += "&tagsAdd=adventure";
                else if(AdventureGenre == TagData.Exclude)
                    URL += "&tagsRemove=adventure";
            }

            if(ComedyGenre != TagData.Default)
            {
                if(ComedyGenre == TagData.Include)
                    URL += "&tagsAdd=comedy";
                else if(ComedyGenre == TagData.Exclude)
                    URL += "&tagsRemove=comedy";
            }

            if(ContemporaryGenre != TagData.Default)
            {
                if(ContemporaryGenre == TagData.Include)
                    URL += "&tagsAdd=contemporary";
                else if(ContemporaryGenre == TagData.Exclude)
                    URL += "&tagsRemove=contemporary";
            }

            if(DramaGenre != TagData.Default)
            {
                if(DramaGenre == TagData.Include)
                    URL += "&tagsAdd=drama";
                else if(DramaGenre == TagData.Exclude)
                    URL += "&tagsRemove=drama";
            }

            if(FantasyGenre != TagData.Default)
            {
                if(FantasyGenre == TagData.Include)
                    URL += "&tagsAdd=fantasy";
                else if(FantasyGenre == TagData.Exclude)
                    URL += "&tagsRemove=fantasy";
            }

            if(HistoricalGenre != TagData.Default)
            {
                if(HistoricalGenre == TagData.Include)
                    URL += "&tagsAdd=historical";
                else if(HistoricalGenre == TagData.Exclude)
                    URL += "&tagsRemove=historical";
            }

            if(HorrorGenre != TagData.Default)
            {
                if(HorrorGenre == TagData.Include)
                    URL += "&tagsAdd=horror";
                else if(HorrorGenre == TagData.Exclude)
                    URL += "&tagsRemove=horror";
            }

            if(MysteryGenre != TagData.Default)
            {
                if(MysteryGenre == TagData.Include)
                    URL += "&tagsAdd=mystery";
                else if(MysteryGenre == TagData.Exclude)
                    URL += "&tagsRemove=mystery";
            }

            if(PsychologicalGenre != TagData.Default)
            {
                if(PsychologicalGenre == TagData.Include)
                    URL += "&tagsAdd=psychological";
                else if(PsychologicalGenre == TagData.Exclude)
                    URL += "&tagsRemove=psychological";
            }

            if(RomanceGenre != TagData.Default)
            {
                if(RomanceGenre == TagData.Include)
                    URL += "&tagsAdd=romance";
                else if(RomanceGenre == TagData.Exclude)
                    URL += "&tagsRemove=romance";
            }

            if(SatireGenre != TagData.Default)
            {
                if(SatireGenre == TagData.Include)
                    URL += "&tagsAdd=satire";
                else if(SatireGenre == TagData.Exclude)
                    URL += "&tagsRemove=satire";
            }

            if(Sci_FiGenre != TagData.Default)
            {
                if(Sci_FiGenre == TagData.Include)
                    URL += "&tagsAdd=sci_fi";
                else if(Sci_FiGenre == TagData.Exclude)
                    URL += "&tagsRemove=sci_fi";
            }

            if(ShortStoryGenre != TagData.Default)
            {
                if(ShortStoryGenre == TagData.Include)
                    URL += "&tagsAdd=one_shot";
                else if(ShortStoryGenre == TagData.Exclude)
                    URL += "&tagsRemove=one_shot";
            }

            if(TragedyGenre != TagData.Default)
            {
                if(TragedyGenre == TagData.Include)
                    URL += "&tagsAdd=tragedy";
                else if(TragedyGenre == TagData.Exclude)
                    URL += "&tagsRemove=tragedy";
            }

            // Warnings
            if(Profanity != TagData.Default)
            {
                if(Profanity == TagData.Include)
                    URL += "&tagsAdd=profanity";
                else if(Profanity == TagData.Exclude)
                    URL += "&tagsRemove=profanity";
            }

            if(SexualContent != TagData.Default)
            {
                if(SexualContent == TagData.Include)
                    URL += "&tagsAdd=sexuality";
                else if(SexualContent == TagData.Exclude)
                    URL += "&tagsRemove=sexuality";
            }

            if(GraphicViolence != TagData.Default)
            {
                if(GraphicViolence == TagData.Include)
                    URL += "&tagsAdd=graphic_violence";
                else if(GraphicViolence == TagData.Exclude)
                    URL += "&tagsRemove=graphic_violence";
            }

            if(SensitiveContent != TagData.Default)
            {
                if(SensitiveContent == TagData.Include)
                    URL += "&tagsAdd=sensitive";
                else if(SensitiveContent == TagData.Exclude)
                    URL += "&tagsRemove=sensitive";
            }

            if(AI_AssistedContent != TagData.Default)
            {
                if(AI_AssistedContent == TagData.Include)
                    URL += "&tagsAdd=ai_assisted";
                else if(AI_AssistedContent == TagData.Exclude)
                    URL += "&tagsRemove=ai_assisted";
            }

            if(AI_GeneratedContent != TagData.Default)
            {
                if(AI_GeneratedContent == TagData.Include)
                    URL += "&tagsAdd=ai_generated";
                else if(AI_GeneratedContent == TagData.Exclude)
                    URL += "&tagsRemove=ai_generated";
            }

            // Page Counters
            if(PageMax != 20000)
                URL += "&maxPages=" + PageMax;

            if(PageMin != 0)
                URL += "&minPages=" + PageMin;

            // Rating Counter
            if(RatingMax != 5.0f)
                URL += "&maxRating=" + RatingMax;

            if(RatingMin != 0.0f)
                URL += "&minRating=" + RatingMin;

            if(!TypeDropdown.getSelectedItem().toString().equals("All"))
            {
                if(TypeDropdown.getSelectedItem().toString().equals("Fan Fiction"))
                    URL += "&type=fanfiction";
                else if(TypeDropdown.getSelectedItem().toString().equals("Original"))
                    URL += "&type=original";
            }

            if(!OrderByDropdown.getSelectedItem().toString().equals("Relevance"))
            {
                if(OrderByDropdown.getSelectedItem().toString().equals("Popularity"))
                    URL += "&orderBy=popularity";
                else if(OrderByDropdown.getSelectedItem().toString().equals("Average Rating"))
                    URL += "&orderBy=rating";
                else if(OrderByDropdown.getSelectedItem().toString().equals("Last Update"))
                    URL += "&orderBy=last_update";
                else if(OrderByDropdown.getSelectedItem().toString().equals("Release Date"))
                    URL += "&orderBy=release_date";
                else if(OrderByDropdown.getSelectedItem().toString().equals("Followers"))
                    URL += "&orderBy=followers";
                else if(OrderByDropdown.getSelectedItem().toString().equals("Number of Pages"))
                    URL += "&orderBy=length";
                else if(OrderByDropdown.getSelectedItem().toString().equals("Views"))
                    URL += "&orderBy=views";
                else if(OrderByDropdown.getSelectedItem().toString().equals("Title"))
                    URL += "&orderBy=title";
                else if(OrderByDropdown.getSelectedItem().toString().equals("Author"))
                    URL += "&orderBy=author";
            }

            if(!OrderByAscendingDropdown.getSelectedItem().toString().equals("Descending"))
                URL += "&dir=asc";
        }

        if(PageNum > 1)
            URL += "&page=" + PageNum;

        Log.println(Log.INFO, "Hi", URL);

        return URL;
    }
}