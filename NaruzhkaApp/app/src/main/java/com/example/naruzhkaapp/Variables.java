package com.example.naruzhkaapp;

import android.app.Activity;
import android.widget.ImageView;

public class Variables {
    public static boolean addTPFlag=false;
    public static boolean addLampFlag = false;
    public static Activity activity;
    public static ImageView addTP;
    public static ImageView addLamp;

    public static void init(){
        addTP = activity.findViewById(R.id.addTP);
        addLamp = activity.findViewById(R.id.addLamp);
    }
}
