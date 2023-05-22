package com.example.royalroad;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.google.android.gms.common.data.DataBufferUtils.hasData;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.*;

import android.view.View;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class HomeActivityTest {

    @Rule
    public ActivityScenarioRule<HomeActivity> activityScenarioRule = new ActivityScenarioRule<HomeActivity>(HomeActivity.class);
    HomeActivity homeActivity;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception
    {
        homeActivity = null;
    }

    @Test
    public void TestOpenLibrary()
    {
        onView(withId(R.id.LibraryBTN)).perform();
    }

    @Test
    public void TestOpenDiscovery()
    {
        onView(withId(R.id.DiscoverBTN)).perform();
    }

    @Test
    public void TestOpenWrite()
    {
        onView(withId(R.id.WriteBTN)).perform();
    }

    @Test
    public void TestOpenForums()
    {
        onView(withId(R.id.ForumsBTN)).perform();
    }

    @Test
    public void TestOpenFriends()
    {
        onView(withId(R.id.FriendsBTN)).perform();
    }

    @Test
    public void TestOpenSettings()
    {
        onView(withId(R.id.SettingsBTN)).perform();
    }

    @Test
    public void TestOpenLogout()
    {
        onView(withId(R.id.LoginBTN)).perform();
    }
}