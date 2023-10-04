package com.example.naruzhkaapp;

import android.widget.TextView;

import com.yandex.mapkit.map.PlacemarkMapObject;

import java.util.Vector;

public class TP {
    public String name="";      //Наименование подстанции
    public String adress="";       //Адрес подстанции
    public double latitude=0;       //Долгота подстанции
    public double longtitude=0;         //Ширина подстанции
    public TextView textView=null;          //Панель подстанции в тулбаре слева
    public String comments="";              //Комментарии к подстанции
    public int color=0;                     //Цвет значка подстанции
    public int currentStolbCount=1;         //Счетчик текущего столба(для отображения)
    PlacemarkMapObject placemark = null;        //Иконка на карте
    public Vector<Lamp> lamps = new Vector<Lamp>();     //Список светильников, привязанных к подстанции


    public TP(String name,String adress, float latitude, float longtitude, String comments, int color,TextView textView,PlacemarkMapObject placemark){
        this.name = name;
        this.adress = adress;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.comments = comments;
        this.color = color;
        this.textView = textView;
        this.placemark = placemark;
    }

    public TP() {

    }
}
