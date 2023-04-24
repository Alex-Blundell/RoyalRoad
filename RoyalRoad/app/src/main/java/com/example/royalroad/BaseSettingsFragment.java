package com.example.royalroad;

import android.content.ContentResolver;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.provider.Settings.System;
import android.view.WindowManager.LayoutParams;

public class BaseSettingsFragment extends Fragment {

    Spinner AppThemeDropdown;
    Spinner ReadingThemeDropdown;
    Spinner AppFontDropdown;
    Spinner ReadingFontDropdown;

    private ContentResolver cResolver;
    SeekBar BrightnessSlider;
    int CurrentBrightness;
    private Window window;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        window = getActivity().getWindow();

        AppThemeDropdown = (Spinner) getView().findViewById(R.id.AppThemeDropdown);
        ReadingThemeDropdown = (Spinner) getView().findViewById(R.id.ReadingThemeDropdown);
        AppFontDropdown = (Spinner) getView().findViewById(R.id.AppFontDropdown);
        ReadingFontDropdown = (Spinner) getView().findViewById(R.id.ReadingFontDropdown);

        BrightnessSlider = (SeekBar) getView().findViewById(R.id.BrightnessSlider);

        ArrayAdapter<CharSequence> ThemeAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.ThemeArray, android.R.layout.simple_spinner_item);
        ThemeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> FontAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.FontArray, android.R.layout.simple_spinner_item);
        FontAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        AppThemeDropdown.setAdapter(ThemeAdapter);
        ReadingThemeDropdown.setAdapter(ThemeAdapter);
        AppFontDropdown.setAdapter(FontAdapter);
        ReadingFontDropdown.setAdapter(FontAdapter);

        cResolver = getActivity().getContentResolver();

        try
        {
            CurrentBrightness = System.getInt(cResolver, System.SCREEN_BRIGHTNESS);
        }
        catch (Settings.SettingNotFoundException e)
        {
            throw new RuntimeException(e);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (!Settings.System.canWrite(getActivity().getApplicationContext()))
            {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + getActivity().getPackageName()));
                startActivity(intent);
            }
        }

        BrightnessSlider.setProgress(CurrentBrightness);
        BrightnessSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(i <= 20)
                {
                    CurrentBrightness = 20;
                }
                else
                {
                    CurrentBrightness = i;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                System.putInt(cResolver, System.SCREEN_BRIGHTNESS, CurrentBrightness);

                LayoutParams layoutpars = window.getAttributes();
                layoutpars.screenBrightness = CurrentBrightness / (float)255;
                window.setAttributes(layoutpars);
            }
        });

        return inflater.inflate(R.layout.fragment_base_settings, container, false);
    }
}