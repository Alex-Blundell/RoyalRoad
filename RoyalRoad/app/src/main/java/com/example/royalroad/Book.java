package com.example.royalroad;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.annotations.concurrent.Background;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.Current;
import org.checkerframework.common.returnsreceiver.qual.This;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Book implements Serializable
{
    public enum BookType
    {
        Original,
        Fanfiction
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

    public static class Review implements Serializable
    {
        String Title;
        String ReviewedAt;
        String ReviewAuthor;
        String[] ReviewDescription;

        String DateTime;

        double OverallScore;
        double StyleScore;
        double StoryScore;
        double GrammerScore;
        double CharacterScore;

        boolean IsAdvancedReview;
    }

    public static class Chapter implements Serializable
    {
        public int ID;
        public String Name;
        public String URL;
        public ArrayList<Paragraph> Content;

        public int ChapterProgress;
    }

    public static class Paragraph implements Serializable
    {
        public int ParagraphID;
        public String Content;
    }

    public static class Details implements Serializable
    {
        public Details(String text, Drawable icon, int strokeColor, int iconColor)
        {
            this.Text = text;
            this.Icon = icon;
            this.IconColor = iconColor;
            this.StrokeID = strokeColor;
        }

        public Details(String text, int strokeID)
        {
            this.Text = text;
            this.StrokeID = strokeID;
        }

        public String Text;
        public Drawable Icon;
        public int IconColor;

        public int StrokeID;
    }

    public enum LineStyle
    {
        Default,
        Bold,
        Italic,
        Italic_Bold
    }

    public int InternalID;
    public int ExternalID;
    public BookType Type;
    public String Title;
    public String Author;
    public String Description;
    public String CoverURL;
    public ArrayList<Genres> SelectedGenres;
    public int PageCount;
    public int Followers;
    public int Favourites;
    public double Rating;
    public Date CreatedDatetime;
    public Date LastUpdatedDatetime;
    public Date DownloadedDatetime;
    public ArrayList<Chapter> Chapters;
    public ArrayList<Tags> TagsList;
    public ArrayList<Warnings> ContentWarnings;
    public boolean HasRead;
    public int LastReadChapter;

    public int ProfileID;

    public boolean HasUnreadUpdate = false;

    public boolean IsCompleted = false;

    private Document StoryDoc;

    private boolean DeleteCharacters;

    private String[] RemoveTags = new String[] {
            "</em>",
            "<em>",
            "</strong>",
            "<strong>",
            " &nbsp;"
    };

    private String[] ReplaceTags = new String[]
    {
        "<p style=\"text-align: center\">",
    };


    public Book()
    {

    }

    @Background
    public Book CreateBook(Context context, long ExternalID, boolean GetChapterContent, boolean AddToDB, boolean AddToFirebase) throws InterruptedException {
        Book NewBook = new Book();
        Thread BookThread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    NewBook.IsCompleted = false;

                    String URL = "https://www.royalroad.com/fiction/" + ExternalID + "/";
                    Connection.Response response = null;

                    response = Jsoup.connect(URL).followRedirects(true).execute();

                    String RealURL = String.valueOf(response.url());
                    StoryDoc = Jsoup.connect(RealURL).ignoreHttpErrors(true).timeout(0).get();
                    DBHandler SQLiteDB = new DBHandler(context);

                    NewBook.SetExternalID((int)ExternalID);

                    NewBook.SetType(CreateBookType());

                    String[] TitleAuthor = CreateTitleAuthor();

                    NewBook.SetTitle(TitleAuthor[0]);
                    NewBook.SetAuthor(TitleAuthor[1]);
                    NewBook.ProfileID = Integer.parseInt(TitleAuthor[2]);

                    NewBook.SetDescription(CreateDescription());

                    NewBook.SetCover(CreateCoverURL());

                    NewBook.SetWarnings(CreateWarnings());
                    NewBook.SetGenres(CreateGenres());
                    NewBook.SetTags(CreateTags());

                    int[] Counts = CreateCounts();

                    NewBook.SetFollowerCount(Counts[0]);
                    NewBook.SetFavouriteCount(Counts[1]);
                    NewBook.SetPageCount(Counts[2]);

                    NewBook.SetRating(CreateRating());

                    // DateTimes.
                    NewBook.SetCreatedDateTime(Calendar.getInstance().getTime());
                    NewBook.SetLastUpdatedDateTime(Calendar.getInstance().getTime());
                    NewBook.SetDownloadedDateTime(Calendar.getInstance().getTime());

                    if (GetChapterContent)
                    {
                        NewBook.SetAllChapters(CreateChapters());
                    }
                    else
                    {
                        ArrayList<Chapter> AllChapters = new ArrayList<>();
                        Elements ChapterLinks = StoryDoc.select("td:not([class]) a");

                        int Index = 0;
                        for(Element Link : ChapterLinks)
                        {
                            Chapter NewChapter = new Chapter();

                            NewChapter.ID = Index;
                            NewChapter.Name = Link.text();
                            NewChapter.URL = Link.attr("abs:href");
                            NewChapter.ChapterProgress = 0;

                            AllChapters.add(new Chapter());
                            Index++;
                        }

                        NewBook.SetAllChapters(AllChapters);
                    }

                    Log.println(Log.INFO, "Hi", "Setting HasRead and LastReadChapter to 0");

                    NewBook.SetHasRead(false);
                    NewBook.SetLastReadChapter(0);
                    NewBook.HasUnreadUpdate = false;

                    StoryDoc = null;
                    NewBook.IsCompleted = true;

                    if(AddToDB)
                    {
                        new AddToDBTask(context, NewBook).execute();
                    }

                    if(AddToFirebase)
                    {
                        FirebaseFirestore FireDB = FirebaseFirestore.getInstance();

                        SharedPreferences Pref = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
                        int UserID = Pref.getInt("UserID", 1);


                        FirebaseFirestore finalFireDB = FireDB;
                        FireDB.collection("User_Books")
                                .whereEqualTo("UserID", UserID)
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>()
                                {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots)
                                    {
                                        if(queryDocumentSnapshots.getDocuments().isEmpty())
                                        {
                                            Log.println(Log.INFO, "Hi", "Creating New Document");
                                            // Can't Find a User_Book Entry for this User. Create One.

                                            Map<String, Object> User = new HashMap<>();
                                            ArrayList<Integer> ExternalIDs = new ArrayList<>();

                                            ExternalIDs.add((int) ExternalID);

                                            User.put("UserID", UserID);
                                            User.put("ExternalID", ExternalIDs);

                                            finalFireDB.collection("User_Books").add(User);
                                        }
                                        else
                                        {
                                            DocumentReference Update = queryDocumentSnapshots.getDocuments().get(0).getReference();

                                            finalFireDB.collection("User_Books")
                                                    .document(Update.getId())
                                                    .update("ExternalID", FieldValue.arrayUnion(ExternalID));
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener()
                                {
                                    @Override
                                    public void onFailure(@NonNull Exception e)
                                    {

                                    }
                                });


                        FireDB = null;
                    }

                    SQLiteDB.close();
                }
                catch (IOException e)
                {
                    throw new RuntimeException(e);
                }
            }
        });

        BookThread.start();

        return NewBook;
    }

    private double CreateRating()
    {
        double Rating = 0.00;

        Log.println(Log.INFO, "Hi", "Getting Rating");
        Elements RatingElements = this.StoryDoc.getElementsByClass("list-item");

        for (int i = 0; i < RatingElements.size(); i++)
        {
            if (RatingElements.get(i).childrenSize() > 0)
            {
                String RatingTitle = RatingElements.get(i).child(0).attr("data-original-title");

                if (RatingTitle.equals("Overall Score"))
                {
                    String RatingString = RatingElements.get(i).child(0).attr("data-content");
                    RatingString = RatingString.substring(0, RatingString.length() - 4);

                    Rating = Double.parseDouble(RatingString);
                }
            }
        }

        RatingElements.clear();

        return Rating;
    }

    private String CreateDescription()
    {
        return this.StoryDoc.selectFirst(".description").text();
    }

    private String[] CreateTitleAuthor()
    {
        String[] TitleAuthor = new String[3];

        Log.println(Log.INFO, "Hi", "Getting Title and Author");

        Element Title = StoryDoc.selectFirst("h1.font-white");
        Element Author = StoryDoc.selectFirst("h4.font-white");

        String AuthorTXT = Author.text();
        AuthorTXT = AuthorTXT.substring(3);

        TitleAuthor[0] = Title.text();
        TitleAuthor[1] = AuthorTXT;
        TitleAuthor[2] = Author.getElementsByTag("a").get(0).attr("href").split("/")[2];

        return TitleAuthor;
    }

    private ArrayList<Book.Chapter> CreateChapters() throws IOException
    {
        Log.println(Log.INFO, "Hi", "Getting Chapters");

        ArrayList<Book.Chapter> AllChapters = new ArrayList<>();
        Elements ChapterLinks = StoryDoc.select("td:not([class]) a");

        int[] ChapterIndex = {0};

        for (Element Link : ChapterLinks)
        {
            Document ChapterContent = Jsoup.connect(Link.attr("abs:href")).get();

            Book.Chapter NewChapter = new Book.Chapter();

            NewChapter.ID = ChapterIndex[0];
            NewChapter.Name = Link.text();
            NewChapter.URL = Link.attr("abs:href");
            NewChapter.ChapterProgress = 0;

            String RawChapter = ChapterContent.select(".chapter-content").first().toString();
            NewChapter.Content = CleanChapter(RawChapter);

            int ChapterID = ChapterIndex[0] + 1;

            Log.println(Log.INFO, "Hi", "Got Chapter " + ChapterID + " / " + ChapterLinks.size());
            ChapterContent = null;

            AllChapters.add(NewChapter);
            ChapterIndex[0]++;
        }

        Log.println(Log.INFO, "Hi", "Finished Getting Chapters");

        ChapterLinks.clear();
        return AllChapters;
    }

    public ArrayList<Paragraph> CleanChapter(String RawChapter)
    {
        ArrayList<Paragraph> Content = new ArrayList<>();
        String[] Paragraphs;

        int ParagraphID = 0;

        // A Fix that seems to happen in some stories where they have two break tags next to each other.
        RawChapter = RawChapter.replace("<br><br>", "<br>");

        // This is a Fix for something that happens sometimes in the HTML Code, the Break comes before the end of a Bold or Italic Line.
        RawChapter = RawChapter.replace("<br></strong>", "</strong><br>");
        RawChapter = RawChapter.replace("<br></em>", "</em><br>");

        // Replacing HTML Tags that break a Text into seperate Paragraphs.
        RawChapter = RawChapter.replace("</p>", "<RR_APP_PARAGRAPH_BREAK>");
        RawChapter = RawChapter.replace("<br>", "<RR_APP_PARAGRAPH_BREAK>");

        // Split Chapter into Seperate Paragraphs.
        Paragraphs = RawChapter.split("<RR_APP_PARAGRAPH_BREAK>");

        for(int i = 0; i < Paragraphs.length; i++)
        {
            Paragraph ThisParagraph = new Paragraph();

            ThisParagraph.ParagraphID = ParagraphID;
            String AlteredParagraph = Paragraphs[i];

            AlteredParagraph = AlteredParagraph.replace("&nbsp;", "");
            AlteredParagraph = AlteredParagraph.replace("&amp;", "&");
            AlteredParagraph = AlteredParagraph.replace("<em></em>", "");
            AlteredParagraph = AlteredParagraph.replace("<em> </em>", "");

            AlteredParagraph = AlteredParagraph.replace("<p style=\"text-align: center\">", "<RR_APP_PARAGRAPH_ALIGN_CENTER>");
            AlteredParagraph = AlteredParagraph.replace("<p style=\"text-align: left\">", "<RR_APP_PARAGRAPH_ALIGN_LEFT>");
            AlteredParagraph = AlteredParagraph.replace("<p style=\"text-align: right\">", "<RR_APP_PARAGRAPH_ALIGN_RIGHT>");
            AlteredParagraph = AlteredParagraph.replace("<p style=\"text-align: justify\">", "<RR_APP_PARAGRAPH_ALIGN_JUSTIFY>");
            AlteredParagraph = AlteredParagraph.replace("<span style=\"text-decoration: underline\">", "<RR_APP_PARAGRAPH_UNDERLINE>");

            DeleteCharacters = false;
            StringBuilder NewParagraph = new StringBuilder(AlteredParagraph);
            ArrayList<Integer> DeltedIndexes = new ArrayList<Integer>();

            for(int j = 0; j < AlteredParagraph.length(); j++)
            {
                if(AlteredParagraph.charAt(j) == '<')
                {
                    DeleteCharacters = true;
                }

                if(DeleteCharacters)
                {
                    boolean ProtectedTag = false;

                    if(AlteredParagraph.length() > j + 1 && AlteredParagraph.charAt(j) == '<')
                    {
                        if(AlteredParagraph.charAt(j + 1) == 'e')
                        {
                            if(AlteredParagraph.charAt( j + 2) == 'm') // Protecting Italics.
                            {
                                ProtectedTag = true;
                                DeleteCharacters = false;
                            }
                        }
                        else if(AlteredParagraph.charAt(j + 1) == '/' && j != 0)
                        {
                            if(AlteredParagraph.charAt(j + 2) == 'e')
                            {
                                if(AlteredParagraph.charAt(j + 3) == 'm') // Protecting End Italics.
                                {
                                    ProtectedTag = true;
                                    DeleteCharacters = false;
                                }
                            }
                            else if(AlteredParagraph.charAt(j + 2) == 's')
                            {
                                if(AlteredParagraph.charAt(j + 3) == 't')
                                {
                                    if(AlteredParagraph.charAt(j + 4) == 'r')
                                    {
                                        if(AlteredParagraph.charAt(j + 5) == 'o')
                                        {
                                            if(AlteredParagraph.charAt(j + 6) == 'n')
                                            {
                                                if(AlteredParagraph.charAt(j + 7) == 'g')
                                                {
                                                    if(AlteredParagraph.charAt(j + 8) == '>') // Protecting End Bold.
                                                    {
                                                        ProtectedTag = true;
                                                        DeleteCharacters = false;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else if(AlteredParagraph.charAt(j + 1) == 'i')
                        {
                            if(AlteredParagraph.charAt(j + 2) == 'm')
                            {
                                if(AlteredParagraph.charAt(j + 3) == 'g') // Protecting Image Tag.
                                {
                                    ProtectedTag = true;
                                    DeleteCharacters = false;
                                }
                            }
                        }
                        else if(AlteredParagraph.charAt(j + 1) == 's')
                        {
                            if(AlteredParagraph.charAt(j + 2) == 't')
                            {
                                if(AlteredParagraph.charAt(j + 3) == 'r')
                                {
                                    if(AlteredParagraph.charAt(j + 4) == 'o')
                                    {
                                        if(AlteredParagraph.charAt(j + 5) == 'n')
                                        {
                                            if(AlteredParagraph.charAt(j + 6) == 'g')
                                            {
                                                if(AlteredParagraph.charAt(j + 7) == '>') // Protecting Bold Tag.
                                                {
                                                    ProtectedTag = true;
                                                    DeleteCharacters = false;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else if(AlteredParagraph.charAt(j + 1) == 'h')
                        {
                            if(AlteredParagraph.charAt(j + 2) == 'r')
                            {
                                if(AlteredParagraph.charAt(j + 3) == '>')
                                {
                                    ProtectedTag = true;
                                    DeleteCharacters = false;
                                }
                            }
                        }
                        else if(AlteredParagraph.charAt(j + 1) == 'b')
                        {
                            if(AlteredParagraph.charAt(j + 2) == 'r')
                            {
                                if(AlteredParagraph.charAt( j + 3) == '>')
                                {
                                    ProtectedTag = true;
                                    DeleteCharacters = true;
                                }
                            }
                        }
                        else if(AlteredParagraph.charAt(j + 1) == 'R')
                        {
                            if(AlteredParagraph.charAt(j + 2) == 'R')
                            {
                                if(AlteredParagraph.charAt(j + 3) == '_')
                                {
                                    if(AlteredParagraph.charAt(j + 4) == 'A')
                                    {
                                        if(AlteredParagraph.charAt(j + 5) == 'P')
                                        {
                                            if(AlteredParagraph.charAt(j + 6) == 'P')
                                            {
                                                ProtectedTag = true;
                                                DeleteCharacters = false;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if(!ProtectedTag)
                    {
                        DeltedIndexes.add(j);

                        if(AlteredParagraph.charAt(j) == '>')
                        {
                            DeleteCharacters = false;
                        }
                    }
                }
            }

            int DeleteCounter = 0;

            for(int Index : DeltedIndexes)
            {
                Index -= DeleteCounter;
                NewParagraph.deleteCharAt(Index);

                DeleteCounter++;
            }

            for(int j = NewParagraph.length() - 1; j > 0; j--)
            {
                if(Character.isWhitespace(NewParagraph.charAt(j)) || NewParagraph.charAt(j) == ' ')
                {
                    NewParagraph.deleteCharAt(j);
                }
                else
                {
                    break;
                }
            }

            int DeleteOffset = 0;
            for(int j = 0; j < NewParagraph.toString().length(); j++)
            {
                if(Character.isWhitespace(NewParagraph.charAt(j)) || NewParagraph.charAt(j) == ' ')
                {
                    NewParagraph.deleteCharAt(j - DeleteOffset);
                    DeleteOffset++;
                }
                else
                {
                    break;
                }
            }

            if(!NewParagraph.toString().isEmpty())
            {
                if(NewParagraph.charAt(0) == ' ')
                {
                    NewParagraph.delete(0, 1);
                }

                ThisParagraph.Content = NewParagraph.toString();
                Content.add(ThisParagraph);

                ParagraphID++;
            }
        }

        return Content;
    }

    private ArrayList<Book.Warnings> CreateWarnings()
    {
        Log.println(Log.INFO, "Hi", "Getting Warnings");

        Elements WarningElements = StoryDoc.getElementsByClass("text-center font-red-sunglo");
        ArrayList<Book.Warnings> BookWarnings = new ArrayList<>();

        if (WarningElements.size() > 0) {
            if (WarningElements.get(0).getElementsByClass("list-inline").get(0).childrenSize() > 1) {
                for (int i = 0; i < WarningElements.get(0).getElementsByClass("list-inline").get(0).childrenSize(); i++) {
                    String Warning = WarningElements.get(0).getElementsByClass("list-inline").get(0).child(i).toString();
                    Warning = Warning.substring(4, Warning.length() - 5);

                    switch (Warning) {
                        case "Gore":
                            BookWarnings.add(Book.Warnings.Gore);
                            break;

                        case "Profanity":
                            BookWarnings.add(Book.Warnings.Profanity);
                            break;

                        case "Traumatising content":
                            BookWarnings.add(Book.Warnings.Traumatising_Content);
                            break;

                        case "Sexual Content":
                            BookWarnings.add(Book.Warnings.Sexual_Content);
                            break;
                    }
                }
            }
        }

        WarningElements.clear();
        return BookWarnings;
    }

    private ArrayList<Book.Genres> CreateGenres() {
        Log.println(Log.INFO, "Hi", "Getting Genres");

        Elements GenreElements = StoryDoc.getElementsByClass("label label-default label-sm bg-blue-dark fiction-tag");
        ArrayList<Book.Genres> SelectedGenres = new ArrayList<>();

        // Add Tags and Genres.
        for (int i = 0; i < GenreElements.size(); i++) {
            switch (GenreElements.get(i).text()) {
                // Genres.
                case "Action":
                    SelectedGenres.add(Book.Genres.Action);
                    break;

                case "Adventure":
                    SelectedGenres.add(Book.Genres.Adventure);
                    break;

                case "Comedy":
                    SelectedGenres.add(Book.Genres.Comedy);
                    break;

                case "Contemporary":
                    SelectedGenres.add(Book.Genres.Contemporary);
                    break;

                case "Drama":
                    SelectedGenres.add(Book.Genres.Drama);
                    break;

                case "Fantasy":
                    SelectedGenres.add(Book.Genres.Fantasy);
                    break;

                case "Historical":
                    SelectedGenres.add(Book.Genres.Historical);
                    break;

                case "Horror":
                    SelectedGenres.add(Book.Genres.Horror);
                    break;

                case "Mystery":
                    SelectedGenres.add(Book.Genres.Mystery);
                    break;

                case "Psychological":
                    SelectedGenres.add(Book.Genres.Psychological);
                    break;

                case "Romance":
                    SelectedGenres.add(Book.Genres.Romance);
                    break;

                case "Satire":
                    SelectedGenres.add(Book.Genres.Satire);
                    break;

                case "Sci-fi":
                    SelectedGenres.add(Book.Genres.Sci_Fi);
                    break;

                case "Short Story":
                    SelectedGenres.add(Book.Genres.Short_Story);
                    break;

                case "Tragedy":
                    SelectedGenres.add(Book.Genres.Tragedy);
                    break;
            }
        }

        GenreElements.clear();

        return SelectedGenres;
    }

    private ArrayList<Book.Tags> CreateTags() {
        ArrayList<Book.Tags> BookTags = new ArrayList<>();

        Elements SelectedTags = this.StoryDoc.select(".tags span");

        for (Element Tag : SelectedTags) {
            switch (Tag.text()) {
                case "Anti-Hero Lead":
                    BookTags.add(Book.Tags.Anti_Hero_Lead);
                    break;

                case "Artificial Intelligence":
                    BookTags.add(Book.Tags.Artificial_Intelligence);
                    break;

                case "Attractive Lead":
                    BookTags.add(Book.Tags.Attractive_Lead);
                    break;

                case "Cyberpunk":
                    BookTags.add(Book.Tags.Cyberpunk);
                    break;

                case "Dungeon":
                    BookTags.add(Book.Tags.Dungeon);
                    break;

                case "Dystopia":
                    BookTags.add(Book.Tags.Dystopia);
                    break;

                case "Female Lead":
                    BookTags.add(Book.Tags.Female_Lead);
                    break;

                case "First Contact":
                    BookTags.add(Book.Tags.First_Contact);
                    break;

                case "GameLit":
                    BookTags.add(Book.Tags.GameLit);
                    break;

                case "Gender Bender":
                    BookTags.add(Book.Tags.Gender_Bender);
                    break;

                case "Grimdark":
                    BookTags.add(Book.Tags.GrimDark);
                    break;

                case "Hard Sci-fi":
                    BookTags.add(Book.Tags.Hard_SciFi);
                    break;

                case "Harem":
                    BookTags.add(Book.Tags.Harem);
                    break;

                case "High Fantasy":
                    BookTags.add(Book.Tags.High_Fantasy);
                    break;

                case "LitRPG":
                    BookTags.add(Book.Tags.LitRPG);
                    break;

                case "Loop":
                    BookTags.add(Book.Tags.Loop);
                    break;

                case "Low Fantasy":
                    BookTags.add(Book.Tags.Low_Fantasy);
                    break;

                case "Magic":
                    BookTags.add(Book.Tags.Magic);
                    break;

                case "Male Lead":
                    BookTags.add(Book.Tags.Male_Lead);
                    break;

                case "Martial Arts":
                    BookTags.add(Book.Tags.Martial_Arts);
                    break;

                case "Multiple Lead Characters":
                    BookTags.add(Book.Tags.Multiple_Lead_Characters);
                    break;

                case "Mythos":
                    BookTags.add(Book.Tags.Mythos);
                    break;

                case "Non-Human Lead":
                    BookTags.add(Book.Tags.Non_Human_Lead);
                    break;

                case "Portal Fantasy / Isekai":
                    BookTags.add(Book.Tags.Portal_Fantasy_Isekai);
                    break;

                case "Post Apocalypse":
                    BookTags.add(Book.Tags.Post_Apocalypse);
                    break;

                case "Progression":
                    BookTags.add(Book.Tags.Progression);
                    break;

                case "Reader Interactive":
                    BookTags.add(Book.Tags.Reader_Interactive);
                    break;

                case "Reincarnation":
                    BookTags.add(Book.Tags.Reincarnation);
                    break;

                case "Ruling Class":
                    BookTags.add(Book.Tags.Ruling_Class);
                    break;

                case "School Life":
                    BookTags.add(Book.Tags.School_Life);
                    break;

                case "Secret Identity":
                    BookTags.add(Book.Tags.Secret_Identity);
                    break;

                case "Slice of Life":
                    BookTags.add(Book.Tags.Slice_of_Life);
                    break;

                case "Space Opera":
                    BookTags.add(Book.Tags.Space_Opera);
                    break;

                case "Sports":
                    BookTags.add(Book.Tags.Sports);
                    break;

                case "Steampunk":
                    BookTags.add(Book.Tags.Steampunk);
                    break;

                case "Strategy":
                    BookTags.add(Book.Tags.Strategy);
                    break;

                case "Strong Lead":
                    BookTags.add(Book.Tags.Strong_Lead);
                    break;

                case "Super Heroes":
                    BookTags.add(Book.Tags.Super_Heroes);
                    break;

                case "Supernatural":
                    BookTags.add(Book.Tags.Supernatural);
                    break;

                case "Technological Engineered":
                    BookTags.add(Book.Tags.Technologically_Engineered);
                    break;

                case "Time Travel":
                    BookTags.add(Book.Tags.Time_Travel);
                    break;

                case "Urban Fantasy":
                    BookTags.add(Book.Tags.Urban_Fantasy);
                    break;

                case "Villainous Lead":
                    BookTags.add(Book.Tags.Villainous_Lead);
                    break;

                case "Virtual Reality":
                    BookTags.add(Book.Tags.Virtual_Reality);
                    break;

                case "War and Military":
                    BookTags.add(Book.Tags.War_and_Military);
                    break;

                case "Wuxia":
                    BookTags.add(Book.Tags.Wuxia);
                    break;

                case "Xianxia":
                    BookTags.add(Book.Tags.Xianxia);
                    break;
            }
        }

        SelectedTags.clear();
        return BookTags;
    }

    private Book.BookType CreateBookType() {
        Log.println(Log.INFO, "Hi", "Getting Book Type.");

        Elements BookTypeElements = StoryDoc.getElementsByClass("label label-default label-sm bg-blue-hoki");
        Book.BookType SelectedBookType = Book.BookType.Original;

        for (int i = 0; i < BookTypeElements.size(); i++) {
            if (BookTypeElements.get(i).text().equals("Original") || BookTypeElements.get(i).text().equals("Fan Fiction")) {
                if (BookTypeElements.get(i).text().equals("Original")) {
                    SelectedBookType = Book.BookType.Original;
                } else if (BookTypeElements.get(i).text().equals("Fan Fiction")) {
                    SelectedBookType = Book.BookType.Fanfiction;
                }
            }

            if (BookTypeElements.get(i).text().equals("ONGOING") || BookTypeElements.get(i).text().equals("COMPLETED")) {

            }
        }

        BookTypeElements.clear();
        return SelectedBookType;
    }

    private String CreateCoverURL() {
        return this.StoryDoc.select("img.thumbnail").attr("abs:src");
    }

    private int[] CreateCounts() {
        Log.println(Log.INFO, "Hi", "Getting Counts");

        int[] Counts = new int[3];
        Elements CountElements = StoryDoc.getElementsByClass("bold uppercase font-red-sunglo");

        String Followers = CountElements.get(2).text();
        Followers = Followers.replace(",", "");
        Counts[0] = Integer.parseInt(Followers);

        String Favourites = CountElements.get(3).text();
        Favourites = Favourites.replace(",", "");
        Counts[1] = Integer.parseInt(Favourites);

        String PageCount = CountElements.get(5).text();
        PageCount = PageCount.replace(",", "");
        Counts[2] = Integer.parseInt(PageCount);

        CountElements.clear();
        return Counts;
    }

    public Chapter CreateChapter(int ExternalID, int ChapterID)
    {
        Chapter NewChapter = new Chapter();

        Thread CreateThread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                String URL = "https://www.royalroad.com/fiction/" + ExternalID + "/";
                try
                {
                    Document StoryDoc2 = Jsoup.connect(URL).get();
                    Elements ChapterLinks = StoryDoc2.select("td:not([class]) a");

                    int Index = 0;
                    for(Element Link : ChapterLinks)
                    {
                        if(Index == ChapterID)
                        {
                            NewChapter.ID = Index;
                            NewChapter.Name = Link.text();
                            NewChapter.URL = Link.attr("abs:href");
                            NewChapter.ChapterProgress = 0;
                        }

                        Index++;
                    }
                }
                catch (IOException e)
                {
                    throw new RuntimeException(e);
                }
            }
        });

        CreateThread.start();
        try
        {
            CreateThread.join();
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }

        return NewChapter;
    }

    public int GetInternalID()
    {
        return this.InternalID;
    }
    public int GetExternalID()
    {
        return this.ExternalID;
    }

    public void SetExternalID(int ID)
    {
        this.ExternalID = ID;
    }

    public BookType GetType()
    {
        return this.Type;
    }

    public void SetType(BookType ThisType)
    {
        this.Type = ThisType;
    }
    public String GetTitle()
    {
        return this.Title;
    }

    public void SetTitle(String title)
    {
        this.Title = title;
    }

    public String GetAuthor()
    {
        return this.Author;
    }

    public void SetAuthor(String AuthorName)
    {
        this.Author = AuthorName;
    }

    public String GetDescription()
    {
        return this.Description;
    }

    public void SetDescription(String description)
    {
        this.Description = description;
    }

    public String GetCover()
    {
        return this.CoverURL;
    }

    public void SetCover(String URL)
    {
        this.CoverURL = URL;
    }

    public ArrayList<Genres> GetGenres()
    {
        return this.SelectedGenres;
    }

    public void SetGenres(ArrayList<Genres> selectedGenres)
    {
        this.SelectedGenres = selectedGenres;
    }

    public int GetPageCount()
    {
        return this.PageCount;
    }

    public void SetPageCount(int Count)
    {
        this.PageCount = Count;
    }

    public int GetFollowerCount()
    {
        return this.Followers;
    }

    public void SetFollowerCount(int Count)
    {
        this.Followers = Count;
    }

    public int GetFavouriteCount()
    {
        return this.Favourites;
    }

    public void SetFavouriteCount(int Count)
    {
        this.Favourites = Count;
    }

    public double GetRating()
    {
        return this.Rating;
    }

    public void SetRating(Double rating)
    {
        this.Rating = rating;
    }

    public Date GetCreatedDateTime()
    {
        return this.CreatedDatetime;
    }

    public void SetCreatedDateTime(Date DateTime)
    {
        this.CreatedDatetime = DateTime;
    }

    public Date GetLastUpdatedDateTime()
    {
        return this.LastUpdatedDatetime;
    }

    public void SetLastUpdatedDateTime(Date DateTime)
    {
        this.LastUpdatedDatetime = DateTime;
    }

    public Date GetDownloadedDateTime()
    {
        return this.DownloadedDatetime;
    }

    public void SetDownloadedDateTime(Date DateTime)
    {
        this.DownloadedDatetime = DateTime;
    }
    public ArrayList<Tags> GetTags()
    {
        return this.TagsList;
    }

    public void SetTags(ArrayList<Tags> SelectedTags)
    {
        this.TagsList = SelectedTags;
    }

    public ArrayList<Warnings> GetWarnings()
    {
        return this.ContentWarnings;
    }

    public void SetWarnings(ArrayList<Warnings> warnings)
    {
        this.ContentWarnings = warnings;
    }

    public ArrayList<Chapter> GetAllChapters()
    {
        return this.Chapters;
    }

    public void SetAllChapters(ArrayList<Chapter> AllChapters)
    {
        this.Chapters = AllChapters;
    }
    public Chapter GetChapter(int Index)
    {
        Chapter SelectedChapter = this.Chapters.get(Index);
        return SelectedChapter;
    }

    public void ReplaceChapter(int Index, Chapter SelectedChapter)
    {
        //this.Chapters.get(Index).ContentLines = SelectedChapter.ContentLines;
        this.Chapters.get(Index).ID = SelectedChapter.ID;
        this.Chapters.get(Index).Name = SelectedChapter.Name;
        this.Chapters.get(Index).URL = SelectedChapter.URL;
    }

    public boolean GetHasRead()
    {
        return this.HasRead;
    }

    public void SetHasRead(boolean hasRead)
    {
        this.HasRead = hasRead;
    }

    public int GetLastReadChapter()
    {
        return this.LastReadChapter;
    }

    public void SetLastReadChapter(int lastReadChapter)
    {
        this.LastReadChapter = lastReadChapter;
    }
}