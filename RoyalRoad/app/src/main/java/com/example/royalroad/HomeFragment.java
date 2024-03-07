package com.example.royalroad;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.sql.Ref;
import java.util.ArrayList;
import java.util.Collections;

public class HomeFragment extends Fragment
{
    Button LastReadBTN;
    Button DiscoverBTN;
    Button LibraryBTN;
    Button ForumsBTN;
    Button FriendsBTN;
    Button AdvancedSearchBTN;
    ImageButton MailBTN;
    ImageButton NotificationsBTN;

    TextView UsernameTXT;
    TextView LastReadTXT;
    ImageView AvatarImage;
    ImageView DarkBackground;
    ImageView DarkBackgroundAvatar;
    ImageView BackgroundAvatar;
    ImageView LastReadCover;

    ShapeableImageView SelectBTN;
    ShapeableImageView CloseBTN;

    RelativeLayout LastReadArea;

    TextView LastReadTitle;
    TextView LastReadAuthor;

    SearchView Searchbar;

    View Divider;

    Boolean IsDarkMode;
    Boolean IsDownloadingAlready;

    FirebaseFirestore db;
    DBHandler SQLiteDB;

    int SELECT_PICTURE = 200;
    public static final String UPDATE_CHANNEL = "updatechannel";

    private boolean IsExpandedSearch = false;

    private SharedPreferences Pref;
    private SharedPreferences.Editor PrefEditor;

    ArrayList<Book> HistoryBooks = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();
        SQLiteDB = new DBHandler(getActivity());

        IsExpandedSearch = false;

        Pref = getActivity().getSharedPreferences("Settings", MODE_PRIVATE);
        PrefEditor = Pref.edit();

        IsDarkMode = Pref.getBoolean("AppTheme", false);
        IsDownloadingAlready = Pref.getBoolean("DownloadingThemeSwitch", false);

        LastReadTXT = view.findViewById(R.id.LastReadTXT);
        LastReadArea = view.findViewById(R.id.LastReadArea);
        LastReadBTN = view.findViewById(R.id.LastReadBTN);
        LastReadCover = view.findViewById(R.id.LastReadCover);
        LastReadTitle = view.findViewById(R.id.LastReadTitle);
        LastReadAuthor = view.findViewById(R.id.LastReadAuthor);

        DiscoverBTN = view.findViewById(R.id.DiscoverBTN);
        LibraryBTN = view.findViewById(R.id.LibraryBTN);
        ForumsBTN = view.findViewById(R.id.ForumsBTN);
        FriendsBTN = view.findViewById(R.id.FriendsBTN);

        MailBTN = view.findViewById(R.id.MailBTN);
        NotificationsBTN = view.findViewById(R.id.NotificationBTN);

        UsernameTXT = view.findViewById(R.id.Username);
        AvatarImage = view.findViewById(R.id.ProfileImage);

        DarkBackground = view.findViewById(R.id.DarkBackground);

        DarkBackgroundAvatar = view.findViewById(R.id.DarkBackgroundAvatar);
        BackgroundAvatar = view.findViewById(R.id.BackgroundAvatar);
        SelectBTN = view.findViewById(R.id.SelectBTN);
        CloseBTN = view.findViewById(R.id.CloseBTN);

        DarkBackground.setVisibility(View.GONE);

        DarkBackgroundAvatar.setVisibility(View.GONE);
        BackgroundAvatar.setVisibility(View.GONE);
        SelectBTN.setVisibility(View.GONE);
        CloseBTN.setVisibility(View.GONE);

        Divider = view.findViewById(R.id.DividerOne);

        Searchbar = view.findViewById(R.id.HomeSearchbar);

        AdvancedSearchBTN = view.findViewById(R.id.AdvancedSearchBTN);

        AdvancedSearchBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent SearchResultsIntent = new Intent(getActivity(), SearchActivity.class);
                SearchResultsIntent.putExtra("IsExpandedSearch", true);

