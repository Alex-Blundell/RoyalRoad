package com.example.royalroad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.GregorianCalendar;
public class LibraryTasks extends AsyncTask<Void, Void, Book> {
    public long ExternalID;
    public Context context;

    private Book AddBook;

    private Document StoryDoc;
    boolean GetChapterContent;

    public LibraryTasks(Context thisContext, long ID, boolean getChapterContent)
    {
        this.context = thisContext;
        this.ExternalID = ID;
        this.GetChapterContent = getChapterContent;
    }

    @Override
    protected Book doInBackground(Void... voids)
    {
        Log.println(Log.INFO, "Hi", "Adding New Book");

        try
        {
            String URL = "https://www.royalroad.com/fiction/" + ExternalID + "/";
            Connection.Response response = null;

            response = Jsoup.connect(URL).followRedirects(true).execute();

            String RealURL = String.valueOf(response.url());
            this.StoryDoc = Jsoup.connect(RealURL).get();

            DBHandler SQLiteDB = new DBHandler(this.context);

            this.AddBook = new Book();
            AddBook.ExternalID = (int)ExternalID;

            Elements elementsThree = StoryDoc.getElementsByAttribute("href");

            AddBook.Type = GetBookType();

            String[] TitleAuthor = GetTitleAuthor();

            AddBook.Title = TitleAuthor[0];
            AddBook.Author = TitleAuthor[1];

            AddBook.Description = GetDescription();

            AddBook.CoverURL = GetCoverURL();

            AddBook.ContentWarnings = GetWarnings();
            AddBook.SelectedGenres = GetGenres();
            AddBook.TagsList = GetTags();

            int[] Counts = GetCounts();

            AddBook.Followers = Counts[0];
            AddBook.Favourites = Counts[1];
            AddBook.PageCount = Counts[2];

            AddBook.Rating = GetRating();

            // DateTimes.
            AddBook.CreatedDatetime = new GregorianCalendar();
            AddBook.LastUpdatedDatetime = new GregorianCalendar();
            AddBook.DownloadedDatetime = new GregorianCalendar();

            if (GetChapterContent)
            {
                AddBook.Chapters = GetChapters();
            }
            else
            {
                AddBook.Chapters = new ArrayList<>();
            }

            Log.println(Log.INFO, "Hi", "Setting HasRead and LastReadChapter to 0");
            AddBook.HasRead = false;
            AddBook.LastReadChapter = 0;

            //elementNew.clear();
            elementsThree.clear();

            StoryDoc = null;

            Log.println(Log.INFO, "Hi", "New Book: " + AddBook.Title);
            SQLiteDB.close();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        return this.AddBook;
    }

    private double GetRating() {
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

    public String GetDescription() {
        Log.println(Log.INFO, "Hi", "Getting Description");

        Element DescriptionElement = this.StoryDoc.selectFirst(".description");
        return DescriptionElement.text();
    }

    public String[] GetTitleAuthor() {
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

    public ArrayList<Book.Chapter> GetChapters() throws IOException {
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
            //NewChapter.Content = ChapterContent.select(".chapter-content").first().text();

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

    public ArrayList<Book.Warnings> GetWarnings() {
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

    public ArrayList<Book.Genres> GetGenres() {
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

    public ArrayList<Book.Tags> GetTags() {
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

    public Book.BookType GetBookType() {
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

    public String GetCoverURL() {
        return this.StoryDoc.select("img.thumbnail").attr("abs:src");
    }

    public int[] GetCounts() {
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
}