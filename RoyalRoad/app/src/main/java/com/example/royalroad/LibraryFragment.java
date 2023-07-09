package com.example.royalroad;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.royalroad.LibraryActivity.LibraryType;

import java.util.ArrayList;

public class LibraryFragment extends Fragment
{
    ArrayList<Book> BookList = new ArrayList<>();
    LibraryType Type;

    private RecyclerView LibraryRecyclerview;
    private TextView NoBookTXT;

    LibraryActivity libraryActivity;

    private BookAdapter bookAdapter;
    private boolean IsDarkMode;

    boolean DeleteMode;

    int ContinueIndex = 0;

    public LibraryFragment(LibraryType thisType)
    {
        this.Type = thisType;
    }

    public LibraryFragment()
    {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_library, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        libraryActivity = (LibraryActivity)getActivity();

        LibraryRecyclerview = view.findViewById(R.id.LibraryRecyclerview);
        NoBookTXT = view.findViewById(R.id.NoBooksTXT);

        InitializeLibrary();
        SwitchTheme(IsDarkMode);

        DeleteMode = false;
    }

    public void InitializeLibrary()
    {
        SharedPreferences Pref = getActivity().getSharedPreferences("Settings", MODE_PRIVATE);
        IsDarkMode = Pref.getBoolean("AppTheme", false);

        BookList = new ArrayList<>();

        LibraryRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        LibraryRecyclerview.setHasFixedSize(true);

        DBHandler SQLiteDB = new DBHandler(getActivity());

        int LibraryCount = SQLiteDB.GetLibraryCount();

        if(LibraryCount > 0)
        {
            NoBookTXT.setVisibility(View.GONE);

            for(int i = 0; i < LibraryCount; i++)
            {
                Book NewBook = SQLiteDB.GetBook(i + 1);

                if(Type == LibraryType.History && NewBook.HasRead)
                {
                    BookList.add(NewBook);
                }

                if(Type == LibraryType.Downloaded)
                {
                    BookList.add(NewBook);
                }
            }
        }
        else
        {
            NoBookTXT.setVisibility(View.VISIBLE);
        }

        if(Type == LibraryType.History)
        {
            libraryActivity.HistoryCount = BookList.size();

            if(libraryActivity.HistoryCount > 0)
                libraryActivity.BottomTabs.getTabAt(0).setText("History" + " ( " + BookList.size() + " )");
        }
        else if(Type == LibraryType.Downloaded)
        {
            libraryActivity.DownloadedCount = BookList.size();

            if(libraryActivity.DownloadedCount > 0)
                libraryActivity.BottomTabs.getTabAt(1).setText("Downloaded" + " ( " + BookList.size() + " )");

        }
        else if(Type == LibraryType.Read_Later)
        {
            libraryActivity.ReadLaterCount = BookList.size();

            if(libraryActivity.ReadLaterCount > 0)
                libraryActivity.BottomTabs.getTabAt(2).setText("Read Later" + " ( " + BookList.size() + " )");

        }
        else if(Type == LibraryType.Folders)
        {

        }
        else if(Type == LibraryType.Download_Manager)
        {
            libraryActivity.DownloadManagerCount = BookList.size();

            if(libraryActivity.DownloadManagerCount > 0)
                libraryActivity.BottomTabs.getTabAt(4).setText("Downloaded" + " ( " + BookList.size() + " )");
        }

        bookAdapter = new BookAdapter(BookList);
        LibraryRecyclerview.setAdapter(bookAdapter);

        if(ContinueIndex != 0)
        {
            LibraryRecyclerview.getLayoutManager().scrollToPosition(ContinueIndex);
        }
    }

    public void OpenDeleteMenu(boolean Delete)
    {
        if(Delete)
        {

        }
        else
        {

        }
    }

    @SuppressLint("ResourceAsColor")
    public void SwitchTheme(boolean DarkMode)
    {
        if(DarkMode)
        {
            LibraryRecyclerview.setBackgroundColor(getResources().getColor(R.color.DarkOuter));
        }
        else
        {
            LibraryRecyclerview.setBackgroundColor(getResources().getColor(R.color.LightOuter));
        }
    }
}