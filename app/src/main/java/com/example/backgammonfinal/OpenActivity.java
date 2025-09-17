package com.example.backgammonfinal;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class OpenActivity extends AppCompatActivity {

    Intent intent;

    private Dialog dialog;

    private EditText editDUsername, editDEmail, editDPassword;

    private TextView tvDMessage;

    private Button btnDGoBack;

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
            createLoginDialog();
        }, 1500);
    }

    public void createSignInDialog() {
        dialog = new Dialog(this);

        dialog.setContentView(R.layout.signin);

        dialog.setTitle("please register to my app");

        dialog.setCancelable(true);

        tvDMessage = (TextView) dialog.findViewById(R.id.tvSignin);
        editDUsername = (EditText) dialog.findViewById(R.id.edUsername);
        editDEmail = (EditText) dialog.findViewById(R.id.edEmail);
        editDPassword = (EditText) dialog.findViewById(R.id.edPassword);
        btnDGoBack = (Button) dialog.findViewById(R.id.btnRegister);

        btnDGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(OpenActivity.this, MainActivity.class);

                startActivity(intent);
            }
        });

        dialog.show();
    }

    public void createLoginDialog() {
        dialog = new Dialog(this);

        dialog.setContentView(R.layout.login);

        dialog.setTitle("please Login to my app");

        dialog.setCancelable(true);

        tvDMessage = (TextView) dialog.findViewById(R.id.tvLogin);
        editDUsername = (EditText) dialog.findViewById(R.id.edUsername);
        editDPassword = (EditText) dialog.findViewById(R.id.edPassword);
        btnDGoBack = (Button) dialog.findViewById(R.id.btnLogin);

        btnDGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(OpenActivity.this, MainActivity.class);

                startActivity(intent);
            }
        });

        dialog.show();
    }
}