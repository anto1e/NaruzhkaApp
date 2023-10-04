package com.example.naruzhkaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Toast;

import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKit;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.layers.GeoObjectTapEvent;
import com.yandex.mapkit.layers.GeoObjectTapListener;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.InputListener;
import com.yandex.mapkit.map.Map;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.runtime.image.ImageProvider;

public class MainActivity extends AppCompatActivity {
    private UserLocationLayer userLocationLayer;    //Метка местоположения пользователя

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);     //Установка ориентации на горизонтальную
        MapKitFactory.setApiKey("a16fa150-b6b4-4c2c-92c6-26979225dad2");            //Ключ доступа к Яндекс-картам
        MapKitFactory.initialize(this);             //Инициализация Яндекс-карт
        setContentView(R.layout.activity_main);
        Variables.activity = this;
        Variables.init();           //Инициализация переменных
        Buttons.initBtns();         //Инициализация кнопок
        Variables.mapview = (MapView)findViewById(R.id.map);        //Отображение карты на экране
        Variables.mapview.getMap().move(new CameraPosition(         //Установка позиции камеры на карте
                new Point(59.828306, 30.327133),17.0f,0,0
                /* zoom =  17.0f,
                /* azimuth =  150.0f,
                /* tilt =  30.0f*/
        ));
       /* mapview.getMap().move(
                new CameraPosition(new Point(55.751574, 37.573856), 11.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 0),
                null);
// рисуем точку:
        Point mappoint= new Point(55.79, 37.57);
        mapview.getMap().getMapObjects().addPlacemark(mappoint);*/
        //////Отображение метки местоположения пользователя на карте и задание слушателя нажатий на карту//////
        MapKit mapKit = MapKitFactory.getInstance();
        Variables.mapview.getMap().addInputListener(Methods.inputListener);
        userLocationLayer = mapKit.createUserLocationLayer(Variables.mapview.getMapWindow());
        userLocationLayer.setVisible(true);
        userLocationLayer.setHeadingEnabled(true);
        //////////////////////////


    }

    @Override
    protected void onStop() {
        Variables.mapview.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }
    @Override
    protected void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        Variables.mapview.onStart();
    }
    }