package com.example.backgammonfinal;

import java.util.Random;


import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * A simple {@link Fragment} subclass.
 * <p>
 * create an instance of this fragment.
 */
public class Game extends Fragment implements View.OnClickListener {

    private LinearLayout[] layouts;

    private LinearLayout layout, iLEat;
    private ImageView imgCubes, imgC1, imgC2, imgC3, imgC4;
    private Random rnd = new Random();
    private int rndCube1, rndCube2;

    private Button restart;

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
        imgCubes = (ImageView) v.findViewById(R.id.imgCubes);
        imgCubes.setOnClickListener(this);
        restart = (Button) v.findViewById(R.id.btnRestart);
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restartGame();
            }
        });

        setupStartingPosition();

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

    private void addSoldier(int layoutIndex, String color, int count) {
        int resId = getResources().getIdentifier(color + "_solider", "drawable", requireContext().getPackageName());

        for (int i = 0; i < count; i++) {
            ImageView soldier = new ImageView(getContext());
            soldier.setImageResource(resId);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)(32* getResources().getDisplayMetrics().density));
            soldier.setLayoutParams(params);

            layouts[layoutIndex].addView(soldier);
        }
    }

    private void setupStartingPosition() {
        // לבן (White)
        addSoldier(0, "brown", 2);
        addSoldier(11, "brown", 5);
        addSoldier(16, "brown", 3);
        addSoldier(18, "brown", 5);

        // חום (Brown)
        addSoldier(23, "white", 2);
        addSoldier(12, "white", 5);
        addSoldier(7, "white", 3);
        addSoldier(5, "white", 5);
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

        boolean canDoAnything = false;
        boolean[] tempMove = new boolean[24];
        int resIdSolider = getResources().getIdentifier((turn + "_solider"), "drawable", requireContext().getPackageName());

        // שלב א': בדיקה אם יש חייל אכול לשחקן הנוכחי
        boolean hasEaten = false;
        for (int j = 0; j < iLEat.getChildCount(); j++) {
            ImageView img = (ImageView) iLEat.getChildAt(j);
            if (img.getDrawable().getConstantState() == ContextCompat.getDrawable(requireContext(), resIdSolider).getConstantState()) {
                hasEaten = true;
                break;
            }
        }

        if (hasEaten) {
            // אם יש אכול, הבדיקה היחידה שקובעת היא אם הוא יכול להיכנס ללוח (אינדקס 1-)
            if (rndCube1 != 0) paintLinear(selectLinear(rndCube1, -1, turn), layouts, -1, tempMove, iLEat, 0, turn);
            if (rndCube2 != 0) paintLinear(selectLinear(rndCube2, -1, turn), layouts, -1, tempMove, iLEat, 0, turn);
        } else {
            // אם אין אכולים, בודקים את כל החיילים על הלוח כרגיל
            for (int i = 0; i < 24; i++) {
                if (layouts[i].getChildCount() > 0) {
                    ImageView img = (ImageView) layouts[i].getChildAt(0);
                    if (img.getDrawable().getConstantState() == ContextCompat.getDrawable(requireContext(), resIdSolider).getConstantState()) {
                        if (rndCube1 != 0) paintLinear(selectLinear(rndCube1, i, turn), layouts, i, tempMove, iLEat, 0, turn);
                        if (rndCube2 != 0) paintLinear(selectLinear(rndCube2, i, turn), layouts, i, tempMove, iLEat, 0, turn);
                    }
                }
            }
        }

        // בדיקה אם נמצא מהלך חוקי כלשהו
        for (int c = 0; c < 24; c++) {
            if (tempMove[c]) canDoAnything = true;
            layouts[c].setBackgroundColor(Color.TRANSPARENT); // איפוס צבעי הבדיקה
        }

        if (!canDoAnything) {
            // אם הגענו לכאן, סימן שאין מהלכים חוקיים - מאפסים קוביות ומעבירים תור
            rndCube1 = 0;
            rndCube2 = 0;
            imgC1.setVisibility(View.INVISIBLE);
            imgC2.setVisibility(View.INVISIBLE);
            imgC3.setVisibility(View.INVISIBLE);
            imgC4.setVisibility(View.INVISIBLE);
            //changTurn();// קריאה להחלפת תור/
            return true;
        }
        return false;
    }

    private void deleteCube(int selected, int j, String turn) {
        int distance;
        if (selected == -1) {
            distance = (turn.equals("white")) ? (24 - j) : (j + 1);
        } else {
            distance = (turn.equals("white")) ? (selected - j) : (j - selected);
        }

        if (rndCube1 == rndCube2) {
            if (imgC4.getVisibility() == View.VISIBLE) imgC4.setVisibility(View.INVISIBLE);
            else if (imgC3.getVisibility() == View.VISIBLE) imgC3.setVisibility(View.INVISIBLE);
            else if (imgC1.getVisibility() == View.VISIBLE) imgC1.setVisibility(View.INVISIBLE);
            else if (imgC2.getVisibility() == View.VISIBLE) {
                imgC2.setVisibility(View.INVISIBLE);
                rndCube1 = 0;
                rndCube2 = 0;
            }
        } else {
            if (distance == rndCube1) {
                imgC1.setVisibility(View.INVISIBLE);
                rndCube1 = 0;
            } else if (distance == rndCube2) {
                imgC2.setVisibility(View.INVISIBLE);
                rndCube2 = 0;
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
