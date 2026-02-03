package com.example.backgammonfinal;

import java.util.Random;


import android.annotation.SuppressLint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        }
        iLEat = (LinearLayout) v.findViewById(R.id.iLEat);
        iLEat.setOnClickListener(this);

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

        return v;

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
        boolean cantMove = true;
        boolean[] canMove = new boolean[24];
        for (int i = 0; i < 24; i++) {
            if (rndCube1 != 0) {
                paintLinear(selectLinear(rndCube1, i, turn), layouts, i, canMove, iLEat, 0, turn);
                for (int c = 0; c < 24; c++) {
                    layouts[c].setBackgroundColor(Color.TRANSPARENT);
                }
            }
            if (rndCube2 != 0) {
                paintLinear(selectLinear(rndCube2, i, turn), layouts, i, canMove, iLEat, 0, turn);
                for (int c = 0; c < 24; c++) {
                    layouts[c].setBackgroundColor(Color.TRANSPARENT);
                }
            }
        }
        for (int j = 0; j < 24; j++) {
            if (canMove[j]) {
                cantMove = false;
            }
        }
        if (cantMove) {
            rndCube1 = 0;
            rndCube2 = 0;
            imgC1.setVisibility(View.INVISIBLE);
            imgC2.setVisibility(View.INVISIBLE);
            imgC3.setVisibility(View.INVISIBLE);
            imgC4.setVisibility(View.INVISIBLE);
            for (int i = 0; i < 24; i++) {
                layouts[i].setBackgroundColor(Color.TRANSPARENT);
            }
            changTurn(turn);
        }
        return cantMove;
    }

    private void deleteCube(int selected, int j, String turn) {
        if (selected == -1) {
            if (turn.equals("white")) {
                if (24 - j == rndCube1) {
                    if (imgC4.getVisibility() == View.VISIBLE) {
                        imgC4.setVisibility(View.INVISIBLE);
                    } else if (imgC3.getVisibility() == View.VISIBLE) {
                        imgC3.setVisibility(View.INVISIBLE);
                    } else {
                        imgC1.setVisibility(View.INVISIBLE);
                        rndCube1 = 0;
                    }
                } else if (24 - j == rndCube2) {
                    imgC2.setVisibility(View.INVISIBLE);
                    rndCube2 = 0;
                }
            } else if (turn.equals("brown")) {
                if (j + 1 == rndCube1) {
                    if (imgC4.getVisibility() == View.VISIBLE) {
                        imgC4.setVisibility(View.INVISIBLE);
                    } else if (imgC3.getVisibility() == View.VISIBLE) {
                        imgC3.setVisibility(View.INVISIBLE);
                    } else {
                        imgC1.setVisibility(View.INVISIBLE);
                        rndCube1 = 0;
                    }
                } else if (j + 1 == rndCube2) {
                    imgC2.setVisibility(View.INVISIBLE);
                    rndCube2 = 0;
                }
            }
        } else {
            if (turn.equals("white")) {
                if (selected - j == rndCube1) {
                    if (imgC4.getVisibility() == View.VISIBLE) {
                        imgC4.setVisibility(View.INVISIBLE);
                    } else if (imgC3.getVisibility() == View.VISIBLE) {
                        imgC3.setVisibility(View.INVISIBLE);
                    } else {
                        imgC1.setVisibility(View.INVISIBLE);
                        rndCube1 = 0;
                    }
                } else if (selected - j == rndCube2) {
                    imgC2.setVisibility(View.INVISIBLE);
                    rndCube2 = 0;
                }
            } else if (turn.equals("brown")) {
                if (j - selected == rndCube1) {
                    if (imgC4.getVisibility() == View.VISIBLE) {
                        imgC4.setVisibility(View.INVISIBLE);
                    } else if (imgC3.getVisibility() == View.VISIBLE) {
                        imgC3.setVisibility(View.INVISIBLE);
                    } else {
                        imgC1.setVisibility(View.INVISIBLE);
                        rndCube1 = 0;
                    }
                } else if (j - selected == rndCube2) {
                    imgC2.setVisibility(View.INVISIBLE);
                    rndCube2 = 0;
                }
            }
        }
    }

    private void changTurn(String turn) {
        boolean changeTurn = false;
        if (imgC1.getVisibility() == View.INVISIBLE && imgC2.getVisibility() == View.INVISIBLE) {
            if (turn.equals("white")) {
                turn = "brown";
                changeTurn = true;
            }
            if (turn.equals("brown") && !changeTurn) {
                turn = "white";
            }
        }
    }


    @Override
    public void onClick(View view) {

        String turn = "white";
        boolean[] canMove = new boolean[24];
        int selected = -2;

        int eatIndex = -1;

        for (int i = 0; i < 24; i++) {
            ImageView imgColor = (ImageView) (layouts[i].getChildAt(0));
            int resId = getResources().getIdentifier((turn + "_solider"), "drawable", requireContext().getPackageName());
            if (imgColor != null && imgColor.getDrawable().getConstantState() == ContextCompat.getDrawable(requireContext(), resId).getConstantState() && layouts[i].getChildAt(0) == null) {
                layouts[i].setClickable(false);
            }
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
                    for (int k = 0; k < 24; k++) {
                        layouts[k].setClickable(true);
                    }
                    for (int f = 0; f < 24; f++) {
                        canMove[f] = false;
                    }

                    for (int i = 0; i < 24; i++) {
                        layouts[i].setBackgroundColor(Color.TRANSPARENT);
                    }
                    selected = -2;
                    changTurn(turn);
                } else {
                    moveGreen(layouts, m, canMove, selected, iLEat, turn);
                    for (int f = 0; f < 24; f++) {
                        canMove[f] = false;
                    }

                    for (int i = 0; i < 24; i++) {
                        layouts[i].setBackgroundColor(Color.TRANSPARENT);
                    }
                    selected = -2;
                    changTurn(turn);
                }
            }
        }
        for (int i = 0; i < 24; i++) {
            layouts[i].setBackgroundColor(Color.TRANSPARENT);
        }

        for (int i = 0; i < 24; i++) {
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
