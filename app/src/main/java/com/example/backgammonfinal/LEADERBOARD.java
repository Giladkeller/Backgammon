package com.example.backgammonfinal;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
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
        // 1. הגדרת השאילתה לשליפת המובילים לפי ניקוד
        Query query = FirebaseFirestore.getInstance()
                .collection("leaderboard")
                .orderBy("points", Query.Direction.DESCENDING);

        // 2. הגדרת האופציות לאדפטר
        FirestoreRecyclerOptions<Player> options = new FirestoreRecyclerOptions.Builder<Player>()
                .setQuery(query, Player.class)
                .build();

        // 3. יצירת האדפטר (איך להציג כל נתון בתוך ה-XML)
        adapter = new FirestoreRecyclerAdapter<Player, PlayerViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PlayerViewHolder holder, int position, @NonNull Player model) {
                // 1. הגדרת הטקסט
                holder.username.setText(model.getUsername());
                holder.points.setText(String.valueOf(model.getPoints()));

                // 2. יצירת מסגרת אליפטית דינמית (Background)
                GradientDrawable shape = new GradientDrawable();
                shape.setShape(GradientDrawable.RECTANGLE);
                shape.setCornerRadius(100f); // ערך גבוה יוצר מראה אליפטי/מעוגל מאוד
                shape.setStroke(3, Color.parseColor("#808080")); // עובי מסגרת וצבע אפור

                // 3. הגדרת רווח בין השורות (Margins) והגדלת הגובה
                // שים לב: השתמש ב-LayoutParams שמתאים למה שעוטף את ה-item_player (כנראה RecyclerView.LayoutParams)
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
                layoutParams.setMargins(20, 10, 20, 30); // שמאל, למעלה, ימין, למטה (ה-30 יוצר את הרווח)
                holder.itemView.setLayoutParams(layoutParams);

                // הגדרת גובה פנימי (Padding) כדי להגדיל את השורה
                holder.itemView.setPadding(40, 40, 40, 40);

                // 4. לוגיקת צבעים לפי מיקום
                switch (position) {
                    case 0: // זהב
                        shape.setColor(Color.parseColor("#FFD700"));
                        holder.username.setText("👑 " + model.getUsername());
                        holder.username.setTextSize(22); // הגדלת פונט למקום ראשון
                        break;
                    case 1: // כסף
                        shape.setColor(Color.parseColor("#C0C0C0"));
                        holder.username.setText("🥈 " + model.getUsername());
                        holder.username.setTextSize(20);
                        break;
                    case 2: // ארד
                        shape.setColor(Color.parseColor("#CD7F32"));
                        holder.username.setText("🥉 " + model.getUsername());
                        holder.username.setTextSize(18);
                        break;
                    default: // שאר השורות
                        shape.setColor(Color.WHITE);
                        holder.username.setText(" " + (position + 1) + "  " + model.getUsername());
                        holder.username.setTextSize(16);
                        break;
                }

                // החלת העיצוב על השורה
                holder.itemView.setBackground(shape);
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