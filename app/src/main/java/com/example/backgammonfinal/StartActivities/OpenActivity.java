package com.example.backgammonfinal.StartActivities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.backgammonfinal.DataBaseAndFireBase.DatabaseHelper;
import com.example.backgammonfinal.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class OpenActivity extends AppCompatActivity {
    // **** SQLite database
    public DatabaseHelper dbHelper;
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

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
            }
        });

        new Handler(Looper.getMainLooper()).postDelayed(() ->{
            Intent intent = new Intent(OpenActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, 1500);

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        FirebaseFirestore.getInstance().setFirestoreSettings(settings);
    }
}

