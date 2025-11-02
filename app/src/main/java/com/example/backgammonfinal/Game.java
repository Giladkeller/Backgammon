package com.example.backgammonfinal;

import java.util.Random;


import android.annotation.SuppressLint;
import android.os.Bundle;

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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * <p>
 * create an instance of this fragment.
 */
public class Game extends Fragment implements View.OnClickListener {

    private LinearLayout[] layouts ;//= {R.id.lL1, R.id.lL2, R.id.lL3, R.id.lL4, R.id.lL5, R.id.lL6, R.id.lL7, R.id.lL8, R.id.lL9, R.id.lL10, R.id.lL11, R.id.lL12, R.id.lL13, R.id.lL14, R.id.lL15, R.id.lL16, R.id.lL17, R.id.lL18, R.id.lL19, R.id.lL20, R.id.lL21, R.id.lL22, R.id.lL23, R.id.lL24};

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

        for (int i=0;i<24;i++)
        {
            String layoutID = "lL" + (i+1);
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
        imgCubes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
//
//                for (int i = 1; i < 25; i++) {
//                    layout = (LinearLayout) v.findViewById(layouts[i]);
//                    layout.setOnClickListener(new View.OnClickListener() { ;
//                        @Override
//                        public void onClick(View view) {
//                            if ((layout.getId() + rndCube1) > 24){
//                                layouts[layout.getResources().getInteger(R.id.) + rndCube1]
//                            }
//                            ImageView img = (ImageView) (layout.getChildAt(0));
//                            if (img.getDrawable() == getResources().getDrawable(R.drawable.white_solider)) {
//                                layout.removeViewAt(0);
//                                layout = (LinearLayout) v.findViewById(layouts[i + rndCube1]);
//                                img.setImageResource(R.drawable.white_solider);
//                                layout.addView(img);
//                            }
//                        }
//                    });
//                }
//
                if (rndCube1 == rndCube2) {
                    imgC3.setImageResource(getResources().getIdentifier("cube" + rndCube1, "drawable", getActivity().getPackageName()));
                    imgC4.setImageResource(getResources().getIdentifier("cube" + rndCube2, "drawable", getActivity().getPackageName()));
                    imgC3.setVisibility(View.VISIBLE);
                    imgC4.setVisibility(View.VISIBLE);
                }
            }
        });

        return v;

    }
    ImageView img1;
    ImageView img2;
    @Override
    public void onClick(View view) {
        for (int i = -0; i < 24; i++) {
            if(view.getId()==layouts[i].getId() && layouts[i].getChildAt(0)!=null){
                if (i + 1 + rndCube1 > 24){
                    layouts[i].setBackgroundColor(Color.RED);
                }
                else {
                    img1 = (ImageView) (layouts[i].getChildAt(0));
                    img2 = (ImageView) (layouts[i + rndCube1].getChildAt(0));
                    if ((img1.getDrawable()) == (img2.getDrawable()) || (layouts[i+rndCube1].getChildAt(0) == null)) {
                        layouts[i + rndCube1].setBackgroundColor(Color.GREEN);
                        if(view.getId() == layouts[i + rndCube1].getId()){
                            layouts[i].removeViewAt(0);
                            layouts[i + rndCube1].addView(img1);
                            layouts[i + rndCube1].setBackgroundColor(Color.TRANSPARENT);
                        }
                    }
                    else {
                        layouts[i+rndCube1].setBackgroundColor(Color.RED);
                    }

                }
            }
        }
    }
}

