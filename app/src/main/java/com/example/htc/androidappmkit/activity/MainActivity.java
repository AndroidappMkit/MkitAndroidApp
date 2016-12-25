package com.example.htc.androidappmkit.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.htc.androidappmkit.R;
import com.example.htc.androidappmkit.helper.SQLiteHandler;
import com.example.htc.androidappmkit.helper.SessionManager;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

import java.util.HashMap;


public class MainActivity extends Activity {

    private TextView txtName;
    private TextView txtEmail;
    private Button btnLogout;

    private SQLiteHandler db;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
//        fb sdk init
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_main);

        
        txtName = (TextView) findViewById(R.id.name);
        txtEmail = (TextView) findViewById(R.id.email);
        btnLogout = (Button) findViewById(R.id.btnLogout);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();

        }

        try {
            // Fetching user details from sqlite
            HashMap<String, String> user = db.getUserDetails();

            String name = user.get("name");
            String email = user.get("email");

            // Displaying the user details on the screen
            txtName.setText(name);
            txtEmail.setText(email);

            // Logout button click event
            btnLogout.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    logoutUser();
                }
            });
        }catch (Exception e)
        {
            Log.e("Main exeption", e.toString());
        }
    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);
        db.deleteUsers();

        // fb logout
        LoginManager.getInstance().logOut();
        AccessToken.setCurrentAccessToken(null);

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


}