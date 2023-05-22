package com.example.royalroad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.GregorianCalendar;

public class LibraryTasks extends AsyncTask<Void, Void, Void>
{
    public int ExternalID;
    public Context context;

    private Book AddBook;

    public LibraryTasks(Context thisContext, int ID)
    {
        this.context = thisContext;
        this.ExternalID = ID;
    }

    @Override
    protected Void doInBackground(Void... voids)
    {
        Log.println(Log.INFO, "Hi", "Adding New Book");
        try
        {
            DBHandler SQLiteDB = new DBHandler(context);

            Book NewBook = new Book();
            NewBook.ExternalID = ExternalID;

            String URL = "https://www.royalroad.com/fiction/" + ExternalID + "/";

            Connection.Response response = Jsoup.connect(URL).followRedirects(true).execute();

            String RealURL = String.valueOf(response.url());

            Document Doc = Jsoup.connect(RealURL).get();

            Elements elementsOne = Doc.getElementsByClass("font-white");
            Elements elementsTwo = Doc.getElementsByClass("description");
            Elements elementsThree = Doc.getElementsByAttribute("href");
            Elements elementsFour = Doc.getElementsByClass("text-center font-red-sunglo");
            Elements elementsFive = Doc.getElementsByClass("bold uppercase font-red-sunglo");
            Elements elementsSix = Doc.getElementsByClass("thumbnail inline-block");
            Elements elementsSeven = Doc.getElementsByClass("list-item");
            Elements elementsEight = Doc.getElementsByClass("label label-default label-sm bg-blue-hoki");
            Elements elementsNine = Doc.getElementsByClass("label label-default label-sm bg-blue-dark fiction-tag");

            for(int i = 0; i < elementsEight.size(); i++)
            {
                if(elementsEight.get(i).text().equals("Original") || elementsEight.get(i).text().equals("Fan Fiction"))
                {
                    if(elementsEight.get(i).text().equals("Original"))
                    {
                        NewBook.Type = Book.BookType.Original;
                    }
                    else if(elementsEight.get(i).text().equals("Fan Fiction"))
                    {
                        NewBook.Type = Book.BookType.Fanfiction;
                    }
                }

                if(elementsEight.get(i).text().equals("ONGOING") || elementsEight.get(i).text().equals("COMPLETED"))
                {

                }
            }

            NewBook.SelectedGenres = new ArrayList<>();
            NewBook.TagsList = new ArrayList<>();

            // Add Tags and Genres.
            for(int i = 0; i < elementsNine.size(); i++)
            {
                switch (elementsNine.get(i).text())
                {
                    // Genres.
                    case "Action":
                        NewBook.SelectedGenres.add(Book.Genres.Action);
                        break;

                    case "Adventure":
                        NewBook.SelectedGenres.add(Book.Genres.Adventure);
                        break;

                    case "Comedy":
                        NewBook.SelectedGenres.add(Book.Genres.Comedy);
                        break;

                    case "Contemporary":
                        NewBook.SelectedGenres.add(Book.Genres.Contemporary);
                        break;

                    case "Drama":
                        NewBook.SelectedGenres.add(Book.Genres.Drama);
                        break;

                    case "Fantasy":
                        NewBook.SelectedGenres.add(Book.Genres.Fantasy);
                        break;

                    case "Historical":
                        NewBook.SelectedGenres.add(Book.Genres.Historical);
                        break;

                    case "Horror":
                        NewBook.SelectedGenres.add(Book.Genres.Horror);
                        break;

                    case "Mystery":
                        NewBook.SelectedGenres.add(Book.Genres.Mystery);
                        break;

                    case "Psychological":
                        NewBook.SelectedGenres.add(Book.Genres.Psychological);
                        break;

                    case "Romance":
                        NewBook.SelectedGenres.add(Book.Genres.Romance);
                        break;

                    case "Satire":
                        NewBook.SelectedGenres.add(Book.Genres.Satire);
                        break;

                    case "Sci-fi":
                        NewBook.SelectedGenres.add(Book.Genres.Sci_Fi);
                        break;

                    case "Short Story":
                        NewBook.SelectedGenres.add(Book.Genres.Short_Story);
                        break;

                    case "Tragedy":
                        NewBook.SelectedGenres.add(Book.Genres.Tragedy);
                        break;

                    // Tags.
                }
            }

            if(elementsOne.size() > 1)
            {
                NewBook.Title = elementsOne.get(0).text();
                NewBook.Author = elementsOne.get(3).text();
            }

            NewBook.Description = elementsTwo.get(0).text();
            NewBook.ContentWarnings = new ArrayList<>();

            if(elementsFour.size() > 0)
            {
                if(elementsFour.get(0).getElementsByClass("list-inline").get(0).childrenSize() > 1)
                {
                    for(int i = 0; i < elementsFour.get(0).getElementsByClass("list-inline").get(0).childrenSize(); i++)
                    {
                        String Warning = elementsFour.get(0).getElementsByClass("list-inline").get(0).child(i).toString();
                        Warning = Warning.substring(4, Warning.length() - 5);

                        switch (Warning)
                        {
                            case "Gore":
                                NewBook.ContentWarnings.add(Book.Warnings.Gore);
                                break;

                            case "Profanity":
                                NewBook.ContentWarnings.add(Book.Warnings.Profanity);
                                break;

                            case "Traumatising content":
                                NewBook.ContentWarnings.add(Book.Warnings.Traumatising_Content);
                                break;

                            case "Sexual Content":
                                NewBook.ContentWarnings.add(Book.Warnings.Sexual_Content);
                                break;
                        }
                    }
                }
            }

            String Followers = elementsFive.get(2).text();
            Followers = Followers.replace(",", "");

            String Favourites = elementsFive.get(3).text();
            Favourites = Favourites.replace(",", "");

            String PageCount = elementsFive.get(5).text();
            PageCount = PageCount.replace(",", "");

            NewBook.Followers = Integer.parseInt(Followers);
            NewBook.Favourites = Integer.parseInt(Favourites);
            NewBook.PageCount = Integer.parseInt(PageCount);

            if(elementsSix.size() == 1)
            {
                String CoverURL = elementsSix.get(0).attr("src");

                if(CoverURL.contains("?"))
                {
                    String[] Splitter = CoverURL.split("\\?");
                    CoverURL = Splitter[0];
                }

                NewBook.CoverURL = CoverURL;
            }

            for(int i = 0; i < elementsSeven.size(); i++)
            {
                if(elementsSeven.get(i).childrenSize() > 0)
                {
                    String RatingTitle = elementsSeven.get(i).child(0).attr("data-original-title");

                    if(RatingTitle.equals("Overall Score"))
                    {
                        String Rating = elementsSeven.get(i).child(0).attr("data-content");
                        Rating = Rating.substring(0, Rating.length() - 4);

                        NewBook.Rating = Double.parseDouble(Rating);
                    }
                }
            }

            // DateTimes.
            NewBook.CreatedDatetime = new GregorianCalendar();
            NewBook.LastUpdatedDatetime = new GregorianCalendar();
            NewBook.DownloadedDatetime = new GregorianCalendar();

            Elements elementNew = Doc.getElementsByTag("time");

            String[] DateString = elementNew.get(0).attr("datetime").split("-");
            String[] SplitString = DateString[2].split("T");
            String[] TimeString = SplitString[1].split(":");
            String[] SecondSplitString = TimeString[2].split("\\.");

            DateString[2] = SplitString[0];
            TimeString[2] = SecondSplitString[0];

            int Year = Integer.parseInt(DateString[0]);
            int Month = Integer.parseInt(DateString[1]);
            int Date = Integer.parseInt(DateString[2]);

            int Hour = Integer.parseInt(TimeString[0]);
            int Minute = Integer.parseInt(TimeString[1]);
            int Seconds = Integer.parseInt(TimeString[2]);

            NewBook.CreatedDatetime.set(Year, Month, Date, Hour, Minute, Seconds);

            DateString = elementNew.get(elementNew.size() - 1).attr("datetime").split("-");
            SplitString = DateString[2].split("T");
            TimeString = SplitString[1].split(":");
            SecondSplitString = TimeString[2].split("\\.");

            DateString[2] = SplitString[0];
            TimeString[2] = SecondSplitString[0];

            Year = Integer.parseInt(DateString[0]);
            Month = Integer.parseInt(DateString[1]);
            Date = Integer.parseInt(DateString[2]);

            Hour = Integer.parseInt(TimeString[0]);
            Minute = Integer.parseInt(TimeString[1]);
            Seconds = Integer.parseInt(TimeString[2]);

            NewBook.LastUpdatedDatetime.set(Year, Month, Date, Hour, Minute, Seconds);

            NewBook.Chapters = new ArrayList<>();
            int ChapterIndex = 0;

            for (int i = 0; i < elementsThree.size(); i++)
            {
                String ElementString = elementsThree.get(i).toString();

                if(ElementString.contains("fiction") && !ElementString.contains("data")
                        && !ElementString.contains("nofollow") && !ElementString.contains("nav-link")
                        && !ElementString.contains("label-sm") && !ElementString.contains("button-icon")
                        && !ElementString.contains("canonical") && !ElementString.contains("btn-lg")
                        && !ElementString.contains("returnurl"))
                {
                    String[] StringArray = ElementString.split(">");

                    String SeperateURL = StringArray[0];
                    SeperateURL = SeperateURL.substring(9, SeperateURL.length() - 1);

                    String ChapterName = elementsThree.get(i).text();
                    String ChapterURL = "https://www.royalroad.com" + SeperateURL;

                    Book.Chapter NewChapter = new Book.Chapter();
                    NewChapter.ContentLines = new ArrayList<>();

                    NewChapter.ID = ChapterIndex;
                    NewChapter.Name = ChapterName;
                    NewChapter.URL = ChapterURL;

                    // Get Chapter Content.
                    Document ChapterDoc = Jsoup.connect(ChapterURL).get();
                    Elements element = ChapterDoc.getElementsByTag("p");

                    int LineID = 0;

                    for (int j = 0; j < element.size(); j++)
                    {
                        if(!element.get(j).text().contains("Bio") && !element.get(j).text().contains("Royal Road"))
                        {
                            Book.ChapterLine NewLine = new Book.ChapterLine();

                            if(j > 0)
                            {
                                if(element.get(j - 1).text().isEmpty() && element.get(j).text().isEmpty())
                                {
                                    NewLine.ID = LineID;

                                    NewLine.Line = "SPLITTER";
                                    NewLine.Style = 0;

                                    NewChapter.ContentLines.add(NewLine);

                                    LineID++;
                                }
                            }

                            NewLine.ID = LineID;

                            NewLine.Line = element.get(j).text();
                            NewLine.Style = 0;

                            LineID++;

                            NewChapter.ContentLines.add(NewLine);
                            NewLine = null;
                        }
                    }

                    element.clear();
                    ChapterDoc = null;

                    NewBook.Chapters.add(NewChapter);
                    NewChapter = null;

                    ChapterIndex++;
                }
            }

            NewBook.HasRead = false;
            NewBook.LastReadChapter = 0;

            this.AddBook = NewBook;

            elementNew.clear();
            elementsOne.clear();
            elementsTwo.clear();
            elementsThree.clear();
            elementsFour.clear();
            elementsFive.clear();
            elementsSix.clear();
            elementsSeven.clear();
            elementsEight.clear();
            elementsNine.clear();

            Doc = null;

            Log.println(Log.INFO, "Hi", "New Book: " + NewBook.Title);
            SQLiteDB.close();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
        DBHandler SQLiteDB = new DBHandler(context);

        SQLiteDB.AddBook(AddBook);

        SQLiteDB.close();
    }
}