                startActivity(SearchResultsIntent);
            }
        });

        Searchbar.setIconifiedByDefault(false);
        Searchbar.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String s)
            {
                Searchbar.setQuery("", false);
                Searchbar.clearFocus();

                Intent SearchResultsIntent = new Intent(getActivity(), SearchActivity.class);

                SearchResultsIntent.putExtra("IsExpandedSearch", IsExpandedSearch);
                SearchResultsIntent.putExtra("Title", s);

                startActivity(SearchResultsIntent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s)
            {
                return false;
            }
        });

        Searchbar.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View view, boolean b)
            {
                if(b)
                {
                    Searchbar.setBackgroundColor(getActivity().getColor(R.color.white));
                    DarkBackground.setVisibility(View.VISIBLE);

                }
                else
                {
                    Searchbar.setBackgroundColor(getActivity().getColor(R.color.DarkOuter));
                    DarkBackground.setVisibility(View.GONE);
                    Searchbar.setQuery("", false);
                }

                DarkBackground.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        Searchbar.clearFocus();
                    }
                });
            }
        });

        boolean LoggedIn = Pref.getBoolean("IsLoggedIn", false);

        if (LoggedIn)
        {
            String AvatarURL = Pref.getString("UserAvatarURL", "");
            UsernameTXT.setText(Pref.getString("Username", ""));

            if (!AvatarURL.isEmpty())
            {
                Glide.with(this)
                        .load(AvatarURL)
                        .into(AvatarImage);

                Glide.with(this)
                        .load(AvatarURL)
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
        else
        {
            UsernameTXT.setText("Not Logged In.");
        }

        SwitchThemes(IsDarkMode);

        GetLastRead();

        DiscoverBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent DiscoverIntent = new Intent(getActivity(), DiscoverActivity.class);
                startActivity(DiscoverIntent);
            }
        });

        LibraryBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent LibraryIntent = new Intent(getActivity(), LibraryActivity.class);
                startActivity(LibraryIntent);
            }
        });

        ForumsBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ForumsIntent = new Intent(getActivity(), ForumsActivity.class);
                startActivity(ForumsIntent);
            }
        });

        FriendsBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent FriendsIntent = new Intent(getActivity(), FriendsActivity.class);
                startActivity(FriendsIntent);
            }
        });

        try
        {
            if(!IsDownloadingAlready)
            {
                CheckUpdates();
            }
            else
            {
                this.Pref = getActivity().getSharedPreferences("Settings", MODE_PRIVATE);
                SharedPreferences.Editor PrefEditor = this.Pref.edit();

                PrefEditor.putBoolean("DownloadingThemeSwitch", false);
                PrefEditor.apply();
            }
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }

    private void CheckUpdates() throws InterruptedException
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        Context ThisContext = getActivity().getApplicationContext();

        // Check if Online.
        boolean IsOnline = (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED);

        if(IsOnline)
        {
            SharedPreferences Pref = getActivity().getSharedPreferences("Settings", MODE_PRIVATE);
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

                                for(int i = 0; i < ExternalIDs.size(); i++)
                                {
                                    boolean ExistsInDB = false;

                                    if(DB_Records != 0)
                                    {
                                        ExistsInDB = SQLiteDB.GetLibraryBook(ExternalIDs.get(i));
                                    }

                                    try
                                    {
                                        if(!ExistsInDB)
                                        {
                                            Book NewBook = new Book().CreateBook(ThisContext, ExternalIDs.get(i), true, true, false);
                                        }
                                    }
                                    catch (InterruptedException e)
                                    {
                                        throw new RuntimeException(e);
                                    }
                                }
                            }
                        }
                    });

            SQLiteDB.close();
        }
        else
        {
            Toast.makeText(getActivity(), "Connect to internet to update.", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("ResourceAsColor")
    public void SwitchThemes(boolean DarkMode)
    {
        if (DarkMode)
        {
            getActivity().getWindow().getDecorView().setBackgroundColor(getActivity().getColor(R.color.DarkOuter));
            UsernameTXT.setTextColor(getActivity().getColor(R.color.DarkText));

            MailBTN.setImageTintList(ColorStateList.valueOf(getActivity().getColor(R.color.DarkText)));
            NotificationsBTN.setImageTintList(ColorStateList.valueOf(getActivity().getColor(R.color.DarkText)));
            Searchbar.setBackgroundColor(getActivity().getColor(R.color.DarkOuter));

            Divider.setBackgroundColor(getActivity().getColor(R.color.DarkBorder));
        }
        else
        {
            getActivity().getWindow().getDecorView().setBackgroundColor(getActivity().getColor(R.color.LightOuter));
            UsernameTXT.setTextColor(getActivity().getColor(R.color.black));

            MailBTN.setImageTintList(ColorStateList.valueOf(getActivity().getColor(R.color.black)));
            NotificationsBTN.setImageTintList(ColorStateList.valueOf(getActivity().getColor(R.color.black)));
            Searchbar.setBackgroundColor(getActivity().getColor(R.color.white));
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();

        SharedPreferences Pref = getActivity().getSharedPreferences("Settings", MODE_PRIVATE);
        boolean TempDarkMode = Pref.getBoolean("AppTheme", false);

        SwitchThemes(TempDarkMode);

        GetLastRead();
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
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                            {
                                db.collection("Users")
                                        .whereEqualTo("Username", UsernameTXT.getText().toString())
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>()
                                        {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots)
                                            {
                                                SharedPreferences Pref = getActivity().getSharedPreferences("Settings", MODE_PRIVATE);
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

    public void GetLastRead()
    {
        int LibraryCount = SQLiteDB.GetLibraryCount();

        if(LibraryCount > 0)
        {
            for (int i = 0; i < LibraryCount; i++)
            {
                boolean BookExists = SQLiteDB.DoesBookExist(i + 1);

                if(BookExists)
                {
                    Book CurrentBook = SQLiteDB.GetBook(i + 1);

                    if (CurrentBook.HasRead)
                    {
                        HistoryBooks.add(CurrentBook);
                    }
                }
            }

            if(HistoryBooks.size() > 0)
            {
                Collections.sort(HistoryBooks, Collections.reverseOrder());

                ConnectivityManager connectivityManager = (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

                boolean IsOnline = (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED);

                if(IsOnline)
                {
                    Glide.with(getContext())
                            .load(HistoryBooks.get(0).GetCover())
                            .into(LastReadCover);
                }
                else
                {
                    LastReadCover.setImageResource(R.drawable.default_cover);
                }

                LastReadTitle.setText(HistoryBooks.get(0).Title);
                LastReadAuthor.setText("by " + HistoryBooks.get(0).Author);

                LastReadBTN.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        Intent ThisIntent = new Intent(getContext(), ReadActivity.class);
                        ThisIntent.putExtra("Book", HistoryBooks.get(0));

                        DBHandler SQLiteDB = new DBHandler(getContext());

                        boolean HasDownloaded = SQLiteDB.GetLibraryBook(HistoryBooks.get(0).GetExternalID());
                        ThisIntent.putExtra("HasDownloaded", HasDownloaded);

                        SQLiteDB.close();
                        startActivity(ThisIntent);
                    }
                });
            }
            else
            {
                LastReadTXT.setVisibility(View.INVISIBLE);
                LastReadArea.setVisibility(View.INVISIBLE);
            }
        }
        else
        {
            LastReadTXT.setVisibility(View.INVISIBLE);
            LastReadArea.setVisibility(View.INVISIBLE);
        }
    }

    private String getRealPathFromURI(Uri FileURI)
    {
        Log.println(Log.INFO, "Hi", "In Real Path");

        String Result;
        String[] Projection = {MediaStore.Images.Media.DATA};

        Cursor cursor = getActivity().getContentResolver().query(FileURI, Projection, null, null, null);

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