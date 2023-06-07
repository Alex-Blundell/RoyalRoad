package com.example.royalroad;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.annotations.concurrent.Background;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.common.returnsreceiver.qual.This;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.GregorianCalendar;

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

    public static class Chapter implements Serializable
    {
        public int ID;
        public String Name;
        public String URL;
        public ArrayList<Paragraph> Content;
    }

    public static class Paragraph
    {
        public int ParagraphID;
        public String Content;
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
    public GregorianCalendar CreatedDatetime;
    public GregorianCalendar LastUpdatedDatetime;
    public GregorianCalendar DownloadedDatetime;
    public ArrayList<Chapter> Chapters;
    public ArrayList<Tags> TagsList;
    public ArrayList<Warnings> ContentWarnings;
    public boolean HasRead;
    public int LastReadChapter;

    public boolean IsCompleted = false;

    private Document StoryDoc;

    private String[] RemoveTags = new String[] {
            "</p>",
            "<div>",
            "</div>",
            "<div class=\"chapter-inner chapter-content\">",
            " &nbsp;",
            "</em><em>",
            "</strong><strong>",
            "<span style=\"font-weight: 400\">",
            "</span>",
            "&nbsp;",
            "<hr>",
            "<p style=\"text-align: center\">",
            " <div style=\"max-width: 100%; overflow:auto\">",
            "<div style=\"max-width: 100%; overflow:auto\">",
            "<table>",
            "<tbody>",
            "<tr>",
            "<td width=\"623\">",
            "</td>",
            "</tr>",
            "</tbody>",
            "</table>",
            "<br>",
            "<p style=\"margin-bottom: 1em\">",
            "<p style=\"margin-bottom: 2em\">",
            "<span style=\"font-size: 1.3em\">",
            "<span style=\"color: rgba(255, 255, 255, 1)\">",
            "<td style=\"width: 0; height: 68px\">",
            "</sup>",
            "<sup>",
            "<td style=\"text-align: center\">",
            "<td style=\"text-align: left\">",
            "<span style=\"text-decoration: underline;\">",
            "</ul>",
            "<li style=\"font-weight: 400\">",
            "<span style=\"color: rgba(224, 62, 45, 1)\">",
            "</li>",
            "<ul>",
            "<p style=\"font-style: normal; font-variant-caps: normal; font-weight: 400; letter-spacing: normal; text-indent: 0; text-transform: none; word-spacing: 0; text-decoration: none; margin: 0 0 12px; font-stretch: normal; font-family: Helvetica\">",
            "<span style=\"font-family: &quot;Open Sans&quot;, sans-serif; font-size: 1.1em\">"
    };

    private String[] ReplaceTags = new String[] {
            "<p style=\"text-align: center\">",
    };


    public Book()
    {

    }

    @Background
    public Book CreateBook(Context context, long ExternalID, boolean GetChapterContent, boolean AddToDB) throws InterruptedException {
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
                    NewBook.SetCreatedDateTime(new GregorianCalendar());
                    NewBook.SetLastUpdatedDateTime(new GregorianCalendar());
                    NewBook.SetDownloadedDateTime(new GregorianCalendar());

                    if (GetChapterContent)
                    {
                        NewBook.SetAllChapters(CreateChapters());
                    }
                    else
                    {
                        NewBook.SetAllChapters(new ArrayList<>());
                    }

                    Log.println(Log.INFO, "Hi", "Setting HasRead and LastReadChapter to 0");

                    NewBook.SetHasRead(false);
                    NewBook.SetLastReadChapter(0);

                    StoryDoc = null;
                    NewBook.IsCompleted = true;

                    Log.println(Log.INFO, "Hi", "New Book: " + NewBook.Title);

                    if(AddToDB)
                    {
                        new AddToDBTask(context, NewBook).execute();
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

    private double CreateRating() {
        Log.println(Log.INFO, "Hi", "Getting Rating");
        Elements RatingElements = StoryDoc.getElementsByClass("list-item");
        double Rating = 0.0;

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

    private String CreateDescription() {
        Log.println(Log.INFO, "Hi", "Getting Description");

        Element DescriptionElement = this.StoryDoc.selectFirst(".description");
        return DescriptionElement.text();
    }

    private String[] CreateTitleAuthor() {
        Log.println(Log.INFO, "Hi", "Getting Title and Author");

        String[] TitleAuthor = new String[2];

        Element Title = this.StoryDoc.selectFirst("h1.font-white");
        Element Author = this.StoryDoc.selectFirst("h4.font-white");

        String AuthorTXT = Author.text();
        AuthorTXT = AuthorTXT.substring(3);

        TitleAuthor[0] = Title.text();
        TitleAuthor[1] = AuthorTXT;

        return TitleAuthor;
    }

    private ArrayList<Book.Chapter> CreateChapters() throws IOException {
        Log.println(Log.INFO, "Hi", "Getting Chapters");

        ArrayList<Book.Chapter> AllChapters = new ArrayList<>();
        Elements ChapterLinks = StoryDoc.select("td:not([class]) a");

        int ChapterIndex = 0;

        for (Element Link : ChapterLinks) {
            Book.Chapter NewChapter = new Book.Chapter();

            NewChapter.ID = ChapterIndex;
            NewChapter.Name = Link.text();
            NewChapter.URL = Link.attr("abs:href");

            Document ChapterContent = Jsoup.connect(Link.attr("abs:href")).get();

            String RawChapter = ChapterContent.select(".chapter-content").first().toString();
            NewChapter.Content = CleanChapter(RawChapter);

            int ChapterID = ChapterIndex + 1;

            Log.println(Log.INFO, "Hi", "Got Chapter " + ChapterID + " / " + ChapterLinks.size());
            ChapterContent = null;

            AllChapters.add(NewChapter);
            ChapterIndex++;
        }

        ChapterLinks.clear();

        Log.println(Log.INFO, "Hi", "Finished Getting Chapters");
        return AllChapters;
    }

    private ArrayList<Paragraph> CleanChapter(String RawChapter)
    {
        ArrayList<Paragraph> Content = new ArrayList<>();
        String[] Paragraphs = RawChapter.split("<p>");

        for(int i = 0; i < Paragraphs.length; i++)
        {
            Paragraph ThisParagraph = new Paragraph();

            ThisParagraph.ParagraphID = i;
            String AlteredParagraph = Paragraphs[i];

            for(int j = 0; j < RemoveTags.length; j++)
            {
                AlteredParagraph = AlteredParagraph.replace(RemoveTags[j], "");
            }

            //String[] BoldLines = AlteredParagraph.split("<strong>");
            //String[] ItalicLines = AlteredParagraph.split("<em>");

            AlteredParagraph = AlteredParagraph.replace("<em>", "");
            AlteredParagraph = AlteredParagraph.replace("</em>", "");
            AlteredParagraph = AlteredParagraph.replace("<strong>", "");
            AlteredParagraph = AlteredParagraph.replace("</strong>", "");

            // No Bold or Italic Lines.
            //if(BoldLines.length == 0 && ItalicLines.length == 0)
            //{

            //}

            for(int j = AlteredParagraph.length(); j > 0; j--)
            {
                if(AlteredParagraph.charAt(j - 1) == ' ')
                {
                    AlteredParagraph = AlteredParagraph.substring(0, AlteredParagraph.length() - 1);
                }
                else
                {
                    break;
                }
            }

            ThisParagraph.Content = AlteredParagraph;

            Content.add(ThisParagraph);
        }

        // Clean Chapter Here.
        // Remove HTML Tags.
        // Create Seperate Paragraphs.
        // Check Which Parts of the Paragraph is in Bold, Italics and Italic Bold.
        // Seperate These and create ID's For them
        // Set Style to Default, Bold, Italics or Italic Bold.
        // Remove All Other Tags.

        return Content;
    }

    private ArrayList<Book.Warnings> CreateWarnings() {
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

    public GregorianCalendar GetCreatedDateTime()
    {
        return this.CreatedDatetime;
    }

    public void SetCreatedDateTime(GregorianCalendar DateTime)
    {
        this.CreatedDatetime = DateTime;
    }

    public GregorianCalendar GetLastUpdatedDateTime()
    {
        return this.LastUpdatedDatetime;
    }

    public void SetLastUpdatedDateTime(GregorianCalendar DateTime)
    {
        this.LastUpdatedDatetime = DateTime;
    }

    public GregorianCalendar GetDownloadedDateTime()
    {
        return this.DownloadedDatetime;
    }

    public void SetDownloadedDateTime(GregorianCalendar DateTime)
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