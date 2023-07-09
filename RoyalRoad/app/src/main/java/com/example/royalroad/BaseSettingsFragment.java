package com.example.royalroad;

import static android.content.Context.MODE_PRIVATE;

import android.accounts.Account;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.fonts.Font;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.provider.Settings.System;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class BaseSettingsFragment extends Fragment
{
    Button NotificationBTN;

    Spinner AppThemeDropdown;
    Spinner ReadingThemeDropdown;
    Spinner AppFontDropdown;
    Spinner ReadingFontDropdown;

    Button AccountInfoBTN;

    SeekBar BrightnessSlider;
    SeekBar FontSizeSlider;
    SeekBar AppFontSizeSlider;

    private Boolean IsDarkMode;
    private int FontSize;
    private int AppFontSize;

    ImageView NextBTN;
    ImageView NextNotificationsBTN;
    ArrayList<TextView> ChangeTexts = new ArrayList<>();

    View TopDivider;
    View AccountDivider;
    View NotificationDivider;
    View SettingsDivider;
    View AppSettingsDivider;

    public enum FontStyle
    {
        Ariel,
        Atkinson_Hyperlegible,
        Caslon,
        Comic_Sans,
        Franklin_Gothic,
        Garamond,
        Lucida,
        Minion,
        Open_Dyslexic,
        Open_Sans,
        Roboto,
        Sans_Serif,
        Verdanda,
        Ubuntu,
        Ubuntu_Condensed
    }

    public enum AppFontStyle
    {
        Ariel,
        Lucida,
        Open_Sans,
        Roboto
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_base_settings, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences Pref = getActivity().getSharedPreferences("Settings", MODE_PRIVATE);
        SharedPreferences.Editor PrefEditor = Pref.edit();

        IsDarkMode = Pref.getBoolean("AppTheme", false);

        NotificationBTN = view.findViewById(R.id.NotificationBTN);
        AccountInfoBTN = (Button) view.findViewById(R.id.AccountInfoBTN);

        AppThemeDropdown = (Spinner) view.findViewById(R.id.AppThemeDropdown);
        ReadingThemeDropdown = (Spinner) view.findViewById(R.id.ReadingThemeDropdown);
        AppFontDropdown = (Spinner) view.findViewById(R.id.AppFontDropdown);
        ReadingFontDropdown = (Spinner) view.findViewById(R.id.ReadingFontDropdown);

        BrightnessSlider = (SeekBar) view.findViewById(R.id.BrightnessSlider);
        FontSizeSlider = (SeekBar) view.findViewById(R.id.FontSizeSlider);
        AppFontSizeSlider = (SeekBar) view.findViewById(R.id.AppFontSizeSlider);

        NextBTN = (ImageView) view.findViewById(R.id.NextBTN);
        NextNotificationsBTN = (ImageView) view.findViewById(R.id.NextNotificationsBTN);

        ArrayAdapter<CharSequence> ThemeAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.ThemeArray, android.R.layout.simple_spinner_item);
        ThemeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> FontAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.FontArray, android.R.layout.simple_spinner_item);
        FontAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> AppFontAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.AppFontArray, android.R.layout.simple_spinner_item);
        FontAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        AppThemeDropdown.setAdapter(ThemeAdapter);
        ReadingThemeDropdown.setAdapter(ThemeAdapter);
        AppFontDropdown.setAdapter(AppFontAdapter);
        ReadingFontDropdown.setAdapter(FontAdapter);

        // Other Settings.
        ChangeTexts.add(view.findViewById(R.id.AccountInfoTXT));
        ChangeTexts.add(view.findViewById(R.id.NotificationInfoTXT));

        // Reading Settings.
        ChangeTexts.add(view.findViewById(R.id.ReadingThemeTXT));
        ChangeTexts.add(view.findViewById(R.id.FontSizeTXT));
        ChangeTexts.add(view.findViewById(R.id.ReadingFontTXT));

        // App Settings.
        ChangeTexts.add(view.findViewById(R.id.AppThemeTXT));
        ChangeTexts.add(view.findViewById(R.id.AppFontSizeTXT));
        ChangeTexts.add(view.findViewById(R.id.BrightnessTXT));
        ChangeTexts.add(view.findViewById(R.id.LanguageTXT));
        ChangeTexts.add(view.findViewById(R.id.AppFontTXT));

        AppFontSize = Pref.getInt("AppFontSize", 14);

        TopDivider = view.findViewById(R.id.TopDivider);
        AccountDivider = view.findViewById(R.id.AccountDivider);
        NotificationDivider = view.findViewById(R.id.NotificationDivider);
        SettingsDivider = view.findViewById(R.id.SettingsDivider);
        AppSettingsDivider = view.findViewById(R.id.AppSettingsDivider);

        AppFontSizeSlider.setProgress(AppFontSize - 10);

        SwitchTheme(IsDarkMode);

        int SelectedAppFont = Pref.getInt("AppFont", AppFontStyle.Open_Sans.ordinal());
        int SelectedReadingFont = Pref.getInt("ReadingFont", FontStyle.Open_Sans.ordinal());

        AppFontDropdown.setSelection(SelectedAppFont);
        ReadingFontDropdown.setSelection(SelectedReadingFont);

        for(int i = 0; i < ChangeTexts.size(); i++)
        {
            ChangeTexts.get(i).setTextSize(AppFontSize);
            Typeface tf = ResourcesCompat.getFont(getContext(), R.font.open_sans);

            switch (SelectedAppFont)
            {
                case 0:
                     tf = ResourcesCompat.getFont(getContext(), R.font.arial);
                    break;

                case 1:
                    tf = ResourcesCompat.getFont(getContext(), R.font.lucida);
                    break;

                case 2:
                    tf = ResourcesCompat.getFont(getContext(), R.font.open_sans);
                    break;

                case 3:
                    tf = ResourcesCompat.getFont(getContext(), R.font.roboto);
                    break;
            }
            ChangeTexts.get(i).setTypeface(tf);
        }

        boolean DarkReading = Pref.getBoolean("ReadingTheme", false);

        if(DarkReading)
        {
            ReadingThemeDropdown.setSelection(1);
        }
        else
        {
            ReadingThemeDropdown.setSelection(1);
        }

        AppFontSizeSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                AppFontSize = progress;
                AppFontSize += 10;

                PrefEditor.putInt("AppFontSize", AppFontSize);
                PrefEditor.apply();

                for(int i = 0; i < ChangeTexts.size(); i++)
                {
                    ChangeTexts.get(i).setTextSize(AppFontSize);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        AppThemeDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0)
                {
                    PrefEditor.putBoolean("AppTheme", false);
                    PrefEditor.apply();
                    SwitchTheme(false);

                    //((SettingsActivity)getActivity()).SwitchTheme(false);
                }
                else if(i == 1)
                {
                    PrefEditor.putBoolean("AppTheme", true);
                    PrefEditor.apply();
                    SwitchTheme(true);

                    //((SettingsActivity)getActivity()).SwitchTheme(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        AppFontDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Typeface tf = ResourcesCompat.getFont(getContext(), R.font.open_sans);

                switch (position)
                {
                    case 0:
                        tf = ResourcesCompat.getFont(getContext(), R.font.arial);
                        break;

                    case 1:
                        tf = ResourcesCompat.getFont(getContext(), R.font.lucida);
                        break;

                    case 2:
                        tf = ResourcesCompat.getFont(getContext(), R.font.open_sans);
                        break;

                    case 3:
                        tf = ResourcesCompat.getFont(getContext(), R.font.roboto);
                        break;
                }

                for(int i = 0; i < ChangeTexts.size(); i++)
                {
                    ChangeTexts.get(i).setTypeface(tf);
                }

                PrefEditor.putInt("AppFont", position);
                PrefEditor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ReadingFontDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PrefEditor.putInt("ReadingFont", position);
                PrefEditor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        AccountInfoBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                HomeActivity CurrentHome = (HomeActivity) getActivity();

                int ID = CurrentHome.HomePager.getCurrentItem();

                CurrentHome.vpAdapter.ReplaceFragment(ID, new AccountSettingFragment());
                CurrentHome.vpAdapter.notifyItemChanged(ID);

                CurrentHome.HomePager.setAdapter(CurrentHome.vpAdapter);
                CurrentHome.HomePager.setCurrentItem(ID);
            }
        });

        ReadingThemeDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0)
                {
                    PrefEditor.putBoolean("ReadingTheme", false);
                    PrefEditor.apply();
                }
                else if(i == 1)
                {
                    PrefEditor.putBoolean("ReadingTheme", true);
                    PrefEditor.apply();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        FontSize = Pref.getInt("FontSize", 14);
        FontSizeSlider.setProgress(FontSize - 10);

        FontSizeSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                FontSize = i + 10;
                PrefEditor.putInt("FontSize", FontSize);
                PrefEditor.apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        NotificationBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent();
                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
            }
        });
    }

    void SwitchTheme(@NonNull Boolean DarkMode)
    {
        if(DarkMode)
        {
            if(AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_YES)
            {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }

            AppThemeDropdown.setSelection(1);

            getActivity().getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.DarkInner));

            for(int i = 0; i < ChangeTexts.size(); i++)
            {
                ChangeTexts.get(i).setTextColor(getResources().getColor(R.color.DarkText));
            }

            NextBTN.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.DarkText)));
            NextNotificationsBTN.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.DarkText)));

            TopDivider.setBackgroundColor(getResources().getColor(R.color.DarkBorder));
            AccountDivider.setBackgroundColor(getResources().getColor(R.color.DarkBorder));
            NotificationDivider.setBackgroundColor(getResources().getColor(R.color.DarkBorder));
            SettingsDivider.setBackgroundColor(getResources().getColor(R.color.DarkBorder));
            AppSettingsDivider.setBackgroundColor(getResources().getColor(R.color.DarkBorder));
        }
        else
        {
            if(AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_NO)
            {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }

            AppThemeDropdown.setSelection(0);
            getActivity().getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.white));

            for(int i = 0; i < ChangeTexts.size(); i++)
            {
                ChangeTexts.get(i).setTextColor(getResources().getColor(R.color.black));
            }

            NextBTN.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
            NextNotificationsBTN.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
        }
    }
}