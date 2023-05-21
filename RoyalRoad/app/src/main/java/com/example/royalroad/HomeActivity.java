package com.example.royalroad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Debug;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.net.Uri;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.FirebaseError;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class HomeActivity extends AppCompatActivity {
    Button LoginBTN;
    Button DiscoverBTN;
    Button LibraryBTN;
    Button WriteBTN;
    Button ForumsBTN;
    Button FriendsBTN;
    Button SettingsBTN;
    ImageButton MailBTN;
    ImageButton NotificationsBTN;

    RelativeLayout MainLayout;

    TextView UsernameTXT;
    ImageView AvatarImage;
    ImageView DarkBackground;
    ImageView DarkBackgroundAvatar;
    ImageView BackgroundAvatar;

    ShapeableImageView SelectBTN;
    ShapeableImageView CloseBTN;

    Boolean IsDarkMode;
    String FilePath = "";

    FirebaseFirestore db;

    int SELECT_PICTURE = 200;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        db = FirebaseFirestore.getInstance();

        SharedPreferences Pref = getSharedPreferences("Settings", MODE_PRIVATE);
        IsDarkMode = Pref.getBoolean("AppTheme", false);

        LoginBTN = findViewById(R.id.LoginBTN);

        DiscoverBTN = findViewById(R.id.DiscoverBTN);
        LibraryBTN = findViewById(R.id.LibraryBTN);
        WriteBTN = findViewById(R.id.WriteBTN);
        ForumsBTN = findViewById(R.id.ForumsBTN);
        FriendsBTN = findViewById(R.id.FriendsBTN);
        SettingsBTN = findViewById(R.id.SettingsBTN);

        MailBTN = findViewById(R.id.MailBTN);
        NotificationsBTN = findViewById(R.id.NotificationBTN);

        UsernameTXT = findViewById(R.id.Username);
        AvatarImage = findViewById(R.id.ProfileImage);

        DarkBackground = findViewById(R.id.DarkBackground);

        DarkBackgroundAvatar = findViewById(R.id.DarkBackgroundAvatar);
        BackgroundAvatar = findViewById(R.id.BackgroundAvatar);
        SelectBTN = findViewById(R.id.SelectBTN);
        CloseBTN = findViewById(R.id.CloseBTN);

        DarkBackground.setVisibility(View.GONE);

        DarkBackgroundAvatar.setVisibility(View.GONE);
        BackgroundAvatar.setVisibility(View.GONE);
        SelectBTN.setVisibility(View.GONE);
        CloseBTN.setVisibility(View.GONE);

        SearchView Searchbar = findViewById(R.id.HomeSearchbar);
        Searchbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Searchbar.setIconified(false);
                DarkBackground.setVisibility(View.VISIBLE);

                DarkBackground.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Searchbar.setIconified(true);
                    }
                });
            }
        });

        Searchbar.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                DarkBackground.setVisibility(View.GONE);
                return false;
            }
        });

        boolean LoggedIn = Pref.getBoolean("IsLoggedIn", false);

        if (LoggedIn) {
            String AvatarURL = Pref.getString("UserAvatarURL", "");

            if (!AvatarURL.isEmpty()) {
                StorageReference Reference = FirebaseStorage.getInstance().getReferenceFromUrl(AvatarURL);

                Glide.with(this)
                        .load(Reference)
                        .into(AvatarImage);

                Glide.with(this)
                        .load(Reference)
                        .into(BackgroundAvatar);
            }
            else
            {
                AvatarImage.setImageResource(R.drawable.default_profile);
                BackgroundAvatar.setImageResource(R.drawable.default_profile);
            }

            AvatarImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DarkBackgroundAvatar.setVisibility(View.VISIBLE);
                    BackgroundAvatar.setVisibility(View.VISIBLE);
                    SelectBTN.setVisibility(View.VISIBLE);
                    CloseBTN.setVisibility(View.VISIBLE);

                    SelectBTN.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent ImageIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(ImageIntent, SELECT_PICTURE);
                        }
                    });

                    CloseBTN.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            DarkBackgroundAvatar.setVisibility(View.GONE);
                            BackgroundAvatar.setVisibility(View.GONE);
                            SelectBTN.setVisibility(View.GONE);
                            CloseBTN.setVisibility(View.GONE);
                        }
                    });

                    DarkBackgroundAvatar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            DarkBackgroundAvatar.setVisibility(View.GONE);
                            BackgroundAvatar.setVisibility(View.GONE);
                            SelectBTN.setVisibility(View.GONE);
                            CloseBTN.setVisibility(View.GONE);
                        }
                    });
                }
            });
        }

        SwitchThemes(IsDarkMode);

        DiscoverBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent DiscoverIntent = new Intent(HomeActivity.this, DiscoverActivity.class);
                startActivity(DiscoverIntent);
            }
        });

        LibraryBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent LibraryIntent = new Intent(HomeActivity.this, LibraryActivity.class);
                startActivity(LibraryIntent);
            }
        });

        WriteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent WriteIntent = new Intent(HomeActivity.this, WriteActivity.class);
                startActivity(WriteIntent);
            }
        });

        ForumsBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ForumsIntent = new Intent(HomeActivity.this, ForumsActivity.class);
                startActivity(ForumsIntent);
            }
        });

        FriendsBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent FriendsIntent = new Intent(HomeActivity.this, FriendsActivity.class);
                startActivity(FriendsIntent);
            }
        });

        SettingsBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent SettingsIntent = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(SettingsIntent);
            }
        });

        if (LoggedIn) {
            String Username = Pref.getString("Username", "");
            UsernameTXT.setText(Username);
            LoginBTN.setText("Logout");
        } else {
            LoginBTN.setText("Login");
            UsernameTXT.setText("Not logged in.");
        }

        SharedPreferences.Editor PrefEditor = Pref.edit();

        LoginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (LoggedIn) {
                    // Logout.
                    PrefEditor.putBoolean("IsLoggedIn", false);
                    PrefEditor.putString("Username", "");
                    PrefEditor.putInt("UserID", 0);
                    PrefEditor.putString("UserAvatarURL", "");

                    PrefEditor.apply();
                } else {
                    PrefEditor.putBoolean("HasSkipped", false);

                    PrefEditor.apply();
                }

                Intent LoginIntent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(LoginIntent);
                finish();
            }
        });

        CheckUpdates();
    }

    private void CheckUpdates()
    {
        Log.println(Log.INFO, "Hello", "Checking Updates");

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        // Check if Online.
        boolean IsOnline = (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED);

        if(IsOnline)
        {
            Log.println(Log.INFO, "Hello", "I am Online.");

            SharedPreferences Pref = getSharedPreferences("Settings", MODE_PRIVATE);
            int UserID = Pref.getInt("UserID", 1);

            boolean HasUpdated = false;

            db.collection("User_Books")
                    .whereEqualTo("UserID", UserID)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.getResult().size() == 1)
                            {
                                DocumentSnapshot BooksDocument = task.getResult().getDocuments().get(0);
                                ArrayList<Long> ExternalIDs = (ArrayList<Long>) BooksDocument.get("ExternalID");

                                DBHandler SQLiteDB = new DBHandler(HomeActivity.this);

                                int DB_Records = SQLiteDB.GetLibraryCount();

                                if(DB_Records == 0)
                                {
                                    for(int i = 0; i < ExternalIDs.size(); i++)
                                    {
                                        AddBook(ExternalIDs.get(i));
                                    }
                                }
                                else
                                {
                                    for(int i = 0; i < ExternalIDs.size(); i++)
                                    {
                                        boolean ExistsInDB = SQLiteDB.GetLibraryBook(ExternalIDs.get(i));

                                        if(!ExistsInDB)
                                        {
                                            // Add Item, Else Ignore it.
                                            AddBook(ExternalIDs.get(i));
                                        }
                                    }
                                }
                            }
                        }
                    });

            // Run through the SQLite Database ExternalIDs and Check if any have Updated.
            ArrayList<Integer> ExternalIDs = new ArrayList<Integer>();

            if(HasUpdated)
            {
                // Update SQLite Database Entry.
                // Send Update Notification.
            }
        }
        else
        {
            Toast.makeText(this, "Offline", Toast.LENGTH_SHORT).show();
        }
    }

    public void AddBook(long ExternalID)
    {
        DBHandler SQLiteDB = new DBHandler(HomeActivity.this);

        Book NewBook = new Book();
        NewBook.ExternalID = (int)ExternalID;

        String URL = "https://www.royalroad.com/fiction/" + ExternalID + "/";

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try
                {
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

                            String URL = StringArray[0];
                            URL = URL.substring(9, URL.length() - 1);

                            String ChapterName = elementsThree.get(i).text();
                            String ChapterURL = "https://www.royalroad.com" + URL;

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
                                            NewLine.Style = "Default";

                                            NewChapter.ContentLines.add(NewLine);

                                            LineID++;
                                        }
                                    }

                                    NewLine.ID = LineID;

                                    NewLine.Line = element.get(j).text();
                                    NewLine.Style = "Default";

                                    LineID++;

                                    NewChapter.ContentLines.add(NewLine);
                                }
                            }

                            NewBook.Chapters.add(NewChapter);

                            ChapterIndex++;
                        }
                    }

                    NewBook.HasRead = false;
                    NewBook.LastReadChapter = 0;

                    //Log.println(Log.INFO, "Hi", "Added Book " + NewBook.Title);
                    //SQLiteDB.AddBook(NewBook);

                    SQLiteDB.close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    public void SwitchThemes(boolean DarkMode) {
        if (DarkMode) {
            getWindow().getDecorView().setBackgroundColor(getColor(R.color.DarkOuter));
            UsernameTXT.setTextColor(getColor(R.color.white));

            MailBTN.setImageTintList(ColorStateList.valueOf(getColor(R.color.white)));
            NotificationsBTN.setImageTintList(ColorStateList.valueOf(getColor(R.color.white)));
        } else {
            getWindow().getDecorView().setBackgroundColor(getColor(R.color.white));
            UsernameTXT.setTextColor(getColor(R.color.black));

            MailBTN.setImageTintList(ColorStateList.valueOf(getColor(R.color.black)));
            NotificationsBTN.setImageTintList(ColorStateList.valueOf(getColor(R.color.black)));
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri SelectedImageURI = data.getData();

                if (null != SelectedImageURI) {
                    // Upload Image to Storage.
                    String FilePath = getRealPathFromURI(SelectedImageURI);

                    if(FilePath != null)
                    {
                        String[] Paths = FilePath.split("/");
                        String FileName = Paths[Paths.length - 1];

                        FileName = FileName.replace('.', '/');
                        String[] Extension = FileName.split("/");
                        String FileExtension = "." + Extension[Extension.length - 1];

                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageRef = storage.getReference("Avatar Images");

                        String FullFileName = UsernameTXT.getText().toString() + "Avatar" + FileExtension;

                        UploadTask UpTask = storageRef.child(FullFileName).putFile(SelectedImageURI);
                        UpTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                db.collection("Users")
                                        .whereEqualTo("Username", UsernameTXT.getText().toString())
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                SharedPreferences Pref = getSharedPreferences("Settings", MODE_PRIVATE);
                                                SharedPreferences.Editor PrefEditor = Pref.edit();

                                                DocumentSnapshot SelectedDocument = queryDocumentSnapshots.getDocuments().get(0);
                                                DocumentReference SelectedReference = SelectedDocument.getReference();

                                                db.collection("Users").document(SelectedReference.getId()).update("AvatarURL", "gs://royalroad-blundell.appspot.com/Avatar Images/" + FullFileName);

                                                PrefEditor.putString("UserAvatarURL", "gs://royalroad-blundell.appspot.com/Avatar Images/" + FullFileName);
                                                PrefEditor.apply();
                                            }
                                        });
                            }
                        });

                        AvatarImage.setImageURI(SelectedImageURI);
                        BackgroundAvatar.setImageURI(SelectedImageURI);
                    }
                }
            }
        }
    }

    private String getRealPathFromURI(Uri FileURI)
    {
        String Result;
        String[] Projection = {MediaStore.Images.Media.DATA};

        Cursor cursor = getContentResolver().query(FileURI, Projection, null, null, null);

        if (cursor == null)
        {
            Result = FileURI.getPath();
        }
        else
        {
            cursor.moveToFirst();

            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);

            Result = cursor.getString(idx);
            cursor.close();
        }

        return Result;
    }
}