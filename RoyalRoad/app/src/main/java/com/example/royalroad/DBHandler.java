package com.example.royalroad;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.Rating;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.Current;
import org.checkerframework.common.returnsreceiver.qual.This;
import org.jsoup.select.Evaluator;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;

public class DBHandler extends SQLiteOpenHelper
{
    // Database Variables.
    public static final String DB_NAME = "BookLibrary";
    Context context;
    public static final int DB_VERSION = 37;

    // Tables.
    public static final String LIBRARY_TABLE_NAME = "Library";
    public static final String TAGS_TABLE_NAME = "Tags";
    public static final String GENRE_TABLE_NAME = "Genres";
    public static final String WARNINGS_TABLE_NAME = "Warnings";
    public static final String CHAPTERS_TABLE_NAME = "Chapters";
    public static final String CHAPTER_PARAGRAPHS_TABLE_NAME = "Chapters_Paragraphs";
    public static final String HISTORY_LIBRARY_TABLE_NAME = "History_Library";
    public static final String DOWNLOADING_LIST_TABLE_NAME = "Downloading_List";

    // Library Columns.
    public static final String ID = "ID";
    public static final String EXTERNAL_ID = "ExternalID";
    public static final String USER_ID = "UserID";
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
    public static final String PROFILE_ID = "ProfileID";
    public static final String HAS_UNREAD_UPDATE = "HasUnreadUpdate";

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
    public static final String CHAPTER_PROGRESS = "ChapterProgress";

    // Chapter_Paragraphs Columns.
    public static final String CHAPTER_PARAGRAPH_ID = "ParagraphID";
    public static final String CHAPTER_PARAGRAPH_CONTENT = "ParagraphContent";

    // History Library Columns.
    public static final String HAS_DOWNLOADED = "HasDownloaded";
    public static final String LAST_READ_DATETIME = "LastReadDatetime";

    NotificationManager NotifyManager;

    public static final String DOWNLOAD_CHANNEL = "downloadchannel";
    String GROUP_KEY_STORY_NOTIFICATION = "com.android.example.STORY_NOTIFICATION";

    public DBHandler(@Nullable Context context)
    {
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
                USER_ID + " INTEGER NOT NULL, " +
                TYPE + " INTEGER NOT NULL, " +
                TITLE + " TEXT NOT NULL, " +
                AUTHOR + " TEXT NOT NULL, " +
                DESCRIPTION + " TEXT NOT NULL, " +
                COVER_URL + " TEXT DEFAULT ''," +
                CHAPTER_COUNT + " INTEGER NOT NULL, " +
                PAGE_COUNT + " INTEGER NOT NULL, " +
                FOLLOWERS + " INTEGER NOT NULL, " +
                FAVOURITES + " INTEGER NOT NULL, " +
                RATING + " NUMERIC DEFAULT 0 NOT NULL, " +
                CREATED_DATE_TIME + " TEXT NOT NULL, " +
                UPDATED_DATE_TIME + " TEXT, " +
                DOWNLOADED_DATE_TIME + " TEXT NOT NULL, " +
                HAS_READ + " INTEGER DEFAULT 0, " +
                LAST_READ_CHAPTER + " INTEGER DEFAULT 0, " +
                PROFILE_ID + " INTEGER DEFAULT 0, " +
                HAS_UNREAD_UPDATE + " INTEGER DEFAULT 0);";

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
                CHAPTER_PROGRESS + " INTEGER DEFAULT 0, " +
                "FOREIGN KEY (" + BOOK_ID + ") REFERENCES " + LIBRARY_TABLE_NAME +"("+ ID +"));";

        SQLiteDB.execSQL(CreateChapters);

        // Chapter_Paragraphs Database. - For Each Paragraph of the Chapter Content.
        String CreateChapterParagraph = "CREATE TABLE " + CHAPTER_PARAGRAPHS_TABLE_NAME +
                " (" + BOOK_ID + " INTEGER, " +
                CHAPTER_ID + " INTEGER, " +
                CHAPTER_PARAGRAPH_ID + " INTEGER, " +
                CHAPTER_PARAGRAPH_CONTENT + " TEXT, " +
                "FOREIGN KEY (" + BOOK_ID + ") REFERENCES " + LIBRARY_TABLE_NAME +"("+ ID +"), " +
                "FOREIGN KEY (" + CHAPTER_ID + ") REFERENCES " + CHAPTERS_TABLE_NAME +"("+ CHAPTER_ID +"));";

        SQLiteDB.execSQL(CreateChapterParagraph);

        // History_Library Database.
        String CreateHistoryLibrary = "CREATE TABLE " + HISTORY_LIBRARY_TABLE_NAME +
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
                LAST_READ_DATETIME + " TEXT NOT NULL, " +
                UPDATED_DATE_TIME + " TEXT, " +
                HAS_READ + " INTEGER DEFAULT 0, " +
                HAS_DOWNLOADED + " INTEGER DEFAULT 0, " +
                LAST_READ_CHAPTER + " INTEGER DEFAULT 0);";

        SQLiteDB.execSQL(CreateHistoryLibrary);
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

        // Drop Chapters_Paragraphs Table.
        String DropChapterParagraphQuery = "DROP TABLE IF EXISTS " + CHAPTER_PARAGRAPHS_TABLE_NAME;
        SQLiteDB.execSQL(DropChapterParagraphQuery);

        // Drop History_Library Table.
        String DropHistoryLibraryQuery = "DROP TABLE IF EXISTS " + HISTORY_LIBRARY_TABLE_NAME;
        SQLiteDB.execSQL(DropHistoryLibraryQuery);

        // Recreate Tables.
        onCreate(SQLiteDB);
    }

