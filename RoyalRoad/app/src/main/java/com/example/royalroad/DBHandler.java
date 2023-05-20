package com.example.royalroad;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHandler extends SQLiteOpenHelper
{
    // Database Variables.
    public static final String DB_NAME = "BookLibrary";
    public static final int DB_VERSION = 1;

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

    //Chapter_Content Columns.
    public static final String CHAPTER_LINE = "ChapterLine";
    public static final String LINE_STYLE = "LineStyle";

    public DBHandler(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
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
                                WARNING_PROFANITY + " INTEGER, " +
                                WARNING_SEXUAL_CONTENT + " INTEGER, " +
                                WARNING_GORE + " INTEGER, " +
                                WARNING_TRAUMATISING + " INTEGER, " +
                                "FOREIGN KEY (" + BOOK_ID + ") REFERENCES " + LIBRARY_TABLE_NAME +"("+ ID +"));";

        SQLiteDB.execSQL(CreateWarnings);

        // Chapters Database.
        String CreateChapters = "CREATE TABLE " + CHAPTERS_TABLE_NAME +
                                " (" + BOOK_ID + " INTEGER, " +
                                CHAPTER_ID + " INTEGER, " +
                                CHAPTER_NAME + " TEXT, " +
                                "FOREIGN KEY (" + BOOK_ID + ") REFERENCES " + LIBRARY_TABLE_NAME +"("+ ID +"));";

        SQLiteDB.execSQL(CreateChapters);

        // Chapter_Content Database. - For Each Paragraph of the Chapter Content.
        String CreateChapterContent = "CREATE TABLE " + CHAPTER_CONTENT_TABLE_NAME +
                                      " (" + CHAPTER_ID + " INTEGER, " +
                                      CHAPTER_LINE + " TEXT, " +
                                      LINE_STYLE + " INTEGER, " +
                                      "FOREIGN KEY (" + CHAPTER_ID + ") REFERENCES " + CHAPTERS_TABLE_NAME +"("+ CHAPTER_ID +"));";

        SQLiteDB.execSQL(CreateChapterContent);
    }

    @Override
    public void onUpgrade(SQLiteDatabase SQLiteDB, int oldVersion, int newVersion)
    {
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
}