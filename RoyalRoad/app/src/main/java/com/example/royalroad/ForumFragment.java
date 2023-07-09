package com.example.royalroad;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class ForumFragment extends Fragment
{
    RecyclerView ActiveThreadsRV;
    RecyclerView ImportantThreadsRV;
    RecyclerView SubForumsRV;

    Toolbar ImportantThreadsToolbar;
    Toolbar SubForumsToolbar;

    ThreadAdapter ActiveThreadsAdapter;
    ThreadAdapter ImportantThreadsAdapter;
    ForumAdapter SubForumsAdapter;

    ArrayList<ForumThreadData> ActiveThreads;
    ArrayList<ForumThreadData> ImportantThreads;
    ArrayList<ForumData> SubForums;

    boolean HasSubForums = false;
    boolean HasImportantThreads = false;

    String ForumType = "";

    public ForumFragment(boolean hasSubForums, String forumType)
    {
        this.HasSubForums = hasSubForums;
        this.ForumType = forumType;
    }

    public ForumFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_forum, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        ActiveThreadsRV = view.findViewById(R.id.ActiveTopicsRV);
        ImportantThreadsRV = view.findViewById(R.id.ImportantThreadsRV);
        SubForumsRV = view.findViewById(R.id.SubForumsRV);

        ImportantThreadsToolbar = view.findViewById(R.id.ImportantThreadsToolbar);
        SubForumsToolbar = view.findViewById(R.id.SubForumsToolbar);

        ActiveThreadsRV.setLayoutManager(new LinearLayoutManager(getContext()));
        ActiveThreadsRV.setHasFixedSize(true);

        ImportantThreadsRV.setLayoutManager(new LinearLayoutManager(getContext()));
        ImportantThreadsRV.setHasFixedSize(true);

        ActiveThreads = new ArrayList<>();
        ImportantThreads = new ArrayList<>();

        ForumThreadData DebugThreadData = new ForumThreadData();

        DebugThreadData.IsHot = false;
        DebugThreadData.ThreadTitle = "Debug Thread";
        DebugThreadData.ThreadAuthor = "Debug Author";

        DebugThreadData.RepliesNumber = 0;
        DebugThreadData.ViewsNumber = 0;

        DebugThreadData.ThreadDateTime = "11/11/1996";
        DebugThreadData.ThreadLastReplyUser = "Hello";
        DebugThreadData.ThreadLastReplyDateTime = "11/11/1996";

        ActiveThreads.add(DebugThreadData);
        ActiveThreads.add(DebugThreadData);
        ActiveThreads.add(DebugThreadData);
        ActiveThreads.add(DebugThreadData);
        ActiveThreads.add(DebugThreadData);
        ActiveThreads.add(DebugThreadData);
        ActiveThreads.add(DebugThreadData);
        ActiveThreads.add(DebugThreadData);
        ActiveThreads.add(DebugThreadData);
        ActiveThreads.add(DebugThreadData);

        ActiveThreadsAdapter = new ThreadAdapter(getActivity(), ActiveThreads);
        ImportantThreadsAdapter = new ThreadAdapter(getActivity(), ActiveThreads);

        if(HasImportantThreads)
        {

        }
        else
        {
            ImportantThreadsToolbar.setVisibility(View.GONE);
            ImportantThreadsRV.setVisibility(View.GONE);
        }

        if(HasSubForums)
        {
            // Create Sub Forums Data.
            SubForumsRV.setLayoutManager(new LinearLayoutManager(getContext()));
            SubForumsRV.setHasFixedSize(true);

            SubForums = new ArrayList<>();

            if(ForumType.equals("Art"))
            {
                SubForums.add(new ForumData("Showcase", "Show the world the artwork that you made, or commissioned. CoverArt, Characters, or Maps.", 0, 0));
                SubForums.add(new ForumData("Art Guides", "Guides for CoverArts, Characters & Maps.", 0, 0));
                SubForums.add(new ForumData("Video", "", 0, 0));
                SubForums.add(new ForumData("Request / Find Art", "Here you can find multiple threads by artists offering their free artwork. You can also find a subforum for Requesting Art.", 0, 0));
                SubForums.add(new ForumData("Poetry", "", 0, 0));
            }
            else if(ForumType.equals("Reviewing: Tips & Discussion"))
            {
                SubForums.add(new ForumData("Review Swaps", "A forum for requesting review swaps.", 0, 0));
            }
            else if(ForumType.equals("Marketing"))
            {
                SubForums.add(new ForumData("Promote your Fiction", "You can discuss how to promote a story & Promote your story in the forum.", 0, 0));
            }

            SubForumsAdapter = new ForumAdapter(getActivity(), SubForums);
            SubForumsRV.setAdapter(SubForumsAdapter);
        }
        else
        {
            SubForumsToolbar.setVisibility(View.GONE);
            SubForumsRV.setVisibility(View.GONE);
        }

        ActiveThreadsRV.setAdapter(ActiveThreadsAdapter);
        ImportantThreadsRV.setAdapter(ImportantThreadsAdapter);
    }
}