package com.example.royalroad;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

import android.os.Bundle;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class LoginFragmentTest {

    @Rule
    //public FragmentTestRule<?, LoginFragment> fragmentTestRule = FragmentTestRule.create(LoginFragment.class);
            // Just could not get this to work. How do I Test individual Fragments?
    LoginFragment loginFragment;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        loginFragment = null;
    }

    @Test
    public void TestCorrectUsernamePassword()
    {
        String TestUsername = "Admin";
        String TestPassword = "Admin";

        onView(withId(R.id.UsernameField)).perform(typeText(TestUsername));
        onView(withId(R.id.UsernameField)).perform(typeText(TestPassword));
        closeSoftKeyboard();

        onView(withId(R.id.LoginBTN)).perform();
    }

    @Test
    public void TestIncorrectUsername()
    {
        String TestUsername = "Adminzzzzzz";
        String TestPassword = "Admin";

        onView(withId(R.id.UsernameField)).perform(typeText(TestUsername));
        onView(withId(R.id.UsernameField)).perform(typeText(TestPassword));
        closeSoftKeyboard();

        onView(withId(R.id.LoginBTN)).perform();
    }

    @Test
    public void TestIncorrectPassword()
    {
        String TestUsername = "Admin";
        String TestPassword = "Adminzzzzz";

        onView(withId(R.id.UsernameField)).perform(typeText(TestUsername));
        onView(withId(R.id.UsernameField)).perform(typeText(TestPassword));
        closeSoftKeyboard();

        onView(withId(R.id.LoginBTN)).perform();
    }

    @Test
    public void TestEmptyUsername()
    {
        String TestUsername = "";
        String TestPassword = "Adminzzzzz";

        onView(withId(R.id.UsernameField)).perform(typeText(TestUsername));
        onView(withId(R.id.UsernameField)).perform(typeText(TestPassword));
        closeSoftKeyboard();

        onView(withId(R.id.LoginBTN)).perform();
    }

    @Test
    public void TestEmptyPassword()
    {
        String TestUsername = "Admin";
        String TestPassword = "";

        onView(withId(R.id.UsernameField)).perform(typeText(TestUsername));
        onView(withId(R.id.UsernameField)).perform(typeText(TestPassword));
        closeSoftKeyboard();

        onView(withId(R.id.LoginBTN)).perform();

        //if(onView(withId(R.id.ErrorTXT).matches(withText(""))))
        //{
        //
        //}
    }
}