    public void AddBook(Book NewBook)
    {
        Log.println(Log.INFO, "Hi", "Adding " + NewBook.Title + " To DB.");

        NotifyManager = this.context.getSystemService(NotificationManager.class);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            NotificationChannel DownloadChannel = new NotificationChannel(DOWNLOAD_CHANNEL, "Download Channel", NotificationManager.IMPORTANCE_DEFAULT);
            NotifyManager.createNotificationChannel(DownloadChannel);
        }

        SQLiteDatabase db = DBHandler.this.getWritableDatabase();
        long Result;

        // Add Library Data.
        db.beginTransaction();
        try
        {
            SharedPreferences Pref = context.getSharedPreferences("Settings", MODE_PRIVATE);

            ContentValues LibraryCV = new ContentValues();

            LibraryCV.put(EXTERNAL_ID, NewBook.ExternalID);
            LibraryCV.put(USER_ID, Pref.getInt("UserID", 1));
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
            LibraryCV.put(PROFILE_ID, NewBook.ProfileID);

            if(NewBook.HasUnreadUpdate)
            {
                LibraryCV.put(HAS_UNREAD_UPDATE, 1);
            }
            else
            {
                LibraryCV.put(HAS_UNREAD_UPDATE, 0);
            }

            Result = db.insertOrThrow(LIBRARY_TABLE_NAME, null, LibraryCV);
            LibraryCV.clear();

            db.setTransactionSuccessful();
        }
        finally
        {
            db.endTransaction();
        }
        // Continue On.
        int BookID = GetBookID(NewBook.ExternalID);

        // Add Tags.
        if (NewBook.TagsList.size() > 0)
        {
            for (int i = 0; i < NewBook.TagsList.size(); i++)
            {
                db.beginTransaction();
                try
                {
                    ContentValues TagsCV = new ContentValues();

                    TagsCV.put(BOOK_ID, BookID);
                    TagsCV.put(TAG_ID, NewBook.TagsList.get(i).ordinal());

                    Result = db.insertOrThrow(TAGS_TABLE_NAME, null, TagsCV);
                    TagsCV.clear();

                    if (Result == -1)
                    {
                        Toast.makeText(context, "Failed to Add Book Tags.", Toast.LENGTH_SHORT).show();
                    }

                    db.setTransactionSuccessful();
                }
                finally
                {
                    db.endTransaction();
                }
            }
        }

