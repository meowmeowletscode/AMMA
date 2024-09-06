package com.example.amma;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText txtUserName;
    EditText txtPassword;
    Button btnClear;
    Button btnLogin;
    TextView textViewForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        txtUserName = findViewById(R.id.txtUserName);
        txtPassword = findViewById(R.id.txtPassword);

        btnClear = findViewById(R.id.btnClear);
        btnLogin = findViewById(R.id.btnLogin);
        textViewForgotPassword = findViewById(R.id.textViewForgotPassword);

        btnClear.setOnClickListener(new BtnClearClickListener());
        btnLogin.setOnClickListener(new BtnLoginClickListener());
        textViewForgotPassword.setOnClickListener(new ForgotPasswordClickListener());
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

    private class ForgotPasswordClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            showForgotPasswordDialog();
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

    private void showForgotPasswordDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Forgot Password")
                .setMessage("Please contact support from your organization to reset your password.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
