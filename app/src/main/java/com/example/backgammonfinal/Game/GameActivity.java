package com.example.backgammonfinal.Game;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.backgammonfinal.StartActivities.MainActivity;
import com.example.backgammonfinal.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class GameActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    private ImageView imgBack;
    private TabLayoutMediator mediator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);

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

        String p1Name = getIntent().getStringExtra("p1");
        String p2Name = getIntent().getStringExtra("p2");

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> {
            Intent intent = new Intent(GameActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        MainPagerAdapter adapter = new MainPagerAdapter(this,p1Name, p2Name);
        viewPager.setAdapter(adapter);

        viewPager.setCurrentItem(1);

        viewPager.setOffscreenPageLimit(3);

        mediator = new TabLayoutMediator(tabLayout, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(TabLayout.Tab tab, int position) {
                        switch (position) {
                            case 0: tab.setText("Rules"); break;
                            case 1: tab.setText("GAME"); break;
                            case 2: tab.setText("Leader Board"); break;
                        }
                    }
                });
        mediator.attach();
    }

    @Override
    protected void onDestroy() {
        if (mediator != null) mediator.detach();
        super.onDestroy();
    }
}