        // Add Genres.
        if (NewBook.SelectedGenres.size() > 0)
        {
            db.beginTransaction();
            try {
                ContentValues GenresCV = new ContentValues();

                int GenresSize = NewBook.SelectedGenres.size();

                if (GenresSize > 0) {
                    GenresCV.put(BOOK_ID, BookID);
                    GenresCV.put(GENRE_ONE, NewBook.SelectedGenres.get(0).ordinal());

                    if (GenresSize > 1) {
                        GenresCV.put(GENRE_TWO, NewBook.SelectedGenres.get(1).ordinal());

                        if (GenresSize > 2) {
                            GenresCV.put(GENRE_THREE, NewBook.SelectedGenres.get(2).ordinal());

                            if (GenresSize > 3) {
                                GenresCV.put(GENRE_FOUR, NewBook.SelectedGenres.get(3).ordinal());
                            }
                        }
                    }

                    Result = db.insertOrThrow(GENRE_TABLE_NAME, null, GenresCV);
                    GenresCV.clear();

                    if (Result == -1)
                    {
                        Toast.makeText(context, "Failed to Add Book Genres.", Toast.LENGTH_SHORT).show();
                    }
                    db.setTransactionSuccessful();
                }
            }
            finally
            {
                db.endTransaction();
            }
        }

        // Add Warnings Data if they Exist.
        if (NewBook.ContentWarnings.size() > 0)
        {
            db.beginTransaction();
            try
            {
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

                Result = db.insertOrThrow(WARNINGS_TABLE_NAME, null, WarningsCV);

                WarningsCV.clear();

                if (Result == -1)
                {
                    Toast.makeText(context, "Failed to Add Book Warnings", Toast.LENGTH_SHORT).show();
                }

                db.setTransactionSuccessful();
            }
            finally
            {
                db.endTransaction();
            }
        }

        // Add Chapters.
        db.beginTransaction();
        try
        {
            for (Book.Chapter ThisChapter : NewBook.Chapters)
            {
                int CurrentID = ThisChapter.ID + 1;

                Log.println(Log.INFO, "Hi", "Adding Chapter " + CurrentID + " / " + NewBook.Chapters.size() + " To DB.");

                ContentValues ChapterCV = new ContentValues();

                ChapterCV.put(BOOK_ID, BookID);
                ChapterCV.put(CHAPTER_ID, ThisChapter.ID);
                ChapterCV.put(CHAPTER_NAME, ThisChapter.Name);
                ChapterCV.put(CHAPTER_URL, ThisChapter.URL);
                ChapterCV.put(CHAPTER_PROGRESS, ThisChapter.ChapterProgress);

                Result = db.insertOrThrow(CHAPTERS_TABLE_NAME, null, ChapterCV);

                ChapterCV.clear();

                db.beginTransaction();
                try
                {
                    for (Book.Paragraph ThisParagraph : ThisChapter.Content)
                    {
                        ContentValues ParagraphCV = new ContentValues();

                        ParagraphCV.put(BOOK_ID, BookID);
                        ParagraphCV.put(CHAPTER_ID, ThisChapter.ID);
                        ParagraphCV.put(CHAPTER_PARAGRAPH_ID, ThisParagraph.ParagraphID);
                        ParagraphCV.put(CHAPTER_PARAGRAPH_CONTENT, ThisParagraph.Content);

                        Result = db.insertOrThrow(CHAPTER_PARAGRAPHS_TABLE_NAME, null, ParagraphCV);

                        ParagraphCV.clear();
                    }

                    db.setTransactionSuccessful();
                }
                finally
                {
                    db.endTransaction();
                }

                if (Result == -1)
                {
                    Toast.makeText(context, "Failed to Add Book Chapter: " + ThisChapter.ID, Toast.LENGTH_SHORT).show();
                }
            }

            db.setTransactionSuccessful();
        }
        finally
        {
            db.endTransaction();

            Log.println(Log.INFO, "Hi", "Added Book " + NewBook.Title + " To DB");

            Intent NotificationIntent = new Intent(context, ReadActivity.class);

            for(Book.Chapter ThisChapter : NewBook.Chapters)
            {
                ThisChapter.Content = new ArrayList<>();
            }

            NotificationIntent.putExtra("Book", NewBook);
            NotificationIntent.putExtra("HasDownloaded", true);
            NotificationIntent.putExtra("FromNotification", true);

            TaskStackBuilder NotificationTaskStack = TaskStackBuilder.create(context);
            NotificationTaskStack.addNextIntentWithParentStack(NotificationIntent);

            PendingIntent ReadPendingIntent = NotificationTaskStack.getPendingIntent(0,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            Notification DownloadedNotification = new NotificationCompat.Builder(context, DOWNLOAD_CHANNEL)
                    .setSmallIcon(R.mipmap.icon)
                    .setContentTitle(NewBook.Title)
                    .setContentText("Story Downloaded")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setCategory(Notification.CATEGORY_MESSAGE)
                    .setContentIntent(ReadPendingIntent)
                    .setWhen(System.currentTimeMillis())
                    .setGroup(GROUP_KEY_STORY_NOTIFICATION)
                    .setGroupSummary(true)
                    .build();

            NotifyManager.notify(NewBook.ExternalID, DownloadedNotification);
        }
    }

