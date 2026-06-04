package com.example.backgammonfinal.Game;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MainPagerAdapter extends FragmentStateAdapter {

    private final String p1Name;
    private final String p2Name;

    // הקונסטרקטור המעודכן שמקבל את השמות מה-Activity
    public MainPagerAdapter(@NonNull FragmentActivity fa, String p1, String p2) {
        super(fa);
        this.p1Name = p1;
        this.p2Name = p2;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // יצירת ה-Bundle להעברת הנתונים
        Bundle args = new Bundle();
        args.putString("p1", p1Name);
        args.putString("p2", p2Name);

        switch (position) {
            case 0:
                Role roleFragment = new Role();
                roleFragment.setArguments(args);
                return roleFragment;
            case 1:
                Game gameFragment = new Game();
                gameFragment.setArguments(args);
                return gameFragment;
            case 2:
                LEADERBOARD leaderboardFragment = new LEADERBOARD();
                leaderboardFragment.setArguments(args);
                return leaderboardFragment;
            default:
                Game defaultFragment = new Game();
                defaultFragment.setArguments(args);
                return defaultFragment;
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}