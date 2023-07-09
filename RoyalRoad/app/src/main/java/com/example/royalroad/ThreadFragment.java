package com.example.royalroad;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class ThreadFragment extends Fragment
{
    public RecyclerView PostsRV;
    public ArrayList<ForumInnerData> PostData;
    public ThreadInnerAdapter PostsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_thread, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        PostsRV = view.findViewById(R.id.PostRV);

        PostsRV.setLayoutManager(new LinearLayoutManager(getContext()));
        PostsRV.setHasFixedSize(true);

        ForumInnerData DebugPostData = new ForumInnerData();

        DebugPostData.IsOP = true;
        DebugPostData.PostTitle = "This is a Debug Post.";
        DebugPostData.PostAuthor = "by DebugAuthor";
        DebugPostData.PostBody = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
        DebugPostData.PostDateTime = "11/11/1996";

        PostData = new ArrayList<>();

        PostData.add(DebugPostData);
        PostData.add(DebugPostData);
        PostData.add(DebugPostData);
        PostData.add(DebugPostData);
        PostData.add(DebugPostData);
        PostData.add(DebugPostData);
        PostData.add(DebugPostData);
        PostData.add(DebugPostData);
        PostData.add(DebugPostData);
        PostData.add(DebugPostData);

        PostsAdapter = new ThreadInnerAdapter(PostData);
        PostsRV.setAdapter(PostsAdapter);
    }
}