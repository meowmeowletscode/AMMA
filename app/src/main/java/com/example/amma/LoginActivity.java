package com.example.amma;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.ResultSet;
import java.sql.Statement;


public class LoginActivity extends AppCompatActivity {

    java.sql.Connection connection;
    EditText txtUserName;
    EditText txtPassword;
    Button btnClear;
    Button btnLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        txtUserName = findViewById(R.id.txtUserName);
        txtPassword = findViewById(R.id.txtPassword);

        btnClear = findViewById(R.id.btnClear);
        btnLogin = findViewById(R.id.btnLogin);

        btnClear.setOnClickListener(new BtnClearClickListener());
        btnLogin.setOnClickListener(new BtnLoginClickListener());

    }

    private class BtnClearClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            txtUserName.setText("");
            txtPassword.setText("");
        }
    }

    private class BtnLoginClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            LogIn();
        }
    }

    private void LogIn() {
        String userName = txtUserName.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();

        UserSQL userSQL = new UserSQL();

        if (userSQL.isValidUser(userName)) {
            String storedPassword = userSQL.getStoredPassword(userName);

            if (storedPassword != null && password.equals(storedPassword)) {
                String role = userSQL.getUserRole(userName);
                User currentUser = new User(userName, password, role);
                UserManager.getInstance().setCurrentUser(currentUser);

                // Get the device ID
                String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

                // Update Last Login and insert UserLoginHistory with Device ID
                userSQL.updateLastLogin(userName);
                userSQL.insertUserLoginHistory(userName, deviceId);

                Toast.makeText(LoginActivity.this, "Login Successful.", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
            } else {
                Toast.makeText(LoginActivity.this, "The password is incorrect. Please check and try again.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(LoginActivity.this, "User " + userName + " not found. Please check and try again.", Toast.LENGTH_SHORT).show();
        }
    }


}
