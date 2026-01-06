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

    private LinearLayout[] layouts;//= {R.id.lL1, R.id.lL2, R.id.lL3, R.id.lL4, R.id.lL5, R.id.lL6, R.id.lL7, R.id.lL8, R.id.lL9, R.id.lL10, R.id.lL11, R.id.lL12, R.id.lL13, R.id.lL14, R.id.lL15, R.id.lL16, R.id.lL17, R.id.lL18, R.id.lL19, R.id.lL20, R.id.lL21, R.id.lL22, R.id.lL23, R.id.lL24};

    private LinearLayout layout, iLEat;
    private ImageView imgCubes, imgC1, imgC2, imgC3, imgC4;

    //    private LinearLayout lL1, lL2, lLwhite3, lLwhite4, lLwhite5, lLwhite6, lLwhite7, lLwhite8, lLwhite9, lLwhite10, lLwhite11, lLwhite12, lLBlack1, lLBlack2, lLBlack3, lLBlack4, lLBlack5, lLBlack6, lLBlack7, lLBlack8, lLBlack9, lLBlack10, lLBlack11, lLBlack12;
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

    private void clear(LinearLayout[] layouts, int i, String turn, boolean[] clear) {
        if (layouts[i].getChildAt(0) != null) {
            ImageView imChild = (ImageView) layouts[i].getChildAt(0);
            int resId = getResources().getIdentifier((turn + "_solider"), "drawable", requireContext().getPackageName());
            if (imChild.getDrawable().getConstantState() == ContextCompat.getDrawable(requireContext(), resId).getConstantState()) {
                if (turn.equals("white")) {
                    if (!(i - rndCube1 < 0) && rndCube1 != 0) {
                        if (layouts[i - rndCube1].getChildAt(0) == null) {
                            layouts[i - rndCube1].setBackgroundColor(Colors.GREEN.get());
                            clear[i - rndCube1] = true;
                        }
                    }
                    if (!(i - rndCube2 < 0) && rndCube2 != 0) {
                        if (layouts[i - rndCube2].getChildAt(0) == null) {
                            layouts[i - rndCube2].setBackgroundColor(Colors.GREEN.get());
                            clear[i - rndCube2] = true;
                        }
                    }
                }
                if (turn.equals("brown")) {
                    if (!(i + rndCube1 >= 24) && rndCube1 != 0) {
                        if (layouts[i + rndCube1].getChildAt(0) == null) {
                            layouts[i + rndCube1].setBackgroundColor(Colors.GREEN.get());
                            clear[i + rndCube1] = true;
                        }
                    }
                    if (!(i + rndCube2 >= 24) && rndCube2 != 0) {
                        if (layouts[i + rndCube2].getChildAt(0) == null) {
                            layouts[i + rndCube2].setBackgroundColor(Colors.GREEN.get());
                            clear[i + rndCube2] = true;
                        }
                    }
                }
            } else {
                Toast.makeText(getContext(), "it's not your turn", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void yourSoliders(LinearLayout[] layouts, int i, String turn, boolean[] yourSoliders) {
        if (layouts[i].getChildAt(0) != null) {
            ImageView imChild = (ImageView) layouts[i].getChildAt(0);
            int resId = getResources().getIdentifier((turn + "_solider"), "drawable", requireContext().getPackageName());
            if (imChild.getDrawable().getConstantState() == ContextCompat.getDrawable(requireContext(), resId).getConstantState()) {
                if (turn.equals("white")) {
                    if (!(i - rndCube1 < 0) && rndCube1 != 0) {
                        if (layouts[i - rndCube1].getChildAt(0) != null) {
                            ImageView imgSelect1 = (ImageView) (layouts[i - rndCube1].getChildAt(0));
                            if (imChild.getDrawable().getConstantState() == imgSelect1.getDrawable().getConstantState()) {
                                layouts[i - rndCube1].setBackgroundColor(Colors.GREEN.get());
                                yourSoliders[i - rndCube1] = true;
                            }
                        }
                    }
                    if (!(i - rndCube2 < 0) && rndCube2 != 0) {
                        if (layouts[i - rndCube2].getChildAt(0) != null) {
                            ImageView imgSelect2 = (ImageView) (layouts[i - rndCube2].getChildAt(0));
                            if (imChild.getDrawable().getConstantState() == imgSelect2.getDrawable().getConstantState()) {
                                layouts[i - rndCube2].setBackgroundColor(Colors.GREEN.get());
                                yourSoliders[i - rndCube2] = true;
                            }
                        }
                    }
                }
                if (turn.equals("brown")) {
                    if (!(i + rndCube1 >= 24) && rndCube1 != 0) {
                        if (layouts[i + rndCube1].getChildAt(0) != null) {
                            ImageView imgSelect1 = (ImageView) (layouts[i + rndCube1].getChildAt(0));
                            if (imChild.getDrawable().getConstantState() == imgSelect1.getDrawable().getConstantState()) {
                                layouts[i + rndCube1].setBackgroundColor(Colors.GREEN.get());
                                yourSoliders[i + rndCube1] = true;
                            }
                        }
                    }
                    if (!(i + rndCube2 >= 24) && rndCube2 != 0) {
                        if (layouts[i + rndCube2].getChildAt(0) != null) {
                            ImageView imgSelect2 = (ImageView) (layouts[i + rndCube2].getChildAt(0));
                            if (imChild.getDrawable().getConstantState() == imgSelect2.getDrawable().getConstantState()) {
                                layouts[i + rndCube2].setBackgroundColor(Colors.GREEN.get());
                                yourSoliders[i + rndCube2] = true;
                            }
                        }
                    }
                }
            } else {
                Toast.makeText(getContext(), "it's not your turn", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void eatAnother(LinearLayout[] layouts, int i, String turn, boolean[] eat) {
        ImageView imChild = (ImageView) layouts[i].getChildAt(0);
        int resId = getResources().getIdentifier((turn + "_solider"), "drawable", requireContext().getPackageName());
        if (imChild.getDrawable().getConstantState() == ContextCompat.getDrawable(requireContext(), resId).getConstantState()) {
            if (turn.equals("white")) {
                if (!(i - rndCube1 < 0) && rndCube1 != 0) {
                    if (layouts[i - rndCube1].getChildAt(0) != null) {
                        ImageView imgSelect1 = (ImageView) (layouts[i - rndCube1].getChildAt(0));
                        if (imChild.getDrawable().getConstantState() != imgSelect1.getDrawable().getConstantState() && layouts[i - rndCube1].getChildCount() == 1) {
                            layouts[i - rndCube1].setBackgroundColor(Colors.GREEN.get());
                            eat[i - rndCube1] = true;
                        }
                    }
                }
                if (!(i - rndCube2 < 0) && rndCube2 != 0) {
                    if (layouts[i - rndCube2].getChildAt(0) != null) {
                        ImageView imgSelect2 = (ImageView) (layouts[i - rndCube2].getChildAt(0));
                        if (imChild.getDrawable().getConstantState() != imgSelect2.getDrawable().getConstantState() && layouts[i - rndCube2].getChildCount() == 1) {
                            layouts[i - rndCube2].setBackgroundColor(Colors.GREEN.get());
                            eat[i - rndCube2] = true;
                        }
                    }
                }
            }
            if (turn.equals("brown")){
                if (!(i + rndCube1 >= 24) && rndCube1 != 0) {
                    if (layouts[i + rndCube1].getChildAt(0) != null) {
                        ImageView imgSelect1 = (ImageView) (layouts[i + rndCube1].getChildAt(0));
                        if (imChild.getDrawable().getConstantState() != imgSelect1.getDrawable().getConstantState() && layouts[i + rndCube1].getChildCount() == 1) {
                            layouts[i + rndCube1].setBackgroundColor(Colors.GREEN.get());
                            eat[i + rndCube1] = true;
                        }
                    }
                }
                if (!(i + rndCube2 >= 24) && rndCube2 != 0) {
                    if (layouts[i + rndCube2].getChildAt(0) != null) {
                        ImageView imgSelect2 = (ImageView) (layouts[i + rndCube2].getChildAt(0));
                        if (imChild.getDrawable().getConstantState() != imgSelect2.getDrawable().getConstantState() && layouts[i + rndCube2].getChildCount() == 1) {
                            layouts[i + rndCube2].setBackgroundColor(Colors.GREEN.get());
                            eat[i + rndCube2] = true;
                        }
                    }
                }
            }
        } else{
            Toast.makeText(getContext(), "it's not your turn", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteCube(int selected, int j, String turn) {
        if (turn.equals("white")){
            if (selected - j == rndCube1){
                if (imgC4.getVisibility() == View.VISIBLE){
                    imgC4.setVisibility(View.INVISIBLE);
                } else if (imgC3.getVisibility() == View.VISIBLE) {
                    imgC3.setVisibility(View.INVISIBLE);
                } else {
                    imgC1.setVisibility(View.INVISIBLE);
                }
            } else if (selected - j == rndCube2) {
                imgC2.setVisibility(View.INVISIBLE);
            }
        } else if (turn.equals("brown")) {
            if (j - selected == rndCube1){
                if (imgC4.getVisibility() == View.VISIBLE){
                    imgC4.setVisibility(View.INVISIBLE);
                } else if (imgC3.getVisibility() == View.VISIBLE) {
                    imgC3.setVisibility(View.INVISIBLE);
                } else {
                    imgC1.setVisibility(View.INVISIBLE);
                    rndCube1 = 0;
                }
            }
            if (j - selected == rndCube2) {
                rndCube2 = 0;
                imgC2.setVisibility(View.INVISIBLE);
            }
        }
    }

    String turn = "white";
    boolean[] canMove = new boolean[24];
    int selected = -1;

    @Override
    public void onClick(View view) {
        //throw cubes
        if (imgCubes.getId() == view.getId()) {
            throwCubes();
        }

        if (imgC1.getVisibility() == View.INVISIBLE && imgC2.getVisibility() == View.INVISIBLE){
            if (turn.equals("white"))
                turn = "brown";
            if (turn.equals("brown"))
                turn = "white";
        }

        for (int i = 0; i < 24; i++) {
            layouts[i].setBackgroundColor(Color.TRANSPARENT);
        }

        for (int j = 0; j < 24; j++) {
            if (selected != -1 && canMove[j] && layouts[selected].getChildAt(0) != null && layouts[j].getId() == view.getId()){
                ImageView imgMove = (ImageView) (layouts[selected].getChildAt(0));
                layouts[selected].removeViewAt(0);
                layouts[j].addView(imgMove);
                for (int i = 0; i < 24; i++) {
                    layouts[i].setBackgroundColor(Color.TRANSPARENT);
                }
                deleteCube(selected,j,turn);
                selected = -1;
                for (int h = 0; h < 24; h++) {
                    canMove[h] = false;
                }
                return;
            }
        }


        for (int i = 0; i < 24; i++) {
            if (layouts[i].getId() == view.getId()){
                selected = i;
                clear(layouts,i,turn,canMove);
                yourSoliders(layouts,i,turn,canMove);
                break;
            }
        }
    }


//    private void moveToYourOtherSoliders(LinearLayout[] layouts, int i, String turn, LinearLayout iLEat) {
//        for (int j = 0; j < 24; j++) {
//            layouts[j].setBackgroundColor(Color.TRANSPARENT);
//        }
//        LinearLayout selectedLayout1 = layouts[i];
//        LinearLayout selectedLayout2 = layouts[i];
////        Boolean eat = false;
////        for (int j = 0; iLEat.getChildAt(j) != null; j++) {
////            int resId = getResources().getIdentifier((turn + "_solider"), "drawable", requireContext().getPackageName());
////            ImageView imgEat = (ImageView) iLEat.getChildAt(j);
////            if (imgEat.getDrawable().getConstantState() == ContextCompat.getDrawable(requireContext(), resId).getConstantState()) {
////                eat = true;
////            }
////        }
////        if (!eat) {
//        ImageView imChild = (ImageView) layouts[i].getChildAt(0);
//        int resId = getResources().getIdentifier((turn + "_solider"), "drawable", requireContext().getPackageName());
//        if (imChild.getDrawable().getConstantState() == ContextCompat.getDrawable(requireContext(), resId).getConstantState()) {
//            if (turn.equals("white")) {
//                if (i - rndCube1 < 0 && i - rndCube2 < 0) {
//                    Toast.makeText(getContext(), "not all your soliders in your home", Toast.LENGTH_SHORT).show();
//                    return;
//                } else if (i - rndCube1 < 0) {
//                    Toast.makeText(getContext(), "you can move only step by cube 2", Toast.LENGTH_SHORT).show();
//                    selectedLayout1.setBackgroundColor(Color.RED);
//                    selectedLayout1 = null;
//                    selectedLayout2 = layouts[i - rndCube2];
//                } else if (i - rndCube2 < 0) {
//                    Toast.makeText(getContext(), "you can move only step by cube 1", Toast.LENGTH_SHORT).show();
//                    selectedLayout2.setBackgroundColor(Color.RED);
//                    selectedLayout1 = layouts[i - rndCube1];
//                    selectedLayout2 = null;
//                } else {
//                    if (rndCube1 != 0) {
//                        selectedLayout1 = layouts[i - rndCube1];
//                    } else selectedLayout1 = null;
//                    if (rndCube2 != 0) {
//                        selectedLayout2 = layouts[i - rndCube2];
//                    } else selectedLayout2 = null;
//                }
//            } else {
//                if (i + rndCube1 >= 24 && i + rndCube2 >= 24) {
//                    Toast.makeText(getContext(), "not all your soliders in your home", Toast.LENGTH_SHORT).show();
//                    return;
//                } else if (i + rndCube1 >= 24) {
//                    Toast.makeText(getContext(), "you can move only step by cube 2", Toast.LENGTH_SHORT).show();
//                    selectedLayout1.setBackgroundColor(Color.RED);
//                    selectedLayout1 = null;
//                    selectedLayout2 = layouts[i + rndCube2];
//                } else if (i + rndCube2 >= 24) {
//                    Toast.makeText(getContext(), "you can move only step by cube 1", Toast.LENGTH_SHORT).show();
//                    selectedLayout2.setBackgroundColor(Color.RED);
//                    selectedLayout1 = layouts[i + rndCube1];
//                    selectedLayout2 = null;
//                } else {
//                    if (rndCube1 != 0) {
//                        selectedLayout1 = layouts[i + rndCube1];
//                    } else selectedLayout1 = null;
//                    if (rndCube2 != 0) {
//                        selectedLayout2 = layouts[i + rndCube2];
//                    } else selectedLayout2 = null;
//                }
//            }
//
//            if (selectedLayout1 != null) {
//                if (selectedLayout1.getChildAt(0) == null) {
//                    selectedLayout1.setBackgroundColor(Colors.GREEN.get());
//                } else {
//                    ImageView imgSelect = (ImageView) (selectedLayout1.getChildAt(0));
//                    ImageView imgTap = (ImageView) (layouts[i].getChildAt(0));
//                    if (imgSelect != null && imgTap != null) {
//                        if (imgSelect.getDrawable().getConstantState() == imgTap.getDrawable().getConstantState()) {
//                            selectedLayout1.setBackgroundColor(Colors.GREEN.get());
//                        } else if (imgSelect.getDrawable().getConstantState() != imgTap.getDrawable().getConstantState() && selectedLayout1.getChildCount() == 1) {
//                            selectedLayout1.setBackgroundColor(Colors.GREEN.get());
//                        } else {
//                            selectedLayout1.setBackgroundColor(Color.RED);
//                        }
//                    }
//                }
//            }
//
//
//            if (selectedLayout2 != null) {
//                if (selectedLayout2.getChildAt(0) == null) {
//                    selectedLayout2.setBackgroundColor(Colors.GREEN.get());
//                } else {
//                    ImageView imgSelect = (ImageView) (selectedLayout2.getChildAt(0));
//                    ImageView imgTap = (ImageView) (layouts[i].getChildAt(0));
//                    if (imgSelect != null && imgTap != null) {
//                        if (imgSelect.getDrawable().getConstantState() == imgTap.getDrawable().getConstantState()) {
//                            selectedLayout2.setBackgroundColor(Colors.GREEN.get());
//                        } else if (imgSelect.getDrawable().getConstantState() != imgTap.getDrawable().getConstantState() && selectedLayout2.getChildCount() == 1) {
//                            selectedLayout2.setBackgroundColor(Colors.GREEN.get());
//                        } else {
//                            selectedLayout2.setBackgroundColor(Color.RED);
//                        }
//                    }
//                }
//            }
//
//        } else {
//            Toast.makeText(getContext(), "it's not your turn", Toast.LENGTH_SHORT).show();
//        }
//        } else {
//            int place1, place2;
//            if (turn.equals("white")) {
//                place1 = 24 - rndCube1;
//                place2 = 24 - rndCube2;
//            } else {
//                place1 = rndCube1 - 1;
//                place2 = rndCube2 - 1;
//            }
//                selectedLayout1 = layouts[place1];
//                if (selectedLayout1.getChildAt(0) == null) {
//                    selectedLayout1.setBackgroundColor(Colors.GREEN.get());
//                } else {
//                    ImageView imgSelect = (ImageView) (selectedLayout1.getChildAt(0));
//                    ImageView imgTap = (ImageView) (iLEat.getChildAt(0));
//                    if (imgSelect != null && imgTap != null) {
//                        if (imgSelect.getDrawable().getConstantState() == imgTap.getDrawable().getConstantState()) {
//                            selectedLayout1.setBackgroundColor(Colors.GREEN.get());
//                        } else if (imgSelect.getDrawable().getConstantState() != imgTap.getDrawable().getConstantState() && selectedLayout1.getChildCount() == 1) {
//                            selectedLayout1.setBackgroundColor(Colors.GREEN.get());
//                        } else {
//                            selectedLayout1.setBackgroundColor(Color.RED);
//                        }
//                    }
//                }
//                selectedLayout2 = layouts[place2];
//                if (selectedLayout2.getChildAt(0) == null) {
//                    selectedLayout2.setBackgroundColor(Colors.GREEN.get());
//                } else {
//                    ImageView imgSelect = (ImageView) (selectedLayout2.getChildAt(0));
//                    ImageView imgTap = (ImageView) (iLEat.getChildAt(0));
//                    if (imgSelect != null && imgTap != null) {
//                        if (imgSelect.getDrawable().getConstantState() == imgTap.getDrawable().getConstantState()) {
//                            selectedLayout2.setBackgroundColor(Colors.GREEN.get());
//                        } else if (imgSelect.getDrawable().getConstantState() != imgTap.getDrawable().getConstantState() && selectedLayout2.getChildCount() == 1) {
//                            selectedLayout2.setBackgroundColor(Colors.GREEN.get());
//                        } else {
//                            selectedLayout2.setBackgroundColor(Color.RED);
//                        }
//                    }
//                }
//            }
//    }
//
//    private void ifEat(LinearLayout[] layouts, LinearLayout iLEat, String turn) {
//        boolean eat = false;
//        for (int j = 0; j < 24; j++) {
//            layouts[j].setBackgroundColor(Color.TRANSPARENT);
//        }
//        for (int j = 0; iLEat.getChildAt(j) != null; j++) {
//            int resId = getResources().getIdentifier((turn + "_solider"), "drawable", requireContext().getPackageName());
//            ImageView imgEat = (ImageView) iLEat.getChildAt(j);
//            if (imgEat.getDrawable().getConstantState() == ContextCompat.getDrawable(requireContext(), resId).getConstantState()) {
//                eat = true;
//            }
//        }
//        if (eat) {
//            int place1 = -1, place2 = -1;
//            if (turn.equals("white")) {
//                place1 = 24 - rndCube1;
//                place2 = 24 - rndCube2;
//            } else {
//                place1 = rndCube1 - 1;
//                place2 = rndCube2 - 1;
//            }
//
//            if (place1 >= 0 && place1 < layouts.length) {
//                if (layouts[place1].getChildAt(0) == null) {
//                    layouts[place1].setBackgroundColor(Colors.GREEN.get());
//                } else {
//                    ImageView imgSelect = (ImageView) (layouts[place1].getChildAt(0));
//                    ImageView imgTap = (ImageView) (iLEat.getChildAt(0));
//                    if (imgSelect != null && imgTap != null) {
//                        if (imgSelect.getDrawable().getConstantState() == imgTap.getDrawable().getConstantState()) {
//                            layouts[place1].setBackgroundColor(Colors.GREEN.get());
//                        } else if (imgSelect.getDrawable().getConstantState() != imgTap.getDrawable().getConstantState() && layouts[place1].getChildCount() == 1) {
//                            layouts[place1].setBackgroundColor(Colors.GREEN.get());
//                        } else {
//                            layouts[place1].setBackgroundColor(Color.RED);
//                        }
//                    }
//                }
//            }
//
//            if (place2 >= 0 && place2 < layouts.length) {
//                if (layouts[place2].getChildAt(0) == null) {
//                    layouts[place2].setBackgroundColor(Colors.GREEN.get());
//                } else {
//                    ImageView imgSelect = (ImageView) (layouts[place2].getChildAt(0));
//                    ImageView imgTap = (ImageView) (iLEat.getChildAt(0));
//                    if (imgSelect != null && imgTap != null) {
//                        if (imgSelect.getDrawable().getConstantState() == imgTap.getDrawable().getConstantState()) {
//                            layouts[place2].setBackgroundColor(Colors.GREEN.get());
//                        } else if (imgSelect.getDrawable().getConstantState() != imgTap.getDrawable().getConstantState() && layouts[place2].getChildCount() == 1) {
//                            layouts[place2].setBackgroundColor(Colors.GREEN.get());
//                        } else {
//                            layouts[place2].setBackgroundColor(Color.RED);
//                        }
//                    }
//                }
//            }
//        } else return;
//    }
//
//
//    private void move(LinearLayout[] layouts, int i, LinearLayout selectedLayout, String turn) {
//        ImageView imgMove, imgSelected;
//        int selectedIndex = -1;
//        for (int j = 0; j < 24; j++) {
//            if (selectedLayout == layouts[j]) {
//                selectedIndex = j;
//            }
//        }
//        LinearLayout moveLayout = layouts[i];
//        if (moveLayout.getBackground() != null && ((ColorDrawable) moveLayout.getBackground()).getColor() == Colors.GREEN.get()) {
//            if (moveLayout.getChildAt(0) == null) {
//                ImageView movingImg = (ImageView) selectedLayout.getChildAt(0);
//                selectedLayout.removeViewAt(0);
//                moveLayout.addView(movingImg);
//                moveLayout.setBackgroundColor(Color.TRANSPARENT);
//            } else {
//                imgMove = (ImageView) moveLayout.getChildAt(0);
//                imgSelected = (ImageView) selectedLayout.getChildAt(0);
//                if (imgMove != null && imgSelected != null) {
//                    if (imgMove.getDrawable().getConstantState() == imgSelected.getDrawable().getConstantState()) {
//                        ImageView movingImg = (ImageView) selectedLayout.getChildAt(0);
//                        selectedLayout.removeViewAt(0);
//                        moveLayout.addView(movingImg);
//                        moveLayout.setBackgroundColor(Color.TRANSPARENT);
//                    } else if (imgMove.getDrawable().getConstantState() != imgSelected.getDrawable().getConstantState() && moveLayout.getChildCount() == 1) {
//                        ImageView eatMovingIMg = (ImageView) moveLayout.getChildAt(0);
//                        moveLayout.removeViewAt(0);
//                        iLEat.addView(eatMovingIMg);
//                        ImageView movingImg = (ImageView) selectedLayout.getChildAt(0);
//                        selectedLayout.removeViewAt(0);
//                        moveLayout.addView(movingImg);
//                        moveLayout.setBackgroundColor(Color.TRANSPARENT);
//                    }
//                }
//            }
//
//            if (imgC3.getVisibility() == View.VISIBLE) {
//                if (imgC4.getVisibility() == View.VISIBLE)
//                    imgC4.setVisibility(View.INVISIBLE);
//                else
//                    imgC3.setVisibility(View.INVISIBLE);
//            } else {
//                if (turn.equals("white")) {
//                    if (selectedIndex - i == rndCube1) {
//                        rndCube1 = 0;
//                        imgC1.setVisibility(View.INVISIBLE);
//                    } else {
//                        if (selectedIndex - i == rndCube2) {
//                            rndCube2 = 0;
//                            imgC2.setVisibility(View.INVISIBLE);
//                        }
//                    }
//                } else {
//                    if (i - selectedIndex == rndCube1) {
//                        rndCube1 = 0;
//                        imgC1.setVisibility(View.INVISIBLE);
//                    } else {
//                        if (i - selectedIndex == rndCube2) {
//                            rndCube2 = 0;
//                            imgC2.setVisibility(View.INVISIBLE);
//                        }
//                    }
//                }
//            }
//
//            for (int j = 0; j < 24; j++) {
//                layouts[j].setBackgroundColor(Color.TRANSPARENT);
//            }
//        }
//    }
//
//    private void ifEatMove(LinearLayout[] layouts, int i, String turn) {
//        ImageView imgMove, imgSelected;
//        LinearLayout moveLayout = null;
//        int selectedIndex = -1;
//
//        if (layouts[i].getBackground() != null && ((ColorDrawable) layouts[i].getBackground()).getColor() == Colors.GREEN.get()) {
//            moveLayout = layouts[i];
//            selectedIndex = i;
//            if (moveLayout.getChildAt(0) == null) {
//                ImageView movingImg = (ImageView) selectedLayout.getChildAt(0);
//                selectedLayout.removeViewAt(0);
//                moveLayout.addView(movingImg);
//                moveLayout.setBackgroundColor(Color.TRANSPARENT);
//            } else {
//                imgMove = (ImageView) moveLayout.getChildAt(0);
//                imgSelected = (ImageView) selectedLayout.getChildAt(0);
//                if (imgMove != null && imgSelected != null) {
//                    if (imgMove.getDrawable().getConstantState() == imgSelected.getDrawable().getConstantState()) {
//                        ImageView movingImg = (ImageView) selectedLayout.getChildAt(0);
//                        selectedLayout.removeViewAt(0);
//                        moveLayout.addView(movingImg);
//                        moveLayout.setBackgroundColor(Color.TRANSPARENT);
//                    } else if (imgMove.getDrawable().getConstantState() != imgSelected.getDrawable().getConstantState() && moveLayout.getChildCount() == 1) {
//                        ImageView eatMovingIMg = (ImageView) moveLayout.getChildAt(0);
//                        moveLayout.removeViewAt(0);
//                        iLEat.addView(eatMovingIMg);
//                        ImageView movingImg = (ImageView) selectedLayout.getChildAt(0);
//                        selectedLayout.removeViewAt(0);
//                        moveLayout.addView(movingImg);
//                        moveLayout.setBackgroundColor(Color.TRANSPARENT);
//                    }
//                }
//            }
//
//            if (imgC3.getVisibility() == View.VISIBLE) {
//                if (imgC4.getVisibility() == View.VISIBLE)
//                    imgC4.setVisibility(View.INVISIBLE);
//                else
//                    imgC3.setVisibility(View.INVISIBLE);
//            } else {
//                if (turn.equals("white")) {
//                    if (24 - selectedIndex == rndCube1) {
//                        rndCube1 = 0;
//                        imgC1.setVisibility(View.INVISIBLE);
//                    } else {
//                        if (24 - selectedIndex == rndCube2) {
//                            rndCube2 = 0;
//                            imgC2.setVisibility(View.INVISIBLE);
//                        }
//                    }
//                    for (int j = 0; j < 24; j++) {
//                        layouts[j].setBackgroundColor(Color.TRANSPARENT);
//                    }
//                } else {
//                    if (selectedIndex + 1 == rndCube1) {
//                        rndCube1 = 0;
//                        imgC1.setVisibility(View.INVISIBLE);
//                    } else {
//                        if (selectedIndex + 1 == rndCube2) {
//                            rndCube2 = 0;
//                            imgC2.setVisibility(View.INVISIBLE);
//                        }
//                    }
//                    for (int j = 0; j < 24; j++) {
//                        layouts[j].setBackgroundColor(Color.TRANSPARENT);
//                    }
//                }
//
//            }
//        } else if (layouts[i].getBackground() != null && ((ColorDrawable) layouts[i].getBackground()).getColor() == Colors.RED.get()) {
//            if (rndCube1 == rndCube2) {
//                imgC1.setVisibility(View.INVISIBLE);
//                imgC2.setVisibility(View.INVISIBLE);
//                imgC3.setVisibility(View.INVISIBLE);
//                imgC4.setVisibility(View.INVISIBLE);
//                for (int j = 0; j < 24; j++) {
//                    layouts[j].setBackgroundColor(Color.TRANSPARENT);
//                }
//            } else {
//                imgC1.setVisibility(View.INVISIBLE);
//                imgC2.setVisibility(View.INVISIBLE);
//                for (int j = 0; j < 24; j++) {
//                    layouts[j].setBackgroundColor(Color.TRANSPARENT);
//                }
//            }
//        }
//    }
//
//    ImageView img1;
//    ImageView img2;
//    ImageView img3;
//    LinearLayout selectedLayout1 = null, selectedLayout2 = null;
//
//    LinearLayout selectedLayout = null;
//
//    String whiteTurn = "white";
//    String brownTurn = "brown";
//    String turn = "white";
//
//    @Override
//    public void onClick(View view) {
//        //throw cubes
//        if (imgCubes.getId() == view.getId()) {
//            throwCubes();
//        }
//
//        boolean eat = false;
//        if (selectedLayout == null) {
//            for (int i = 0; i < 24; i++) {
//                for (int j = 0; iLEat.getChildAt(j) != null; j++) {
//                    int resId = getResources().getIdentifier((turn + "_solider"), "drawable", requireContext().getPackageName());
//                    ImageView imgEat = (ImageView) iLEat.getChildAt(j);
//                    if (imgEat.getDrawable().getConstantState() == ContextCompat.getDrawable(requireContext(), resId).getConstantState()) {
//                        eat = true;
//                    }
//                }
//                if (view.getId() == iLEat.getId()) {
//                    ifEat(layouts, iLEat, turn);
//                    selectedLayout = iLEat;
//                }
//                if (view.getId() == layouts[i].getId() && !eat) {
//                    if (layouts[i].getChildAt(0) != null) {
//                        moveToYourOtherSoliders(layouts, i, turn, iLEat);
//                        selectedLayout = layouts[i];
//                    }
//                }
//            }
//        } else {
//            for (int j = 0; j < 24; j++) {
//                if (view.getId() == layouts[j].getId() && selectedLayout == iLEat) {
//                    ifEatMove(layouts, j, turn);
//                }
//                if (view.getId() == layouts[j].getId() && !eat) {
//                    move(layouts, j, selectedLayout, turn);
//                }
//            }
//            selectedLayout = null;
//        }
//
//        if (imgC1.getVisibility() == View.INVISIBLE && imgC2.getVisibility() == View.INVISIBLE) {
//            if (turn.equals("white")) {
//                turn = "brown";
//            } else {
//                turn = "white";
//            }
//        }
//        //   }
//
//                    ImageView imChild = (ImageView) layouts[i].getChildAt(0);
//                    int resId = getResources().getIdentifier((turn + "_solider"), "drawable", requireContext().getPackageName());
//                    if (imChild.getDrawable().getConstantState() == ContextCompat.getDrawable(requireContext(), resId).getConstantState()) {
//                        if (view.getId() == layouts[i].getId()) {
//                            selectedLayout = layouts[i];
//                            if (rndCube1 != 0) {
//                                int targetIndex;
//                                if (turn.equals(whiteTurn)) {
//                                    targetIndex = i - rndCube1;
//                                } else {
//                                    targetIndex = i + rndCube1;
//                                }
//                                if (targetIndex >= 24 || targetIndex < 0) {
//                                    layouts[i].setBackgroundColor(Color.RED);
//                                    selectedLayout = null;
//                                    return;
//                                }
//
//                                //clean all color
//                                for (int j = 0; j < 24; j++) {
//                                    layouts[j].setBackgroundColor(Color.TRANSPARENT);
//                                }
//
//                                //check if can move peace 1
//                                img1 = (ImageView) layouts[i].getChildAt(0);
//                                View child = layouts[targetIndex].getChildAt(0);
//                                if (child == null) {
//                                    layouts[targetIndex].setBackgroundColor(Colors.GREEN.get());
//                                } else {
//                                    img2 = (ImageView) child;
//                                    if (img1.getDrawable() != null && img2.getDrawable() != null && img1.getDrawable().getConstantState() == img2.getDrawable().getConstantState()) {
//                                        layouts[targetIndex].setBackgroundColor(Colors.GREEN.get());
//                                    } else if (img1.getDrawable() != null && img2.getDrawable() != null && layouts[targetIndex].getChildCount() == 1 && img1.getDrawable().getConstantState() != img2.getDrawable().getConstantState()) {
//                                        layouts[targetIndex].setBackgroundColor(Colors.GREEN.get());
//                                    } else {
//                                        layouts[targetIndex].setBackgroundColor(Color.RED);
//                                        selectedLayout = null;
//                                    }
//                                }
//                            }
//
//                            //check if layouts[i] + rndCube2 >= 24
//                            if (rndCube2 != 0) {
//                                int targetIndex2;
//                                if (turn.equals(whiteTurn)) {
//                                    targetIndex2 = i - rndCube2;
//                                } else {
//                                    targetIndex2 = i + rndCube2;
//                                }
//                                if (targetIndex2 >= 24 || targetIndex2 < 0) {
//                                    layouts[i].setBackgroundColor(Color.RED);
//                                    selectedLayout2 = null;
//                                    return;
//                                }
//
//                                //check if can move peace 2
//                                img1 = (ImageView) layouts[i].getChildAt(0);
//                                View child2 = layouts[targetIndex2].getChildAt(0);
//                                if (child2 == null) {
//                                    layouts[targetIndex2].setBackgroundColor(Colors.GREEN.get());
//                                } else {
//                                    img3 = (ImageView) child2;
//                                    if (img1.getDrawable() != null && img3.getDrawable() != null &&
//                                            img1.getDrawable().getConstantState() == img3.getDrawable().getConstantState()) {
//                                        layouts[targetIndex2].setBackgroundColor(Colors.GREEN.get());
//                                    } else if (img1.getDrawable() != null && img3.getDrawable() != null && layouts[targetIndex2].getChildCount() == 1 && img1.getDrawable().getConstantState() != img3.getDrawable().getConstantState()) {
//                                        layouts[targetIndex2].setBackgroundColor(Colors.GREEN.get());
//                                        eat = true;
//
//
//                                    } else {
//                                        layouts[targetIndex2].setBackgroundColor(Color.RED);
//                                        selectedLayout2 = null;
//                                    }
//                                }
//                                break;
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        //if tap on one linear
//        else {
//            for (int i = 0; i < 24; i++) {
//                if (layouts[i].getBackground() != null && ((ColorDrawable) layouts[i].getBackground()).getColor() == Colors.GREEN.get() && view.getId() == layouts[i].getId()) {
//
//                    int selectedIndex = -1;
//                    for (int h = 0; h < layouts.length; h++) {
//                        if (layouts[h] == selectedLayout) {
//                            selectedIndex = h;
//                            break;
//                        }
//                    }
//                    boolean ifMove = false;
//                    //move
//                    if (!ifMove) {
//                        if (selectedLayout.getChildAt(0) != null && layouts[i].getChildAt(0) != null) {
//                            img1 = (ImageView) selectedLayout.getChildAt(0);
//                            img2 = (ImageView) layouts[i].getChildAt(0);
//                            if (img1.getDrawable() != null && img2.getDrawable() != null && img1.getDrawable().getConstantState() != img2.getDrawable().getConstantState()) {
//                                ImageView movingImgEat1 = (ImageView) layouts[i].getChildAt(0);
//                                layouts[i].removeViewAt(0);
//                                iLEat.addView(movingImgEat1);
//                                ImageView movingImgEat2 = (ImageView) selectedLayout.getChildAt(0);
//                                selectedLayout.removeViewAt(0);
//                                layouts[i].addView(movingImgEat2);
//                                layouts[i].setBackgroundColor(Color.TRANSPARENT);
//                            } else {
//                                ImageView movingImg = (ImageView) selectedLayout.getChildAt(0);
//                                selectedLayout.removeViewAt(0);
//                                layouts[i].addView(movingImg);
//                                layouts[i].setBackgroundColor(Color.TRANSPARENT);
//                            }
//                            if (imgC3.getVisibility() == View.VISIBLE) {
//                                if (imgC4.getVisibility() == View.VISIBLE)
//                                    imgC4.setVisibility(View.INVISIBLE);
//                                else
//                                    imgC3.setVisibility(View.INVISIBLE);
//                            } else {
//                                if (turn.equals(whiteTurn)) {
//                                    if (selectedIndex - i == rndCube1) {
//                                        rndCube1 = 0;
//                                        imgC1.setVisibility(View.INVISIBLE);
//                                    } else {
//                                        if (selectedIndex - i == rndCube2) {
//                                            rndCube2 = 0;
//                                            imgC2.setVisibility(View.INVISIBLE);
//                                        }
//                                    }
//                                } else {
//                                    if (i - selectedIndex == rndCube1) {
//                                        rndCube1 = 0;
//                                        imgC1.setVisibility(View.INVISIBLE);
//                                    } else {
//                                        if (i - selectedIndex == rndCube2) {
//                                            rndCube2 = 0;
//                                            imgC2.setVisibility(View.INVISIBLE);
//                                        }
//                                    }
//                                }
//                            }
//                            ifMove = true;
//                        }
//                    }
//                    if (!ifMove) {
//                        ImageView movingImg = (ImageView) selectedLayout.getChildAt(0);
//                        selectedLayout.removeViewAt(0);
//                        layouts[i].addView(movingImg);
//                        layouts[i].setBackgroundColor(Color.TRANSPARENT);
//                        if (imgC3.getVisibility() == View.VISIBLE) {
//                            if (imgC4.getVisibility() == View.VISIBLE)
//                                imgC4.setVisibility(View.INVISIBLE);
//                            else
//                                imgC3.setVisibility(View.INVISIBLE);
//                        } else {
//                            if (turn.equals(whiteTurn)) {
//                                if (selectedIndex - i == rndCube1) {
//                                    rndCube1 = 0;
//                                    imgC1.setVisibility(View.INVISIBLE);
//                                } else {
//                                    if (selectedIndex - i == rndCube2) {
//                                        rndCube2 = 0;
//                                        imgC2.setVisibility(View.INVISIBLE);
//                                    }
//                                }
//                            } else {
//                                if (i - selectedIndex == rndCube1) {
//                                    rndCube1 = 0;
//                                    imgC1.setVisibility(View.INVISIBLE);
//                                } else {
//                                    if (i - selectedIndex == rndCube2) {
//                                        rndCube2 = 0;
//                                        imgC2.setVisibility(View.INVISIBLE);
//                                    }
//                                }
//                            }
//                        }
//                        ifMove = true;
//                        break;
//                    }
//                }
//            }
//
//            for (int j = 0; j < 24; j++) {
//                layouts[j].setBackgroundColor(Color.TRANSPARENT);
//            }
//
//            selectedLayout = null;
//        }
//        if (imgC1.getVisibility() == View.INVISIBLE && imgC2.getVisibility() == View.INVISIBLE) {
//            if (turn.equals(whiteTurn)) {
//                turn = brownTurn;
//                return;
//            }
//            if (turn.equals(brownTurn)) {
//                turn = whiteTurn;
//            }
//        }
//    }

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


