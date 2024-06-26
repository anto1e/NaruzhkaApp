package com.example.naruzhkaapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.runtime.image.ImageProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Buttons {

    public static final int CAMERA_PERM_CODE = 101;     //Код доступа к камере
    public static final int CAMERA_REQUEST_CODE = 102;      //Код доступа к камере
    public static void initBtns(){              //Инициализация кнопок


        Variables.copyPaste.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (Variables.currentTP!=null && Variables.currentLamp!=null){
                    if (!Variables.copyFlag){
                        disableAddLamp();
                        disableRemoveLamp();
                        //Variables.refreshStolbs();
                        Variables.copyFlag=true;
                        Variables.copiedLamp = Variables.currentLamp;
                        Variables.copyPaste.setBackgroundColor(Color.RED);
                    }
                    else{
                        disableCopyPaste();
                    }
                }
                return false;
            }
        });

        Variables.undo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (Variables.lastLamp != null) {
                    if (Objects.equals(Variables.lastOperation, "ADD") && Variables.currentTP!=null && Variables.lastLamp!=null){
                        Methods.setCurrentLamp(null);
                        Methods.removeLamp(Variables.lastLamp);
                        Variables.currentTP.lamps.remove(Variables.lastLamp);
                        Methods.showCurrentTPInfo();
                        Variables.currentLamp=null;
                        Variables.lastLamp=null;
                        Variables.lastOperation="";
                    }else if (Objects.equals(Variables.lastOperation, "DELETE") && Variables.currentTP!=null && Variables.lastLamp!=null){
                        Variables.currentTP.lamps.add(Variables.lastLamp);            //Добавление светильников к подстанции
                        MapObjectCollection pointCollection = Variables.mapview.getMap().getMapObjects().addCollection();
                        pointCollection.addTapListener(Methods.placemarkTapListener);
                        PlacemarkMapObject placemark = pointCollection.addPlacemark(new Point(Variables.lastLamp.latitude,Variables.lastLamp.longtitude),
                                ImageProvider.fromBitmap(Methods.drawLamp(String.valueOf(Variables.lastLamp.stolbNumber), Variables.currentTP.color)));
                        Variables.lastLamp.placemark = placemark;
                        Methods.makeLampActive(Variables.lastLamp);
                        Methods.showCurrentLampInfo();
                        Methods.displayLampsTPAmount(Variables.currentTP);      //Отображение количества светильников текущей подстанции
                        Variables.lastLamp=null;
                        Variables.lastOperation="";
                    }
                }
                return false;
            }
        });



        Variables.zoomMe.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                try {
                    CameraPosition pos = Variables.userLocationLayer.cameraPosition();
                    Variables.mapview.getMap().move(pos);
                }catch (Exception ex){

                }
                return false;
            }
        });

        Variables.takeTpPic.setOnTouchListener(new View.OnTouchListener() {      //При нажатии - активируем камеру
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (Variables.currentTP!=null) {
                    Variables.takePhotoFlag = 1;
                    verifyPermissions(1);
                }
                return false;
            }
        });

        Variables.makeExcel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (Variables.tpList.size()>0) {
                    try {
                        SaveExcelThread thread = new SaveExcelThread(); //Создаем новый поток для сохранения в Эксель
                        thread.start();     //Запускаем поток
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                return false;
            }
        });

        Variables.takeRoadPic.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (Variables.currentLamp!=null) {
                    Variables.takePhotoFlag = 2;
                    verifyPermissions(2);
                }
                return false;
            }
        });

        Variables.takeLampPic.setOnTouchListener(new View.OnTouchListener() {      //При нажатии - активируем камеру
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (Variables.currentLamp!=null) {
                    Variables.takePhotoFlag =0;
                    verifyPermissions(0);
                }
                return false;
            }
        });



        Variables.addPolylines.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Methods.clearPolylines();
                Methods.displayPolylines();
                return false;
            }
        });

        Variables.openFile.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent()
                        .setType("*/*")
                        .setAction(Intent.ACTION_GET_CONTENT);
                Variables.activity.startActivityForResult(Intent.createChooser(intent, "Select a file"), 123);
                return false;
            }
        });

        Variables.saveFile.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                try {
                    FileParser.saveFile(Variables.filePath);
                    Variables.activity.runOnUiThread(() -> {           //Выключаем вращение и выводим текст об удачном экспорте в эксель
                        Toast.makeText(Variables.activity.getApplicationContext(), "Файл сохранен!", Toast.LENGTH_SHORT).show();
                    });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return false;
            }
        });

        Variables.addLamp.setOnTouchListener(new View.OnTouchListener() {           //Обработка нажатия на кнопку добавления светильника
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!Variables.addTPFlag) {         //Если не активен флаг добавления подстанции
                    if (!Variables.addLampFlag) {           //Если не активен флаг добавления светильника
                        Variables.addLampFlag = true;           //Установка флага в true
                        disableRemoveLamp();
                        disableCopyPaste();
                        Variables.addLamp.setBackgroundColor(Color.RED);        //Установка цвета кнопки
                    } else {
                        disableAddLamp();           //Иначе, деактивация кнопки
                    }
                }
                return false;
            }
        });

        Variables.removeLamp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (Variables.TPAdded) {
                    if (!Variables.removeLampFlag) {
                        Variables.removeLampFlag = true;
                        disableAddLamp();
                        disableCopyPaste();
                        Variables.removeLamp.setBackgroundColor(Color.RED);
                    } else {
                        disableRemoveLamp();
                    }
                }
                return false;
            }
        });

        Variables.removeTpFromList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (Variables.TPAdded) {
                    if (Variables.currentTP != null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Variables.activity);
                        builder.setCancelable(true);
                        builder.setTitle("Удалить");
                        builder.setMessage("Вы действительно хотите удалить текущую подстанцию?");
                        builder.setPositiveButton("Удалить",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {        //Если выбрано удалить - удаляем светильник с экрана
                                        for (Lamp lamp : Variables.currentTP.lamps) {
                                            Methods.removeLamp(lamp);
                                        }
                                        Variables.currentTP.lamps.clear();
                                        Variables.currentTP.placemark.getParent().remove(Variables.currentTP.placemark);
                                        Variables.tpList.remove(Variables.currentTP);
                                        Variables.activity.runOnUiThread(() -> {
                                            Variables.TPList.removeView(Variables.currentTPFolder);
                                        });
                                        Variables.currentTPFolder = null;
                                        Variables.currentTP = null;
                                        Variables.currentLamp = null;
                                    }
                                });
                        builder.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {       //Иначе - отменяем удаление
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }
                return false;
            }
        });

        Variables.addTPToList.setOnTouchListener(new View.OnTouchListener() {       //Добавление подстанции в список
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (Variables.TPAdded) {            //Если подстанция была добавлена
                    Methods.disactiveLamp();
                    final EditText txtUrl = new EditText(Variables.activity);

// Set the default text to a link of the Queen

                    new AlertDialog.Builder(Variables.activity)     //Создание диалогового окна
                            .setTitle("Название ТП")
                            .setMessage("Введите название ТП")
                            .setView(txtUrl)
                            .setPositiveButton("Подтвердить", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    String name = txtUrl.getText().toString();      //Получение текста из поля ввода
                                    if (name.length() > 0) {        //Если пользователь ввел не пустую строку
                                        //Сброс и установка флагов//
                                        Variables.TPAdded = false;
                                        Variables.addTPFlag = true;
                                        TP tp = new TP();       //Создание новой подстанции
                                        Variables.currentTP = tp;       //Установка созданной подстанции текущей
                                        tp.name = name;         //Задание имени подстанции
                                        addTPToList(name);          //Добавление панели подстанции в список
                                        tp.color = Variables.colors[Variables.currentColor];        //Задание цвета значка подстанции
                                        Variables.currentColor++;               //Инкремент указателя на текущий цвет
                                        if (Variables.currentColor > Variables.colors.length - 1) {     //Если указатель вышел за пределы массива с цветами
                                            Variables.currentColor = 0;     //Сброс указателя на изначальную позици/
                                        }
                                        Variables.tpList.add(tp);           //Добавление подстанции в список
                                    }
                                }
                            })
                            .setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                }
                            })
                            .show();
                }
                return false;
            }
        });


        Variables.TPNameEdit.addTextChangedListener(new TextWatcher() {     //Слушатель на изменение названия подстанции
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Variables.currentTP!=null){
                    Variables.currentTP.name= String.valueOf(Variables.TPNameEdit.getText());
                }
            }
        });

        Variables.LampHeightEdit.addTextChangedListener(new TextWatcher() {     //Слушатель на изменение высоты светильника
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Variables.currentLamp!=null){
                    Variables.currentLamp.lampHeight= String.valueOf(Variables.LampHeightEdit.getText());
                }
            }
        });

        Variables.TPAdressEdit.addTextChangedListener(new TextWatcher() {           //Слушатель на изменение адреса подстанции
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Variables.currentTP!=null){
                    Variables.currentTP.adress= String.valueOf(Variables.TPAdressEdit.getText());
                }

            }
        });

        Variables.LampFromRoadDistEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Variables.currentLamp!=null){
                    Variables.currentLamp.fromRoadDist = String.valueOf(Variables.LampFromRoadDistEdit.getText());
                }
            }
        });


        Variables.LampViletKronsEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Variables.currentLamp!=null){
                    Variables.currentLamp.viletKronst = String.valueOf(Variables.LampViletKronsEdit.getText());
                }
            }
        });

        Variables.TPCommentsEdit.addTextChangedListener(new TextWatcher() {           //Слушатель на изменение комментариев к подстанции
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Variables.currentTP!=null){
                    Variables.currentTP.comments= String.valueOf(Variables.TPCommentsEdit.getText());
                }
            }
        });

        Variables.LampTypeEdit.addTextChangedListener(new TextWatcher() {           //Слушатель на изменение типа светильника
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Variables.currentLamp!=null){
                    Variables.currentLamp.type= String.valueOf(Variables.LampTypeEdit.getText());
                }
            }
        });

        Variables.LampOporaHeightEdit.addTextChangedListener(new TextWatcher() {           //Слушатель на изменение типа светильника
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Variables.currentLamp!=null){
                    Variables.currentLamp.oporaHeight= String.valueOf(Variables.LampOporaHeightEdit.getText());
                }
            }
        });

        Variables.RoadWidthEdit.addTextChangedListener(new TextWatcher() {           //Слушатель на изменение типа светильника
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Variables.currentLamp!=null){
                    Variables.currentLamp.roadWidth= String.valueOf(Variables.RoadWidthEdit.getText());
                }
            }
        });

        Variables.RoadSPolotnaEdit.addTextChangedListener(new TextWatcher() {           //Слушатель на изменение типа светильника
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Variables.currentLamp!=null){
                    Variables.currentLamp.roadLength= String.valueOf(Variables.RoadSPolotnaEdit.getText());
                }
            }
        });

        Variables.RoadOsobennostEdit.addTextChangedListener(new TextWatcher() {           //Слушатель на изменение типа светильника
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Variables.currentLamp!=null){
                    Variables.currentLamp.roadOsobennost= String.valueOf(Variables.RoadOsobennostEdit.getText());
                }
            }
        });

        Variables.RoadRasstanovkaEdit.addTextChangedListener(new TextWatcher() {           //Слушатель на изменение типа светильника
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Variables.currentLamp!=null){
                    Variables.currentLamp.roadRasstanovka= String.valueOf(Variables.RoadRasstanovkaEdit.getText());
                }
            }
        });


        Variables.LampPowerEdit.addTextChangedListener(new TextWatcher() {           //Слушатель на изменение мощности светильника
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Variables.currentLamp!=null){
                    Variables.currentLamp.power= String.valueOf(Variables.LampPowerEdit.getText());
                }
            }
        });




        Variables.LampCommentsEdit.addTextChangedListener(new TextWatcher() {           //Слушатель на изменение комментариев к светильнику
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Variables.currentLamp!=null){
                    Variables.currentLamp.comments= String.valueOf(Variables.LampCommentsEdit.getText());
                }
            }
        });


        Variables.spinRasstanovka.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (Variables.currentLamp!=null && Variables.spinRasstanovka.getSelectedItemPosition()!=0){
                    Variables.currentLamp.roadRasstanovka = String.valueOf(Variables.spinRasstanovka.getSelectedItem());
                    Variables.RoadRasstanovkaEdit.setText(String.valueOf(Variables.spinRasstanovka.getSelectedItem()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Variables.LampMontageEdit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (Variables.currentLamp!=null){
                    Variables.currentLamp.montageSelection= Variables.LampMontageEdit.getSelectedItemPosition();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Variables.spinPolos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (Variables.currentLamp!=null && Variables.spinPolos.getSelectedItemPosition()!=0){
                    Variables.currentLamp.roadPolosSelection = Variables.spinPolos.getSelectedItemPosition();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Variables.spinOsobennost.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (Variables.currentLamp!=null && Variables.spinOsobennost.getSelectedItemPosition()!=0){
                    Variables.currentLamp.roadOsobennostSelection = Variables.spinOsobennost.getSelectedItemPosition();
                    Variables.currentLamp.roadOsobennost = String.valueOf(Variables.spinOsobennost.getSelectedItem());
                    Variables.RoadOsobennostEdit.setText(String.valueOf(Variables.spinOsobennost.getSelectedItem()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Variables.spinKronstTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (Variables.currentLamp!=null){
                    Variables.currentLamp.typeKronstSelection = Variables.spinKronstTypes.getSelectedItemPosition();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Variables.spinLampsAmount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (Variables.currentLamp!=null){
                    Variables.currentLamp.lampAmountSelection = Variables.spinLampsAmount.getSelectedItemPosition();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Variables.spinTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {            //Обработка выбора типа светильника из выпадающего списка
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(Variables.currentLamp!=null && Variables.spinTypes.getSelectedItemPosition()!=0) {
                    Variables.currentLamp.typeSelection = Variables.spinTypes.getSelectedItemPosition();
                    switch (String.valueOf(Variables.spinTypes.getSelectedItem())){
                        case "РТУ-125":
                            Variables.currentLamp.type="ДРЛ";
                            Variables.currentLamp.power="125Вт";
                            Variables.currentLamp.montageSelection=1;
                            break;
                        case "РТУ-150":
                            Variables.currentLamp.type="ДРЛ";
                            Variables.currentLamp.power="150Вт";
                            Variables.currentLamp.montageSelection=1;
                            break;
                        case "РТУ-250":
                            Variables.currentLamp.type="ДРЛ";
                            Variables.currentLamp.power="250Вт";
                            Variables.currentLamp.montageSelection=1;
                            break;
                        case "РКУ-250":
                            Variables.currentLamp.type="ДРЛ";
                            Variables.currentLamp.power="250Вт";
                            Variables.currentLamp.montageSelection=0;
                            break;
                        case "РКУ-400":
                            Variables.currentLamp.type="ДРЛ";
                            Variables.currentLamp.power="400Вт";
                            Variables.currentLamp.montageSelection=0;
                            break;
                        case "ЖТУ-250":
                            Variables.currentLamp.type="ДНаТ";
                            Variables.currentLamp.power="250Вт";
                            Variables.currentLamp.montageSelection=1;
                            break;
                        case "ЖКУ-100":
                            Variables.currentLamp.type="ДНаТ";
                            Variables.currentLamp.power="100Вт";
                            Variables.currentLamp.montageSelection=0;
                            break;
                        case "ЖКУ-150":
                            Variables.currentLamp.type="ДНаТ";
                            Variables.currentLamp.power="150Вт";
                            Variables.currentLamp.montageSelection=0;
                            break;
                        case "ЖКУ-250":
                            Variables.currentLamp.type="ДНаТ";
                            Variables.currentLamp.power="250Вт";
                            break;
                        case "ЖКУ-400":
                            Variables.currentLamp.type="ДНаТ";
                            Variables.currentLamp.power="400Вт";
                            break;
                        case "Инд.-120":
                            Variables.currentLamp.type="Индукционный";
                            Variables.currentLamp.power="120Вт";
                            break;
                        case "LED-50":
                            Variables.currentLamp.type="Светодиодный";
                            Variables.currentLamp.power="50Вт";
                            break;
                        case "LED-75":
                            Variables.currentLamp.type="Светодиодный";
                            Variables.currentLamp.power="75Вт";
                            break;
                        case "LED-100":
                            Variables.currentLamp.type="Светодиодный";
                            Variables.currentLamp.power="100Вт";
                            break;
                        case "LED-130":
                            Variables.currentLamp.type="Светодиодный";
                            Variables.currentLamp.power="130Вт";
                            break;
                        case "LED-150":
                            Variables.currentLamp.type="Светодиодный";
                            Variables.currentLamp.power="150Вт";
                            break;
                        case "LED-180":
                            Variables.currentLamp.type="Светодиодный";
                            Variables.currentLamp.power="180Вт";
                            break;
                        case "LED-200":
                            Variables.currentLamp.type="Светодиодный";
                            Variables.currentLamp.power="200Вт";
                            break;
                        case "Пр.-35":
                            Variables.currentLamp.type="";
                            Variables.currentLamp.power="35Вт";
                            break;
                        case "Пр.-70":
                            Variables.currentLamp.type="";
                            Variables.currentLamp.power="70Вт";
                            break;
                        case "Пр.-150":
                            Variables.currentLamp.type="";
                            Variables.currentLamp.power="150Вт";
                            break;
                        case "Пр.-300":
                            Variables.currentLamp.type="";
                            Variables.currentLamp.power="300Вт";
                            break;
                        case "Пр.-400":
                            Variables.currentLamp.type="";
                            Variables.currentLamp.power="400Вт";
                            break;
                        case "Пр.-500":
                            Variables.currentLamp.type="";
                            Variables.currentLamp.power="500Вт";
                            break;
                        case "Пр.-1000":
                            Variables.currentLamp.type="";
                            Variables.currentLamp.power="1000Вт";
                            break;
                        case "BR-250":
                            Variables.currentLamp.type="ГОБО";
                            Variables.currentLamp.power="250Вт";
                            break;
                        case "GS-240":
                            Variables.currentLamp.type="ГОБО";
                            Variables.currentLamp.power="240Вт";
                            break;
                        case "ДРЛ-125":
                            Variables.currentLamp.type="ДРЛ";
                            Variables.currentLamp.power="125Вт";
                            Variables.currentLamp.montageSelection = 2;
                            break;
                        case "ДРЛ-250":
                            Variables.currentLamp.type="ДРЛ";
                            Variables.currentLamp.power="250Вт";
                            Variables.currentLamp.montageSelection = 2;
                            break;
                        case "ДРЛ-400":
                            Variables.currentLamp.type="ДРЛ";
                            Variables.currentLamp.power="400Вт";
                            Variables.currentLamp.montageSelection = 2;
                            break;
                        case "ДНаТ-100":
                            Variables.currentLamp.type="ДНаТ";
                            Variables.currentLamp.power="100Вт";
                            Variables.currentLamp.montageSelection = 2;
                            break;
                        case "ДНаТ-150":
                            Variables.currentLamp.type="ДНаТ";
                            Variables.currentLamp.power="150Вт";
                            Variables.currentLamp.montageSelection = 2;
                            break;
                        case "ДНаТ-250":
                            Variables.currentLamp.type="ДНаТ";
                            Variables.currentLamp.power="250Вт";
                            Variables.currentLamp.montageSelection = 2;
                            break;
                        case "ДНаТ-400":
                            Variables.currentLamp.type="ДНаТ";
                            Variables.currentLamp.power="400Вт";
                            Variables.currentLamp.montageSelection = 2;
                            break;
                    }
                }
                Methods.showCurrentLampInfo();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }

    private static void disableAddLamp(){       //Деактивация функции добавления светильников
        Variables.addLampFlag=false;            //Сброс флага
        Variables.addLamp.setBackgroundColor(Color.WHITE);          //Сброс цвета кнопки
    }
    private static void disableRemoveLamp(){       //Деактивация функции добавления светильников
        Variables.removeLampFlag=false;            //Сброс флага
        Variables.removeLamp.setBackgroundColor(Color.WHITE);          //Сброс цвета кнопки
    }


    static void addTPToList(String name){       //Функция добавления панели подстанции в список
        TextView txt = new TextView(Variables.activity);        //Создание текстового поля
        txt.setLayoutParams(new ViewGroup.LayoutParams(250, 40));       //Задание размеров
        txt.setText(name);      //Задание текста
        txt.setTextSize(20);        //Задание размера текста
        txt.setBackgroundColor(Color.RED);      //Установка цвета панели подстанции
        if (Variables.currentTPFolder!=null){       //Если была предыдущая активаня подстаниция - сбрасываем ее цвет
            Variables.currentTPFolder.setBackgroundColor(Color.WHITE);
        }
        Variables.currentTP.textView = txt;     //Установка созданной подстанции - текущей
        Variables.currentTPFolder=txt;          //Установка созданной панели подстанции - текущей
        txt.setOnTouchListener(new View.OnTouchListener() {     //Слушатель нажатий на панель подстанции
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (Variables.TPAdded) {
                    Buttons.disableCopyPaste();
                    Variables.lastOperation="";
                    Variables.lastLamp=null;
                    Methods.disactiveLamp();
                    if (Variables.currentTPFolder != null) {       //Если была предыдущая активная подстанция
                        Variables.currentTPFolder.setBackgroundColor(Color.WHITE);      //Сброс цвета панели предыдущей активной подстанции
                    }
                    Variables.currentTPFolder = (TextView) v;       //Установка созданной панели подстанции - текущей
                    v.setBackgroundColor(Color.RED);                //Установка цвета панели подстанции
                    for (TP tp : Variables.tpList) {           //Поиск подстанции по нажатию на панель подстанции
                        if (tp.textView == v) {
                            Variables.currentTP = tp;
                            break;
                        }
                    }
                    Methods.showCurrentTPInfo();
                }
                return false;
            }
        });
        Variables.activity.runOnUiThread(() -> {
            Variables.TPList.addView(txt);      //Добавление панели подстанции в список панелей подстанции
        });
    }


    static void addTPToListFromFile(TP tp){       //Функция добавления панели подстанции в список
        TextView txt = new TextView(Variables.activity);        //Создание текстового поля
        txt.setLayoutParams(new ViewGroup.LayoutParams(250, 40));       //Задание размеров
        txt.setText(tp.name);      //Задание текста
        txt.setTextSize(20);        //Задание размера текста
        txt.setBackgroundColor(Color.WHITE);
        tp.textView = txt;     //Установка созданной подстанции - текущей
        txt.setOnTouchListener(new View.OnTouchListener() {     //Слушатель нажатий на панель подстанции
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (Variables.TPAdded) {
                    Buttons.disableCopyPaste();
                    Methods.disactiveLamp();
                    if (Variables.currentTPFolder != null) {       //Если была предыдущая активная подстанция
                        Variables.currentTPFolder.setBackgroundColor(Color.WHITE);      //Сброс цвета панели предыдущей активной подстанции
                    }
                    Variables.currentTPFolder = (TextView) v;       //Установка созданной панели подстанции - текущей
                    v.setBackgroundColor(Color.RED);                //Установка цвета панели подстанции
                    for (TP tp : Variables.tpList) {           //Поиск подстанции по нажатию на панель подстанции
                        if (tp.textView == v) {
                            Variables.currentTP = tp;
                            break;
                        }
                    }
                    Methods.showCurrentTPInfo();
                }
                return false;
            }
        });
        Variables.activity.runOnUiThread(() -> {
            Variables.TPList.addView(txt);      //Добавление панели подстанции в список панелей подстанции
        });

    }



    private static void verifyPermissions(int type){       //Получение разрешений на использование камеры
        String[] permissions = {android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA};

       /* if(ContextCompat.checkSelfPermission(Variables.activity.getApplicationContext(),
                permissions[0]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(Variables.activity.getApplicationContext(),
                permissions[1]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(Variables.activity.getApplicationContext(),
                permissions[2]) == PackageManager.PERMISSION_GRANTED){*/
        dispatchTakePictureIntent(type);
        /*}else{
            ActivityCompat.requestPermissions(Variables.activity,
                    permissions,
                    CAMERA_PERM_CODE);
        }*/
    }
    public static void dispatchTakePictureIntent(int type) {        //Функция создания изображения
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(Variables.activity.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile(type);
            } catch (IOException ex) {
                System.out.println(ex.toString());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(Variables.activity,
                        BuildConfig.APPLICATION_ID + ".provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                Variables.activity.startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }


    private static File createImageFile(int type) throws IOException {          //Функция создания фотографии
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName="temp";
        if (type==1) {
            if (Variables.currentTP != null) {
                imageFileName = Variables.currentTP.name + "$"+timeStamp;
            }
        }else if (type==0){
            if (Variables.currentLamp != null) {
                if (Variables.currentTP!=null)
                    imageFileName = Variables.currentTP.name + "$Столб " + Variables.currentLamp.stolbNumber + "$"+String.valueOf(Variables.spinTypes.getItemAtPosition(Variables.currentLamp.typeSelection))+"$" + Variables.currentLamp.type + " " + Variables.currentLamp.power + "$"+timeStamp;
            }
        }else if (type==2){
            if (Variables.currentLamp != null) {
                if (Variables.currentTP!=null)
                    //imageFileName = "Дорога: "+Variables.currentLamp.adress + "&"+timeStamp;
                    imageFileName = Variables.currentLamp.adress + "&Дорога"+timeStamp;
            }
        }
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File directory = new File(Variables.folderPath + "/" + "NaruzhkaApp");
        if (!directory.exists()) {
            directory.mkdir();
            // If you require it to make the entire directory path including parents,
            // use directory.mkdirs(); here instead.
        }
        directory = new File(Variables.folderPath + "/" + "NaruzhkaApp/"+Variables.currentTP.name);
        if (!directory.exists()) {
            directory.mkdir();
            // If you require it to make the entire directory path including parents,
            // use directory.mkdirs(); here instead.
        }
        //String.valueOf(Variables.activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS));
        File storageDir = new File(Variables.folderPath + "/" + "NaruzhkaApp/"+Variables.currentTP.name);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        if (type==1)
            Variables.currentTP.photoPaths.add(image.getAbsolutePath());
        else if (type==0)
            Variables.currentLamp.photoPaths.add(image.getAbsolutePath());
        else if (type==2)
            Variables.currentLamp.roadPhotoPaths.add(image.getAbsolutePath());
        return image;
    }

    public static void disableCopyPaste(){
        Variables.copyFlag=false;
        Variables.copiedLamp=null;
        Variables.copyPaste.setBackgroundColor(Color.WHITE);
    }

}
