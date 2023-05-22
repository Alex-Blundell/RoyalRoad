package com.example.royalroad;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class BookHomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Book ReadBook = ((ReadActivity)getActivity()).ReadBook;

        TextView TitleTXT = view.findViewById(R.id.BookTitle);
        TextView AuthorTXT = view.findViewById(R.id.BookAuthor);
        TextView Description = view.findViewById(R.id.BookDescription);
        RatingBar Rating = view.findViewById(R.id.BookRating);
        ImageView Cover = view.findViewById(R.id.BookCover);

        TitleTXT.setText(ReadBook.Title);
        AuthorTXT.setText(ReadBook.Author);
        Description.setText(ReadBook.Description);
        Rating.setRating((float) ReadBook.Rating);

        Glide.with(getContext())
                .load(ReadBook.CoverURL)
                .into(Cover);
    }
}