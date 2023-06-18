package com.example.royalroad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.provider.Settings.System;

import android.os.Bundle;
import android.util.Log;
import android.view.PointerIcon;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.view.WindowManager.LayoutParams;

import com.google.android.material.button.MaterialButton;

public class SettingsActivity extends AppCompatActivity {

    boolean IsDarkMode;
    public Boolean IsFirstFragment;
    private FrameLayout FragmentLayout;
    MaterialButton BackBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        IsFirstFragment = true;

        SharedPreferences Pref = getSharedPreferences("Settings", MODE_PRIVATE);

        IsDarkMode = Pref.getBoolean("AppTheme", false);

        BackBTN = findViewById(R.id.BackBTN);
        FragmentLayout = findViewById(R.id.FragmentLayout);

        SwitchTheme(IsDarkMode);

        ReplaceFragment(new BaseSettingsFragment());
    }

    public void BackBTN(View view)
    {
        if(IsFirstFragment)
        {
            finish();
        }
        else
        {
            ReplaceFragment(new BaseSettingsFragment());
        }
    }

    public void ReplaceFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.FragmentLayout, fragment);
        fragmentTransaction.commit();
    }

    public void SwitchTheme(boolean DarkMode)
    {
        if(DarkMode)
        {
            getWindow().getDecorView().setBackgroundColor(getColor(R.color.DarkOuter));
            BackBTN.setIconTint(ColorStateList.valueOf(getColor(R.color.DarkText)));
            FragmentLayout.setBackgroundColor(getColor(R.color.DarkOuter));
        }
        else
        {
            getWindow().getDecorView().setBackgroundColor(getColor(R.color.white));
            BackBTN.setIconTint(ColorStateList.valueOf(getColor(R.color.black)));
            FragmentLayout.setBackgroundColor(getColor(R.color.white));
        }
    }
}