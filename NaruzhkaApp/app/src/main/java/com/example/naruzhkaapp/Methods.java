package com.example.naruzhkaapp;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.geometry.Polyline;
import com.yandex.mapkit.map.InputListener;
import com.yandex.mapkit.map.Map;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.MapObjectTapListener;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.map.PolylineMapObject;
import com.yandex.runtime.image.ImageProvider;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import kotlin.reflect.KVariance;

public class Methods {
    public static Bitmap drawLamp(String number,int color) {          //Отрисовка значка светильника
        int picSize = 40;           //Размер значка
        Bitmap bitmap = Bitmap.createBitmap(picSize, picSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        // отрисовка плейсмарка
        Paint paint = new Paint();
        paint.setColor(color);               //Цвет значка
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(picSize / 2, picSize / 2, picSize / 2, paint);        //Отрисовка круга
        // отрисовка текста
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setTextSize(10);          //Размер текста
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(number, picSize / 2,
                picSize / 2 - ((paint.descent() + paint.ascent()) / 2), paint);             //Отрисовка текста внутри круга
        return bitmap;
    }

    public static Bitmap drawCurrentLamp(String number) {          //Отрисовка значка светильника
        int picSize = 40;           //Размер значка
        Bitmap bitmap = Bitmap.createBitmap(picSize, picSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        // отрисовка плейсмарка
        Paint paint = new Paint();
        paint.setColor(Color.RED);               //Цвет значка
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(picSize / 2, picSize / 2, picSize / 2, paint);        //Отрисовка круга
        // отрисовка текста
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setTextSize(10);          //Размер текста
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

    public static Bitmap drawTPWithColor(String number,int color) {                //Рисуем подстанцию
        int picSize = 50;               //Размер значка подстанции
        Bitmap bitmap = Bitmap.createBitmap(picSize, picSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        // отрисовка плейсмарка
        Paint paint = new Paint();
        paint.setColor(color);          //Цвет значка
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


    public static void getCity(Lamp lamp,TP tp, double latitude, double longitude) {          //Функция получение адреса по ширине и долготе
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
            if (lamp == null && tp!=null) {
                tp.adress = adress;     //Если адрес получен - задание адреса подстанции
            }else if (tp==null && lamp!=null){
                lamp.adress = adress;
            }
        }
    }


    public static MapObjectTapListener placemarkTapListener = new MapObjectTapListener() {      //Обработка нажатий на метки
        @Override
        public boolean onMapObjectTap(@NonNull MapObject mapObject, @NonNull Point point) {
            String data = mapObject.getUserData().toString();
            String[] split_str = data.split("%");
            if (Variables.currentTP!=null){
            if (!Variables.removeLampFlag) {            //Если простое нажатие - выводим информацию
                if (split_str[0].equals("TP")) {        //Если нажатие на подстанцию - ищем подстанцию в списке
                    for (TP tp : Variables.tpList) {
                        if (split_str[1].equals(tp.toString())) {       //Выводим информацию найденной подстанции
                            Toast toast = Toast.makeText(Variables.activity,
                                    tp.name, Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                } else if (split_str[0].equals("LAMP")) {
                    for (Lamp lamp : Variables.currentTP.lamps) {
                        if (split_str[1].equals(lamp.toString())) {
                            makeLampActive(lamp);
                            showCurrentLampInfo();
                        }
                    }
                }
            } else {          //Если активировано удаление - удаляем светильник
                if (split_str[0].equals("LAMP")) {
                    Lamp lampToDelete = null;
                    for (Lamp lamp : Variables.currentTP.lamps) {
                        if (split_str[1].equals(lamp.toString())) {
                            setCurrentLamp(null);
                            removeLamp(lamp);
                            lampToDelete = lamp;
                        }
                    }
                    if (lampToDelete != null) {
                        Variables.currentTP.lamps.remove(lampToDelete);
                        showCurrentTPInfo();
                    }
                }
            }
        }
            return false;
        }
    };

    public static void removeLamp(Lamp lamp){               //Удаление метки с экрана
        lamp.placemark.getParent().remove(lamp.placemark);
        //Variables.currentTP.lamps.remove(lamp);
    }

    public static final InputListener inputListener = new InputListener() {     //Обработка надажатий на план
        @Override
        public void onMapTap(@NonNull Map map, @NonNull Point point) {

            Point mappoint= new Point(point.getLatitude(), point.getLongitude());   //Создание точки на карте
            if (Variables.addTPFlag) {      //Если активен флаг добавления подстации
                Variables.currentTP.latitude = point.getLatitude();     //Задание широты
                Variables.currentTP.longtitude = point.getLongitude();      //Задание долготы
                Methods.getCity(null,Variables.currentTP,point.getLatitude(),point.getLongitude());      //Получение адреса подстанции

                MapObjectCollection pointCollection = Variables.mapview.getMap().getMapObjects().addCollection();
                pointCollection.addTapListener(Methods.placemarkTapListener);
                PlacemarkMapObject placemark = pointCollection.addPlacemark(mappoint,
                        ImageProvider.fromBitmap(Methods.drawTP(Variables.currentTP.name)));        //Создание и отрисовка метки
                Variables.currentTP.placemark = placemark;
                placemark.setUserData("TP%"+Variables.currentTP.toString());        //Сохранение данных в метку
                showCurrentTPInfo();

                //////Сброс флагов//////
                Variables.addTPFlag=false;
                Variables.TPAdded=true;
            }else if (Variables.addLampFlag){           //Иначе, если активен флаг добавления светильников
                Lamp lamp = new Lamp();         //Создание светильника
                lamp.latitude = point.getLatitude();            //Широта местоположения светильника
                lamp.longtitude = point.getLongitude();             //Долгота местоположения светильника
                lamp.stolbNumber = Variables.currentTP.currentStolbCount;
                Variables.currentTP.currentStolbCount++;
                Methods.getCity(lamp,null, lamp.latitude, lamp.longtitude);         //Получение адреса светильника
                Variables.currentTP.lamps.add(lamp);            //Добавление светильников к подстанции
                MapObjectCollection pointCollection = Variables.mapview.getMap().getMapObjects().addCollection();
                pointCollection.addTapListener(Methods.placemarkTapListener);
                PlacemarkMapObject placemark =  pointCollection.addPlacemark(mappoint,
                        ImageProvider.fromBitmap(Methods.drawLamp(String.valueOf(lamp.stolbNumber),Variables.currentTP.color)));
                lamp.placemark = placemark;
                makeLampActive(lamp);
                placemark.setUserData("LAMP%"+Variables.currentLamp.toString());        //Сохранение данных в метку
                setCurrentLamp(lamp);
                showCurrentLampInfo();
                displayLampsTPAmount(Variables.currentTP);      //Отображение количества светильников текущей подстанции
                /*List<Point> list = new ArrayList<Point>();
                list.add(point);
                list.add(new Point(0,0));
                Polyline polyline = new Polyline(list);
                Variables.mapview.getMap().getMapObjects().addCollection().addPolyline(polyline);*/
            }
        }

        @Override
        public void onMapLongTap(@NonNull Map map, @NonNull Point point) {

        }
// Do something.
    };

    public static void displayLampsTPAmount(TP tp){         //Отображение количества светильников, привязанных к подстанции
        int countLamps=0;
        for (Lamp lamp:tp.lamps){
            countLamps++;
        }
        Variables.TPLampsText.setText(String.valueOf(countLamps));
    }
    public static void showCurrentTPInfo(){         //Отображение тинформации по текущей подстанции
        Variables.TPNameEdit.setText(Variables.currentTP.name);
        Variables.TPAdressEdit.setText(Variables.currentTP.adress);
        displayLampsTPAmount(Variables.currentTP);
        showAllPhotos(Variables.currentTP);
    }

    public static void showCurrentLampInfo() {
        if (Variables.currentLamp != null){
            Variables.LampTypeEdit.setText(Variables.currentLamp.type);
        Variables.LampPowerEdit.setText(Variables.currentLamp.power);
        Variables.LampMontageEdit.setText(Variables.currentLamp.montage);
        Variables.LampAdressEdit.setText(Variables.currentLamp.adress);
        Variables.LampCommentsEdit.setText(Variables.currentLamp.comments);
        Variables.LampAmountEdit.setText(String.valueOf(Variables.currentLamp.lampAmount));
        Variables.LampHeightEdit.setText(String.valueOf(Variables.currentLamp.lampHeight));
        Variables.LampFromRoadDistEdit.setText(Variables.currentLamp.fromRoadDist);
        Variables.LampTypeKronstEdit.setText(Variables.currentLamp.typeKronst);
        Variables.LampViletKronsEdit.setText(Variables.currentLamp.viletKronst);
        Variables.LampOporaHeightEdit.setText(Variables.currentLamp.oporaHeight);
        showLampsAllPhotos(Variables.currentLamp);
    }
    }

    public static void setCurrentLamp(Lamp lamp){
        Variables.currentLamp = lamp;
    }

    public static void makeLampActive(Lamp lamp){
        disactiveLamp();
        Variables.currentLamp=null;
        Variables.spinTypes.setSelection(0);
        Point mappoint= new Point(lamp.latitude, lamp.longtitude);   //Создание точки на карте
        lamp.placemark.getParent().remove(lamp.placemark);
        MapObjectCollection pointCollection = Variables.mapview.getMap().getMapObjects().addCollection();
        pointCollection.addTapListener(Methods.placemarkTapListener);
        PlacemarkMapObject placemark =  pointCollection.addPlacemark(mappoint,
                ImageProvider.fromBitmap(Methods.drawCurrentLamp(String.valueOf(lamp.stolbNumber))));
        lamp.placemark = placemark;
        placemark.setUserData("LAMP%"+lamp.toString());        //Сохранение данных в метку
        Variables.currentLamp=lamp;
    }

    public static void disactiveLamp(){
        if (Variables.currentLamp!=null) {
            Point mappoint = new Point(Variables.currentLamp.latitude, Variables.currentLamp.longtitude);   //Создание точки на карте
            Variables.currentLamp.placemark.getParent().remove(Variables.currentLamp.placemark);
            MapObjectCollection pointCollection = Variables.mapview.getMap().getMapObjects().addCollection();
            pointCollection.addTapListener(Methods.placemarkTapListener);
            PlacemarkMapObject placemark = pointCollection.addPlacemark(mappoint,
                    ImageProvider.fromBitmap(Methods.drawLamp(String.valueOf(Variables.currentLamp.stolbNumber), Variables.currentTP.color)));
            Variables.currentLamp.placemark = placemark;
            placemark.setUserData("LAMP%" + Variables.currentLamp.toString());        //Сохранение данных в метку
            Variables.currentLamp = null;
        }
    }

    public static void clearAll() {
        for (TP tp : Variables.tpList) {
            for (Lamp lamp : tp.lamps) {
                Methods.removeLamp(lamp);
            }
            tp.lamps.clear();
            tp.placemark.getParent().remove(tp.placemark);
            Variables.activity.runOnUiThread(() -> {
                Variables.TPList.removeView(tp.textView);
            });
        }
        Variables.currentTPFolder = null;
        Variables.currentTP = null;
        Variables.currentLamp = null;
        Variables.tpList.clear();
    }

    public static void showTpsAndLamps(){
        for (TP tp:Variables.tpList){
            Point mappoint= new Point(tp.latitude, tp.longtitude);   //Создание точки на карте
            MapObjectCollection pointCollection = Variables.mapview.getMap().getMapObjects().addCollection();
            pointCollection.addTapListener(Methods.placemarkTapListener);
            PlacemarkMapObject placemark = pointCollection.addPlacemark(mappoint,
                    ImageProvider.fromBitmap(Methods.drawTPWithColor(tp.name,tp.color)));        //Создание и отрисовка метки
            tp.placemark = placemark;
            placemark.setUserData("TP%"+tp.toString());        //Сохранение данных в метку
            Buttons.addTPToListFromFile(tp);          //Добавление панели подстанции в список
            Variables.currentColor++;               //Инкремент указателя на текущий цвет
            if (Variables.currentColor > Variables.colors.length - 1) {     //Если указатель вышел за пределы массива с цветами
                Variables.currentColor = 0;     //Сброс указателя на изначальную позицию
            }


            for (Lamp lamp:tp.lamps){
                mappoint= new Point(lamp.latitude, lamp.longtitude);   //Создание точки на карте
                tp.currentStolbCount++;
                MapObjectCollection pointCollection1 = Variables.mapview.getMap().getMapObjects().addCollection();
                pointCollection1.addTapListener(Methods.placemarkTapListener);
                PlacemarkMapObject placemark1 =  pointCollection1.addPlacemark(mappoint,
                        ImageProvider.fromBitmap(Methods.drawLamp(String.valueOf(lamp.stolbNumber),tp.color)));
                lamp.placemark = placemark1;
                placemark1.setUserData("LAMP%"+lamp.toString());        //Сохранение данных в метку
            }
        }
    }


    /*List<Point> list = new ArrayList<Point>();
list.add(point);
list.add(new Point(0,0));
Polyline polyline = new Polyline(list);
Variables.mapview.getMap().getMapObjects().addCollection().addPolyline(polyline);*/

    public static void clearPolylines(){
        for (PolylineMapObject polylineMapObject:Variables.polylines){
            polylineMapObject.getParent().remove(polylineMapObject);
        }
        Variables.polylines.clear();
        Variables.points.clear();
    }


    public static void displayPolylines(){
        for (TP tp:Variables.tpList){
            Variables.points.add(new Point(tp.latitude,tp.longtitude));
            for (Lamp lamp:tp.lamps){
                Variables.points.add(new Point(lamp.latitude,lamp.longtitude));
            }
            Polyline polyline = new Polyline(Variables.points);
            Variables.points.clear();
            PolylineMapObject polyline1 = Variables.mapview.getMap().getMapObjects().addCollection().addPolyline(polyline);
            Variables.polylines.add(polyline1);
            polyline1.setStrokeColor(tp.color);
            polyline1.setStrokeWidth(3);
        }
    }

    public static void createNewPhotoRoom(File f, boolean type){     //Создание новой фотографии комнаты(светильника)
        ImageView view = new ImageView(Variables.activity);
        view.setLayoutParams(new ViewGroup.LayoutParams((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, Variables.activity.getResources().getDisplayMetrics()), (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, Variables.activity.getResources().getDisplayMetrics())));
        //lay.setBackgroundResource(R.drawable.txtviewborder);
        view.setImageURI(Uri.fromFile(f));
        if (type){      //Если сфотографирована комната
            Variables.tpGrid.addView(view);}
        else{   //иначе - светильник
            Variables.lampGrid.addView(view);
        }
    }

    public static void showAllPhotos(TP tp){            //Отображение всех фотографий помещения
        Variables.tpGrid.removeAllViews();
        for (String str:tp.photoPaths){
            File f = new File(str);
            Methods.createNewPhotoRoom(f,true);
        }
    }
    public static void showLampsAllPhotos(Lamp lamp){       //Отображение всех фотографий светильника
        Variables.lampGrid.removeAllViews();
        for (String str:lamp.photoPaths){
            File f = new File(str);
            Methods.createNewPhotoRoom(f,false);
        }
    }
}
