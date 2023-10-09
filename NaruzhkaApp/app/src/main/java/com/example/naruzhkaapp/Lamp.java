package com.example.naruzhkaapp;

import com.yandex.mapkit.map.PlacemarkMapObject;

import java.util.Vector;

public class Lamp {
    public int typeSelection=0;     //Тип выбранного светильника(Для выпадающего списка)
    public String type="";      //Тип светильника
    public String power ="";        //Мощность светильника
    public int stolbNumber=0;           //Порядковый номер столба
    public double latitude=0;       //Широта местоположения светильника
    public double longtitude=0;     //Долгота местоположения светильника
    public String adress="";        //Адрес местоположения светильника;
    public String comments="";      //Комментарии к светильнику;
    public String montage="";           //Тип монтажа светильника
    public String oporaHeight="0";
    public int lampAmountSelection = 1;          //Количество светильников на столбе
    public String lampHeight="0";
    public String fromRoadDist = "0";
    public int typeKronstSelection = 0;
    public String viletKronst = "0";
    public String roadWidth="";
    public int roadPolosSelection=0;
    public String roadLength="";
    public int roadOsobennostSelection=0;
    public String roadOsobennost="";
    public String roadRasstanovka="";
    PlacemarkMapObject placemark = null;        //Иконка на карте

    Vector<String> photoPaths = new Vector<String>();       //Вектор путей к фотографиям комнаты

    public Lamp(int typeSelection,String type,String power,double latitude, double longtitude,String adress,String comments,PlacemarkMapObject placemark,int stolbNumber,String montage, String lampHeight){
        this.typeSelection = typeSelection;
        this.type=type;
        this.power = power;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.adress = adress;
        this.comments = comments;
        this.placemark = placemark;
        this.stolbNumber = stolbNumber;
        this.montage = montage;
        this.lampHeight = lampHeight;
    }

    public Lamp() {

    }
}
