package com.example.backgammonfinal;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //toolbar
    androidx.appcompat.widget.Toolbar toolbar;
    Intent intent;
    // for login / register players card
    private Dialog dialog;
    private EditText editdUsername, editDEmail, editDPassword;
    private Button btnDlogin,btnDRegister;
    private TextView tvUserName,tvDMessage,btnNewAccount;
    // broadcast reciver
    private boolean isFirstTime;
    // **** SQLite database
    public  DatabaseHelper dbHelper;
    private Button btnLogin,btnRegister, btnMusic, btnExit, btnScoreList;
    //SharedPreferences save user name in this phone
    private SharedPreferences sharedPreferences;
    private String savedUsername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        this.isFirstTime = true;
// **** SQLite database
        dbHelper = new DatabaseHelper(this);
//toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//battery check
// Retrieve the username from SharedPreferences
        this.sharedPreferences = getSharedPreferences("MyAppPrefs",
                Context.MODE_PRIVATE);
        this.savedUsername = sharedPreferences.getString("USERNAME", "DefaultUser");  // "DefaultUser" is a fallback value
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        if (this.savedUsername.equals("DefaultUser")) {
            tvUserName.setText("");
            this.btnLogin.setText("Login ");
        }
        else {
            tvUserName.setText(" Wellcome " + this.savedUsername + "! ");
            this.btnRegister.setText("Sign in");
        }
        btnMusic = (Button) findViewById(R.id.btnMusic);
        btnMusic.setOnClickListener(this);
        btnScoreList = (Button) findViewById(R.id.btnScoreList);
        btnScoreList.setOnClickListener(this);
        btnExit = (Button) findViewById(R.id.btnExit);
        btnExit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        if (v.getId() == btnRegister.getId()) {
            createRegistrationDialog();
        }
        if (v.getId() == btnLogin.getId()) {
            createLoginDialog();
        }
//        if (v.getId() == btnScoreList.getId()) {
//            intent = new Intent(this, LeaderBoard.class);
//            startActivity(intent);
//        }
//        if (v.getId() == btnMusic.getId()) {
//            intent = new Intent(this, MusicList.class);
//            startActivity(intent);
//        }
        if (v.getId() == btnExit.getId()) {
// This will finish the current activity and all activities in the task.
            finishAffinity();
        }
    }
    public void createLoginDialog() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.login);
        dialog.setTitle("Login");
        dialog.setCancelable(true);
        tvDMessage = (TextView) dialog.findViewById(R.id.tvLogin);
        btnNewAccount = (TextView) dialog.findViewById(R.id.btnNewAccount);
        btnNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                createRegistrationDialog();
            }
        });
        tvDMessage.setVisibility(View.INVISIBLE);
        editdUsername = (EditText) dialog.findViewById(R.id.edUsername);
        editDPassword = (EditText) dialog.findViewById(R.id.edPassword);
        btnDlogin = (Button) dialog.findViewById(R.id.btnLogin);
        btnDlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editdUsername.getText().toString().trim();
                String password = editDPassword.getText().toString().trim();
// Validate fields
                if (username.isEmpty() ||password.isEmpty()) {
                    tvDMessage.setVisibility(View.VISIBLE);
                    tvDMessage.setText("Please fill all fields");
                    return;
                }
                boolean isRegistered = dbHelper.loginUserByUsername(username, password);
                tvDMessage.setVisibility(View.VISIBLE);
                if (isRegistered) {
                    tvDMessage.setText("Login successful");
// Save the username in SharedPreferences
                    SharedPreferences sharedPreferences =
                            getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("USERNAME", username);  // 'username' is avariable holding the user's name
                    editor.apply(); // or editor.commit();
                    intent = new Intent(MainActivity.this, users.class);
                    intent.putExtra("USERNAME", username);
                    startActivity(intent);
// Optionally, navigate to LoginActivity here.
                } else {
                    tvDMessage.setText("Login failed please sign in first");
                }
            }
        });
        dialog.show();
    }
    public void createRegistrationDialog() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.signin);
        dialog.setTitle("Registration");
        dialog.setCancelable(true);
        tvDMessage = (TextView) dialog.findViewById(R.id.tvSignin);
        tvDMessage.setVisibility(View.INVISIBLE);
        editdUsername = (EditText) dialog.findViewById(R.id.edUsername);
        editDPassword = (EditText) dialog.findViewById(R.id.edPassword);
        editDEmail = (EditText) dialog.findViewById(R.id.edEmail);
        btnDRegister = (Button) dialog.findViewById(R.id.btnRegister);
        btnDRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editdUsername.getText().toString().trim();
                String email = editDEmail.getText().toString().trim();
                String password = editDPassword.getText().toString().trim();
// Validate fields
                if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    tvDMessage.setVisibility(View.VISIBLE);
                    tvDMessage.setText("Please fill all fields");
                    return;
                }
// Validate email format (contains '@' and '.com')
                if (!email.contains("@") || !email.contains(".com")) {
                    tvDMessage.setVisibility(View.VISIBLE);
                    tvDMessage.setText("Invalid email format. Email must contain '@' and '.com'");
                }
                boolean isRegistered = dbHelper.registerUser(username, email, password);
                tvDMessage.setVisibility(View.VISIBLE);
                if (isRegistered) {
                    tvDMessage.setText("Registration successful");
                    dialog.dismiss();
                    createLoginDialog();
// Optionally, navigate to LoginActivity here.
                } else {
                    tvDMessage.setText("Registration failed user/main exist");
                }
            }
        });
        dialog.show();
    }
}