package com.example.royalroad;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.checkerframework.checker.units.qual.Current;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Date;

public class ProfileFragment extends Fragment
{
    ImageView ProfileIcon;
    ImageView BlurryProfileImage;

    Button BlockBTN;
    ImageButton SendPrivateMessage;
    ImageButton ReportBTN;

    TextView ProfileName;
    TextView FollowsNum;
    TextView FavouritesNum;
    TextView ReviewsNum;
    TextView FictionsNum;
    TextView JoinedDate;
    TextView LastActiveDate;
    TextView Gender;
    TextView Location;
    TextView Website;
    TextView Bio;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        ProfileActivity CurrentActivity = (ProfileActivity)getActivity();

        BlockBTN = view.findViewById(R.id.BlockBTN);
        SendPrivateMessage = view.findViewById(R.id.SendPMBTN);
        ReportBTN = view.findViewById(R.id.ReportBTN);

        ProfileIcon = view.findViewById(R.id.ProfileIcon);
        BlurryProfileImage = view.findViewById(R.id.BlurryBackground);
        ProfileName = view.findViewById(R.id.ProfileName);

        FollowsNum = view.findViewById(R.id.FollowsNumTXT);
        FavouritesNum = view.findViewById(R.id.FavouritesNumTXT);
        ReviewsNum = view.findViewById(R.id.ReviewsNumTXT);
        FictionsNum = view.findViewById(R.id.FictionsNumTXT);

        JoinedDate = view.findViewById(R.id.JoinedDate);
        LastActiveDate = view.findViewById(R.id.LastActiveDate);
        Gender = view.findViewById(R.id.GenderTXT);
        Location = view.findViewById(R.id.LocationTXT);
        Website = view.findViewById(R.id.WebsiteTXT);
        Bio = view.findViewById(R.id.BioTXT);

        if(CurrentActivity.CurrentProfile == null)
        {
            CurrentActivity.CurrentProfile = new ProfileActivity.ProfileData();
            CurrentActivity.CurrentProfile.URL = CurrentActivity.ProfileURL;
        }

        Thread GetData = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    Document ProfileDoc = Jsoup.connect(CurrentActivity.CurrentProfile.URL).get();
                    CurrentActivity.CurrentProfile.Username = ProfileDoc.selectFirst("h1").text();
                    Elements ProfileStats = ProfileDoc.getElementsByClass("stat-value");

                    CurrentActivity.CurrentProfile.Follows = ProfileStats.get(0).text();
                    CurrentActivity.CurrentProfile.Favourites = ProfileStats.get(1).text();
                    CurrentActivity.CurrentProfile.Reviews = ProfileStats.get(2).text();
                    CurrentActivity.CurrentProfile.Fictions = ProfileStats.get(3).text();

                    CurrentActivity.CurrentProfile.ProfileIcon = ProfileDoc.getElementsByClass("avatar-container-general large").get(0).tagName("img").toString().split(" src=")[1].split("\"")[1];

                    Elements ProfileTables = ProfileDoc.getElementsByClass("portlet light");
                    for(int i = 1; i < 4; i++)
                    {
                        Elements TDElements = ProfileTables.get(i).getElementsByTag("td");

                        if(i == 1)
                        {
                            CurrentActivity.CurrentProfile.JoinedDate = TDElements.get(0).text();
                            CurrentActivity.CurrentProfile.LastActiveDate = TDElements.get(1).text();
                            CurrentActivity.CurrentProfile.Gender = TDElements.get(2).text();
                            CurrentActivity.CurrentProfile.Location = TDElements.get(3).text();

                            if(TDElements.size() > 5)
                            {
                                CurrentActivity.CurrentProfile.HasWebsite = true;

                                CurrentActivity.CurrentProfile.Website = TDElements.get(4).text();
                                CurrentActivity.CurrentProfile.Bio = TDElements.get(5).text();
                            }
                            else
                            {
                                CurrentActivity.CurrentProfile.HasWebsite = false;
                                CurrentActivity.CurrentProfile.Bio = TDElements.get(4).text();
                            }
                        }
                        else if(i == 2)
                        {

                        }
                        else if(i == 3)
                        {

                        }
                    }
                }
                catch (IOException e)
                {
                    throw new RuntimeException(e);
                }
            }
        });

        GetData.start();
        try
        {
            GetData.join();
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }

        ProfileName.setText(CurrentActivity.CurrentProfile.Username);

        Glide.with(this)
                .load(CurrentActivity.CurrentProfile.ProfileIcon)
                .into(ProfileIcon);

        Glide.with(this)
                .load(CurrentActivity.CurrentProfile.ProfileIcon)
                .into(BlurryProfileImage);

        FollowsNum.setText(CurrentActivity.CurrentProfile.Follows);
        FavouritesNum.setText(CurrentActivity.CurrentProfile.Favourites);
        ReviewsNum.setText(CurrentActivity.CurrentProfile.Reviews);
        FictionsNum.setText(CurrentActivity.CurrentProfile.Fictions);

        JoinedDate.setText(JoinedDate.getText() + CurrentActivity.CurrentProfile.JoinedDate);
        LastActiveDate.setText(LastActiveDate.getText() + CurrentActivity.CurrentProfile.LastActiveDate);
        Gender.setText(Gender.getText() + CurrentActivity.CurrentProfile.Gender);
        Location.setText(Location.getText() + CurrentActivity.CurrentProfile.Location);

        if(!CurrentActivity.CurrentProfile.HasWebsite)
        {
            Website.setVisibility(View.GONE);
        }
        else
        {
            if(Website.getVisibility() == View.GONE)
            {
                Website.setVisibility(View.VISIBLE);
            }

            Website.setText(Website.getText() + CurrentActivity.CurrentProfile.Website);
        }

        Bio.setText(Bio.getText() + CurrentActivity.CurrentProfile.Bio);
    }
}