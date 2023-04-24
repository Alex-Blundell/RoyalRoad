package com.example.royalroad;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

public class Book
{
    public enum BookType
    {
        Fanfiction,
        Original
    }

    public enum Genres
    {
        Action,
        Adventure,
        Comedy,
        Contemporary,
        Drama,
        Fantasy,
        Historical,
        Horror,
        Mystery,
        Psychological,
        Romance,
        Satire,
        Sci_Fi,
        Short_Story,
        Tragedy
    }

    public enum Tags
    {
        Anti_Hero_Lead,
        Artificial_Intelligence,
        Attractive_Lead,
        Cyberpunk,
        Dungeon,
        Dystopia,
        Female_Lead,
        First_Contact,
        GameLit,
        Gender_Bender,
        GrimDark,
        Hard_SciFi,
        Harem,
        High_Fantasy,
        LitRPG,
        Loop,
        Low_Fantasy,
        Magic,
        Male_Lead,
        Martial_Arts,
        Multiple_Lead_Characters,
        Mythos,
        Non_Human_Lead,
        Portal_Fantasy_Isekai,
        Post_Apocalypse,
        Progression,
        Reader_Interactive,
        Reincarnation,
        Ruling_Class,
        School_Life,
        Secret_Identity,
        Slice_of_Life,
        Space_Opera,
        Sports,
        Steampunk,
        Strategy,
        Strong_Lead,
        Super_Heroes,
        Supernatural,
        Technologically_Engineered,
        Time_Travel,
        Urban_Fantasy,
        Villainous_Lead,
        Virtual_Reality,
        War_and_Military,
        Wuxia,
        Xianxia
    }

    public enum Warnings
    {
        Profanity,
        Sexual_Content,
        Gore,
        Traumatising_Content
    }

    public class Chapter
    {
        public int ID;
        public String Name;
        public String Content;
    }

    public int InternalID;
    public int ExternalID;
    public BookType Type;
    public String Title;
    public String Description;
    public ArrayList<Genres> SelectedGenres;
    public int PageCount;
    public int Followers;
    public int Favourites;
    public double Rating;
    public GregorianCalendar CreatedDatetime;
    public GregorianCalendar LastUpdatedDatetime;
    public GregorianCalendar DownloadedDatetime;
    public ArrayList<Chapter> Chapters;
    public ArrayList<Tags> TagsList;
    public ArrayList<Warnings> ContentWarnings;

    public Book(int internalID, int externalID, BookType type)
    {
        this.InternalID = internalID;
        this.ExternalID = externalID;
        this.Type = type;
    }

    public void getAllChapters()
    {
        this.Chapters = new ArrayList<Chapter>();
    }

    public void getCover()
    {

    }

    public void getTitle()
    {

    }

    public void getDescription()
    {

    }

    public void getGenres()
    {

    }

    public void getPageCount()
    {

    }

    public void getFollowerCount()
    {

    }

    public void getDates()
    {

    }

    public void getTags()
    {

    }

    public Chapter GetChapter(int Index)
    {
        Chapter SelectedChapter = Chapters.get(Index);
        return SelectedChapter;
    }
}