    public void AddToHistory(Book AddBook)
    {

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

            db.beginTransaction();
            try
            {
                cursor.moveToFirst();
                SharedPreferences Pref = context.getSharedPreferences("Settings", MODE_PRIVATE);

                int CurrentUserID = Pref.getInt("UserID", 1);
                int BookUserID = NewBook.ExternalID = Integer.parseInt(cursor.getString(2));

                if(CurrentUserID == BookUserID)
                {
                    NewBook.InternalID = Integer.parseInt(cursor.getString(0));
                    NewBook.ExternalID = Integer.parseInt(cursor.getString(1));

                    int TypeCaster = Integer.parseInt(cursor.getString(3));

                    if(TypeCaster == 0)
                    {
                        NewBook.Type = Book.BookType.Original;
                    }
                    else if(TypeCaster == 1)
                    {
                        NewBook.Type = Book.BookType.Fanfiction;
                    }

                    NewBook.Title = cursor.getString(4);
                    NewBook.Author = cursor.getString(5);
                    NewBook.Description = cursor.getString(6);

                    NewBook.CoverURL = cursor.getString(7);

                    int ChapterCount = Integer.parseInt(cursor.getString(8));

                    NewBook.Chapters = new ArrayList<>();

                    db.beginTransaction();
                    try
                    {
                        String ChapterQuery = "SELECT * FROM " + CHAPTERS_TABLE_NAME + " WHERE " + BOOK_ID + " = " + NewBook.InternalID;

                        Cursor ChapterCursor = db.rawQuery(ChapterQuery, null);
                        ChapterCursor.moveToFirst();

                        for(int i = 0; i < ChapterCount; i++)
                        {
                            Book.Chapter NewChapter = new Book.Chapter();

                            NewChapter.ID = Integer.parseInt(ChapterCursor.getString(1));
                            NewChapter.Name = ChapterCursor.getString(2);
                            NewChapter.URL = ChapterCursor.getString(3);
                            NewChapter.ChapterProgress = Integer.parseInt(ChapterCursor.getString(4));

                            NewBook.Chapters.add(NewChapter);

                            ChapterCursor.moveToNext();
                        }

                        ChapterCursor.close();

                        db.setTransactionSuccessful();
                    }
                    finally
                    {
                        db.endTransaction();
                    }

                    NewBook.PageCount = Integer.parseInt(cursor.getString(9));
                    NewBook.Followers = Integer.parseInt(cursor.getString(10));
                    NewBook.Favourites = Integer.parseInt(cursor.getString(11));
                    NewBook.Rating = Double.parseDouble(cursor.getString(12));

                    //NewBook.CreatedDatetime = cursor.getString(13);
                    //NewBook.LastUpdatedDatetime = Double.parseDouble(cursor.getString(14));
                    //NewBook.DownloadedDatetime = Double.parseDouble(cursor.getString(15));

                    int HasReadCaster = Integer.parseInt(cursor.getString(16));

                    if(HasReadCaster == 0)
                    {
                        NewBook.SetHasRead(false);
                    }
                    else if(HasReadCaster == 1)
                    {
                        NewBook.SetHasRead(true);
                    }

                    NewBook.SetLastReadChapter(Integer.parseInt(cursor.getString(17)));
                    NewBook.ProfileID = Integer.parseInt(cursor.getString(18));

                    int HasUnreadUpdate = Integer.parseInt(cursor.getString(19));

                    if(HasUnreadUpdate == 0)
                    {
                        NewBook.HasUnreadUpdate = false;
                    }
                    else if(HasUnreadUpdate == 1)
                    {
                        NewBook.HasUnreadUpdate = true;
                    }
                }
                else
                {
                    NewBook = null;
                }

                cursor.close();
                db.setTransactionSuccessful();
            }
            finally
            {
                db.endTransaction();
            }
        }

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

