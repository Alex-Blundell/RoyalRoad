package com.example.royalroad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

public class ForumsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forums);

        ReplaceFragment(new ForumsHomeFragment());

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean IsOnline = (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED);

        if(!IsOnline)
        {
            Toast.makeText(this, "Connect to internet for Forums.", Toast.LENGTH_SHORT).show();
        }
    }

    public void ReplaceFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.ForumsFragmentFrame, fragment);
        fragmentTransaction.commit();
    }

    public void BackBTN(int ID)
    {
        switch(ID)
        {
            case 0:
                // Return to Home Activity.
                finish();
                break;

            case 1:
                // Return to Forum Home Fragment.
                ReplaceFragment(new ForumsHomeFragment());
                break;

            case 2:
                // Return from Thread;
                ReplaceFragment(new ForumFragment(false, ""));
                break;

            case 3:
                // Return from SubForum.
                ReplaceFragment(new ForumFragment(false, ""));
                break;
        }
    }
}