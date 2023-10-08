package com.example.naruzhkaapp;

import android.app.Activity;
import android.graphics.Color;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.geometry.Polyline;
import com.yandex.mapkit.map.PolylineMapObject;
import com.yandex.mapkit.mapview.MapView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    public static EditText LampAmountEdit=null;         //Количество светильников на столбе
    public static EditText LampFromRoadDistEdit = null;
    public static EditText LampTypeKronstEdit = null;
    public static EditText LampViletKronsEdit = null;
    public static EditText LampOporaHeightEdit=null;
    public static EditText LampHeightEdit=null;
    public static TextView TPLampsText = null;          //Поле отображение количества светильников подстанции
    public static TextView saveFile = null;             //Кнопка сохранения файла
    public static TextView openFile=null;
    public static TextView addPolylines = null;
    public static TextView makeExcel=null;
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
    public static ImageView takeTpPic = null;
    public static ImageView takeLampPic=null;
    public static GridLayout tpGrid=null;
    public static GridLayout lampGrid=null;
    public static boolean takePhotoFlag=false;
    public static MapView mapview;              //Карта
    public static Vector<TP> tpList = new Vector<TP>();         //Список подстанций
    public static String filePath="";
    public static String folderPath="/storage/emulated/0/Documents";
    public static Vector<PolylineMapObject> polylines = new Vector<PolylineMapObject>();
    public static List<Point> points = new ArrayList<Point>();

    //Массив цветов для подстанций и светильников
    public static int[] colors = {Color.BLACK,Color.GREEN,Color.BLUE,Color.GRAY,Color.DKGRAY,Color.YELLOW,Color.CYAN,Color.LTGRAY,Color.MAGENTA};

    public static String[] lampTypes = {"-","РТУ-125","РТУ-150","РТУ-250","РКУ-250","РКУ-400","ЖКУ-100","ЖКУ-150","ЖТУ-250","ЖКУ-250","ЖКУ-400","Инд.-120","LED-50","LED-75","LED-100","LED-130","LED-150","LED-180","LED-200","Пр.-35","Пр.-70","Пр.-150","Пр.-300","Пр.-400","Пр.-500","Пр.-1000","BR-250","GS-240"};
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
        saveFile = activity.findViewById(R.id.saveFile);
        LampAmountEdit = activity.findViewById(R.id.LampAmountEdit);
        openFile = activity.findViewById(R.id.openFile);
        addPolylines = activity.findViewById(R.id.addPolylines);
        LampHeightEdit = activity.findViewById(R.id.LampHeightEdit);
        takeTpPic = activity.findViewById(R.id.takePicBtn);
        takeLampPic = activity.findViewById(R.id.takePicLampBtn);
        tpGrid = activity.findViewById(R.id.tpGrid);
        lampGrid = activity.findViewById(R.id.lampGrid);
        makeExcel = activity.findViewById(R.id.makeExcel);
        LampFromRoadDistEdit = activity.findViewById(R.id.LampFromRoadDistEdit);
        LampTypeKronstEdit = activity.findViewById(R.id.LampTypeKronstEdit);
        LampViletKronsEdit = activity.findViewById(R.id.LampViletKronstEdit);
        LampOporaHeightEdit = activity.findViewById(R.id.LampOporaHeightEdit);
        ArrayAdapter<String> adapter = new ArrayAdapter(activity, R.layout.spinner_item, lampTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinTypes.setAdapter(adapter);
    }


    public static String getCurrentTimeStamp(){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH;mm;ss");
            String currentDateTime = dateFormat.format(new Date()); // Find todays date

            return currentDateTime;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }
}
