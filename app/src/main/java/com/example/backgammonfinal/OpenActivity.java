package com.example.backgammonfinal;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class OpenActivity extends AppCompatActivity {
    // **** SQLite database
    public  DatabaseHelper dbHelper;
    private Button btnLogin,btnRegister, btnMusic, btnExit, btnScoreList;
    //SharedPreferences save user name in this phone
    private SharedPreferences sharedPreferences;
    private String savedUsername;

    private AlertDialog.Builder builder;
    Intent intent;

    private Dialog loginDialog, signinDialog,dialog;

    private EditText editDUsername, editDEmail, editDPassword;

    private TextView tvDMessage, btnNewAccount;

    private Button btnDGoBack;

    private String userName;
    androidx.appcompat.widget.Toolbar toolbar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_open);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        new Handler(Looper.getMainLooper()).postDelayed(() ->{
            Intent intent = new Intent(OpenActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, 1500);
    }

    public void createSignInDialog() {
        signinDialog = new Dialog(this);

        signinDialog.setContentView(R.layout.signin);

        signinDialog.setTitle("please register to my app");

        signinDialog.setCancelable(true);

        tvDMessage = (TextView) signinDialog.findViewById(R.id.tvSignin);
        editDUsername = (EditText) signinDialog.findViewById(R.id.edUsername);
        editDEmail = (EditText) signinDialog.findViewById(R.id.edEmail);
        editDPassword = (EditText) signinDialog.findViewById(R.id.edPassword);
        btnDGoBack = (Button) signinDialog.findViewById(R.id.btnRegister);

        btnDGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = editDUsername.getText().toString().trim();
                intent = new Intent(OpenActivity.this, MainActivity.class);
                intent.putExtra("USERNAME_KEY" , userName);

                if (editDUsername.getText().toString().isEmpty()||editDPassword.getText().toString().isEmpty()||editDEmail.getText().toString().isEmpty()){
                    builder = new AlertDialog.Builder(OpenActivity.this);
                    builder.setTitle("Error");
                    builder.setMessage("UserName, Password or Email Null");
                    builder.setPositiveButton("ok", (dialog, which) -> {
                        dialog.dismiss();
                    });
                    builder.setNegativeButton("I have account!", (dialog , which) -> {
                        dialog.dismiss();
                        signinDialog.dismiss();
                        createLoginDialog();
                    });
                    builder.show();
                }
                else
                    startActivity(intent);
            }
        });

        signinDialog.show();
    }

    public void createLoginDialog() {
        loginDialog = new Dialog(this);

        loginDialog.setContentView(R.layout.login);

        loginDialog.setTitle("please Login to my app");

        loginDialog.setCancelable(true);


        tvDMessage = (TextView) loginDialog.findViewById(R.id.tvLogin);
        btnNewAccount = (TextView) loginDialog.findViewById(R.id.btnNewAccount);
        editDUsername = (EditText) loginDialog.findViewById(R.id.edUsername);
        editDPassword = (EditText) loginDialog.findViewById(R.id.edPassword);
        btnDGoBack = (Button) loginDialog.findViewById(R.id.btnLogin);
        btnNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginDialog.dismiss();
                createSignInDialog();
            }
        });
        btnDGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userName = editDUsername.getText().toString().trim();
                intent = new Intent(OpenActivity.this, MainActivity.class);
                intent.putExtra("USERNAME_KEY" , userName);

                if (editDUsername.getText().toString().isEmpty()||editDPassword.getText().toString().isEmpty()){
                    builder = new AlertDialog.Builder(OpenActivity.this);
                    builder.setTitle("Error");
                    builder.setMessage("UserName or Password Null");
                    builder.setPositiveButton("ok", (dialog, which) -> {
                        dialog.dismiss();
                    });
                    builder.setNegativeButton("New User?", (dialog , which) -> {
                        dialog.dismiss();
                        loginDialog.dismiss();
                       createSignInDialog();
                    });
                    builder.show();
                }
                else
                    startActivity(intent);
            }
        });

        loginDialog.show();
    }
}