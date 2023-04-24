package com.example.royalroad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    private String DebugUsername = "Admin";
    private String DebugPassword = "Admin";

    EditText UsernameField;
    EditText PasswordField;

    Button LoginBTN;
    Button RegisterBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        UsernameField = findViewById(R.id.UsernameField);
        PasswordField = findViewById(R.id.PasswordField);

        LoginBTN = findViewById(R.id.LoginBTN);
        RegisterBTN = findViewById(R.id.RegisterBTN);

        LoginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckLogin();
            }
        });

        RegisterBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterUser();
            }
        });
    }

    public void CheckLogin()
    {
        String Username = UsernameField.getText().toString();
        String Password = PasswordField.getText().toString();

        if(Username.equals(DebugUsername))
        {
            if(Password.equals(DebugPassword))
            {
                Intent ThisIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(ThisIntent);
            }
        }
    }

    public void RegisterUser()
    {

    }
}