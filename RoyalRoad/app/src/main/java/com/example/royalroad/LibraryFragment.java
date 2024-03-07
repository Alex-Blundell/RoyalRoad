package com.example.royalroad;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.royalroad.LibraryActivity.LibraryType;

import org.checkerframework.checker.units.qual.Current;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

public class LibraryFragment extends Fragment
{
    public ArrayList<Book> BookList = new ArrayList<>();
    LibraryType Type;

    private RecyclerView LibraryRecyclerview;


    private TextView NoBookTXT;

    LibraryActivity libraryActivity;

    public BookAdapter bookAdapter;
    private boolean IsDarkMode;



    boolean DeleteMode;

    int ContinueIndex = 0;

    ArrayList<Book> DeleteBooks;


    @Override
    public void onResume()
    {
        super.onResume();
        ((LibraryActivity)getActivity()).UpdateTabName(Type);
    }

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

        DeleteBooks = new ArrayList<>();
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



        libraryActivity.DeleteArea.setVisibility(View.GONE);

        InitializeLibrary(' ');
        SwitchTheme(IsDarkMode);

        DeleteMode = false;

    }

    @SuppressLint("JavascriptInterface")
    public void InitializeLibrary(char RestrictedLetter)
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

            int LibraryExcess = 0;

            for(int i = 0; i < LibraryCount; i++)
            {
                boolean BookExists = SQLiteDB.DoesBookExist(i + 1);

                if(BookExists)
                {
                    Book NewBook = SQLiteDB.GetBook(i + 1 + LibraryExcess);
                    boolean AddBook = true;

                    if(NewBook == null)
                    {
                        AddBook = false;
                    }

                    if(RestrictedLetter != ' ')
                    {
                        if(RestrictedLetter == '1')
                        {
                            Log.println(Log.INFO, "Hi", "Restricting by Numbers ( Hopefully )");

                            if(NewBook.Title.charAt(0) == '0')
                            {
                                AddBook = true;
                            }
                            else if(NewBook.Title.charAt(0) == '1')
                            {
                                AddBook = true;
                            }
                            else if(NewBook.Title.charAt(0) == '2')
                            {
                                AddBook = true;
                            }
                            else if(NewBook.Title.charAt(0) == '3')
                            {
                                AddBook = true;
                            }
                            else if(NewBook.Title.charAt(0) == '4')
                            {
                                AddBook = true;
                            }
                            else if(NewBook.Title.charAt(0) == '5')
                            {
                                AddBook = true;
                            }
                            else if(NewBook.Title.charAt(0) == '6')
                            {
                                AddBook = true;
                            }
                            else if(NewBook.Title.charAt(0) == '7')
                            {
                                AddBook = true;
                            }
                            else if(NewBook.Title.charAt(0) == '8')
                            {
                                AddBook = true;
                            }
                            else if(NewBook.Title.charAt(0) == '9')
                            {
                                AddBook = true;
                            }
                            else
                            {
                                AddBook = false;
                            }
                        }
                        else
                        {
                            if(NewBook.Title.charAt(0) != RestrictedLetter)
                            {
                                AddBook = false;
                            }
                        }
                    }

                    if(AddBook)
                    {
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
                    LibraryExcess++;
                }
            }
        }
        else
        {
            NoBookTXT.setVisibility(View.VISIBLE);
        }

        if(Type == LibraryType.History)
        {
            Collections.sort(BookList, Collections.reverseOrder());
            libraryActivity.HistoryCount = BookList.size();
        }
        else if(Type == LibraryType.Downloaded)
        {
            libraryActivity.DownloadedCount = BookList.size();

            ArrayList<Book> AlteredBookList = new ArrayList<>();
            ArrayList<Book> CorrectedBookList = new ArrayList<>();

            // Finding All Books that have an Unread Update.
            for(int i = 0; i < BookList.size(); i++)
            {
                Book CurrentBook = BookList.get(i);

                // Check to see if the CurrentBook.HasUnreadUpdate is set to true.
                if(CurrentBook.HasUnreadUpdate)
                {
                    BookList.remove(CurrentBook);
                    AlteredBookList.add(CurrentBook);
                }
            }

            // Order Books that have an Unread Update by the Latest Updates.
            for(Book CurrentBook : AlteredBookList)
            {
                // Add to CorrectedBookList.
                CorrectedBookList.add(CurrentBook);
            }

            // Reverse the BookList, The remaining Books that do not have an Update. (This is Done so that the BookList shows the Latest Downloaded First).
            Collections.reverse(BookList);

            // Add the Reversed BookList to the CorrectedBookList, This will have all of the Updated Books First,
            // In their Order of Updates, followed by any Non-Updated Books in order of Last Download.
            CorrectedBookList.addAll(BookList);
            BookList = CorrectedBookList;
        }
        else if(Type == LibraryType.Folders)
        {

        }

        bookAdapter = new BookAdapter(BookList);
        LibraryRecyclerview.setAdapter(bookAdapter);

        if(ContinueIndex != 0)
        {
            LibraryRecyclerview.getLayoutManager().scrollToPosition(ContinueIndex);
        }
    }

    public void RenewLibrary(boolean IsFocus)
    {
        // Get Current Location in Library.
        int CurrentItem = 0;

        if(IsFocus)
        {
            CurrentItem = ((LinearLayoutManager)LibraryRecyclerview.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        }

        InitializeLibrary(' ');

        if(IsFocus)
        {
            LibraryRecyclerview.scrollToPosition(CurrentItem);
        }
    }

    public void RestrictLibrary(char RestricLetter)
    {
        InitializeLibrary(RestricLetter);
    }

    public void AddToDelete(Book DeleteBook)
    {
        if(DeleteBooks.size() == 0)
        {
            libraryActivity.DeleteArea.setVisibility(View.VISIBLE);
        }

        DeleteBooks.add(DeleteBook);
    }

    public void RemoveFromDelete(Book RemoveBook)
    {
        if(DeleteBooks.size() == 0)
        {
            libraryActivity.DeleteArea.setVisibility(View.GONE);
        }

        DeleteBooks.remove(RemoveBook);
    }

    public int GetDeleteBooksCount()
    {
        return DeleteBooks.size();
    }

    public void DeleteBooks()
    {
        DBHandler SQLiteDB = new DBHandler(getContext());

        for(int i = 0; i < DeleteBooks.size(); i++)
        {
            if(Type == LibraryType.History)
            {
                // Delete From History.
                SQLiteDB.DeleteHistoryBook(DeleteBooks.get(i).InternalID);
            }
            else if(Type == LibraryType.Downloaded)
            {
                // Delete From Downloaded.
                SQLiteDB.DeleteBook(DeleteBooks.get(i).ExternalID);
            }
        }

        libraryActivity.DeleteArea.setVisibility(View.GONE);

        SQLiteDB.close();
        InitializeLibrary(' ');
    }

    @SuppressLint("ResourceAsColor")
    public void SwitchTheme(boolean DarkMode)
    {
        if(DarkMode)
        {
            LibraryRecyclerview.setBackgroundColor(getResources().getColor(R.color.DarkOuter));
            NoBookTXT.setTextColor(getResources().getColor(R.color.DarkText));
        }
        else
        {
            LibraryRecyclerview.setBackgroundColor(getResources().getColor(R.color.LightOuter));
            NoBookTXT.setTextColor(getResources().getColor(R.color.black));
        }
    }
}