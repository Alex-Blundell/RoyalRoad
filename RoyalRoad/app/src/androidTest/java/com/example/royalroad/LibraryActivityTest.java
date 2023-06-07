package com.example.royalroad;

import static org.junit.Assert.*;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class LibraryActivityTest {

    @Rule
    public ActivityScenarioRule<LibraryActivity> activityScenarioRule = new ActivityScenarioRule<LibraryActivity>(LibraryActivity.class);
    LibraryActivity libraryActivity;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        libraryActivity = null;
    }

    @Test
    public void TestOnlineConnection()
    {
        //boolean IsOnline = (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                //connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED);

    }

    @Test
    void TestGetItemFromDB()
    {
        DBHandler SQLiteDB = new DBHandler(libraryActivity.getApplicationContext());

        int Count = SQLiteDB.GetLibraryCount();

    }

    @Test
    public void TestConnectToCoverURL()
    {

    }

    @Test
    public void TestGetCoverImage()
    {

    }
}