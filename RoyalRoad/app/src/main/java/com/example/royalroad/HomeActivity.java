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
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.StrictMode;
import android.os.strictmode.Violation;
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

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.FirebaseError;
import com.google.firebase.annotations.concurrent.Background;
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
import java.util.concurrent.ExecutionException;

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

    SearchView Searchbar;

    Boolean IsDarkMode;
    String FilePath = "";

    FirebaseFirestore db;
    DBHandler SQLiteDB;

    int SELECT_PICTURE = 200;
    public static final String UPDATE_CHANNEL = "updatechannel";
    String GROUP_KEY_STORY_NOTIFICATION = "com.android.example.STORY_NOTIFICATION";

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        db = FirebaseFirestore.getInstance();
        SQLiteDB = new DBHandler(HomeActivity.this);

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

        Searchbar = findViewById(R.id.HomeSearchbar);
        Searchbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Searchbar.setIconified(false);
                DarkBackground.setVisibility(View.VISIBLE);

                if(IsDarkMode)
                {
                    Searchbar.setBackgroundColor(getColor(R.color.white));
                }

                DarkBackground.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        Searchbar.setIconified(true);
                    }
                });
            }
        });

        Searchbar.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose()
            {
                if(IsDarkMode)
                {
                    Searchbar.setBackgroundColor(getColor(R.color.DarkOuter));
                }

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
                            Intent ImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
            public void onClick(View view)
            {
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

        if (LoggedIn)
        {
            String Username = Pref.getString("Username", "");
            UsernameTXT.setText(Username);
            LoginBTN.setText("Logout");
        }
        else
        {
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

        try
        {
            CheckUpdates();
        } catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }

    private void CheckUpdates() throws InterruptedException {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        // Check if Online.
        boolean IsOnline = (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED);

        if(IsOnline)
        {
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

                                int DB_Records = SQLiteDB.GetLibraryCount();

                                if(DB_Records == 0)
                                {
                                    Book[] AddBooks = new Book[ExternalIDs.size()];

                                    for(int i = 0; i < ExternalIDs.size(); i++)
                                    {
                                        //Book NewBook = new LibraryTasks(HomeActivity.this, ExternalIDs.get(i), false).execute().get();
                                        AddBooks[i] = new Book();
                                        try {
                                            AddBooks[i] = AddBooks[i].CreateBook(HomeActivity.this, ExternalIDs.get(i), true, true);
                                        } catch (InterruptedException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                }
                                else
                                {
                                    for(int i = 0; i < ExternalIDs.size(); i++)
                                    {
                                        boolean ExistsInDB = SQLiteDB.GetLibraryBook(ExternalIDs.get(i));

                                        if(!ExistsInDB)
                                        {
                                            Book[] AddBooks = new Book[ExternalIDs.size()];
                                            AddBooks[i] = new Book();
                                            try {
                                                AddBooks[i] = AddBooks[i].CreateBook(HomeActivity.this, ExternalIDs.get(i), true, true);
                                            } catch (InterruptedException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    });



            if(HasUpdated)
            {
                int Count = SQLiteDB.GetLibraryCount();

                if(Count > 0)
                {
                    long[] ExternalIDs = SQLiteDB.GetExternalIDs();

                    for(int i = 0; i < ExternalIDs.length; i++)
                    {
                        String URL = "https://www.royalroad.com/fiction/" + ExternalIDs[i];

                        int finalI = i;
                        Thread CheckThread = new Thread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                try
                                {
                                    int CurrentChapterCount = SQLiteDB.GetChapterCount(ExternalIDs[finalI]);
                                    Document UpdateDoc = Jsoup.connect(URL).get();

                                    int ChapterCount = Integer.parseInt(UpdateDoc.getElementsByClass("label label-default pull-right").get(0).text());

                                    if(CurrentChapterCount != ChapterCount)
                                    {
                                        String Title = SQLiteDB.GetBookTitle(ExternalIDs[finalI]);

                                        // Something has Changed.
                                        // New Chapters.
                                        if(ChapterCount > CurrentChapterCount)
                                        {

                                        }

                                        // Update in DB.

                                        NotificationManager NotifyManager = getApplicationContext().getSystemService(NotificationManager.class);

                                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
                                        {
                                            NotificationChannel DownloadChannel = new NotificationChannel(UPDATE_CHANNEL, "Download Channel", NotificationManager.IMPORTANCE_DEFAULT);
                                            NotifyManager.createNotificationChannel(DownloadChannel);
                                        }

                                        // Send Notification.
                                        Notification DownloadedNotification = new NotificationCompat.Builder(getApplicationContext(), UPDATE_CHANNEL)
                                                .setSmallIcon(R.mipmap.icon)
                                                .setContentTitle(Title)
                                                .setContentText("Story Updated")
                                                .setPriority(Notification.PRIORITY_DEFAULT)
                                                .setCategory(Notification.CATEGORY_MESSAGE)
                                                .setGroup(GROUP_KEY_STORY_NOTIFICATION)
                                                .build();

                                        NotifyManager.notify((int) ExternalIDs[finalI], DownloadedNotification);
                                    }
                                }
                                catch (IOException e)
                                {
                                    throw new RuntimeException(e);
                                }
                            }
                        });

                        CheckThread.start();
                        CheckThread.join();
                    }
                }
            }

            SQLiteDB.close();
        }
        else
        {
            Toast.makeText(this, "Connect to internet to update.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences Pref = getSharedPreferences("Settings", MODE_PRIVATE);
        boolean TempDarkMode = Pref.getBoolean("AppTheme", false);

        SwitchThemes(TempDarkMode);
    }

    @SuppressLint("ResourceAsColor")
    public void SwitchThemes(boolean DarkMode) {
        Log.println(Log.INFO, "Hi", "In Switch Theme");

        if (DarkMode)
        {
            getWindow().getDecorView().setBackgroundColor(getColor(R.color.DarkOuter));
            UsernameTXT.setTextColor(getColor(R.color.white));

            MailBTN.setImageTintList(ColorStateList.valueOf(getColor(R.color.white)));
            NotificationsBTN.setImageTintList(ColorStateList.valueOf(getColor(R.color.white)));
            Searchbar.setBackgroundColor(getColor(R.color.DarkOuter));
        }
        else
        {
            getWindow().getDecorView().setBackgroundColor(getColor(R.color.white));
            UsernameTXT.setTextColor(getColor(R.color.black));

            MailBTN.setImageTintList(ColorStateList.valueOf(getColor(R.color.black)));
            NotificationsBTN.setImageTintList(ColorStateList.valueOf(getColor(R.color.black)));
            Searchbar.setBackgroundColor(getColor(R.color.white));
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.println(Log.INFO, "Hi", "In Activity Result");

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
        Log.println(Log.INFO, "Hi", "In Real Path");

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