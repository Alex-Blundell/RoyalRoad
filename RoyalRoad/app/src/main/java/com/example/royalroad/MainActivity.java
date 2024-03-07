package com.example.royalroad;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.TintInfo;
import androidx.fragment.app.Fragment;
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
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {

    TextView EmailTXT;
    TextView PasswordTXT;
    TextView TwoFactorTXT;

    EditText EmailField;
    EditText PasswordField;
    EditText TwoFactorField;

    Button LoginBTN;
    Button RegisterBTN;
    Button SubmitBTN;

    String Email;
    String Password;
    String TwoFactor;

    RelativeLayout LoginView;
    RelativeLayout TwoFactorView;

    WebView LoginWebView;

    ShapeableImageView Background;

    String BaseURL = "https://www.royalroad.com/account/login";
    String TwoFactorURL = "https://www.royalroad.com/account/loginwith2fa?RememberMe=True";

    String LoginJScript;
    String TwoFactorJScript;

    boolean LoadLoginJScript;
    boolean LoadTwoFactorJScript;

    boolean IsDarkMode;

    String Username;
    String AvatarURL;
    int UserID;

    boolean GotUsername = false;
    boolean GotUserID = false;
    boolean GotAvatarURL = false;

    SharedPreferences Pref;
    SharedPreferences.Editor PrefEditor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Pref = getSharedPreferences("Settings", MODE_PRIVATE);
        PrefEditor = Pref.edit();

        boolean LoggedIn = Pref.getBoolean("IsLoggedIn", false);

        if(LoggedIn)
        {
            Intent ThisIntent = new Intent(MainActivity.this, HomeActivity.class);

            finish();
            startActivity(ThisIntent);
        }

        EmailTXT = findViewById(R.id.EmailTXT);
        PasswordTXT = findViewById(R.id.PasswordTXT);
        TwoFactorTXT = findViewById(R.id.TwoFactorTXT);

        EmailField = findViewById(R.id.EmailField);
        PasswordField = findViewById(R.id.PasswordField);
        TwoFactorField = findViewById(R.id.TwoFactorField);

        LoginBTN = findViewById(R.id.LoginBTN);
        RegisterBTN = findViewById(R.id.RegisterBTN);

        SubmitBTN = findViewById(R.id.SubmitBTN);

        LoginWebView = findViewById(R.id.TestWebView);
        LoginWebView.setVisibility(View.GONE);

        LoginView = findViewById(R.id.LoginView);
        TwoFactorView = findViewById(R.id.TwoFactorView);
        TwoFactorView.setVisibility(View.GONE);

        LoadLoginJScript = false;
        LoadTwoFactorJScript = false;

        Background = findViewById(R.id.BackgroundTwo);

        LoginWebView.addJavascriptInterface(this, "Android");
        LoginWebView.getSettings().setJavaScriptEnabled(true);

        LoginBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                try
                {
                    SubmitLogin();
                }
                catch (UnsupportedEncodingException e)
                {
                    throw new RuntimeException(e);
                }
            }
        });

        SubmitBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                try
                {
                    TwoFactorLogin();
                }
                catch (UnsupportedEncodingException e)
                {
                    throw new RuntimeException(e);
                }
            }
        });

        LoginWebView.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageFinished(WebView view, String url)
            {
                super.onPageFinished(view, url);

                Log.println(Log.INFO, "Hi", url);

                if(LoadLoginJScript)
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    {
                        LoginWebView.evaluateJavascript(LoginJScript, new ValueCallback< String >() {
                            @Override
                            public void onReceiveValue(String value)
                            {
                                //JS injected
                            }
                        });
                    }
                    else
                    {
                        LoginWebView.loadUrl(LoginJScript);
                    }
                }

                if(LoadTwoFactorJScript)
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    {
                        LoginWebView.evaluateJavascript(TwoFactorJScript, new ValueCallback< String >() {
                            @Override
                            public void onReceiveValue(String value)
                            {
                                //JS injected
                            }
                        });
                    }
                    else
                    {
                        LoginWebView.loadUrl(TwoFactorJScript);
                    }
                }

                if(url.contains("loginwith2fa"))
                {
                    if(!LoadTwoFactorJScript)
                    {
                        LoadLoginJScript = false;
                        LoginWebView.setVisibility(View.GONE);

                        LoginView.setVisibility(View.GONE);
                        TwoFactorView.setVisibility(View.VISIBLE);
                    }
                }

                if(url.equals("https://www.royalroad.com/home"))
                {
                    String HomeJScript = "javascript: (function() { " +
                                         "   var Username = document.getElementsByClassName(\"username\").item(0).textContent;" +
                                         "   var ID = document.getElementsByClassName(\"dropdown-user\").item(0).children.item(1).children.item(0).children.item(0).getAttribute(\"href\");" +
                                         "   var AvatarURL = document.getElementsByClassName(\"dropdown-user\").item(0).children.item(0).children.item(0).getAttribute(\"src\");" +
                                         "   Android.SendUsername(Username);" +
                                         "   Android.SendID(ID);" +
                                         "   Android.SendAvatarURL(AvatarURL);" +
                                         "})" +
                                         "()";

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    {
                        LoginWebView.evaluateJavascript(HomeJScript, new ValueCallback< String >() {
                            @Override
                            public void onReceiveValue(String value)
                            {
                                //JS injected
                            }
                        });
                    }
                    else
                    {
                        LoginWebView.loadUrl(HomeJScript);
                    }

                    //finish();
                }
            }
        });

        IsDarkMode = Pref.getBoolean("AppTheme", false);
        SwitchThemes(IsDarkMode);
    }


    public void SubmitLogin() throws UnsupportedEncodingException
    {
        if(!EmailField.getText().toString().isEmpty())
        {
            if(!PasswordField.getText().toString().isEmpty())
            {
                LoadLoginJScript = true;

                Email = EmailField.getText().toString();
                Password = PasswordField.getText().toString();

                LoginWebView.setVisibility(View.VISIBLE);

                LoginJScript = "javascript: (function() { " +
                        "document.getElementById(\"email\").value = '" + Email + "';" +
                        "document.getElementById(\"password\").value = '" + Password + "';" +
                        "document.getElementById(\"remember\").checked = true;" +
                        "document.forms[1].submit();" +
                        "})()";

                LoginWebView.loadUrl(BaseURL);
            }
        }
    }

    public void TwoFactorLogin() throws UnsupportedEncodingException
    {
        LoadTwoFactorJScript = true;
        LoginWebView.setVisibility(View.VISIBLE);

        TwoFactor = TwoFactorField.getText().toString();

        TwoFactorJScript = "javascript: (function() { " +
                "document.getElementById(\"TwoFactorCode\").value = '" + TwoFactor + "';" +
                "document.getElementById(\"RememberMachine\").checked = true;" +
                "document.forms[0].submit();" +
                "})()";

        LoginWebView.loadUrl(TwoFactorURL);
    }

    @JavascriptInterface
    public void SendUsername(String Username)
    {
        this.Username = Username;

        Log.println(Log.INFO, "Hi", "Username: " + this.Username);

        GotUsername = true;
        FinishActivity();
    }

    @JavascriptInterface
    public void SendID(String ID)
    {
        String UserID = ID.split("/")[2];
        this.UserID = Integer.parseInt(UserID);

        Log.println(Log.INFO, "Hi", "User ID: " + this.UserID);

        GotUserID = true;
        FinishActivity();
    }

    @JavascriptInterface
    public void SendAvatarURL(String URL)
    {
        this.AvatarURL = URL;

        Log.println(Log.INFO, "Hi", "Avatar URL: " + this.AvatarURL);

        GotAvatarURL = true;
        FinishActivity();
    }

    void FinishActivity()
    {
        if(GotUsername && GotUserID && GotAvatarURL)
        {
            PrefEditor.putBoolean("IsLoggedIn", true);
            PrefEditor.putInt("UserID", this.UserID);
            PrefEditor.putString("Username", this.Username);
            PrefEditor.putString("UserAvatarURL", this.AvatarURL);

            PrefEditor.apply();

            Intent ThisIntent = new Intent(MainActivity.this, HomeActivity.class);

            finish();
            startActivity(ThisIntent);
        }
    }

    @SuppressLint("ResourceAsColor")
    public void SwitchThemes(boolean DarkMode)
    {
        if(DarkMode)
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

            EmailTXT.setTextColor(getColor(R.color.DarkText));
            PasswordTXT.setTextColor(getResources().getColor(R.color.DarkText));
            TwoFactorTXT.setTextColor(getColor(R.color.DarkText));

            Background.setBackgroundColor(getResources().getColor(R.color.DarkOuter));
            Background.setStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.DarkBorder)));

            EmailField.setBackgroundColor(getResources().getColor(R.color.DarkInner));
            EmailField.setTextColor(getResources().getColor(R.color.DarkText));

            PasswordField.setBackgroundColor(getResources().getColor(R.color.DarkInner));
            PasswordField.setTextColor(getResources().getColor(R.color.DarkText));

            TwoFactorField.setBackgroundColor(getResources().getColor(R.color.DarkInner));
            TwoFactorField.setTextColor(getResources().getColor(R.color.DarkText));
        }
        else
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

            Background.setBackgroundColor(getResources().getColor(R.color.LightOuter));

            EmailTXT.setTextColor(getResources().getColor(R.color.black));
            PasswordTXT.setTextColor(getResources().getColor(R.color.black));
            TwoFactorTXT.setTextColor(getColor(R.color.black));

            EmailField.setBackgroundColor(getResources().getColor(R.color.white));
            EmailField.setTextColor(getResources().getColor(R.color.black));

            PasswordField.setBackgroundColor(getResources().getColor(R.color.white));
            PasswordField.setTextColor(getResources().getColor(R.color.black));

            TwoFactorField.setBackgroundColor(getResources().getColor(R.color.white));
            TwoFactorField.setTextColor(getResources().getColor(R.color.black));
        }
    }
}