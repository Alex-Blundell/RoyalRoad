package com.example.royalroad;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class ForumsHomeFragment extends Fragment {

    RelativeLayout ForumHomeLayout;
    ListView PopularThreads;
    ListView CommunityForums;
    ListView FictionsForums;
    ListView TipsForums;
    ListView ForumsForums;

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

        ForumHomeLayout = view.findViewById(R.id.ForumHomeLayout);

        PopularThreads = view.findViewById(R.id.PopularThreads);
        CommunityForums = view.findViewById(R.id.CommunityForums);
        FictionsForums = view.findViewById(R.id.FictionsForums);
        TipsForums = view.findViewById(R.id.TipsForums);
        ForumsForums = view.findViewById(R.id.ForumsForums);

        SwitchTheme(IsDark);
    }

    public void SwitchTheme(boolean DarkMode)
    {
        if(DarkMode)
        {
            ForumHomeLayout.setBackgroundColor(getResources().getColor(R.color.DarkOuter));

            PopularThreads.setBackgroundColor(getResources().getColor(R.color.DarkInner));
            CommunityForums.setBackgroundColor(getResources().getColor(R.color.DarkInner));
            FictionsForums.setBackgroundColor(getResources().getColor(R.color.DarkInner));
            TipsForums.setBackgroundColor(getResources().getColor(R.color.DarkInner));
            ForumsForums.setBackgroundColor(getResources().getColor(R.color.DarkInner));

            
        }
        else
        {
            ForumHomeLayout.setBackgroundColor(getResources().getColor(R.color.LightOuter));

            PopularThreads.setBackgroundColor(getResources().getColor(R.color.white));
            CommunityForums.setBackgroundColor(getResources().getColor(R.color.white));
            FictionsForums.setBackgroundColor(getResources().getColor(R.color.white));
            TipsForums.setBackgroundColor(getResources().getColor(R.color.white));
            ForumsForums.setBackgroundColor(getResources().getColor(R.color.white));
        }
    }
}