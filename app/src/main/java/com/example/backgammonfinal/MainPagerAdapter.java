package com.example.backgammonfinal;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MainPagerAdapter extends FragmentStateAdapter {

    public MainPagerAdapter(@NonNull FragmentActivity fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new Role();
            case 1:
                return new Game();
            case 2:
                return new Chat();
            default:
                return new Role();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
