package com.example.royalroad;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button LoginBTN;
    Button DiscoverBTN;
    Button LibraryBTN;
    Button WriteBTN;
    Button ForumsBTN;
    Button FriendsBTN;
    Button SettingsBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Check if IsLoggedIn, if IsLoggedIn then Change LoginBTN Text to Logout.
        LoginBTN = findViewById(R.id.LoginBTN);

        DiscoverBTN = findViewById(R.id.DiscoverBTN);
        LibraryBTN = findViewById(R.id.LibraryBTN);
        WriteBTN = findViewById(R.id.WriteBTN);
        ForumsBTN = findViewById(R.id.ForumsBTN);
        FriendsBTN = findViewById(R.id.FriendsBTN);
        SettingsBTN = findViewById(R.id.SettingsBTN);

        LoginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // If IsLoggedIn then On Click should Log you out instead.
                // If Not IsLoggedIn Then:
                Intent LoginIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(LoginIntent);
            }
        });

        DiscoverBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent DiscoverIntent = new Intent(MainActivity.this, DiscoverActivity.class);
                startActivity(DiscoverIntent);
            }
        });

        LibraryBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent LibraryIntent = new Intent(MainActivity.this, LibraryActivity.class);
                startActivity(LibraryIntent);
            }
        });

        WriteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent WriteIntent = new Intent(MainActivity.this, WriteActivity.class);
                startActivity(WriteIntent);
            }
        });

        ForumsBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ForumsIntent = new Intent(MainActivity.this, ForumsActivity.class);
                startActivity(ForumsIntent);
            }
        });

        FriendsBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent FriendsIntent = new Intent(MainActivity.this, FriendsActivity.class);
                startActivity(FriendsIntent);
            }
        });

        SettingsBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent SettingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(SettingsIntent);
            }
        });
    }
}