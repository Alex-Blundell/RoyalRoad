package com.example.royalroad;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

import static java.lang.Thread.sleep;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
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
import android.widget.Toast;

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

    FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences Pref = getActivity().getSharedPreferences("Settings", MODE_PRIVATE);
        SharedPreferences.Editor PrefEditor = Pref.edit();

        boolean LoggedIn = Pref.getBoolean("IsLoggedIn", false);

        if(LoggedIn)
        {
            Intent ThisIntent = new Intent(getActivity(), HomeActivity.class);

            getActivity().finish();
            startActivity(ThisIntent);
        }

        db = FirebaseFirestore.getInstance();

        UsernameField = view.findViewById(R.id.UsernameField);
        PasswordField = view.findViewById(R.id.PasswordField);

        LoginBTN = view.findViewById(R.id.LoginBTN);
        RegisterBTN = view.findViewById(R.id.RegisterBTN);

        LoginBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ValidateUsername();
            }
        });

        RegisterBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                RegisterUser();
            }
        });

        boolean IsDarkMode = Pref.getBoolean("AppTheme", false);
        SwitchTheme(IsDarkMode);
    }

    public void ValidateUsername()
    {
        if(UsernameField.getText().toString().isEmpty())
        {
            Toast.makeText(getContext(), "Username is Empty", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if(PasswordField.getText().toString().isEmpty())
            {
                Toast.makeText(getContext(), "Password is Empty", Toast.LENGTH_SHORT).show();
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
                                    }
                                    else if (task.getResult().size() == 0)
                                    {
                                        // Show Incorrect Username Error Message.
                                        PasswordField.setText("");

                                        Toast.makeText(getContext(), "Username was Incorrect, try again.", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(getContext(), "Password was Incorrect, try again.", Toast.LENGTH_SHORT).show();
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
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

            Background.setBackgroundColor(getResources().getColor(R.color.DarkOuter));
            Background.setStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.DarkBorder)));

            UsernameField.setBackgroundColor(getResources().getColor(R.color.DarkInner));
            UsernameField.setTextColor(getResources().getColor(R.color.DarkText));

            PasswordField.setBackgroundColor(getResources().getColor(R.color.DarkInner));
            PasswordField.setTextColor(getResources().getColor(R.color.DarkText));

            UsernameTXT.setTextColor(getResources().getColor(R.color.DarkText));
            PasswordTXT.setTextColor(getResources().getColor(R.color.DarkText));
        }
        else
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

            Background.setBackgroundColor(getResources().getColor(R.color.LightOuter));

            UsernameField.setBackgroundColor(getResources().getColor(R.color.white));
            UsernameField.setTextColor(getResources().getColor(R.color.black));

            PasswordField.setBackgroundColor(getResources().getColor(R.color.white));
            PasswordField.setTextColor(getResources().getColor(R.color.black));

            UsernameTXT.setTextColor(getResources().getColor(R.color.black));
            PasswordTXT.setTextColor(getResources().getColor(R.color.black));
        }
    }
}