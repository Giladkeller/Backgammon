package com.example.backgammonfinal.Users;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.backgammonfinal.Game.GameActivity;
import com.example.backgammonfinal.StartActivities.MainActivity;
import com.example.backgammonfinal.R;

import java.util.Locale;

public class users extends AppCompatActivity implements View.OnClickListener {

    private AlertDialog.Builder builder;
    Intent intent;

    private String userName;

    private EditText editPlayer1;

    private ImageView imgPlay, imgBack;

    private ImageView contact1, contact2;
    private EditText p1, p2;

    private String name1, name2;

    private TextToSpeech textToSpeech;
    //for content provider
    private ActivityResultLauncher<Intent> contentLauncher;
    private int playerNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_users);

        intent = getIntent();
        userName = intent.getStringExtra("USERNAME");

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

        editPlayer1 = (EditText) findViewById(R.id.edPlayer1);
        editPlayer1.setText(userName);

        imgPlay = (ImageView) findViewById(R.id.imgIconPlay);
        imgPlay.setOnClickListener(this);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgBack.setOnClickListener(this);

        this.contact1 = (ImageView) findViewById(R.id.imgContact1);
        contact1.setOnClickListener(this);
        this.contact2 = (ImageView) findViewById(R.id.imgContact2);
        contact2.setOnClickListener(this);

        this.p1 = (EditText) findViewById(R.id.edPlayer1);
        this.p2 = (EditText) findViewById(R.id.edPlayer2);

        this.playerNum = 1;

        //Start TextToSpeach
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i == TextToSpeech.SUCCESS) {
                    int lang = textToSpeech.setLanguage(Locale.ENGLISH);
                }
            }
        });
        initcontentP();
    }

    private void initcontentP() {
        contentLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    public void onActivityResult(ActivityResult result) {
                        Cursor cursor = null;
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent intent = result.getData();
                            try {
                                Uri uri = intent.getData();
                                cursor = getContentResolver().query(uri, null, null, null, null);
                                cursor.moveToFirst();
                                int phoneIndexName = cursor.getColumnIndex
                                        (ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                                String phoneName = cursor.getString(phoneIndexName);
                                //  tvHead.setText(phoneName + " ");
                                if (playerNum == 1)
                                    p1.setText(phoneName);
                                else
                                    p2.setText(phoneName);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == imgBack.getId()){
            intent = new Intent(users.this , MainActivity.class);
            startActivity(intent);
            finish();
        }
        if (v.getId() == imgPlay.getId()){

            name1 = p1.getText().toString();
            name2 = p2.getText().toString();

            if (p2.getText().toString().isEmpty() || p1.getText().toString().isEmpty()){
                builder = new AlertDialog.Builder(users.this);
                builder.setTitle("Error");
                builder.setMessage("UserName Null");
                builder.setPositiveButton("ok", (dialog, which) -> {
                    dialog.dismiss();
                });
                builder.show();
            } else if (name1.equals(name2)){
                builder = new AlertDialog.Builder(users.this);
                builder.setTitle("Error");
                builder.setMessage("UserName Can't Be The Same");
                builder.setPositiveButton("ok", (dialog, which) -> {
                    dialog.dismiss();
                });
                builder.show();
            }
            else {
                // הכנת המשפט להקראה
                String welcomeMessage = "Starting game between " + name1 + " and " + name2;
                textToSpeech.setSpeechRate(0.6f);
                textToSpeech.setLanguage(Locale.US);
                textToSpeech.setPitch(0.9f);
                // TextToSpeech.QUEUE_FLUSH מנקה הודעות קודמות ומשמיע את זו מיד
                textToSpeech.speak(welcomeMessage, TextToSpeech.QUEUE_FLUSH, null, "welcomeMessageId");

                intent = new Intent(users.this, GameActivity.class);
                // העברת השמות
                intent.putExtra("p1", p1.getText().toString());
                intent.putExtra("p2", p2.getText().toString());
                startActivity(intent);
                finish();
            }
        }
        if (v.getId() == contact1.getId()) {
            this.playerNum = 1;
            Intent contactPickerIntent = new Intent(Intent.ACTION_PICK);
            contactPickerIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
            contentLauncher.launch(contactPickerIntent);
        }
        if (v.getId() == contact2.getId()) {
            this.playerNum = 2;
            Intent contactPickerIntent = new Intent(Intent.ACTION_PICK);
            contactPickerIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);

            contentLauncher.launch(contactPickerIntent);
        }
    }
}