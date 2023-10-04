package com.example.naruzhkaapp;

import android.app.Activity;
import android.graphics.Color;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.yandex.mapkit.mapview.MapView;

import org.w3c.dom.Text;

import java.util.Vector;

public class Variables {
    public static EditText TPNameEdit=null;         //Поле названия подстанции
    public static EditText LampMontageEdit = null;      //Тип монтажа светильника
    public static EditText TPCommentsEdit=null;     //Поле комментариев к подстанции
    public static Spinner spinTypes=null;           //Выпадающий список для выбора типа светильника
    public static EditText LampTypeEdit=null;       //Поле ввода типа светильника
    public static EditText LampPowerEdit=null;      //Поле ввода мощности светильника
    public static EditText LampAdressEdit=null;     //Поле ввода адреса светильнкиа
    public static EditText LampCommentsEdit = null;  //Поле ввода комментариев к светильнику
    public static EditText TPAdressEdit = null;         //Поле адреса подстанции
    public static TextView TPLampsText = null;          //Поле отображение количества светильников подстанции
    public static boolean addTPFlag=false;          //Флаг добавления подстанции
    public static boolean addLampFlag = false;          //Флаг добавления светильника
    public static Activity activity;                    //Activity
    public static TextView addTP;                       //Кнопка добавления подстанции
    public static TextView addLamp=null;                     //Кнопка добавления светильника
    public static TextView removeLamp=null;                 //Кнопка удаления светильника
    public static ImageView addTPToList;                //Кнопка добавления панели подстанции
    public static LinearLayout TPList;                  //Layout с панелями подстанций
    public static boolean TPAdded=true;                 //Флаг, обозначающий добавлена ли подстанция
    public static boolean removeLampFlag=false;             //Флаг удаления светильников
    public static TP currentTP = null;                   //Текущая активная подстанция
    public static Lamp currentLamp=null;                    //Текущий выбранный светильник
    public static ImageView removeTpFromList=null;          //Кнопка удаления подстанции из списка
    public static TextView currentTPFolder=null;            //Текущая активная вкладка с подстанцией
    public static MapView mapview;              //Карта
    public static Vector<TP> tpList = new Vector<TP>();         //Список подстанций

    //Массив цветов для подстанций и светильников
    public static int[] colors = {Color.BLACK,Color.GREEN,Color.BLUE,Color.GRAY,Color.DKGRAY,Color.YELLOW,Color.CYAN,Color.LTGRAY,Color.MAGENTA};

    public static String[] lampTypes = {"-","РТУ-250","РКУ-250","ЖТУ-250","ЖКУ-250","LED-100"};
    public static int currentColor=0;       //Указатель на текущий использующийся цвет

    public static void init(){          //Инициализация переменных
        addLamp = activity.findViewById(R.id.addLamp);
        addTPToList = activity.findViewById(R.id.addTPToList);
        TPList = activity.findViewById(R.id.TPList);
        TPNameEdit = activity.findViewById(R.id.TPNameEdit);
        TPAdressEdit = activity.findViewById(R.id.TPAdressEdit);
        TPLampsText = activity.findViewById(R.id.TPLampsText);
        TPCommentsEdit = activity.findViewById(R.id.TPCommentsEdit);
        LampTypeEdit = activity.findViewById(R.id.LampTypeEdit);
        LampPowerEdit = activity.findViewById(R.id.LampPowerEdit);
        LampAdressEdit = activity.findViewById(R.id.LampAdressEdit);
        LampCommentsEdit = activity.findViewById(R.id.LampCommentsEdit);
        LampMontageEdit = activity.findViewById(R.id.LampMontageEdit);
        spinTypes = activity.findViewById(R.id.spinTypes);
        removeLamp = activity.findViewById(R.id.removeLamp);
        removeTpFromList = activity.findViewById(R.id.removeTPTFromList);
        ArrayAdapter<String> adapter = new ArrayAdapter(activity, R.layout.spinner_item, lampTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinTypes.setAdapter(adapter);
    }
}
