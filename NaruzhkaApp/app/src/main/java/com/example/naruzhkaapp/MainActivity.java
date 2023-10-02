package com.example.naruzhkaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Toast;

import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.layers.GeoObjectTapEvent;
import com.yandex.mapkit.layers.GeoObjectTapListener;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.InputListener;
import com.yandex.mapkit.map.Map;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.image.ImageProvider;

public class MainActivity extends AppCompatActivity {
    public MapView mapview;
    public int TPCount=0;
    public int lampCount=0;
    private final InputListener inputListener = new InputListener() {
        @Override
        public void onMapTap(@NonNull Map map, @NonNull Point point) {
            /*Toast toast = Toast.makeText(getApplicationContext(),
                    String.valueOf(point.getLatitude())+";"+String.valueOf(point.getLongitude()), Toast.LENGTH_SHORT);
            toast.show();*/
            Point mappoint= new Point(point.getLatitude(), point.getLongitude());
            //mapview.getMap().getMapObjects().addPlacemark(mappoint,ImageProvider.fromResource(MainActivity.this,R.drawable.test));
            if (Variables.addTPFlag) {
                TPCount++;
                mapview.getMap().getMapObjects().addPlacemark(mappoint,
                        ImageProvider.fromBitmap(Methods.drawTP(String.valueOf(TPCount))));
            }else if (Variables.addLampFlag){
                lampCount++;
                mapview.getMap().getMapObjects().addPlacemark(mappoint,
                        ImageProvider.fromBitmap(Methods.drawLamp(String.valueOf(lampCount))));
            }
        }

        @Override
        public void onMapLongTap(@NonNull Map map, @NonNull Point point) {

        }
// Do something.
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);     //Установка ориентации на горизонтальную
        MapKitFactory.setApiKey("a16fa150-b6b4-4c2c-92c6-26979225dad2");
        MapKitFactory.initialize(this);
        setContentView(R.layout.activity_main);
        Variables.activity = this;
        Variables.init();
        Buttons.initBtns();
        mapview = (MapView)findViewById(R.id.map);
        mapview.getMap().move(new CameraPosition(
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
        mapview.getMap().addInputListener(inputListener);

    }

    @Override
    protected void onStop() {
        mapview.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }
    @Override
    protected void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapview.onStart();
    }
    }