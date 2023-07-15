package com.example.royalroad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity
{
    public static class ProfileData
    {
        public boolean HasWebsite;

        public String URL;
        public String ProfileIcon;
        public String Username;

        public String Follows;
        public String Favourites;
        public String Reviews;
        public String Fictions;

        public boolean AmFollowing;
        public boolean IsBlocked;

        public String JoinedDate;
        public String LastActiveDate;

        public String Gender;

        public String Location;
        public String Website;
        public String Bio;

        public ArrayList<Book> ProfileStories;
        public ArrayList<Book> FavouritesStories;
    }

    ProfileData CurrentProfile;
    ViewPager2 ProfilePager;
    TabLayout ProfileTabs;
    VPAdapter vpAdapter;

    String BaseURL = "https://www.royalroad.com/profile/";

    public String ProfileURL;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent ThisIntent = getIntent();
        int ProfileID = ThisIntent.getIntExtra("ProfileID", 0);

        ProfileURL = BaseURL + ProfileID;

        ProfilePager = findViewById(R.id.ProfilePager);
        ProfileTabs = findViewById(R.id.ProfileTabs);

        vpAdapter = new VPAdapter(getSupportFragmentManager(), getLifecycle());

        vpAdapter.AddFragment(new ProfileFragment());
        vpAdapter.AddFragment(new ProfileStoriesFragment());
        vpAdapter.AddFragment(new ProfileFavouritesFragment());
        vpAdapter.AddFragment(new ProfileFollowsFragment());
        vpAdapter.AddFragment(new ProfileForumsFragment());

        ProfilePager.setAdapter(vpAdapter);
        ProfilePager.setOffscreenPageLimit(5);
        ProfilePager.setCurrentItem(0);

        ProfilePager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback()
        {
            @Override
            public void onPageSelected(int position)
            {
                ProfileTabs.selectTab(ProfileTabs.getTabAt(position));
            }
        });

        ProfileTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                ProfilePager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab)
            {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab)
            {

            }
        });
    }
}