package com.example.royalroad;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class RegistrationFragment extends Fragment {

    EditText UsernameField;
    EditText PasswordField;
    Button RegisterBTN;
    Button BackBTN;

    int NewUserID;
    TextView ErrorText;

    FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registration, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();
        db.collection("Users")
                .document("Users_IDs")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        NewUserID = Integer.parseInt(documentSnapshot.get("NewID").toString());
                    }
                });

        UsernameField = view.findViewById(R.id.UsernameField);
        PasswordField = view.findViewById(R.id.PasswordField);

        RegisterBTN = view.findViewById(R.id.RegisterBTN);

        ErrorText = view.findViewById(R.id.ErrorTXT);
        ErrorText.setVisibility(View.GONE);

        SharedPreferences Pref = getActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        boolean IsDarkMode = Pref.getBoolean("AppTheme", false);

        SwitchTheme(IsDarkMode);

        RegisterBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateUsername();
            }
        });

        BackBTN = view.findViewById(R.id.BackBTN);
        BackBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.LoginFragmentFrame, new LoginFragment());
                fragmentTransaction.commit();
            }
        });
    }

    public void ValidateUsername()
    {
        ErrorText.setVisibility(View.GONE);

        String Username = UsernameField.getText().toString();
        String Password = PasswordField.getText().toString();

        if(Username.isEmpty())
        {
            // Send Username Is Empty Error Message.
            Log.println(Log.INFO, "Register", String.valueOf(NewUserID));

            ErrorText.setText("Username is Empty.");
            ErrorText.setVisibility(View.VISIBLE);
        }
        else
        {
            if(Password.isEmpty())
            {
                // Send Password is Empty Error Message.
                ErrorText.setText("Password is Empty.");
                ErrorText.setVisibility(View.VISIBLE);
            }
            else
            {
                db.collection("Users")
                        .whereEqualTo("Username", Username)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful())
                                {
                                    if(task.getResult().size() > 0)
                                    {
                                        // Show Username Already Exists Error Message.
                                        UsernameField.setText("");
                                        PasswordField.setText("");

                                        Log.println(Log.INFO, "Login", "Username already Exists.");

                                        ErrorText.setText("Username already Exists.");
                                        ErrorText.setVisibility(View.VISIBLE);
                                    }
                                    else if (task.getResult().size() == 0)
                                    {
                                        Log.println(Log.INFO, "Login", "Username does not exist, Validating Password.");

                                        ValidatePassword();
                                    }
                                }
                            }
                        });
            }
        }
    }

    void ValidatePassword()
    {
        String Username = UsernameField.getText().toString();
        String Password = PasswordField.getText().toString();

        // Eventually add Special Character / Number Requirements. As well as maybe a Password Strength Checker.
        if(Password.length() > 5)
        {
            // Password is Valid.
            Map<String, Object> User = new HashMap<>();

            User.put("UserID", NewUserID);
            User.put("Username", Username);
            User.put("Password", Password); // Eventually Encrypt Password so that it is not Stored on the DB in Plain English.

            NewUserID++;

            db.collection("Users").add(User); // Should Add User to DB.
            // Update NewID
            db.collection("Users")
                    .document("Users_IDs")
                    .update("NewID", NewUserID)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                            fragmentTransaction.replace(R.id.LoginFragmentFrame, new LoginFragment());
                            fragmentTransaction.commit();
                        }
                    });
        }
        else
        {
            // Password is Too Short, show Error Message.
        }
    }

    @SuppressLint("ResourceAsColor")
    void SwitchTheme(boolean DarkTheme)
    {
        TextView UsernameTXT = getActivity().findViewById(R.id.UsernameTXTReg);
        TextView PasswordTXT = getActivity().findViewById(R.id.PasswordTXTReg);

        ShapeableImageView Background = getActivity().findViewById(R.id.BackgroundThree);

        if(DarkTheme)
        {
            ErrorText.setTextColor(getResources().getColor(R.color.white));

            Background.setBackgroundColor(getResources().getColor(R.color.DarkOuter));
            Background.setStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.DarkBorder)));

            UsernameTXT.setTextColor(getResources().getColor(R.color.white));
            PasswordTXT.setTextColor(getResources().getColor(R.color.white));

            UsernameField.setBackgroundColor(getResources().getColor(R.color.DarkInner));
            UsernameField.setTextColor(getResources().getColor(R.color.white));

            PasswordField.setBackgroundColor(getResources().getColor(R.color.DarkInner));
            PasswordField.setTextColor(getResources().getColor(R.color.white));
        }
        else
        {
            Background.setBackgroundColor(getResources().getColor(R.color.LightOuter));

            UsernameTXT.setTextColor(getResources().getColor(R.color.black));
            PasswordTXT.setTextColor(getResources().getColor(R.color.black));

            UsernameField.setBackgroundColor(getResources().getColor(R.color.white));
            UsernameField.setTextColor(getResources().getColor(R.color.black));

            PasswordField.setBackgroundColor(getResources().getColor(R.color.white));
            PasswordField.setTextColor(getResources().getColor(R.color.black));
        }
    }
}