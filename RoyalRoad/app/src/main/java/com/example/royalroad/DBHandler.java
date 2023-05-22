package com.example.royalroad;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.Rating;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import org.jsoup.select.Evaluator;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper
{
    // Database Variables.
    public static final String DB_NAME = "BookLibrary";
    Context context;
    public static final int DB_VERSION = 15;

    // Tables.
    public static final String LIBRARY_TABLE_NAME = "Library";
    public static final String TAGS_TABLE_NAME = "Tags";
    public static final String GENRE_TABLE_NAME = "Genres";
    public static final String WARNINGS_TABLE_NAME = "Warnings";
    public static final String CHAPTERS_TABLE_NAME = "Chapters";
    public static final String CHAPTER_CONTENT_TABLE_NAME = "Chapters_Content";

    // Library Columns.
    public static final String ID = "ID";
    public static final String EXTERNAL_ID = "ExternalID";
    public static final String TYPE = "Type";
    public static final String TITLE = "Title";
    public static final String AUTHOR = "Author";
    public static final String DESCRIPTION = "Description";
    public static final String COVER_URL = "CoverURL";
    public static final String PAGE_COUNT = "PageCount";
    public static final String CHAPTER_COUNT = "ChapterCount";
    public static final String FOLLOWERS = "Followers";
    public static final String FAVOURITES = "Favourites";
    public static final String RATING = "Rating";
    public static final String CREATED_DATE_TIME = "CreatedDateTime";
    public static final String UPDATED_DATE_TIME = "UpdatedDateTime";
    public static final String DOWNLOADED_DATE_TIME = "DownloadedDateTime";
    public static final String HAS_READ = "HasRead";
    public static final String LAST_READ_CHAPTER = "LastReadChapter";

    // Tag Columns
    public static final String BOOK_ID = "BookID";
    public static final String TAG_ID = "TagID";

    // Genre Columns
    public static final String GENRE_ONE = "GenreOne";
    public static final String GENRE_TWO = "GenreTwo";
    public static final String GENRE_THREE = "GenreThree";
    public static final String GENRE_FOUR = "GenreFour";

    // Warnings Columns.
    public static final String WARNING_PROFANITY = "Profanity";
    public static final String WARNING_SEXUAL_CONTENT = "SexualContent";
    public static final String WARNING_GORE = "Gore";
    public static final String WARNING_TRAUMATISING = "TraumatisingContent";

    // Chapter Columns.
    public static final String CHAPTER_ID = "ChapterID";
    public static final String CHAPTER_NAME = "ChapterName";
    public static final String CHAPTER_URL = "ChapterURL";

    //Chapter_Content Columns.
    public static final String CHAPTER_LINE_ID = "LineID";
    public static final String CHAPTER_LINE = "ChapterLine";
    public static final String LINE_STYLE = "LineStyle";

    NotificationManager NotifyManager;

    public static final String DOWNLOAD_CHANNEL = "downloadchannel";
    String GROUP_KEY_STORY_NOTIFICATION = "com.android.example.STORY_NOTIFICATION";

    public DBHandler(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase SQLiteDB)
    {
        // Library Database.
        String CreateLibrary = "CREATE TABLE " + LIBRARY_TABLE_NAME +
                               " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                               EXTERNAL_ID + " INTEGER NOT NULL, " +
                               TYPE + " INTEGER NOT NULL, " +
                               TITLE + " TEXT NOT NULL, " +
                               AUTHOR + " TEXT NOT NULL, " +
                               DESCRIPTION + " TEXT NOT NULL, " +
                               COVER_URL + " TEXT DEFAULT ''," +
                               CHAPTER_COUNT + " INTEGER NOT NULL, " +
                               PAGE_COUNT + " INTEGER NOT NULL, " +
                               FOLLOWERS + " NOT NULL, " +
                               FAVOURITES + " INTEGER NOT NULL, " +
                               RATING + " NUMERIC DEFAULT 0 NOT NULL, " +
                               CREATED_DATE_TIME + " TEXT NOT NULL, " +
                               UPDATED_DATE_TIME + " TEXT, " +
                               DOWNLOADED_DATE_TIME + " TEXT NOT NULL, " +
                               HAS_READ + " INTEGER DEFAULT 0, " +
                               LAST_READ_CHAPTER + " INTEGER DEFAULT 0);";

        SQLiteDB.execSQL(CreateLibrary);

        // Tag Database.
        String CreateTags = "CREATE TABLE " + TAGS_TABLE_NAME +
                            " (" + BOOK_ID + " INTEGER, " +
                            TAG_ID + " INTEGER, " +
                            "FOREIGN KEY (" + BOOK_ID + ") REFERENCES " + LIBRARY_TABLE_NAME +"("+ ID +"));";

        SQLiteDB.execSQL(CreateTags);

        // Genres Database.
        String CreateGenres = "CREATE TABLE " + GENRE_TABLE_NAME +
                              " (" + BOOK_ID + " INTEGER PRIMARY KEY, " +
                              GENRE_ONE + " INTEGER, " +
                              GENRE_TWO + " INTEGER, " +
                              GENRE_THREE + " INTEGER, " +
                              GENRE_FOUR + " INTEGER, " +
                              "FOREIGN KEY (" + BOOK_ID + ") REFERENCES " + LIBRARY_TABLE_NAME +"("+ ID +"));";

        SQLiteDB.execSQL(CreateGenres);

        // Warnings Database.
        String CreateWarnings = "CREATE TABLE " + WARNINGS_TABLE_NAME +
                                " (" + BOOK_ID + " INTEGER PRIMARY KEY, " +
                                WARNING_PROFANITY + " INTEGER DEFAULT 0, " +
                                WARNING_SEXUAL_CONTENT + " INTEGER DEFAULT 0, " +
                                WARNING_GORE + " INTEGER DEFAULT 0, " +
                                WARNING_TRAUMATISING + " INTEGER DEFAULT 0, " +
                                "FOREIGN KEY (" + BOOK_ID + ") REFERENCES " + LIBRARY_TABLE_NAME +"("+ ID +"));";

        SQLiteDB.execSQL(CreateWarnings);

        // Chapters Database.
        String CreateChapters = "CREATE TABLE " + CHAPTERS_TABLE_NAME +
                                " (" + BOOK_ID + " INTEGER, " +
                                CHAPTER_ID + " INTEGER, " +
                                CHAPTER_NAME + " TEXT, " +
                                CHAPTER_URL + " TEXT, " +
                                "FOREIGN KEY (" + BOOK_ID + ") REFERENCES " + LIBRARY_TABLE_NAME +"("+ ID +"));";

        SQLiteDB.execSQL(CreateChapters);

        // Chapter_Content Database. - For Each Paragraph of the Chapter Content.
        String CreateChapterContent = "CREATE TABLE " + CHAPTER_CONTENT_TABLE_NAME +
                                      " (" + CHAPTER_ID + " INTEGER, " +
                                      CHAPTER_LINE_ID + " INTEGER, " +
                                      CHAPTER_LINE + " TEXT, " +
                                      LINE_STYLE + " INTEGER, " +
                                      "FOREIGN KEY (" + CHAPTER_ID + ") REFERENCES " + CHAPTERS_TABLE_NAME +"("+ CHAPTER_ID +"));";

        SQLiteDB.execSQL(CreateChapterContent);
    }

    @Override
    public void onUpgrade(SQLiteDatabase SQLiteDB, int oldVersion, int newVersion)
    {
        Log.println(Log.INFO, "Hi", "Dropping Tables.");

        // Drop Library Table.
        String DropLibraryQuery = "DROP TABLE IF EXISTS " + LIBRARY_TABLE_NAME;
        SQLiteDB.execSQL(DropLibraryQuery);

        // Drop Tags Table.
        String DropTagsQuery = "DROP TABLE IF EXISTS " + TAGS_TABLE_NAME;
        SQLiteDB.execSQL(DropTagsQuery);

        // Drop Genres Table.
        String DropGenresQuery = "DROP TABLE IF EXISTS " + GENRE_TABLE_NAME;
        SQLiteDB.execSQL(DropGenresQuery);

        // Drop Warnings Table.
        String DropWarningsQuery = "DROP TABLE IF EXISTS " + WARNINGS_TABLE_NAME;
        SQLiteDB.execSQL(DropWarningsQuery);

        // Drop Chapters Table.
        String DropChapterQuery = "DROP TABLE IF EXISTS " + CHAPTERS_TABLE_NAME;
        SQLiteDB.execSQL(DropChapterQuery);

        // Drop Chapters Table.
        String DropChapterContentQuery = "DROP TABLE IF EXISTS " + CHAPTER_CONTENT_TABLE_NAME;
        SQLiteDB.execSQL(DropChapterContentQuery);

        // Recreate Tables.
        onCreate(SQLiteDB);
    }

    public void AddBook(Book NewBook)
    {
        Log.println(Log.INFO, "Hi", "Adding New Book to DB");

        NotifyManager = context.getSystemService(NotificationManager.class);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            NotificationChannel DownloadChannel = new NotificationChannel(DOWNLOAD_CHANNEL, "Download Channel", NotificationManager.IMPORTANCE_DEFAULT);
            NotifyManager.createNotificationChannel(DownloadChannel);
        }

        SQLiteDatabase db = this.getWritableDatabase();
        long Result;

        // Add Library Data.
        ContentValues LibraryCV = new ContentValues();

        LibraryCV.put(EXTERNAL_ID, NewBook.ExternalID);
        LibraryCV.put(TYPE, NewBook.Type.ordinal());

        LibraryCV.put(TITLE, NewBook.Title);
        LibraryCV.put(AUTHOR, NewBook.Author);
        LibraryCV.put(DESCRIPTION, NewBook.Description);
        LibraryCV.put(COVER_URL, NewBook.CoverURL);

        LibraryCV.put(CHAPTER_COUNT, NewBook.Chapters.size());
        LibraryCV.put(PAGE_COUNT, NewBook.PageCount);
        LibraryCV.put(FOLLOWERS, NewBook.Followers);
        LibraryCV.put(FAVOURITES, NewBook.Favourites);
        LibraryCV.put(RATING, NewBook.Rating);

        LibraryCV.put(CREATED_DATE_TIME, NewBook.CreatedDatetime.toString());
        LibraryCV.put(UPDATED_DATE_TIME, NewBook.LastUpdatedDatetime.toString());
        LibraryCV.put(DOWNLOADED_DATE_TIME, NewBook.DownloadedDatetime.toString());

        if(NewBook.HasRead)
        {
            LibraryCV.put(HAS_READ, 1);
        }
        else
        {
            LibraryCV.put(HAS_READ, 0);
        }

        LibraryCV.put(LAST_READ_CHAPTER, NewBook.LastReadChapter);

        Result = db.insert(LIBRARY_TABLE_NAME, null, LibraryCV);
        LibraryCV.clear();

        if(Result == -1)
        {
            Toast.makeText(context, "Failed to Add Book", Toast.LENGTH_SHORT).show();
        }
        else {
            // Continue On.
            int BookID = GetBookID(NewBook.ExternalID);

            // Add Tags.
            if (NewBook.TagsList.size() > 0) {
                for (int i = 0; i < NewBook.TagsList.size(); i++) {
                    ContentValues TagsCV = new ContentValues();

                    TagsCV.put(BOOK_ID, BookID);
                    TagsCV.put(TAG_ID, NewBook.TagsList.get(i).ordinal());

                    Result = db.insert(TAGS_TABLE_NAME, null, TagsCV);
                    TagsCV.clear();

                    if (Result == -1) {
                        Toast.makeText(context, "Failed to Add Book Tags.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            // Add Genres.
            if (NewBook.SelectedGenres.size() > 0) {
                ContentValues GenresCV = new ContentValues();

                int GenresSize = NewBook.SelectedGenres.size();

                if(GenresSize > 0)
                {
                    GenresCV.put(BOOK_ID, BookID);
                    GenresCV.put(GENRE_ONE, NewBook.SelectedGenres.get(0).ordinal());

                    if(GenresSize > 1)
                    {
                        GenresCV.put(GENRE_TWO, NewBook.SelectedGenres.get(1).ordinal());

                        if(GenresSize > 2)
                        {
                            GenresCV.put(GENRE_THREE, NewBook.SelectedGenres.get(2).ordinal());

                            if(GenresSize > 3)
                            {
                                GenresCV.put(GENRE_FOUR, NewBook.SelectedGenres.get(3).ordinal());
                            }
                        }
                    }
                }
                if(GenresSize > 1)
                {

                }


                Result = db.insert(GENRE_TABLE_NAME, null, GenresCV);
                GenresCV.clear();

                if (Result == -1) {
                    Toast.makeText(context, "Failed to Add Book Genres.", Toast.LENGTH_SHORT).show();
                }
            }

            // Add Warnings Data if they Exist.
            if (NewBook.ContentWarnings.size() > 0) {
                ContentValues WarningsCV = new ContentValues();

                WarningsCV.put(BOOK_ID, BookID);

                for (int i = 0; i < NewBook.ContentWarnings.size(); i++) {
                    Book.Warnings Warning = NewBook.ContentWarnings.get(i);

                    switch (Warning) {
                        case Profanity:
                            WarningsCV.put(WARNING_PROFANITY, 1);
                            break;

                        case Sexual_Content:
                            WarningsCV.put(WARNING_SEXUAL_CONTENT, 1);
                            break;

                        case Gore:
                            WarningsCV.put(WARNING_GORE, 1);
                            break;

                        case Traumatising_Content:
                            WarningsCV.put(WARNING_TRAUMATISING, 1);
                            break;
                    }
                }

                Result = db.insert(WARNINGS_TABLE_NAME, null, WarningsCV);

                WarningsCV.clear();

                if (Result == -1) {
                    Toast.makeText(context, "Failed to Add Book Warnings", Toast.LENGTH_SHORT).show();
                }
            }

            // Add Chapters.
            for (int i = 0; i < NewBook.Chapters.size(); i++) {
                ContentValues ChapterCV = new ContentValues();

                ChapterCV.put(BOOK_ID, BookID);
                ChapterCV.put(CHAPTER_ID, NewBook.Chapters.get(i).ID);
                ChapterCV.put(CHAPTER_NAME, NewBook.Chapters.get(i).Name);
                ChapterCV.put(CHAPTER_URL, NewBook.Chapters.get(i).URL);

                Result = db.insert(CHAPTERS_TABLE_NAME, null, ChapterCV);

                ChapterCV.clear();

                if (Result == -1) {
                    Toast.makeText(context, "Failed to Add Book Chapter: " + NewBook.Chapters.get(i).ID, Toast.LENGTH_SHORT).show();
                }

                // Add Chapter Lines.
                for (int j = 0; j < NewBook.Chapters.get(i).ContentLines.size(); j++) {
                    ContentValues ChapterLinesCV = new ContentValues();

                    ChapterLinesCV.put(CHAPTER_ID, NewBook.Chapters.get(i).ID);
                    ChapterLinesCV.put(CHAPTER_LINE_ID, NewBook.Chapters.get(i).ContentLines.get(j).ID);
                    ChapterLinesCV.put(CHAPTER_LINE, NewBook.Chapters.get(i).ContentLines.get(j).Line);
                    ChapterLinesCV.put(LINE_STYLE, NewBook.Chapters.get(i).ContentLines.get(j).Style);

                    Result = db.insert(CHAPTER_CONTENT_TABLE_NAME, null, ChapterLinesCV);

                    ChapterLinesCV.clear();

                    if (Result == -1) {
                        Toast.makeText(context, "Failed to Add Book Chapter Line: " + NewBook.Chapters.get(i).ContentLines.get(j).Line + " For Chapter: " + NewBook.Chapters.get(i).ID, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }

        Log.println(Log.INFO, "Hi", "Added Book " + NewBook.Title + " To DB");

        // Send Download Notification to Phone. ( CURRENTLY DOES NOT WORK WITH MULTIPLE UPDATES ).
        Notification DownloadedNotification = new NotificationCompat.Builder(context, DOWNLOAD_CHANNEL)
                .setSmallIcon(R.mipmap.icon)
                .setContentTitle(NewBook.Title)
                .setContentText("Story Downloaded")
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setCategory(Notification.CATEGORY_MESSAGE)
                .setGroup(GROUP_KEY_STORY_NOTIFICATION)
                .build();

        NotifyManager.notify(NewBook.ExternalID, DownloadedNotification);
    }

    public Book GetBook(int InternalID)
    {
        Book NewBook = new Book();

        String Query = "SELECT * FROM " + LIBRARY_TABLE_NAME + " WHERE " + ID + " = " + InternalID;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null)
        {
            cursor = db.rawQuery(Query, null);
        }

        cursor.moveToFirst();

        NewBook.InternalID = Integer.parseInt(cursor.getString(0));
        NewBook.ExternalID = Integer.parseInt(cursor.getString(1));

        int TypeCaster = Integer.parseInt(cursor.getString(2));

        if(TypeCaster == 0)
        {
            NewBook.Type = Book.BookType.Original;
        }
        else if(TypeCaster == 1)
        {
            NewBook.Type = Book.BookType.Fanfiction;
        }

        NewBook.Title = cursor.getString(3);
        NewBook.Author = cursor.getString(4);
        NewBook.Description = cursor.getString(5);

        NewBook.CoverURL = cursor.getString(6);

        int ChapterCount = Integer.parseInt(cursor.getString(7));

        NewBook.Chapters = new ArrayList<>();

        String ChapterQuery = "SELECT * FROM " + CHAPTERS_TABLE_NAME + " WHERE " + BOOK_ID + " = " + NewBook.InternalID;

        Cursor ChapterCursor = db.rawQuery(ChapterQuery, null);
        ChapterCursor.moveToFirst();

        for(int i = 0; i < ChapterCount; i++)
        {
            Book.Chapter NewChapter = new Book.Chapter();

            NewChapter.ID = Integer.parseInt(ChapterCursor.getString(1));
            NewChapter.Name = ChapterCursor.getString(2);
            NewChapter.URL = ChapterCursor.getString(3);

            NewChapter.ContentLines = new ArrayList<>();

            /*String ChapterLineQuery = "SELECT * FROM " + CHAPTER_CONTENT_TABLE_NAME + " WHERE " + CHAPTER_ID + " = " + NewChapter.ID;
            Cursor ChapterLineCursor = db.rawQuery(ChapterLineQuery, null);

            ChapterLineCursor.moveToFirst();
            for(int j = 0; j < ChapterLineCursor.getCount(); j++)
            {
                Book.ChapterLine NewChapterLine = new Book.ChapterLine();

                NewChapterLine.ID = Integer.parseInt(ChapterLineCursor.getString(1));
                NewChapterLine.Line = ChapterLineCursor.getString(2);
                NewChapterLine.Style = Integer.parseInt(ChapterLineCursor.getString(3));

                NewChapter.ContentLines.add(NewChapterLine);

                ChapterLineCursor.moveToNext();
            }
            */


            NewBook.Chapters.add(NewChapter);

            //ChapterLineCursor.close();
            ChapterCursor.moveToNext();
        }

        ChapterCursor.close();

        NewBook.PageCount = Integer.parseInt(cursor.getString(8));
        NewBook.Followers = Integer.parseInt(cursor.getString(9));
        NewBook.Favourites = Integer.parseInt(cursor.getString(10));
        NewBook.Rating = Double.parseDouble(cursor.getString(11));

        //NewBook.CreatedDatetime = cursor.getString(12);
        //NewBook.LastUpdatedDatetime = Double.parseDouble(cursor.getString(11));
        //NewBook.DownloadedDatetime = Double.parseDouble(cursor.getString(11));

        cursor.close();
        return NewBook;
    }

    public int GetLibraryCount()
    {
        String Query = "SELECT * FROM " + LIBRARY_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if(db != null)
        {
            cursor = db.rawQuery(Query, null);
        }

        int LibraryCount = cursor.getCount();
        cursor.close();

        return LibraryCount;
    }

    public boolean GetLibraryBook(long ExternalID)
    {
        String Query = "SELECT * FROM " + LIBRARY_TABLE_NAME + " WHERE " + EXTERNAL_ID + " = " + ExternalID;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null)
        {
            cursor = db.rawQuery(Query, null);
        }

        if(cursor.getCount() >= 1)
        {
            cursor.close();
            return true;
        }
        else
        {
            cursor.close();
            return false;
        }
    }

    public int GetBookID(int ExternalID)
    {
        String Query = "SELECT * FROM " + LIBRARY_TABLE_NAME + " WHERE " + EXTERNAL_ID + " = " + ExternalID;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null)
        {
            cursor = db.rawQuery(Query, null);
        }

        cursor.moveToFirst();

        int CursorNum = cursor.getInt(0);
        cursor.close();

        return CursorNum;
    }
}