package com.example.naruzhkaapp;

import com.yandex.mapkit.map.PlacemarkMapObject;

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
    public int lampAmount = 1;          //Количество светильников на столбе
    PlacemarkMapObject placemark = null;        //Иконка на карте

    public Lamp(int typeSelection,String type,String power,double latitude, double longtitude,String adress,String comments,PlacemarkMapObject placemark,int stolbNumber,String montage,int lampAmount){
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
        this.lampAmount = lampAmount;
    }

    public Lamp() {

    }
}
