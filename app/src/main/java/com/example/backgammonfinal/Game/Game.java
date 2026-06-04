package com.example.backgammonfinal.Game;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.backgammonfinal.R;
import com.example.backgammonfinal.StartActivities.MainActivity;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Game extends Fragment implements View.OnClickListener {

    // המערך board (מ-0 עד 23) מייצג את המשולשים על הלוח
    // מספרים חיוביים = חיילים לבנים (למשל, 3 אומר שיש 3 חיילים לבנים)
    // מספרים שליליים = חיילים חומים (למשל, -5 אומר שיש 5 חיילים חומים)
    // 0 אומר שהמשולש ריק
    private int[] board = new int[24];

    // מעקב אחר חיילים אכולים
    private int eatenWhite = 0;
    private int eatenBrown = 0;

    // מעקב אחר חיילים שהוצאו החוצה
    private int outWhite = 0;
    private int outBrown = 0;

    // ניהול תור וקוביות
    private String turn = "white"; // "white" או "brown"
    private int[] availableDice = new int[4]; // יכול להכיל עד 4 מהלכים (במקרה של דאבל)
    private int selectedTriangle = -2; // 1- עבור הבר, 0-23 עבור הלוח, 2- אומר ששום דבר לא נבחר

    // רכיבי תצוגה (UI)
    private LinearLayout[] layouts = new LinearLayout[24];
    private LinearLayout iLEat, lOutWhite, lOutBrown;
    private ImageView imgC1, imgC2, imgC3, imgC4, imgCubes;
    private Button btnTakeOut, restart;
    private TextView tvPlayerW, tvPlayerB;


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Random rnd = new Random();
    private AlertDialog.Builder builder;
    private Intent intent;


    private int whiteSoldierResId;
    private int brownSoldierResId;
    private int heightInDp;


    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable turnPassRunnable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_game, container, false);

        // שמירת מזהי המשאבים פעם אחת
        whiteSoldierResId = getResources().getIdentifier("white_solider", "drawable", requireContext().getPackageName());
        brownSoldierResId = getResources().getIdentifier("brown_solider", "drawable", requireContext().getPackageName());
        heightInDp = (int) (32 * getResources().getDisplayMetrics().density);

        //מיפוי המשולשים בלוח
        for (int i = 0; i < 24; i++) {
            String layoutID = "lL" + (i + 1);
            int resID = getResources().getIdentifier(layoutID, "id", getActivity().getPackageName());
            layouts[i] = v.findViewById(resID);

            // תיוג (Tag) של התצוגה עם האינדקס שלה
            layouts[i].setTag(i);
            layouts[i].setOnClickListener(this);
        }

        //מיפוי אזורים מיוחדים
        iLEat = v.findViewById(R.id.iLEat);
        iLEat.setTag(-1); // 1- מייצג את הבר (חיילים אכולים)
        iLEat.setOnClickListener(this);

        lOutWhite = v.findViewById(R.id.lOutW);
        lOutBrown = v.findViewById(R.id.lOutB);

        //מיפוי קוביות וכפתורים
        imgC1 = v.findViewById(R.id.imgC1);
        imgC2 = v.findViewById(R.id.imgC2);
        imgC3 = v.findViewById(R.id.imgC3);
        imgC4 = v.findViewById(R.id.imgC4);
        imgCubes = v.findViewById(R.id.imgCubes);
        imgCubes.setOnClickListener(this);

        btnTakeOut = v.findViewById(R.id.btnTakeOut);
        btnTakeOut.setOnClickListener(this);

        restart = v.findViewById(R.id.btnRestart);
        restart.setOnClickListener(view -> restartGame());

        tvPlayerW = v.findViewById(R.id.tvPlayerW);
        tvPlayerB = v.findViewById(R.id.tvPlayerB);

        if (getArguments() != null){
            tvPlayerW.setText(getArguments().getString("p1"," "));
            tvPlayerB.setText(getArguments().getString("p2"," "));
        }

        // התחלת המשחק
        restartGame();
        return v;
    }


    // מאפסת את כל נתוני המשחק למצב ההתחלתי
    private void restartGame() {
        for (int i = 0; i < 24; i++)
            board[i] = 0;
        eatenWhite = 0;
        eatenBrown = 0;
        outWhite = 0;
        outBrown = 0;

        turn = "white";
        selectedTriangle = -2;
        clearDice();

        setupStartingPosition();
        updateUI();
        Toast.makeText(getContext(), "המשחק התחיל! תור הלבן", Toast.LENGTH_SHORT).show();
    }

     // מגדירה את המצב המתמטי ההתחלתי של לוח שש-בש
    private void setupStartingPosition() {
        board[23] = 2;   // לבן מתחיל מלמעלה
        board[12] = 5;
        board[7] = 3;
        board[5] = 5;

        board[0] = -2;   // חום מתחיל מלמטה (מספרים שליליים)
        board[11] = -5;
        board[16] = -3;
        board[18] = -5;
    }


     // מציירת את הלוח מחדש לפי מצב המערך בזיכרון צובעת בירוק את המהלכים האפשריים
    private void updateUI() {
        // ציור המשולשים והדגשת יעדים אפשריים
        for (int i = 0; i < 24; i++) {
            layouts[i].removeAllViews();
            layouts[i].setBackgroundColor(Color.TRANSPARENT);

            if (board[i] > 0) {
                addSoldierViews(layouts[i], whiteSoldierResId, board[i]);
            } else if (board[i] < 0) {
                addSoldierViews(layouts[i], brownSoldierResId, Math.abs(board[i]));
            }

            boolean isPossibleDestination = false;

            // אם המשתמש בחר חייל, בודקים אם אפשר ללכת למשולש הנוכחי
            if (selectedTriangle != -2) {
                int distance = calculateDistance(selectedTriangle, i);
                if (isMoveValid(i, distance)) {
                    isPossibleDestination = true;
                }
            }

            // צביעת המשולש
            if (isPossibleDestination) {
                layouts[i].setBackgroundColor(Color.parseColor("#675be851")); // ירוק (יעד אפשרי)
            } else if (i == selectedTriangle) {
                layouts[i].setBackgroundColor(Color.parseColor("#330000FF")); // כחול חלש (החייל שנבחר)
            }
        }

        //  ציור הבר (אכולים)
        iLEat.removeAllViews();
        addSoldierViews(iLEat, whiteSoldierResId, eatenWhite);
        addSoldierViews(iLEat, brownSoldierResId, eatenBrown);

        if (selectedTriangle == -1) {
            iLEat.setBackgroundColor(Color.parseColor("#330000FF"));
        } else {
            iLEat.setBackgroundColor(Color.TRANSPARENT);
        }

        //  ציור אזור ההוצאה החוצה
        lOutWhite.removeAllViews();
        lOutBrown.removeAllViews();
        addSoldierViews(lOutWhite, whiteSoldierResId, outWhite);
        addSoldierViews(lOutBrown, brownSoldierResId, outBrown);

        btnTakeOut.setVisibility(canTakeOut() && selectedTriangle >= 0 ? View.VISIBLE : View.INVISIBLE);
    }


     // פונקציית עזר להוספת חיילים
    private void addSoldierViews(LinearLayout layout, int resId, int count) {
        for (int i = 0; i < count; i++) {
            ImageView soldier = new ImageView(getContext());
            soldier.setImageResource(resId);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightInDp);
            soldier.setLayoutParams(params);
            layout.addView(soldier);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.imgCubes) {
            throwCubes();
            return;
        }

        if (view.getId() == R.id.btnTakeOut) {
            handleBearOff();
            return;
        }

        if (view.getTag() != null) {
            int clickedIndex = (int) view.getTag();
            handleBoardClick(clickedIndex);
        }
    }

     //הליבה של לוגיקת המשחק בעת לחיצה על הלוח
    private void handleBoardClick(int targetIndex) {
        if (hasNoDiceAvailable()) return;

        // מקרה 1: בחירת חייל
        if (selectedTriangle == -2) {
            if (isValidSelection(targetIndex)) {
                selectedTriangle = targetIndex;
                updateUI();
            } else {
                Toast.makeText(getContext(), "בחירה לא חוקית", Toast.LENGTH_SHORT).show();
            }
        }
        // מקרה 2: ניסיון הזזת חייל ליעד
        else {
            if (targetIndex == selectedTriangle) {
                selectedTriangle = -2;
                updateUI();
                return;
            }

            int requiredDistance = calculateDistance(selectedTriangle, targetIndex);
            if (isMoveValid(targetIndex, requiredDistance)) {
                executeMove(selectedTriangle, targetIndex, requiredDistance);
            } else {
                Toast.makeText(getContext(), "תזוזה לא חוקית בדוק את הקוביות שלך", Toast.LENGTH_SHORT).show();
                selectedTriangle = -2;
                updateUI();
            }
        }
    }


     // ביצוע מתמטי של תזוזה, ניצול קוביה ועדכון הלוח
    private void executeMove(int fromIndex, int toIndex, int diceUsed) {
        // 1. הסרה ממעגל המקור
        if (fromIndex == -1) {
            if (turn.equals("white")) eatenWhite--;
            else eatenBrown--;
        } else {
            if (turn.equals("white")) board[fromIndex]--;
            else board[fromIndex]++;
        }

        // 2. טיפול באכילה
        if (turn.equals("white") && board[toIndex] == -1) {
            board[toIndex] = 0;
            eatenBrown++;
        } else if (turn.equals("brown") && board[toIndex] == 1) {
            board[toIndex] = 0;
            eatenWhite++;
        }

        // 3. הוספה ליעד
        if (turn.equals("white")) board[toIndex]++;
        else board[toIndex]--;

        // 4. ניצול קוביה ועדכון
        consumeDie(diceUsed);
        selectedTriangle = -2;
        updateUI();

        // 5. בדיקת סוף תור או חסימה
        if (hasNoDiceAvailable()) {
            changeTurn();
        } else {
            handlePotentialBlock();
        }
    }


     // מוודאת שלשחקן מותר לבחור את המשולש הזה
     private boolean isValidSelection(int index) {
         if (turn.equals("white")) {
             if (eatenWhite > 0) return index == -1;
             return index >= 0 && index < 24 && board[index] > 0;
         } else {
             if (eatenBrown > 0) return index == -1;
             return index >= 0 && index < 24 && board[index] < 0;
         }
     }


     // חישוב מרחק מוחלט בהתחשב בכיוון השחקן
    private int calculateDistance(int from, int to) {
        if (turn.equals("white")) {
            if (from == -1) return 24 - to;
            return from - to;
        } else {
            if (from == -1) return to + 1;
            return to - from;
        }
    }

     // בודקת אם היעד לא חסום ואם יש קוביה שמתאימה
    private boolean isMoveValid(int to, int distance) {
        if (distance <= 0) return false;
        if (!hasMatchingDie(distance)) return false;

        if (turn.equals("white")) {
            return board[to] >= -1;
        } else {
            return board[to] <= 1;
        }
    }


     // זריקת קוביות
    private void throwCubes() {
        if (!hasNoDiceAvailable()) return;

        clearDice();
        int d1 = rnd.nextInt(6) + 1;
        int d2 = rnd.nextInt(6) + 1;

        if (d1 == d2) {
            availableDice[0] = d1;
            availableDice[1] = d1;
            availableDice[2] = d1;
            availableDice[3] = d1;
        } else {
            availableDice[0] = d1;
            availableDice[1] = d2;
        }

        updateDiceUI();

        // בדיקת חסימות אחרי זריקה
        handlePotentialBlock();
    }


     // עדכון תמונות הקוביות
    private void updateDiceUI() {
        ImageView[] imgs = {imgC1, imgC2, imgC3, imgC4};
        for (int i = 0; i < 4; i++) {
            if (availableDice[i] > 0) {
                imgs[i].setImageResource(getResources().getIdentifier("cube" + availableDice[i], "drawable", getActivity().getPackageName()));
                imgs[i].setVisibility(View.VISIBLE);
            } else {
                imgs[i].setVisibility(View.INVISIBLE);
            }
        }
    }


     // בודקת אם מותר לשחקן להוציא חיילים החוצה
    private boolean canTakeOut() {
        if (turn.equals("white")) {
            if (eatenWhite > 0) return false;
            for (int i = 6; i < 24; i++) {
                if (board[i] > 0) return false;
            }
            return true;
        } else {
            if (eatenBrown > 0) return false;
            for (int i = 0; i < 18; i++) {
                if (board[i] < 0) return false;
            }
            return true;
        }
    }


     // לוגיקת הוצאת חייל (Take Out)
    private void handleBearOff() {
        if (selectedTriangle < 0 || !canTakeOut()) return;

        int requiredDistance = turn.equals("white") ? (selectedTriangle + 1) : (24 - selectedTriangle);

        if (hasMatchingDie(requiredDistance)) {
            executeBearOff(requiredDistance);
        } else {
            int largestDie = getLargestAvailableDie();
            if (largestDie > requiredDistance && isFurthestChecker(selectedTriangle)) {
                executeBearOff(largestDie);
            } else {
                Toast.makeText(getContext(), "צריך קוביה מדויקת, או שזה חייב להיות החייל הכי רחוק שלך", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void executeBearOff(int dieUsed) {
        if (turn.equals("white")) {
            board[selectedTriangle]--;
            outWhite++;
        } else {
            board[selectedTriangle]++;
            outBrown++;
        }
        consumeDie(dieUsed);
        selectedTriangle = -2;
        updateUI();
        checkWinCondition();

        if (hasNoDiceAvailable()) {
            changeTurn();
        } else {
            handlePotentialBlock();
        }
    }

    // האם החייל הזה הכי רחוק מהקצה
    private boolean isFurthestChecker(int index) {
        if (turn.equals("white")) {
            for (int i = index + 1; i <= 5; i++) if (board[i] > 0) return false;
        } else {
            for (int i = index - 1; i >= 18; i--) if (board[i] < 0) return false;
        }
        return true;
    }


     // עוברת על הלוח ובודקת אם יש לשחקן מהלך חוקי כלשהו לבצע
    private boolean hasAnyValidMove() {
        if (hasNoDiceAvailable()) return false;

        boolean isWhite = turn.equals("white");

        // 1. בדיקת יציאה מהבר (אם יש אכולים)
        if ((isWhite && eatenWhite > 0) || (!isWhite && eatenBrown > 0)) {
            for (int die : availableDice) {
                if (die > 0) {
                    int to = isWhite ? (24 - die) : (die - 1);
                    if (to >= 0 && to < 24 && isMoveValid(to, die)) return true;
                }
            }
            return false;
        }

        // 2. בדיקת מהלכים רגילים על הלוח
        for (int i = 0; i < 24; i++) {
            if ((isWhite && board[i] > 0) || (!isWhite && board[i] < 0)) {
                for (int die : availableDice) {
                    if (die > 0) {
                        int to = isWhite ? (i - die) : (i + die);

                        if (to >= 0 && to < 24 && isMoveValid(to, die)) return true;

                        if (canTakeOut()) {
                            int reqDist = isWhite ? (i + 1) : (24 - i);
                            if (die == reqDist || (die > reqDist && isFurthestChecker(i))) return true;
                        }
                    }
                }
            }
        }
        return false;
    }


     // אם השחקן חסום, מציגה הודעה ומעבירה תור אוטומטית אחרי השהיה קלה
    private void handlePotentialBlock() {
        if (!hasNoDiceAvailable() && !hasAnyValidMove()) {
            Toast.makeText(getContext(), "אין מהלכים אפשריים! התור עובר", Toast.LENGTH_SHORT).show();

            turnPassRunnable = () -> changeTurn();
            handler.postDelayed(turnPassRunnable, 1500); // ממתין שניה וחצי לפני שמעביר
        }
    }

    //האם יש קוביה שמתאימה למרחק המבוקש
    private boolean hasMatchingDie(int val) {
        for (int die : availableDice) if (die == val) return true;
        return false;
    }

    // מחזירה את הקוביה הגדולה ביותר שעדיין זמינה
    private int getLargestAvailableDie() {
        int max = 0;
        for (int die : availableDice) if (die > max) max = die;
        return max;
    }

    // מסירה קוביה מהמערך הזמין לאחר שימוש ומעדכנת את התצוגה
    private void consumeDie(int val) {
        for (int i = 0; i < 4; i++) {
            if (availableDice[i] == val) {
                availableDice[i] = 0;
                break;
            }
        }
        updateDiceUI();
    }

    // בודקת אם אין קוביות זמינות בכלל (כלומר, התור צריך לעבור)
    private boolean hasNoDiceAvailable() {
        for (int die : availableDice) if (die > 0) return false;
        return true;
    }

    // מאפסת את הקוביות הזמינות ומעדכנת את התצוגה (משמשת בתחילת כל תור)
    private void clearDice() {
        for (int i = 0; i < 4; i++) availableDice[i] = 0;
        updateDiceUI();
    }

    // מחליפה את התור בין לבן לחום, מאפסת בחירת חיילים וקוביות, ומעדכנת את התצוגה
    private void changeTurn() {
        turn = turn.equals("white") ? "brown" : "white";
        selectedTriangle = -2;
        clearDice();
        updateUI();
        Toast.makeText(getContext(), "התור עבר ל: " + turn, Toast.LENGTH_SHORT).show();
    }

    //מערכת ניצחון וניקוד

    // בודקת אם אחד השחקנים הוציא את כל חייליו החוצה ומפעילה את תהליך הניצחון
    private void checkWinCondition() {
        if (outWhite == 15) {
            triggerWin("white", "brown");
        } else if (outBrown == 15) {
            triggerWin("brown", "white");
        }
    }

    // מחשבת את סוג הניצחון והנקודות בהתאם לנסיבות הספציפיות של סיום המשחק, ומציגה דיאלוג עם התוצאה גם מעדכנת את הלידרבורד ב-Firestore
    private void triggerWin(String winner, String loser) {
        int points = calculatePoints(winner, loser);
        String type = getWinType(points);

        String winnerName = winner.equals("white") ?
                getArguments().getString("p1", "Player 1") :
                getArguments().getString("p2", "Player 2");

        updateLeaderboard(winnerName, points);

        builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("המשחק נגמר!");
        builder.setMessage(winner + " ניצח " + type + "! (" + points + " נקודות)");
        builder.setPositiveButton("משחק חדש", (dialog, which) -> {
            dialog.dismiss();
            restartGame();
        });
        builder.setNegativeButton("תפריט ראשי", (dialog, which) -> {
            dialog.dismiss();
            intent = new Intent(requireContext(), MainActivity.class);
            startActivity(intent);
        });
        builder.setCancelable(false);
        builder.show();
    }

    private String getWinType(int points) {
        switch (points) {
            case 4: return "ניצחון ענק! מארס כוכבי! ⭐⭐⭐⭐";
            case 3: return "מארס תורכי! 🎩⭐⭐";
            case 2: return "מארס! 🎲⭐";
            default: return "ניצחון רגיל";
        }
    }

    private int calculatePoints(String winner, String loser) {
        int loserOut = loser.equals("white") ? outWhite : outBrown;
        if (loserOut > 0) return 1;

        int loserEaten = loser.equals("white") ? eatenWhite : eatenBrown;
        if (loserEaten > 0) return 4;

        boolean inWinnerHome = false;
        if (winner.equals("white")) {
            for (int i = 0; i <= 5; i++) if (board[i] < 0) inWinnerHome = true;
        } else {
            for (int i = 18; i <= 23; i++) if (board[i] > 0) inWinnerHome = true;
        }

        if (inWinnerHome) return 3;
        return 2;
    }

    private void updateLeaderboard(String winnerName, int pointsToAdd) {
        if (winnerName == null || winnerName.isEmpty()) return;

        String documentId = winnerName.trim().toLowerCase();
        Map<String, Object> data = new HashMap<>();
        data.put("username", winnerName.trim());
        data.put("points", FieldValue.increment(pointsToAdd));
        data.put("timestamp", FieldValue.serverTimestamp());

        db.collection("leaderboard").document(documentId)
                .set(data, com.google.firebase.firestore.SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firestore", "Leaderboard updated");
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // מונע קריסה אם המשתמש יוצא מהמסך בזמן שמתבצעת השהייה
        if (turnPassRunnable != null) {
            handler.removeCallbacks(turnPassRunnable);
        }
    }
}