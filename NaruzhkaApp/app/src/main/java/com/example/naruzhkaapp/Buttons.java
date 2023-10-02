package com.example.naruzhkaapp;

import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;

public class Buttons {
    public static void initBtns(){
        Variables.addTP.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!Variables.addTPFlag){
                    Variables.addTPFlag=true;
                    Variables.addTP.setBackgroundColor(Color.RED);
                    disableAddLamp();
                }else{
                    disableAddTP();
                }
                return false;
            }
        });

        Variables.addLamp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!Variables.addLampFlag){
                    Variables.addLampFlag=true;
                    Variables.addLamp.setBackgroundColor(Color.RED);
                    disableAddTP();
                }else{
                    disableAddLamp();
                }
                return false;
            }
        });
    }

    private static void disableAddTP(){
        Variables.addTPFlag=false;
        Variables.addTP.setBackgroundColor(Color.WHITE);
    }
    private static void disableAddLamp(){
        Variables.addLampFlag=false;
        Variables.addLamp.setBackgroundColor(Color.WHITE);
    }
}
