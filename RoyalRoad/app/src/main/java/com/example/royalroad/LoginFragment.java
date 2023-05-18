package com.example.royalroad;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

import static java.lang.Thread.sleep;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.TintInfo;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

public class LoginFragment extends Fragment {
    EditText UsernameField;
    EditText PasswordField;

    Button LoginBTN;
    Button RegisterBTN;
    Button SkipLoginBTN;

    FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences Pref = getActivity().getSharedPreferences("Settings", MODE_PRIVATE);
        SharedPreferences.Editor PrefEditor = Pref.edit();

        boolean LoggedIn = Pref.getBoolean("IsLoggedIn", false);
        boolean HasSkipped = false;

        if(!LoggedIn)
        {
            HasSkipped = Pref.getBoolean("HasSkipped", false);
        }

        if(LoggedIn || HasSkipped)
        {
            Intent ThisIntent = new Intent(getActivity(), HomeActivity.class);
            startActivity(ThisIntent);
            getActivity().finish();
        }

        db = FirebaseFirestore.getInstance();

        UsernameField = view.findViewById(R.id.UsernameField);
        PasswordField = view.findViewById(R.id.PasswordField);

        LoginBTN = view.findViewById(R.id.LoginBTN);
        RegisterBTN = view.findViewById(R.id.RegisterBTN);
        SkipLoginBTN = view.findViewById(R.id.SkipLoginBTN);

        LoginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateUsername();
            }
        });

        RegisterBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterUser();
            }
        });

        SkipLoginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefEditor.putBoolean("HasSkipped", true);
                PrefEditor.apply();

                Intent SkipIntent = new Intent(getActivity(), HomeActivity.class);
                startActivity(SkipIntent);
                getActivity().finish();
            }
        });

        boolean IsDarkMode = Pref.getBoolean("AppTheme", false);
        SwitchTheme(IsDarkMode);
    }

    // For Richa:
    // Possible Usernames and Password combinations are:
    // Username | Password
    // TestOne  | TestOne
    // Hello    | Hello
    // Admin    | Admin

    public void ValidateUsername()
    {
        if(UsernameField.getText().toString().isEmpty())
        {
            // Show Username Is Empty Error.
            Log.println(Log.INFO, "Login", "Username Field is Empty.");
        }
        else
        {
            if(PasswordField.getText().toString().isEmpty())
            {
                // Show Password is Empty Error
                Log.println(Log.INFO, "Login", "Password Field is Empty.");
            }
            else
            {
                String Username = UsernameField.getText().toString();

                db.collection("Users")
                        .whereEqualTo("Username", Username)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful())
                                {
                                    if(task.getResult().size() == 1)
                                    {
                                        ValidatePassword();
                                        Log.println(Log.INFO, "Login", "Username Exists, Checking Password.");
                                    }
                                    else if (task.getResult().size() == 0)
                                    {
                                        // Show Incorrect Username Error Message.
                                        PasswordField.setText("");
                                        Log.println(Log.INFO, "Login", "Username was Incorrect, try again.");
                                    }
                                }
                            }
                        });

            }
        }
    }

    void ValidatePassword()
    {
        String Password = PasswordField.getText().toString();

        db.collection("Users")
                .whereEqualTo("Password", Password)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            SharedPreferences Pref = getActivity().getSharedPreferences("Settings", MODE_PRIVATE);
                            SharedPreferences.Editor PrefEditor = Pref.edit();

                            if(task.getResult().size() == 1)
                            {
                                int UserID = Integer.parseInt(task.getResult().getDocuments().get(0).get("UserID").toString());
                                String AvatarURL = task.getResult().getDocuments().get(0).get("AvatarURL").toString();

                                Log.println(Log.INFO, "Login", "Password was correct, Transitioning to Home Activity.");

                                PrefEditor.putBoolean("IsLoggedIn", true);
                                PrefEditor.putInt("UserID", UserID);
                                PrefEditor.putString("UserAvatarURL", AvatarURL);
                                PrefEditor.putString("Username", UsernameField.getText().toString());

                                PrefEditor.apply();

                                Intent ThisIntent = new Intent(getActivity(), HomeActivity.class);
                                startActivity(ThisIntent);
                                getActivity().finish();
                            }
                            else if(task.getResult().size() == 0)
                            {
                                // Show Incorrect Password Error Message.
                                Log.println(Log.INFO, "Login", "Password was Incorrect.");
                            }
                        }
                    }
                });
    }

    public void RegisterUser()
    {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.LoginFragmentFrame, new RegistrationFragment());
        fragmentTransaction.commit();
    }

    @SuppressLint("ResourceAsColor")
    void SwitchTheme(boolean DarkTheme)
    {
        TextView UsernameTXT = getActivity().findViewById(R.id.UsernameTXT);
        TextView PasswordTXT = getActivity().findViewById(R.id.PasswordTXT);

        ShapeableImageView Background = getActivity().findViewById(R.id.BackgroundTwo);

        if(DarkTheme)
        {
            Background.setBackgroundColor(getResources().getColor(R.color.DarkOuter));
            Background.setStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.DarkBorder)));

            UsernameField.setBackgroundColor(getResources().getColor(R.color.DarkInner));
            UsernameField.setTextColor(getResources().getColor(R.color.white));

            PasswordField.setBackgroundColor(getResources().getColor(R.color.DarkInner));
            PasswordField.setTextColor(getResources().getColor(R.color.white));

            UsernameTXT.setTextColor(getResources().getColor(R.color.white));
            PasswordTXT.setTextColor(getResources().getColor(R.color.white));
        }
        else
        {
            Background.setBackgroundColor(getResources().getColor(R.color.LightOuter));

            UsernameField.setBackgroundColor(getResources().getColor(R.color.white));
            UsernameField.setTextColor(getResources().getColor(R.color.black));

            PasswordField.setBackgroundColor(getResources().getColor(R.color.white));
            PasswordField.setTextColor(getResources().getColor(R.color.black));

            UsernameTXT.setTextColor(getResources().getColor(R.color.black));
            PasswordTXT.setTextColor(getResources().getColor(R.color.black));

            Background.setStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.DarkBorder)));
        }
    }
}