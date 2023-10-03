package com.example.naruzhkaapp;

import android.app.Activity;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.yandex.mapkit.mapview.MapView;

import org.w3c.dom.Text;

import java.util.Vector;

public class Variables {
    public static boolean addTPFlag=false;          //Флаг добавления подстанции
    public static boolean addLampFlag = false;          //Флаг добавления светильника
    public static Activity activity;                    //Activity
    public static TextView addTP;                       //Кнопка добавления подстанции
    public static TextView addLamp;                     //Кнопка добавления светильника
    public static ImageView addTPToList;                //Кнопка добавления панели подстанции
    public static LinearLayout TPList;                  //Layout с панелями подстанций
    public static boolean TPAdded=true;                 //Флаг, обозначающий добавлена ли подстанция
    public static TP currentTP = null;                   //Текущая активная подстанция
    public static TextView currentTPFolder=null;            //Текущая активная вкладка с подстанцией
    public static MapView mapview;              //Карта
    public static Vector<TP> tpList = new Vector<TP>();         //Список подстанций

    //Массив цветов для подстанций и светильников
    public static int[] colors = {Color.BLACK,Color.GREEN,Color.BLUE,Color.GRAY,Color.DKGRAY,Color.YELLOW,Color.CYAN,Color.LTGRAY,Color.MAGENTA};
    public static int currentColor=0;       //Указатель на текущий использующийся цвет

    public static void init(){          //Инициализация переменных
        addLamp = activity.findViewById(R.id.addLamp);
        addTPToList = activity.findViewById(R.id.addTPToList);
        TPList = activity.findViewById(R.id.TPList);
    }
}
