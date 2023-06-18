package com.example.royalroad;

import android.content.Context;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class ForumsHomeFragment extends Fragment {

    RelativeLayout ForumHomeLayout;
    RecyclerView PopularThreads;
    RecyclerView CommunityForums;
    RecyclerView FictionsForums;
    RecyclerView TipsForums;
    RecyclerView ForumsForums;

    ArrayList<ForumData> CommunityForumData;
    ArrayList<ForumData> FictionsForumData;
    ArrayList<ForumData> TipsForumData;
    ArrayList<ForumData> ForumsForumData;

    ForumAdapter CommunityAdapter;
    ForumAdapter FictionsAdapter;
    ForumAdapter TipsAdapter;
    ForumAdapter ForumsAdapter;

    ImageView HeaderImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forums_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences Pref = getActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        boolean IsDark = Pref.getBoolean("AppTheme", false);

        CommunityForumData = new ArrayList<>();
        FictionsForumData = new ArrayList<>();
        TipsForumData = new ArrayList<>();
        ForumsForumData = new ArrayList<>();

        ForumHomeLayout = view.findViewById(R.id.ForumHomeLayout);

        PopularThreads = view.findViewById(R.id.PopularThreads);
        CommunityForums = view.findViewById(R.id.CommunityForums);
        FictionsForums = view.findViewById(R.id.FictionsForums);
        TipsForums = view.findViewById(R.id.TipsForums);
        ForumsForums = view.findViewById(R.id.ForumsForums);

        PopularThreads.setLayoutManager(new LinearLayoutManager(getContext()));
        PopularThreads.setHasFixedSize(true);

        CommunityForums.setLayoutManager(new LinearLayoutManager(getContext()));
        CommunityForums.setHasFixedSize(true);

        FictionsForums.setLayoutManager(new LinearLayoutManager(getContext()));
        FictionsForums.setHasFixedSize(true);

        TipsForums.setLayoutManager(new LinearLayoutManager(getContext()));
        TipsForums.setHasFixedSize(true);

        ForumsForums.setLayoutManager(new LinearLayoutManager(getContext()));
        ForumsForums.setHasFixedSize(true);

        HeaderImage = view.findViewById(R.id.ForumHeader);

        SwitchTheme(IsDark);
        InitializeForums();
    }

    public void InitializeForums()
    {
        CommunityForumData.add(new ForumData("General", "", 0, 0));
        CommunityForumData.add(new ForumData("Writathon", "", 0, 0));
        CommunityForumData.add(new ForumData("Celebration", "Something great just happened to you on Royal Road and you want to share it? do it here! YAY!", 0, 0));
        CommunityForumData.add(new ForumData("Introductions", "Introduce yourself, or welcome new people. Remember to give +rep when you like a post, in order to encourage positivity.", 0, 0));
        CommunityForumData.add(new ForumData("Assistance Request", "Do you need help with something? Ask the community!", 0, 0));
        CommunityAdapter = new ForumAdapter(CommunityForumData);
        CommunityForums.setAdapter(CommunityAdapter);

        FictionsForumData.add(new ForumData("Recommendations", "Asking for recommendations? Giving out your own personal favorites list? This is the place to do it.", 0, 0));
        FictionsForumData.add(new ForumData("I forgot the title ...", "Post here if you need help finding a story you read before.", 0, 0));
        FictionsAdapter = new ForumAdapter(CommunityForumData);
        FictionsForums.setAdapter(FictionsAdapter);

        TipsForumData.add(new ForumData("Guides by the Community", "", 0, 0));
        TipsForumData.add(new ForumData("Reviewing: Tips & Discussion", "", 0, 0));
        TipsForumData.add(new ForumData("Writing: Tips & Discussions", "", 0, 0));
        TipsForumData.add(new ForumData("Marketing", "", 0, 0));
        TipsAdapter = new ForumAdapter(TipsForumData);
        TipsForums.setAdapter(TipsAdapter);

        ForumsForumData.add(new ForumData("Art", "Submit your Art; Drawings, carvings, comics, poems... as long as it is art, and you wish to show it, this is the place!", 0, 0));
        ForumsForumData.add(new ForumData("Games", "A special corner to talk about games or gaming!", 0, 0));
        ForumsForumData.add(new ForumData("Debate", "The debate forum exists to enrich the fictional writing that is inspired by real-world content. Discuss or debate serious thoughts that are connected to writing.", 0, 0));
        ForumsForumData.add(new ForumData("Software & Tutorials", "If you know about a useful software, or you can make a tutorial about something that might be useful for the rest of the community, then post it here .", 0, 0));
        ForumsAdapter = new ForumAdapter(ForumsForumData);
        ForumsForums.setAdapter(ForumsAdapter);
    }

    public void SwitchTheme(boolean DarkMode)
    {
        if(DarkMode)
        {
            getActivity().getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.DarkOuter));

            ForumHomeLayout.setBackgroundColor(getResources().getColor(R.color.DarkOuter));

            PopularThreads.setBackgroundColor(getResources().getColor(R.color.DarkInner));
            CommunityForums.setBackgroundColor(getResources().getColor(R.color.DarkInner));
            FictionsForums.setBackgroundColor(getResources().getColor(R.color.DarkInner));
            TipsForums.setBackgroundColor(getResources().getColor(R.color.DarkInner));
            ForumsForums.setBackgroundColor(getResources().getColor(R.color.DarkInner));

            HeaderImage.setBackgroundColor(getResources().getColor(R.color.forumsDark));
        }
        else
        {
            getActivity().getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.white));
            ForumHomeLayout.setBackgroundColor(getResources().getColor(R.color.LightOuter));

            PopularThreads.setBackgroundColor(getResources().getColor(R.color.white));
            CommunityForums.setBackgroundColor(getResources().getColor(R.color.white));
            FictionsForums.setBackgroundColor(getResources().getColor(R.color.white));
            TipsForums.setBackgroundColor(getResources().getColor(R.color.white));
            ForumsForums.setBackgroundColor(getResources().getColor(R.color.white));

            HeaderImage.setBackgroundColor(getResources().getColor(R.color.forumswhite));
        }
    }
}