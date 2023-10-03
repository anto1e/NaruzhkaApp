package com.example.naruzhkaapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.InputListener;
import com.yandex.mapkit.map.Map;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.MapObjectTapListener;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.runtime.image.ImageProvider;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Methods {
    public static Bitmap drawLamp(String number) {          //Отрисовка значка светильника
        int picSize = 40;           //Размер значка
        Bitmap bitmap = Bitmap.createBitmap(picSize, picSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        // отрисовка плейсмарка
        Paint paint = new Paint();
        paint.setColor(Color.DKGRAY);               //Цвет значка
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(picSize / 2, picSize / 2, picSize / 2, paint);        //Отрисовка круга
        // отрисовка текста
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setTextSize(20);          //Размер текста
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(number, picSize / 2,
                picSize / 2 - ((paint.descent() + paint.ascent()) / 2), paint);             //Отрисовка текста внутри круга
        return bitmap;
    }

    public static Bitmap drawTP(String number) {                //Рисуем подстанцию
        int picSize = 50;               //Размер значка подстанции
        Bitmap bitmap = Bitmap.createBitmap(picSize, picSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        // отрисовка плейсмарка
        Paint paint = new Paint();
        paint.setColor(Variables.currentTP.color);          //Цвет значка
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0,0,picSize,picSize,paint);         //Отрисовка квадрата
        // отрисовка текста
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setTextSize(15);              //Размер текста
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(number, picSize / 2,
                picSize / 2 - ((paint.descent() + paint.ascent()) / 2), paint);         //Отрисовка текста внутри квадрата
        return bitmap;
    }


    public static void getCity(TP tp, double latitude, double longitude) {          //Функция получение адреса по ширине и долготе
        Geocoder geocoder = new Geocoder(Variables.activity, Locale.getDefault());
        String adress;
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                adress = returnedAddress.getAddressLine(0);     //Получение адреса
            } else {
                adress = "Error";
            }
        } catch (IOException e) {
            e.printStackTrace();
            adress = "Error";
        }
        if (!adress.equals("Error")){
            tp.adress = adress;     //Если адрес получен - задание адреса подстанции
        }
    }


    public static MapObjectTapListener placemarkTapListener = new MapObjectTapListener() {      //Обработка нажатий на метки
        @Override
        public boolean onMapObjectTap(@NonNull MapObject mapObject, @NonNull Point point) {
            String data = mapObject.getUserData().toString();
            String[] split_str = data.split("%");
            if (split_str[0].equals("TP")) {        //Если нажатие на подстанцию - ищем подстанцию в списке
                for (TP tp : Variables.tpList) {
                    if (split_str[1].equals(tp.toString())) {       //Выводим информацию найденной подстанции
                        Toast toast = Toast.makeText(Variables.activity,
                                tp.name, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }
            return false;
        }
    };

    public static final InputListener inputListener = new InputListener() {     //Обработка надажатий на план
        @Override
        public void onMapTap(@NonNull Map map, @NonNull Point point) {

            Point mappoint= new Point(point.getLatitude(), point.getLongitude());   //Создание точки на карте
            if (Variables.addTPFlag) {      //Если активен флаг добавления подстации
                Variables.currentTP.latitude = point.getLatitude();     //Задание широты
                Variables.currentTP.longtitude = point.getLongitude();      //Задание долготы
                Methods.getCity(Variables.currentTP,point.getLatitude(),point.getLongitude());      //Получение адреса подстанции

                MapObjectCollection pointCollection = Variables.mapview.getMap().getMapObjects().addCollection();
                pointCollection.addTapListener(Methods.placemarkTapListener);
                PlacemarkMapObject placemark = pointCollection.addPlacemark(mappoint,
                        ImageProvider.fromBitmap(Methods.drawTP(Variables.currentTP.name)));        //Создание и отрисовка метки
                placemark.setUserData("TP%"+Variables.currentTP.toString());        //Сохранение данных в метку

                //////Сброс флагов//////
                Variables.addTPFlag=false;
                Variables.TPAdded=true;
            }else if (Variables.addLampFlag){           //Иначе, если активен флаг добавления светильников
                Variables.mapview.getMap().getMapObjects().addPlacemark(mappoint,
                        ImageProvider.fromBitmap(Methods.drawLamp(String.valueOf(0))));
            }
        }

        @Override
        public void onMapLongTap(@NonNull Map map, @NonNull Point point) {

        }
// Do something.
    };

}
