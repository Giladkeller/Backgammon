package com.example.backgammonfinal;

import java.util.Random;


import android.annotation.SuppressLint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * <p>
 * create an instance of this fragment.
 */
public class Game extends Fragment implements View.OnClickListener {

    private LinearLayout[] layouts;//= {R.id.lL1, R.id.lL2, R.id.lL3, R.id.lL4, R.id.lL5, R.id.lL6, R.id.lL7, R.id.lL8, R.id.lL9, R.id.lL10, R.id.lL11, R.id.lL12, R.id.lL13, R.id.lL14, R.id.lL15, R.id.lL16, R.id.lL17, R.id.lL18, R.id.lL19, R.id.lL20, R.id.lL21, R.id.lL22, R.id.lL23, R.id.lL24};

    private LinearLayout layout;
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
    ImageView img1;
    ImageView img2;
    ImageView img3;
    LinearLayout selectedLayout = null, selectedLayout2 = null;

    @Override
    public void onClick(View view) {
        if (imgCubes.getId() == view.getId()){
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
        if (selectedLayout == null && selectedLayout2 == null) {
            for (int i = 0; i < 24; i++) {
                if (view.getId() == layouts[i].getId() && layouts[i].getChildAt(0) != null) {
                    selectedLayout = layouts[i];
                    if (rndCube1 != 0) {
                        int targetIndex = i + rndCube1;
                        if (targetIndex >= 24) {
                            layouts[i].setBackgroundColor(Color.RED);
                            selectedLayout = null;
                            //return;
                        }

                        for (int j = 0; j < 24; j++) {
                            layouts[j].setBackgroundColor(Color.TRANSPARENT);
                        }

                        img1 = (ImageView) layouts[i].getChildAt(0);
                        View child = layouts[targetIndex].getChildAt(0);
                        if (child == null) {
                            layouts[targetIndex].setBackgroundColor(Color.GREEN);
                        } else {
                            img2 = (ImageView) child;
                            if (img1.getDrawable() != null && img2.getDrawable() != null &&
                                    img1.getDrawable().getConstantState() == img2.getDrawable().getConstantState()) {
                                layouts[targetIndex].setBackgroundColor(Color.GREEN);
                            } else {
                                layouts[targetIndex].setBackgroundColor(Color.RED);
                                selectedLayout = null;
                            }
                        }
                    }
                    if (rndCube2 != 0) {
                        int targetIndex2 = i + rndCube2;
                        if (targetIndex2 >= 24) {
                            layouts[i].setBackgroundColor(Color.RED);
                            selectedLayout2 = null;
                            //return;
                        }

                        img1 = (ImageView) layouts[i].getChildAt(0);
                        View child2 = layouts[targetIndex2].getChildAt(0);
                        if (child2 == null) {
                            layouts[targetIndex2].setBackgroundColor(Color.GREEN);
                        } else {
                            img3 = (ImageView) child2;
                            if (img1.getDrawable() != null && img3.getDrawable() != null &&
                                    img1.getDrawable().getConstantState() == img3.getDrawable().getConstantState()) {
                                layouts[targetIndex2].setBackgroundColor(Color.GREEN);
                            } else {
                                layouts[targetIndex2].setBackgroundColor(Color.RED);
                                selectedLayout2 = null;
                            }
                        }
                        break;
                    }
                }
            }
        } else {
            for (int i = 0; i < 24; i++) {
                if (layouts[i].getBackground() != null && ((ColorDrawable) layouts[i].getBackground()).getColor() == Color.GREEN && view.getId() == layouts[i].getId()) {

                    int selectedIndex = -1;
                    for (int h = 0; h < layouts.length; h++) {
                        if (layouts[h] == selectedLayout) {
                            selectedIndex = h;
                            break;
                        }
                    }

                    ImageView movingImg = (ImageView) selectedLayout.getChildAt(0);
                    selectedLayout.removeViewAt(0);
                    layouts[i].addView(movingImg);
                    layouts[i].setBackgroundColor(Color.TRANSPARENT);
                    if (imgC3.getVisibility() == View.VISIBLE) {
                        if (imgC4.getVisibility() == View.VISIBLE)
                            imgC4.setVisibility(View.INVISIBLE);
                        else
                            imgC3.setVisibility(View.INVISIBLE);
                    } else {
                        if (i - selectedIndex == rndCube1) {
                            rndCube1 = 0;
                            imgC1.setVisibility(View.INVISIBLE);
                        }
                        else {
                            if (i - selectedIndex == rndCube2) {
                                    rndCube2 = 0;
                                    imgC2.setVisibility(View.INVISIBLE);
                                }
                            }
                        }
                        break;
                    }
                }

                for (int j = 0; j < 24; j++) {
                    layouts[j].setBackgroundColor(Color.TRANSPARENT);
                }

                selectedLayout = null;
            }
        }

    }