        boolean Result = false;

        if(cursor.getCount() >= 1)
        {
            Result = true;
        }
        else
        {
            Result = false;
        }

        cursor.close();
        return Result;
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

    public int[] GetExternalIDs()
    {
        int BookCount = GetLibraryCount();

        SQLiteDatabase db = this.getReadableDatabase();
        int[] ExternalIDs = new int[BookCount];

        String Query = "SELECT " + EXTERNAL_ID + " FROM " + LIBRARY_TABLE_NAME;

        Cursor cursor = null;
        if(db != null)
        {
            cursor = db.rawQuery(Query, null);
        }

        cursor.moveToFirst();
        for(int i = 0; i < BookCount; i++)
        {
            ExternalIDs[i] = cursor.getInt(0);
            cursor.moveToNext();
        }

        cursor.close();
        return ExternalIDs;
    }

    public int GetChapterCount(long ID)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String Query = "SELECT " + CHAPTER_COUNT + " FROM " + LIBRARY_TABLE_NAME + " WHERE " + EXTERNAL_ID + " = " + ID;
        Cursor cursor = null;

        if(db != null)
        {
            cursor = db.rawQuery(Query, null);
        }

        cursor.moveToFirst();
        int ChapterCount = cursor.getInt(0);

        cursor.close();
        return ChapterCount;
    }

    public String GetBookTitle(long ExternalID)
    {
        String Query = "SELECT " + TITLE + " FROM " + LIBRARY_TABLE_NAME + " WHERE " + EXTERNAL_ID + " = " + ExternalID;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null)
        {
            cursor = db.rawQuery(Query, null);
        }

        cursor.moveToFirst();

        String Title = cursor.getString(0);
        cursor.close();

        return Title;
    }

    public Book.Chapter GetChapter(int BookID, int ChapterID)
    {
        Book.Chapter ChapterGot = new Book.Chapter();
        ChapterGot.Content = new ArrayList<Book.Paragraph>();

        // Getting Chapter
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null)
        {
            db.beginTransaction();
            try
            {
                String ChapterQuery = "SELECT * FROM " + CHAPTERS_TABLE_NAME + " WHERE " + BOOK_ID + " = " + BookID;

                cursor = db.rawQuery(ChapterQuery, null);

                db.setTransactionSuccessful();
            }
            finally
            {
                db.endTransaction();
            }
        }

        cursor.moveToPosition(ChapterID);

        ChapterGot.ID = ChapterID;
        ChapterGot.Name = cursor.getString(2);
        ChapterGot.URL = cursor.getString(3);
        ChapterGot.ChapterProgress = cursor.getInt(4);

        cursor.close();
        cursor = null;

        db.beginTransaction();
        try
        {
            String ParagraphQuery = "SELECT * FROM " + CHAPTER_PARAGRAPHS_TABLE_NAME + " WHERE " + BOOK_ID + " = " + BookID + " AND " + CHAPTER_ID + " = " + ChapterID;

            cursor = db.rawQuery(ParagraphQuery, null);

            db.setTransactionSuccessful();
        }
        finally
        {
            db.endTransaction();
        }

        cursor.moveToFirst();

        for(int i = 0; i < cursor.getCount(); i++)
        {
            // Repeat for Each Paragraph.
            Book.Paragraph Paragraph = new Book.Paragraph();

            Paragraph.ParagraphID = cursor.getInt(2);
            Paragraph.Content = cursor.getString(3);

            ChapterGot.Content.add(Paragraph);

            cursor.moveToNext();
        }

        cursor.close();
        return ChapterGot;
    }

    public void UpdateBookType(int ExternalID, Book.BookType NewType)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues TypeCV = new ContentValues();
        TypeCV.put(TYPE, NewType.ordinal());

        db.update(LIBRARY_TABLE_NAME, TypeCV,EXTERNAL_ID + " = " + ExternalID, null);
    }

    public void UpdateTitle(int ExternalID, String NewTitle)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues TitleCV = new ContentValues();
        TitleCV.put(TITLE, NewTitle);

        db.update(LIBRARY_TABLE_NAME, TitleCV,EXTERNAL_ID + " = " + ExternalID, null);
    }

    public void UpdateAuthor(int ExternalID, String NewAuthor)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues TitleCV = new ContentValues();
        TitleCV.put(AUTHOR, NewAuthor);

        db.update(LIBRARY_TABLE_NAME, TitleCV,EXTERNAL_ID + " = " + ExternalID, null);
    }

    public void UpdateDescription(int ExternalID, String NewDescription)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues DescriptionCV = new ContentValues();
        DescriptionCV.put(DESCRIPTION, NewDescription);

        db.update(LIBRARY_TABLE_NAME, DescriptionCV,EXTERNAL_ID + " = " + ExternalID, null);
    }

    public void UpdateCoverURL(int ExternalID, String NewCoverURL)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues CoverCV = new ContentValues();
        CoverCV.put(COVER_URL, NewCoverURL);

        db.update(LIBRARY_TABLE_NAME, CoverCV,EXTERNAL_ID + " = " + ExternalID, null);
    }

    public void UpdateGenres(int ExternalID, ArrayList<Book.Genres> NewGenres)
    {

    }

    public void UpdatePageCount(int ExternalID, int NewPageCount)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues PageCountCV = new ContentValues();
        PageCountCV.put(PAGE_COUNT, NewPageCount);

        db.update(LIBRARY_TABLE_NAME, PageCountCV, EXTERNAL_ID + " = " + ExternalID, null);
    }

    public void UpdateFollowers(int ExternalID, int NewFollowerCount)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues FollowerCV = new ContentValues();
        FollowerCV.put(FOLLOWERS, NewFollowerCount);

        db.update(LIBRARY_TABLE_NAME, FollowerCV, EXTERNAL_ID + " = " + ExternalID, null);
    }

    public void UpdateFavourites(int ExternalID, int NewFavouriteCount)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues FavouriteCV = new ContentValues();
        FavouriteCV.put(FAVOURITES, NewFavouriteCount);

        db.update(LIBRARY_TABLE_NAME, FavouriteCV, EXTERNAL_ID + " = " + ExternalID, null);
    }

    public void UpdateRating(int ExternalID, double NewRating)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues RatingCV = new ContentValues();
        RatingCV.put(RATING, NewRating);

        db.update(LIBRARY_TABLE_NAME, RatingCV, EXTERNAL_ID + " = " + ExternalID, null);
    }

    public void UpdateLastUpdateDateTime(int ExternalID, GregorianCalendar NewDateTime)
    {

    }

    public void ReplaceChapter(int BookID, int ChapterID, Book.Chapter ReplaceChapter)
    {
        // Send Chapter Modified Notification.
    }

    public void RemoveChapter(int BookID, int ChapterID, Book.Chapter ReplaceChapter)
    {
        // Send Chapter Removed Notification.
    }
    public void AddChapter(int BookID, Book.Chapter NewChapter)
    {
        // Send Chapter Update Notification.
    }

    public void UpdateTags(int ExternalID, ArrayList<Book.Tags> NewTags)
    {

    }

    public void UpdateWarnings(int ExternalID, ArrayList<Book.Warnings> NewWarnings)
    {

    }


    public void UpdateHasRead(int ExternalID, boolean HasRead)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        if(HasRead)
        {
            db.execSQL("UPDATE " + LIBRARY_TABLE_NAME + " SET " + HAS_READ +" = '1' WHERE " + EXTERNAL_ID + " = " + ExternalID);
        }
        else
        {
            db.execSQL("UPDATE " + LIBRARY_TABLE_NAME + " SET " + HAS_READ +" = '0' WHERE " + EXTERNAL_ID + " = " + ExternalID);
        }
    }

    public void UpdateLastReadChapter(int ExternalID, int ChapterID)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues LastReadValues = new ContentValues();

        LastReadValues.put(LAST_READ_CHAPTER, ChapterID);

        db.beginTransaction();
        try
        {
            int Result = db.update(LIBRARY_TABLE_NAME, LastReadValues, EXTERNAL_ID + " = " + ExternalID, null);

            if(Result == -1)
            {
                Log.println(Log.INFO, "Hi", "Failed in Updating LastReadChapter");
            }
            else
            {
                Log.println(Log.INFO, "Hi", "Updated LastReadChapter for " + ExternalID + " to Chapter: " + ChapterID);
            }

            db.setTransactionSuccessful();
        }
        finally
        {
            db.endTransaction();
        }
    }

    public void UpdateChapterProgress(Book.Chapter CurrentChapter, int BookID)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("UPDATE " + CHAPTERS_TABLE_NAME + " SET " + CHAPTER_PROGRESS +" = "  + CurrentChapter.ChapterProgress
                  + " WHERE " + BOOK_ID + " = " + BookID + " AND " + CHAPTER_ID + " = " + CurrentChapter.ID);
    }

    public void UpdateHasUnreadUpdate(int ExternalID, boolean HasUnreadUpdate)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        if(HasUnreadUpdate)
        {
            db.execSQL("UPDATE " + LIBRARY_TABLE_NAME + " SET " + HAS_UNREAD_UPDATE +" = '1' WHERE " + EXTERNAL_ID + " = " + ExternalID);
        }
        else
        {
            db.execSQL("UPDATE " + LIBRARY_TABLE_NAME + " SET " + HAS_UNREAD_UPDATE +" = '0' WHERE " + EXTERNAL_ID + " = " + ExternalID);
        }
    }

    public void DeleteBook(int ExternalID)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int BookID = GetBookID(ExternalID);

        db.delete(LIBRARY_TABLE_NAME, EXTERNAL_ID + " = " + ExternalID, null);
        db.delete(CHAPTERS_TABLE_NAME, BOOK_ID + " = " + BookID, null);
        db.delete(CHAPTER_PARAGRAPHS_TABLE_NAME, BOOK_ID + " = " + BookID, null);

        SharedPreferences Pref = context.getSharedPreferences("Settings", MODE_PRIVATE);
        int UserID = Pref.getInt("UserID", 1);

        // Remove from the Firebase Database.
        FirebaseFirestore FirebaseDB = FirebaseFirestore.getInstance();
        FirebaseDB.collection("User_Books")
                .whereEqualTo("UserID", UserID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>()
                {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots)
                    {
                        DocumentReference Delete = queryDocumentSnapshots.getDocuments().get(0).getReference();

                        FirebaseDB.collection("User_Books")
                                .document(Delete.getId())
                                .update("ExternalID", FieldValue.arrayRemove(ExternalID));
                    }
                });
    }

    public void DeleteChapter(int BookID, int ChapterID)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CHAPTERS_TABLE_NAME, BOOK_ID + " = " + BookID + " AND " +
                CHAPTER_ID +  " = " + ChapterID, null);
    }
}