package com.example.backgammonfinal.Game;

import java.util.Random;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Game#newInstance} factory method to
 * create an instance of this fragment.
 */
import android.graphics.Color;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.backgammonfinal.StartActivities.MainActivity;
import com.example.backgammonfinal.R;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * <p>
 * create an instance of this fragment.
 */
public class Game extends Fragment implements View.OnClickListener {


    private AlertDialog.Builder builder;

    Intent intent;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private LinearLayout[] layouts;

    private LinearLayout layout, iLEat;
    private ImageView imgCubes, imgC1, imgC2, imgC3, imgC4;
    private Random rnd = new Random();
    private int rndCube1, rndCube2;

    private TextView tvPlayerW;
    private TextView tvPlayerB;

    private Button restart, btnTakeOut;

    private LinearLayout lOutWhite, lOutBrown;

    @Nullable
    public View onCreateView() {
        return onCreateView(null, null, null);
    }

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_game, container, false);
        layouts = new LinearLayout[24];

        for (int i = 0; i < 24; i++) {
            String layoutID = "lL" + (i + 1);
            int resID = getResources().getIdentifier(layoutID, "id", getActivity().getPackageName());
            layouts[i] = (LinearLayout) v.findViewById(resID);
            layouts[i].setOnClickListener(this);
            layouts[i].setClickable(false);
        }
        iLEat = (LinearLayout) v.findViewById(R.id.iLEat);
        iLEat.setOnClickListener(this);
        iLEat.setClickable(false);

        imgC1 = (ImageView) v.findViewById(R.id.imgC1);
        imgC2 = (ImageView) v.findViewById(R.id.imgC2);
        imgC3 = (ImageView) v.findViewById(R.id.imgC3);
        imgC4 = (ImageView) v.findViewById(R.id.imgC4);
        imgC1.setVisibility(View.INVISIBLE);
        imgC2.setVisibility(View.INVISIBLE);
        imgC3.setVisibility(View.INVISIBLE);
        imgC4.setVisibility(View.INVISIBLE);
        lOutWhite = (LinearLayout) v.findViewById(R.id.lOutW);
        lOutBrown = (LinearLayout) v.findViewById(R.id.lOutB);
        imgCubes = (ImageView) v.findViewById(R.id.imgCubes);
        imgCubes.setOnClickListener(this);
        btnTakeOut = (Button) v.findViewById(R.id.btnTakeOut);
        btnTakeOut.setOnClickListener(this);
        restart = (Button) v.findViewById(R.id.btnRestart);
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restartGame();
            }
        });

        setupStartingPosition();

        tvPlayerW = (TextView) v.findViewById(R.id.tvPlayerW);
        tvPlayerB = (TextView) v.findViewById(R.id.tvPlayerB);

        if (getArguments() != null){
            tvPlayerW.setText(getArguments().getString("p1"," "));
            tvPlayerB.setText(getArguments().getString("p2"," "));
        }

        return v;

    }

    private void restartGame() {
        // 1. ניקוי כל המשולשים מחיילים
        for (int i = 0; i < 24; i++) {
            layouts[i].removeAllViews();
            layouts[i].setBackgroundColor(Color.TRANSPARENT);
            layouts[i].setClickable(false); // עד שזורקים קוביות
        }

        // 2. ניקוי אזור האכולים
        iLEat.removeAllViews();
        lOutWhite.removeAllViews();
        lOutBrown.removeAllViews();

        // 3. איפוס משתני המשחק
        turn = "white";
        rndCube1 = 0;
        rndCube2 = 0;
        selected = -2;
        eatIndex = -1;

        // 4. הסתרת קוביות
        imgC1.setVisibility(View.INVISIBLE);
        imgC2.setVisibility(View.INVISIBLE);
        imgC3.setVisibility(View.INVISIBLE);
        imgC4.setVisibility(View.INVISIBLE);

        // 5. הצבת החיילים מחדש (פונקציה שנבנה מיד)
        setupStartingPosition();

        Toast.makeText(getContext(), "המשחק אותחל! תור לבן", Toast.LENGTH_SHORT).show();
    }

    private void addSoldiers(int layoutIndex, String color, int count) {
        int resId = getResources().getIdentifier(color + "_solider", "drawable", requireContext().getPackageName());

        for (int i = 0; i < count; i++) {
            ImageView soldier = new ImageView(getContext());
            soldier.setImageResource(resId);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (32 * getResources().getDisplayMetrics().density));
            soldier.setLayoutParams(params);

            layouts[layoutIndex].addView(soldier);
        }
    }

    private void setupStartingPosition() {
        // לבן (White)
        addSoldiers(0, "brown", 2);
        addSoldiers(11, "brown", 5);
        addSoldiers(16, "brown", 3);
        addSoldiers(18, "brown", 5);

        // חום (Brown)
        addSoldiers(23, "white", 2);
        addSoldiers(12, "white", 5);
        addSoldiers(7, "white", 3);
        addSoldiers(5, "white", 5);
    }

    private boolean canTakeOut(LinearLayout[] layouts, int tap, LinearLayout iLEat, int rndCube) {
        if (rndCube == 0) return false;

        int resId = getResources().getIdentifier(turn + "_solider", "drawable", requireContext().getPackageName());
        int startHome = turn.equals("white") ? 0 : 18;
        int endHome = turn.equals("white") ? 5 : 23;

        // 1. בדיקה שכל החיילים בבית
        int countInHome = turn.equals("white") ? lOutWhite.getChildCount() : lOutBrown.getChildCount();
        for (int i = 0; i < 24; i++) {
            if (layouts[i].getChildCount() > 0) {
                ImageView img = (ImageView) layouts[i].getChildAt(0);
                if (img.getDrawable().getConstantState() == ContextCompat.getDrawable(requireContext(), resId).getConstantState()) {
                    // אם החייל מחוץ לטווח הבית
                    if (i < startHome || i > endHome) return false;
                    countInHome += layouts[i].getChildCount();
                }
            }
        }

        // בדיקת אכולים - אם יש אכול מהצבע שלי, אי אפשר להוציא
        for (int i = 0; i < iLEat.getChildCount(); i++) {
            ImageView imgEat = (ImageView) iLEat.getChildAt(i);
            if (imgEat.getDrawable().getConstantState() == ContextCompat.getDrawable(requireContext(), resId).getConstantState()) {
                return false;
            }
        }

        if (countInHome < 15) return false;

        // 2. חישוב המרחק המדויק ליציאה
        // לבן הולך מ-5 ל-0 (יוצא במינוס 1). חום מ-18 ל-23 (יוצא ב-24).
        int distanceToExit = turn.equals("white") ? (tap + 1) : (24 - tap);

        // אופציה א': הקובייה בדיוק במידה
        if (rndCube == distanceToExit) return true;

        // אופציה ב': הקובייה גדולה מהמרחק (מותר רק אם אין חייל מאחוריו)
        if (rndCube > distanceToExit) {
            if (turn.equals("white")) {
                for (int i = tap + 1; i <= 5; i++) { // לבן: לבדוק משבצות גבוהות יותר בתוך הבית
                    if (isPlayerSoldierAt(i, resId)) return false;
                }
            } else {
                for (int i = tap - 1; i >= 18; i--) { // חום: לבדוק משבצות נמוכות יותר בתוך הבית
                    if (isPlayerSoldierAt(i, resId)) return false;
                }
            }
            return true;
        }

        return false; // הקובייה קטנה מהמרחק
    }

    // פונקציית עזר לבדיקה אם יש חייל שלי במשבצת מסוימת
    private boolean isPlayerSoldierAt(int index, int resId) {
        if (layouts[index].getChildCount() > 0) {
            ImageView img = (ImageView) layouts[index].getChildAt(0);
            return img.getDrawable().getConstantState() == ContextCompat.getDrawable(requireContext(), resId).getConstantState();
        }
        return false;
    }


    private void throwCubes() {
        if (imgC1.getVisibility() == View.INVISIBLE && imgC2.getVisibility() == View.INVISIBLE && imgC3.getVisibility() == View.INVISIBLE && imgC4.getVisibility() == View.INVISIBLE) {
            for (int i = 0; i < 24; i++) {
                layouts[i].setBackgroundColor(Color.TRANSPARENT);
            }
            imgC3.setVisibility(View.INVISIBLE);
            imgC4.setVisibility(View.INVISIBLE);
            rndCube1 = rnd.nextInt(6) + 1;
            imgC1.setImageResource(getResources().getIdentifier("cube" + rndCube1, "drawable", getActivity().getPackageName()));
            imgC1.setVisibility(View.VISIBLE);
            rndCube2 = rnd.nextInt(6) + 1;
            imgC2.setImageResource(getResources().getIdentifier("cube" + rndCube2, "drawable", getActivity().getPackageName()));
            imgC2.setVisibility(View.VISIBLE);

            if (rndCube1 == rndCube2) {
                imgC3.setImageResource(getResources().getIdentifier("cube" + rndCube1, "drawable", getActivity().getPackageName()));
                imgC4.setImageResource(getResources().getIdentifier("cube" + rndCube2, "drawable", getActivity().getPackageName()));
                imgC3.setVisibility(View.VISIBLE);
                imgC4.setVisibility(View.VISIBLE);
            }

            for (int i = 0; i < 24; i++) {
                layouts[i].setClickable(true);
            }
        }
    }

    private void performTakeOut(int triangleIndex, int selectedVal, String currentTurn) {
        if (layouts[triangleIndex].getChildCount() > 0) {
            // 1. קבלת ייחוס לחייל והסרתו מהמשולש (מניעת השגיאה Specified child already has a parent)
            ImageView soldier = (ImageView) layouts[triangleIndex].getChildAt(0);
            layouts[triangleIndex].removeView(soldier);

            // 2. עיצוב החייל מחדש כדי שיתאים למחסן הצידי (גובה נמוך יותר)
            int height = (int) (12 * getResources().getDisplayMetrics().density); // גובה של 12dp
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
            params.setMargins(0, 2, 0, 2); // רווח קטן בין החיילים בערימה
            soldier.setLayoutParams(params);

            // 3. הוספה למחסן הנכון
            if (currentTurn.equals("white")) {
                lOutWhite.addView(soldier);
            } else {
                lOutBrown.addView(soldier);
            }

            // 4. עדכון לוגיקת המשחק
            deleteCube(triangleIndex, selectedVal, currentTurn);
            btnTakeOut.setVisibility(View.INVISIBLE);

            // 5. ניקוי צבעים מהלוח
            for (int k = 0; k < 24; k++) {
                layouts[k].setBackgroundColor(Color.TRANSPARENT);
                canMove[k] = false;
            }

            // 6. בדיקה אם נשארו מהלכים והחלפת תור במידת הצורך
            if (!cantMove(layouts, currentTurn)) {
                changTurn();
            }

            // בדיקת ניצחון
            checkWinCondition();
        }
    }

    private void checkWinCondition() {
        if (lOutWhite.getChildCount() == 15) {
            int points = calculatePoints("white", "brown");
            String type = getWinType(points);
            //Toast.makeText(getContext(), "הלבן ניצח " + type + "! (" + points + " נקודות)", Toast.LENGTH_LONG).show();
            builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("GAME OVER");
            builder.setMessage("הלבן ניצח " + type + "! (" + points + " נקודות)");
            builder.setPositiveButton("new game", (dialog, which) -> {
                dialog.dismiss();
                restartGame();
            });
            builder.setNegativeButton("Main Menu", (dialog, which) -> {
                dialog.dismiss();
                intent = new Intent(requireContext(), MainActivity.class);
                startActivity(intent);
            });
            builder.show();
            updateLeaderboard(getArguments().getString("p1"," "), points);
            forceEndTurn();
        } else if (lOutBrown.getChildCount() == 15) {
            int points = calculatePoints("brown", "white");
            String type = getWinType(points);
            //Toast.makeText(getContext(), "החום ניצח " + type + "! (" + points + " נקודות)", Toast.LENGTH_LONG).show();
            builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("GAME OVER");
            builder.setMessage("החום ניצח " + type + "! (" + points + " נקודות)");
            builder.setPositiveButton("new game", (dialog, which) -> {
                dialog.dismiss();
                restartGame();
            });
            builder.setNegativeButton("Main Menu", (dialog, which) -> {
                dialog.dismiss();
                intent = new Intent(requireContext(), MainActivity.class);
                startActivity(intent);
            });
            builder.show();
            updateLeaderboard(getArguments().getString("p2"," "), points);
            forceEndTurn();
        }
    }

    // פונקציית עזר להחזרת שם הניצחון להצגה ב-Toast
    private String getWinType(int points) {
        switch (points) {
            case 4: return  "ניצחון ענק! מארס כוכבי! ⭐⭐⭐⭐";
            case 3: return  "מארס טורקי! 🎩⭐⭐";
            case 2: return  "מארס! 🎲⭐";
            default: return "רגיל";
        }
    }

    private int calculatePoints(String winner, String loser) {
        LinearLayout lOutLoser = loser.equals("white") ? lOutWhite : lOutBrown;

        // 1. ניצחון רגיל (1 נקודה): המפסיד הצליח להוציא לפחות חייל אחד
        if (lOutLoser.getChildCount() > 0) {
            return 1;
        }

        // מעכשיו אנחנו יודעים שהמפסיד לא הוציא כלום (פוטנציאל למארס)
        int loserResId = getResources().getIdentifier(loser + "_solider", "drawable", requireContext().getPackageName());

        // 2. מארס כוכבי (4 נקודות): לא הוציא כלום + יש לו חייל אכול (על ה-Bar)
        for (int i = 0; i < iLEat.getChildCount(); i++) {
            ImageView img = (ImageView) iLEat.getChildAt(i);
            if (img.getDrawable().getConstantState() == ContextCompat.getDrawable(requireContext(), loserResId).getConstantState()) {
                return 4;
            }
        }

        // 3. מארס טורקי (3 נקודות): לא הוציא כלום + יש לו חייל בבית של המנצח
        // בית הלבן: 0-5, בית החום: 18-23
        int winnerHomeStart = winner.equals("white") ? 0 : 18;
        int winnerHomeEnd = winner.equals("white") ? 5 : 23;

        for (int i = winnerHomeStart; i <= winnerHomeEnd; i++) {
            if (layouts[i].getChildCount() > 0) {
                ImageView img = (ImageView) layouts[i].getChildAt(0);
                if (img.getDrawable().getConstantState() == ContextCompat.getDrawable(requireContext(), loserResId).getConstantState()) {
                    return 3;
                }
            }
        }

        // 4. מארס רגיל (2 נקודות): לא הוציא כלום, אין אכולים, וכל החיילים יצאו מבית המנצח
        return 2;
    }

    private void updateLeaderboard(String winnerName, int pointsToAdd) {
        if (winnerName == null || winnerName.isEmpty()) return;

        // 1. נרמול ה-ID: הופך את " Red" ל-"red" כדי למנוע כפילויות במסד הנתונים
        String documentId = winnerName.trim().toLowerCase();

        // 2. הכנת הנתונים לעדכון
        Map<String, Object> data = new HashMap<>();

        // שומרים את השם המקורי (למשל "Red") בשדה נפרד לצורך תצוגה יפה
        data.put("username", winnerName.trim());

        // שימוש ב-increment מבטיח חישוב אטומי בצד השרת (מונע התנגשויות)
        data.put("points", FieldValue.increment(pointsToAdd));

        // שימוש בזמן השרת (מדויק יותר מזמן המכשיר)
        data.put("timestamp", FieldValue.serverTimestamp());

        // 3. ביצוע העדכון עם SetOptions.merge()
        db.collection("leaderboard").document(documentId)
                .set(data, com.google.firebase.firestore.SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firestore", "Leaderboard updated for: " + documentId);
                    if (getContext() != null) {
                        if (isAdded() && getContext() != null) {
                            Toast.makeText(getContext(),
                                    "נוספו " + pointsToAdd + " נקודות ל-" + winnerName.trim() + "!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error updating leaderboard", e);
                });
    }

    private int selectLinear(int rndCube, int i, String turn) {
        if (rndCube != 0) {
            int selected = i;
            if (i == -1) {
                if (turn.equals("white")) {
                    selected = 24 - rndCube;
                } else if (turn.equals("brown")) {
                    selected = rndCube - 1;
                }
            } else {
                if (turn.equals("white")) {
                    selected = i - rndCube;
                } else if (turn.equals("brown")) {
                    selected = i + rndCube;
                }
            }
            return selected;
        } else {
            return -2;
        }
    }

    private void paintLinear(int selected, LinearLayout[] layouts, int i, boolean[] canMove, LinearLayout iLEat, int eatenIndex, String turn) {
        if (i == -1) {
            if (layouts[selected].getChildAt(0) != null && iLEat.getChildAt(0) != null) {
                int resIdEAt = getResources().getIdentifier((turn + "_solider"), "drawable", requireContext().getPackageName());
                ImageView imageSelected = (ImageView) (layouts[selected].getChildAt(0));
                ImageView imageI = (ImageView) (iLEat.getChildAt(eatenIndex));
                if (imageI.getDrawable().getConstantState() == imageSelected.getDrawable().getConstantState() && ContextCompat.getDrawable(requireContext(), resIdEAt).getConstantState() == imageSelected.getDrawable().getConstantState()) {
                    layouts[selected].setBackgroundColor(Colors.GREEN.get());
                    canMove[selected] = true;
                } else if (layouts[selected].getChildCount() == 1) {
                    layouts[selected].setBackgroundColor(Colors.GREEN.get());
                    canMove[selected] = true;
                } else {
                    layouts[selected].setBackgroundColor(Colors.RED.get());
                }
            } else if (layouts[selected].getChildAt(0) == null) {
                layouts[selected].setBackgroundColor(Colors.GREEN.get());
                canMove[selected] = true;
            } else {
                layouts[selected].setBackgroundColor(Colors.RED.get());
            }
        } else {
            if (selected < 24 && selected >= 0) {
                if (layouts[selected].getChildAt(0) != null && layouts[i].getChildAt(0) != null) {
                    ImageView imageSelected = (ImageView) (layouts[selected].getChildAt(0));
                    ImageView imageI = (ImageView) (layouts[i].getChildAt(0));
                    if (imageI.getDrawable().getConstantState() == imageSelected.getDrawable().getConstantState()) {
                        layouts[selected].setBackgroundColor(Colors.GREEN.get());
                        canMove[selected] = true;
                    } else if (layouts[selected].getChildCount() == 1) {
                        layouts[selected].setBackgroundColor(Colors.GREEN.get());
                        canMove[selected] = true;
                    } else {
                        layouts[selected].setBackgroundColor(Colors.RED.get());
                    }
                } else if (layouts[selected].getChildAt(0) == null) {
                    layouts[selected].setBackgroundColor(Colors.GREEN.get());
                    canMove[selected] = true;
                } else {
                    layouts[selected].setBackgroundColor(Colors.RED.get());
                }
            } else {
                // מחוץ ללוח, בדוק אם ניתן להוציא
                if (canTakeOut(layouts, i, iLEat, rndCube1) || canTakeOut(layouts, i, iLEat, rndCube2)) {
                    canMove[i] = true; // אפשרות להוציא
                    layouts[i].setBackgroundColor(Colors.GREEN.get());
                    btnTakeOut.setVisibility(View.VISIBLE);
                    btnTakeOut.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            performTakeOut(i, selected, turn);
                        }
                    });
                } else {
                    canMove[i] = false; // לא ניתן להוציא
                }
            }
        }
    }

    private void moveEatGreen(LinearLayout[] layouts, int i, boolean[] canMove, LinearLayout iLEat, int eatenIndex, String turn) {
        if (canMove[i]) {
            if (layouts[i].getChildAt(0) != null && iLEat.getChildAt(0) != null) {
                int resIdEAt = getResources().getIdentifier((turn + "_solider"), "drawable", requireContext().getPackageName());
                ImageView imageSelected = (ImageView) (iLEat.getChildAt(eatenIndex));
                ImageView imageMove = (ImageView) (layouts[i].getChildAt(0));
                if (imageMove.getDrawable().getConstantState() == imageSelected.getDrawable().getConstantState() && ContextCompat.getDrawable(requireContext(), resIdEAt).getConstantState() == imageSelected.getDrawable().getConstantState()) {
                    iLEat.removeViewAt(eatenIndex);
                    layouts[i].addView(imageSelected);
                    layouts[i].setBackgroundColor(Color.TRANSPARENT);
                    canMove[i] = false;
                    deleteCube(-1, i, turn);
                    if (cantMove(layouts, turn)) {
                        Toast.makeText(getContext(), "you dont have what to do", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    iLEat.removeViewAt(eatenIndex);
                    layouts[i].removeViewAt(0);
                    layouts[i].addView(imageSelected);
                    iLEat.addView(imageMove);
                    layouts[i].setBackgroundColor(Color.TRANSPARENT);
                    canMove[i] = false;
                    deleteCube(-1, i, turn);
                    if (cantMove(layouts, turn)) {
                        Toast.makeText(getContext(), "you dont have what to do", Toast.LENGTH_SHORT).show();
                    }
                }
            } else if (iLEat.getChildAt(0) != null) {
                ImageView imageSelected = (ImageView) (iLEat.getChildAt(eatenIndex));
                iLEat.removeViewAt(eatenIndex);
                layouts[i].addView(imageSelected);
                layouts[i].setBackgroundColor(Color.TRANSPARENT);
                canMove[i] = false;
                deleteCube(-1, i, turn);
                if (cantMove(layouts, turn)) {
                    Toast.makeText(getContext(), "you dont have what to do", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(getContext(), "you can't move to here", Toast.LENGTH_SHORT).show();
        }
    }

    private void moveGreen(LinearLayout[] layouts, int i, boolean[] canMove, int selected, LinearLayout iLEat, String turn) {
        if (canMove[i]) {
            if (layouts[i].getChildAt(0) != null && layouts[selected].getChildAt(0) != null) {
                ImageView imageSelected = (ImageView) (layouts[selected].getChildAt(0));
                ImageView imageMove = (ImageView) (layouts[i].getChildAt(0));
                if (imageMove.getDrawable().getConstantState() == imageSelected.getDrawable().getConstantState()) {
                    layouts[selected].removeViewAt(0);
                    layouts[i].addView(imageSelected);
                    layouts[i].setBackgroundColor(Color.TRANSPARENT);
                    canMove[i] = false;
                    deleteCube(selected, i, turn);
                    if (cantMove(layouts, turn)) {
                        Toast.makeText(getContext(), "you dont have what to do", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    layouts[selected].removeViewAt(0);
                    layouts[i].removeViewAt(0);
                    layouts[i].addView(imageSelected);
                    iLEat.addView(imageMove);
                    layouts[i].setBackgroundColor(Color.TRANSPARENT);
                    canMove[i] = false;
                    deleteCube(selected, i, turn);
                    if (cantMove(layouts, turn)) {
                        Toast.makeText(getContext(), "you dont have what to do", Toast.LENGTH_SHORT).show();
                    }
                }
            } else if (layouts[selected].getChildAt(0) != null) {
                ImageView imageSelected = (ImageView) (layouts[selected].getChildAt(0));
                layouts[selected].removeViewAt(0);
                layouts[i].addView(imageSelected);
                layouts[i].setBackgroundColor(Color.TRANSPARENT);
                canMove[i] = false;
                deleteCube(selected, i, turn);
                if (cantMove(layouts, turn)) {
                    Toast.makeText(getContext(), "you dont have what to do", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(getContext(), "you can't move to here", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean cantMove(LinearLayout[] layouts, String turn) {
        if (rndCube1 == 0 && rndCube2 == 0) return false;

        int resIdSolider = getResources().getIdentifier((turn + "_solider"), "drawable", requireContext().getPackageName());

        // בדיקה אם יש חייל אכול
        boolean hasEaten = false;
        for (int j = 0; j < iLEat.getChildCount(); j++) {
            ImageView img = (ImageView) iLEat.getChildAt(j);
            if (img.getDrawable().getConstantState() == ContextCompat.getDrawable(requireContext(), resIdSolider).getConstantState()) {
                hasEaten = true;
                break;
            }
        }

        if (hasEaten) {
            // בודקים רק אם האכול יכול להיכנס (אינדקס -1)
            if (canActuallyMove(-1, rndCube1, turn) || canActuallyMove(-1, rndCube2, turn)) {
                return false;
            }
        } else {
            // בודקים את כל המשולשים על הלוח
            for (int i = 0; i < 24; i++) {
                if (layouts[i].getChildCount() > 0) {
                    ImageView img = (ImageView) layouts[i].getChildAt(0);
                    if (img.getDrawable().getConstantState() == ContextCompat.getDrawable(requireContext(), resIdSolider).getConstantState()) {
                        if (canActuallyMove(i, rndCube1, turn) || canActuallyMove(i, rndCube2, turn)) {
                            return false; // נמצא לפחות מהלך אחד חוקי
                        }
                    }
                }
            }
        }

        // אם הגענו לכאן - אין מהלכים חוקיים
        forceEndTurn();
        return true;
    }

    // פונקציית עזר חדשה שבודקת חוקיות בלי לשנות UI ובלי לקרוס
    private boolean canActuallyMove(int fromIndex, int dice, String turn) {
        if (dice == 0) return false;

        int target = selectLinear(dice, fromIndex, turn);

        // מקרה של הוצאה מהלוח
        if (target < 0 || target > 23) {
            return canTakeOut(layouts, fromIndex, iLEat, dice);
        }

        // מקרה של תנועה רגילה על הלוח
        if (layouts[target].getChildCount() <= 1) return true; // ריק או חייל אחד (אכילה)

        ImageView targetImg = (ImageView) layouts[target].getChildAt(0);
        int resIdSelf = getResources().getIdentifier((turn + "_solider"), "drawable", requireContext().getPackageName());

        // חוקי אם זה החייל שלי
        return targetImg.getDrawable().getConstantState() == ContextCompat.getDrawable(requireContext(), resIdSelf).getConstantState();
    }

    // פונקציה לניקוי תור כשאין מהלכים
    private void forceEndTurn() {
        rndCube1 = 0;
        rndCube2 = 0;
        imgC1.setVisibility(View.INVISIBLE);
        imgC2.setVisibility(View.INVISIBLE);
        imgC3.setVisibility(View.INVISIBLE);
        imgC4.setVisibility(View.INVISIBLE);
    }

    private void deleteCube(int selected, int j, String turn) {
        int distance;
        if (selected == -1) { // כניסה מאכול
            distance = (turn.equals("white")) ? (24 - j) : (j + 1);
        } else if (j < 0 || j > 23) { // הוצאה מהלוח (Take Out)
            distance = (turn.equals("white")) ? (selected + 1) : (24 - selected);
        } else { // תנועה רגילה
            distance = (turn.equals("white")) ? (selected - j) : (j - selected);
        }

        if (rndCube1 == rndCube2) {
            // במידה וכפולים (דאבל) - הלוגיקה נשארת זהה
            if (imgC4.getVisibility() == View.VISIBLE) imgC4.setVisibility(View.INVISIBLE);
            else if (imgC3.getVisibility() == View.VISIBLE) imgC3.setVisibility(View.INVISIBLE);
            else if (imgC2.getVisibility() == View.VISIBLE) imgC2.setVisibility(View.INVISIBLE);
            else if (imgC1.getVisibility() == View.VISIBLE) {
                imgC1.setVisibility(View.INVISIBLE);
                rndCube1 = 0;
                rndCube2 = 0;
            }
        } else {
            if (j < 0 || j > 23) { // הוצאה מהלוח
                // 1. בדיקה אם יש קובייה ששווה בדיוק למרחק (למשל חייל ב-5 וקובייה 5)
                if (rndCube1 == distance) {
                    imgC1.setVisibility(View.INVISIBLE);
                    rndCube1 = 0;
                } else if (rndCube2 == distance) {
                    imgC2.setVisibility(View.INVISIBLE);
                    rndCube2 = 0;
                }
                // 2. רק אם אין קובייה מדויקת, משתמשים בקובייה גדולה יותר (למשל חייל ב-5 וקובייה 6)
                else if (rndCube1 > distance) {
                    imgC1.setVisibility(View.INVISIBLE);
                    rndCube1 = 0;
                } else if (rndCube2 > distance) {
                    imgC2.setVisibility(View.INVISIBLE);
                    rndCube2 = 0;
                }
            } else {
                // תנועה רגילה - חייב מרחק מדויק
                if (distance == rndCube1) {
                    imgC1.setVisibility(View.INVISIBLE);
                    rndCube1 = 0;
                } else if (distance == rndCube2) {
                    imgC2.setVisibility(View.INVISIBLE);
                    rndCube2 = 0;
                }
            }
        }
    }

    private void changTurn() {
        if (imgC1.getVisibility() == View.INVISIBLE && imgC2.getVisibility() == View.INVISIBLE && imgC3.getVisibility() == View.INVISIBLE && imgC4.getVisibility() == View.INVISIBLE) {
            if (turn.equals("white")) {
                turn = "brown";
            } else {
                turn = "white";
            }
            rndCube1 = 0;
            rndCube2 = 0;
            selected = -2;

            for (int i = 0; i < 24; i++) {
                layouts[i].setClickable(false);
            }

            Toast.makeText(getContext(), "התור עבר ל: " + turn, Toast.LENGTH_SHORT).show();
        }
    }

    String turn = "white";
    boolean[] canMove = new boolean[24];
    int selected = -2;

    int eatIndex = -1;


    @Override
    public void onClick(View view) {

        for (int i = 0; i < 24; i++) {
            ImageView imgColor = (ImageView) (layouts[i].getChildAt(0));
            int resId = getResources().getIdentifier((turn + "_solider"), "drawable", requireContext().getPackageName());
            if (imgColor != null && imgColor.getDrawable().getConstantState() != ContextCompat.getDrawable(requireContext(), resId).getConstantState() && layouts[i].getChildAt(0) == null) {
                layouts[i].setClickable(false);
            } else {
                layouts[i].setClickable(true);
            }
        }
        for (int j = 0; j < iLEat.getChildCount(); j++) {
            ImageView imgColor = (ImageView) (iLEat.getChildAt(0));
            int resId = getResources().getIdentifier((turn + "_solider"), "drawable", requireContext().getPackageName());
            if (imgColor != null && imgColor.getDrawable().getConstantState() == ContextCompat.getDrawable(requireContext(), resId).getConstantState()) {
                iLEat.setClickable(true);
            } else
                iLEat.setClickable(false);
        }
        //throw cubes
        if (imgCubes.getId() == view.getId()) {
            throwCubes();
            if (cantMove(layouts, turn)) {
                Toast.makeText(getContext(), "you dont have what to do", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        for (int g = 0; g < 24; g++) {
            if (canMove[g]) {
                layouts[g].setClickable(true);
            }
        }
        for (int m = 0; m < 24; m++) {
            if (layouts[m].getId() == view.getId() && canMove[m]) {
                btnTakeOut.setVisibility(View.INVISIBLE);
                if (selected == -1) {
                    moveEatGreen(layouts, m, canMove, iLEat, eatIndex, turn);
                } else {
                    moveGreen(layouts, m, canMove, selected, iLEat, turn);
                }
                for (int k = 0; k < 24; k++) {
                    layouts[k].setClickable(true);
                    canMove[k] = false;
                    layouts[k].setBackgroundColor(Color.TRANSPARENT);
                }
                selected = -2;
                changTurn();
                return;
            }
        }

        for (int i = 0; i < 24; i++) {
            layouts[i].setBackgroundColor(Color.TRANSPARENT);
        }

        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 24; j++) {
                canMove[j] = false;
            }
            if (rndCube1 != 0 || rndCube2 != 0) {
                for (int j = 0; j < iLEat.getChildCount(); j++) {
                    ImageView imgColorEat = (ImageView) (iLEat.getChildAt(j));
                    int resIdEAt = getResources().getIdentifier((turn + "_solider"), "drawable", requireContext().getPackageName());
                    if (imgColorEat != null && imgColorEat.getDrawable().getConstantState() == ContextCompat.getDrawable(requireContext(), resIdEAt).getConstantState()) {
                        for (int k = 0; k < 24; k++) {
                            layouts[k].setClickable(false);
                        }
                        if (iLEat.getId() == view.getId()) {
                            btnTakeOut.setVisibility(View.INVISIBLE);
                            eatIndex = j;
                            if (rndCube1 != 0) {
                                paintLinear(selectLinear(rndCube1, -1, turn), layouts, -1, canMove, iLEat, eatIndex, turn);
                                layouts[selectLinear(rndCube1, -1, turn)].setClickable(true);
                            }
                            if (rndCube2 != 0) {
                                paintLinear(selectLinear(rndCube2, -1, turn), layouts, -1, canMove, iLEat, eatIndex, turn);
                                layouts[selectLinear(rndCube2, -1, turn)].setClickable(true);
                            }
                            selected = -1;
                            return;
                        }
                    }
                }
                ImageView imgColor = (ImageView) (layouts[i].getChildAt(0));
                int resId = getResources().getIdentifier((turn + "_solider"), "drawable", requireContext().getPackageName());
                if (layouts[i].getId() == view.getId()) {
                    btnTakeOut.setVisibility(View.INVISIBLE);
                    if (imgColor != null) {
                        if (imgColor.getDrawable().getConstantState() == ContextCompat.getDrawable(requireContext(), resId).getConstantState()) {
                            paintLinear(selectLinear(rndCube1, i, turn), layouts, i, canMove, iLEat, eatIndex, turn);
                            paintLinear(selectLinear(rndCube2, i, turn), layouts, i, canMove, iLEat, eatIndex, turn);
                            selected = i;
                            return;
                        } else {
                            Toast.makeText(getContext(), "this not your turn", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "this triangle null", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }


    public enum Colors {
        GREEN,
        RED;

        public int get() {
            switch (this) {
                case GREEN:
                    return Color.parseColor("#675be851");
                case RED:
                    return Color.RED;
            }
            return 0;
        }
    }

}


