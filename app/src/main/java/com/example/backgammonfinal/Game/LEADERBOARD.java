package com.example.backgammonfinal.Game;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.backgammonfinal.DataBaseAndFireBase.Player;
import com.example.backgammonfinal.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class LEADERBOARD extends Fragment {

    private RecyclerView recyclerView;

    private ProgressBar progressBar;
    private Button btnBack;
    private FirestoreRecyclerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewLeaderboard);
        progressBar = view.findViewById(R.id.progressBar);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(false);

        btnBack = (Button) view.findViewById(R.id.btnBackToGame);
        if (getArguments() != null) {
            boolean shouldHide = getArguments().getBoolean("btnBackToGame", false);
            if (shouldHide) {
                btnBack.setVisibility(View.VISIBLE);
            }
        }
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // פקודה שאומרת למנהל הפרגמנט לחזור אחורה
                getParentFragmentManager().popBackStack();
            }
        });

        setupLeaderboardQuery();

        return view;
    }

    private void setupLeaderboardQuery() {
        //  הגדרת השאילתה לשליפת המובילים לפי ניקוד
        Query query = FirebaseFirestore.getInstance()
                .collection("leaderboard")
                .orderBy("points", Query.Direction.DESCENDING);

        //  הגדרת האופציות לאדפטר
        FirestoreRecyclerOptions<Player> options = new FirestoreRecyclerOptions.Builder<Player>()
                .setQuery(query, Player.class)
                .setLifecycleOwner(this) // חיבור למחזור החיים של הפרגמנט
                .build();

        //  יצירת האדפטר (איך להציג כל נתון בתוך ה-XML)
        adapter = new FirestoreRecyclerAdapter<Player, PlayerViewHolder>(options) {

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                // ברגע שהנתונים נטענו (אפילו אם הרשימה ריקה), נעלים את פס הטעינה
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }

                recyclerView.post(() -> notifyDataSetChanged());
            }

            @Override
            protected void onBindViewHolder(@NonNull PlayerViewHolder holder, int position, @NonNull Player model) {
                //  חישוב הדירוג (Rank)
                int displayRank = calculateRank(position, model.getPoints()); // ברירת מחדל

                //  הגדרת הטקסט
                holder.username.setText(model.getUsername());
                holder.points.setText(String.valueOf(model.getPoints()));

                //  יצירת מסגרת אליפטית דינמית (Background)
                GradientDrawable shape = new GradientDrawable();
                shape.setShape(GradientDrawable.RECTANGLE);
                shape.setCornerRadius(100f); // ערך גבוה יוצר מראה אליפטי/מעוגל מאוד
                shape.setStroke(3, Color.parseColor("#808080")); // עובי מסגרת וצבע אפור

                //  הגדרת רווח בין השורות (Margins) והגדלת הגובה
                //  שימוש ב-LayoutParams שמתאים למה שעוטף את ה-item_player
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
                layoutParams.setMargins(20, 10, 20, 30); // שמאל, למעלה, ימין, למטה (ה-30 יוצר את הרווח)
                holder.itemView.setLayoutParams(layoutParams);

                // הגדרת גובה פנימי (Padding) כדי להגדיל את השורה
                holder.itemView.setPadding(40, 40, 40, 40);

                //  לוגיקת צבעים ואימוג'י לפי הדירוג המחושב (displayRank)
                switch (displayRank) {
                    case 1: // מקום ראשון
                        shape.setColor(Color.parseColor("#FFD700"));
                        holder.username.setText("👑 " + model.getUsername());
                        holder.username.setTextSize(30);
                        break;
                    case 2: // מקום שני
                        shape.setColor(Color.parseColor("#C0C0C0"));
                        holder.username.setText("🥈 " + model.getUsername());
                        holder.username.setTextSize(25);
                        break;
                    case 3: // מקום שלישי
                        shape.setColor(Color.parseColor("#CD7F32"));
                        holder.username.setText("🥉 " + model.getUsername());
                        holder.username.setTextSize(21);
                        break;
                    default: // כל השאר
                        shape.setColor(Color.parseColor("#e8b961")); // כחול בהיר
                        holder.username.setText(" " + displayRank + ". " + model.getUsername());
                        holder.username.setTextSize(18);
                        break;
                }

                holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // שימוש ב-MaterialAlertDialogBuilder לנראות
                        new com.google.android.material.dialog.MaterialAlertDialogBuilder(holder.itemView.getContext())
                                .setTitle("מחיקת שחקן")
                                .setMessage("האם אתה בטוח שברצונך למחוק את " + model.getUsername() + "? פעולה זו אינה ניתנת לביטול.")
                                .setIcon(android.R.drawable.ic_menu_delete) // הוספת אייקון של פח

                                // עיצוב כפתור האישור
                                .setPositiveButton("מחק", (dialog, which) -> {
                                    String documentId = getSnapshots().getSnapshot(holder.getBindingAdapterPosition()).getId();
                                    deletePlayer(documentId);
                                })

                                // עיצוב כפתור הביטול
                                .setNegativeButton("ביטול", (dialog, which) -> dialog.dismiss())

                                .show();
                    }
                });

                holder.itemView.setBackground(shape);
            }

            private void deletePlayer(String docId) {
                FirebaseFirestore.getInstance()
                        .collection("leaderboard")
                        .document(docId)
                        .delete()
                        .addOnSuccessListener(aVoid -> {
                            // המחיקה הצליחה - ה-FirestoreRecyclerAdapter יתעדכן אוטומטית בתצוגה
                            if (adapter != null) adapter.notifyDataSetChanged();
                        })
                        .addOnFailureListener(e -> {
                            // טיפול בשגיאה במידת הצורך
                        });
            }

            private int calculateRank(int currentPos, long currentPoints) {
                if (currentPos == 0) return 1;

                int rank = 1;
                long lastPoints = getItem(0).getPoints();

                for (int i = 1; i <= currentPos; i++) {
                    long currentItemPoints = getItem(i).getPoints();
                    // אם הניקוד הנוכחי קטן מהניקוד הקודם שראינו, סימן שירדנו דרגה בדירוג
                    if (currentItemPoints < lastPoints) {
                        rank++;
                        lastPoints = currentItemPoints;
                    }
                    // אם הגענו למיקום שלנו, מחזירים את הדירוג שנצבר
                    if (i == currentPos) {
                        return rank;
                    }
                }
                return rank;
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
        ImageView btnDelete;

        public PlayerViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.tvUsername);
            points = itemView.findViewById(R.id.tvPoints);
            btnDelete = itemView.findViewById(R.id.btnDelete);
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