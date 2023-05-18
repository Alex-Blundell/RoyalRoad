package com.example.royalroad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;

public class DiscoverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);

        SharedPreferences Pref = getSharedPreferences("Settings", MODE_PRIVATE);
        boolean IsDarkMode = Pref.getBoolean("AppTheme", false);

        SwitchThemes(IsDarkMode);
    }

    void SwitchThemes(boolean DarkMode)
    {
        if(DarkMode)
        {
            getWindow().getDecorView().setBackgroundColor(getColor(R.color.DarkOuter));
        }
        else
        {
            getWindow().getDecorView().setBackgroundColor(getColor(R.color.LightOuter));
        }
    }
}