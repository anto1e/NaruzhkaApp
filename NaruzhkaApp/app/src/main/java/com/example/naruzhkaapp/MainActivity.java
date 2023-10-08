package com.example.naruzhkaapp;

import static com.example.naruzhkaapp.Buttons.CAMERA_REQUEST_CODE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_STORAGE = 101;
    private UserLocationLayer userLocationLayer;    //Метка местоположения пользователя

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);     //Установка ориентации на горизонтальную
        MapKitFactory.setApiKey("a16fa150-b6b4-4c2c-92c6-26979225dad2");            //Ключ доступа к Яндекс-картам
        MapKitFactory.initialize(this);             //Инициализация Яндекс-карт
        setContentView(R.layout.activity_main);
        Variables.activity = this;
        //if (PermissionUtils.hasPermissions(MainActivity.this)) return;
        //PermissionUtils.requestPermissions(MainActivity.this, PERMISSION_STORAGE);
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

        Timer myTimer;
        myTimer = new Timer();

        myTimer.schedule(new TimerTask() {
            public void run() {
                if (Variables.tpList!=null && Variables.tpList.size()>0){
                    try {
                        FileParser.backUpFile();
                        Variables.activity.runOnUiThread(() -> {           //Выключаем вращение и выводим текст об удачном экспорте в эксель
                            Toast.makeText(Variables.activity.getApplicationContext(), "Резервное сохранение", Toast.LENGTH_SHORT).show();
                        });
                    } catch (IOException e) {

                    }
                }
            }
        }, 0, 60*3000); // каждую минуту-сохранение файла;


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


    protected void onActivityResult(int requestCode, int resultCode, Intent data) { //После выбора пользователем файла путем диалогового окна
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == RESULT_OK) {        //Если файл открылся
            Variables.filePath = FileHelper.getRealPathFromURI(this, data.getData());       //Сохраняем путь к файлу
            FileParser.loadFile(Variables.filePath);          //Парсим файл
        }else if(requestCode == CAMERA_REQUEST_CODE){      //Если был запрос на использование камеры - сохраняем картинку
            if(resultCode == Activity.RESULT_OK){
                File f;
                if (Variables.takePhotoFlag) {
                    f = new File(String.valueOf(Variables.currentTP.photoPaths.elementAt(Variables.currentTP.photoPaths.size() - 1)));
                    Methods.createNewPhotoRoom(f, true);      //Создание мини-изображение в layout помещения
                }else {
                    f = new File(String.valueOf(Variables.currentLamp.photoPaths.elementAt(Variables.currentLamp.photoPaths.size() - 1)));
                    Methods.createNewPhotoRoom(f, false);      //Создание мини-изображение в layout помещения
                }
                Variables.takePhotoFlag=false;
                Log.d("tag", "ABsolute Url of Image is " + Uri.fromFile(f));
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);
            }
        }
    }
    }