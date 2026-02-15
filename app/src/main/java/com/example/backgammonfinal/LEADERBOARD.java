package com.example.backgammonfinal;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class LEADERBOARD extends Fragment {

    private RecyclerView recyclerView;
    private FirestoreRecyclerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewLeaderboard);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        setupLeaderboardQuery();

        return view;
    }

    private void setupLeaderboardQuery() {
        // 1. הגדרת השאילתה לשליפת 10 המובילים לפי ניקוד
        Query query = FirebaseFirestore.getInstance()
                .collection("leaderboard")
                .orderBy("points", Query.Direction.DESCENDING)
                .limit(10);

        // 2. הגדרת האופציות לאדפטר
        FirestoreRecyclerOptions<Player> options = new FirestoreRecyclerOptions.Builder<Player>()
                .setQuery(query, Player.class)
                .build();

        // 3. יצירת האדפטר (איך להציג כל נתון בתוך ה-XML)
        adapter = new FirestoreRecyclerAdapter<Player, PlayerViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PlayerViewHolder holder, int position, @NonNull Player model) {
                holder.username.setText(model.getUsername());
                holder.points.setText(String.valueOf(model.getPoints()));

                // עיצוב לפי מיקום (Position)
                switch (position) {
                    case 0: // מקום ראשון
                        holder.username.setTextColor(Color.parseColor("#FFD700")); // צבע זהב
                        holder.username.setText("👑 " + model.getUsername());
                        holder.itemView.setBackgroundColor(Color.parseColor("#1AFFE700")); // רקע צהבהב עדין
                        break;
                    case 1: // מקום שני
                        holder.username.setTextColor(Color.parseColor("#C0C0C0")); // צבע כסף
                        holder.username.setText("🥈 " + model.getUsername());
                        break;
                    case 2: // מקום שלישי
                        holder.username.setTextColor(Color.parseColor("#CD7F32")); // צבע ארד
                        holder.username.setText("🥉 " + model.getUsername());
                        break;
                    default: // כל השאר
                        holder.username.setTextColor(Color.WHITE);
                        holder.username.setText((position + 1) + ". " + model.getUsername());
                        holder.itemView.setBackgroundColor(Color.TRANSPARENT);
                        break;
                }
            }

            @NonNull
            @Override
            public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_player, parent, false);
                return new PlayerViewHolder(v);
            }
        };

        recyclerView.setAdapter(adapter);
    }

    // ViewHolder פנימי לניהול התצוגה של כל שורה
    private static class PlayerViewHolder extends RecyclerView.ViewHolder {
        TextView username, points;

        public PlayerViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.tvUsername);
            points = itemView.findViewById(R.id.tvPoints);
        }
    }

    // הפעלה ועצירה של ההאזנה ל-Firebase בהתאם למצב הפרגמנט
    @Override
    public void onStart() {
        super.onStart();
        if (adapter != null) adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null) adapter.stopListening();
    }
}