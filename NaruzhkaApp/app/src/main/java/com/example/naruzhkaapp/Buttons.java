package com.example.naruzhkaapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class Buttons {
    public static void initBtns(){              //Инициализация кнопок


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
                    FileParser.backUpFile();
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
                if (Variables.currentTP!=null) {
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
                                    Variables.currentLamp=null;
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

        Variables.LampAmountEdit.addTextChangedListener(new TextWatcher() {     //Слушатель на изменение количества светильников на столбе
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Variables.currentLamp!=null){
                    try {
                        Variables.currentLamp.lampAmount= Integer.parseInt(String.valueOf(Variables.LampAmountEdit.getText()));
                    }catch (Exception ex){

                    }
                }
            }
        });

        Variables.LampAdressEdit.addTextChangedListener(new TextWatcher() {           //Слушатель на изменение адреса светильника
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Variables.currentLamp!=null){
                    Variables.currentLamp.adress= String.valueOf(Variables.LampAdressEdit.getText());
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

        Variables.LampMontageEdit.addTextChangedListener(new TextWatcher() {           //Слушатель на изменение комментариев к светильнику
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Variables.currentLamp!=null){
                    Variables.currentLamp.montage= String.valueOf(Variables.LampMontageEdit.getText());
                }
            }
        });

        Variables.spinTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {            //Обработка выбора типа светильника из выпадающего списка
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(Variables.currentLamp!=null && Variables.spinTypes.getSelectedItemPosition()!=0) {
                    Variables.currentLamp.typeSelection = Variables.spinTypes.getSelectedItemPosition();
                    switch (String.valueOf(Variables.spinTypes.getSelectedItem())){
                        case "РТУ-250":
                            Variables.currentLamp.type="ДРЛ";
                            Variables.currentLamp.power="250Вт";
                            Variables.currentLamp.montage="Напольная/Венчающая";
                            break;
                        case "РКУ-250":
                            Variables.currentLamp.type="ДРЛ";
                            Variables.currentLamp.power="250Вт";
                            Variables.currentLamp.montage="Консоль";
                            break;
                        case "ЖТУ-250":
                            Variables.currentLamp.type="ДНаТ";
                            Variables.currentLamp.power="250Вт";
                            Variables.currentLamp.montage="Напольная/Венчающая";
                            break;
                        case "ЖКУ-250":
                            Variables.currentLamp.type="ДНаТ";
                            Variables.currentLamp.power="250Вт";
                            Variables.currentLamp.montage="Консоль";
                            break;
                        case "LED-100":
                            Variables.currentLamp.type="Светодиодный";
                            Variables.currentLamp.power="100Вт";
                            Variables.currentLamp.montage="Консоль";
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
        txt.setLayoutParams(new ViewGroup.LayoutParams(180, 40));       //Задание размеров
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
                Methods.disactiveLamp();
                if (Variables.currentTPFolder!=null){       //Если была предыдущая активная подстанция
                    Variables.currentTPFolder.setBackgroundColor(Color.WHITE);      //Сброс цвета панели предыдущей активной подстанции
                }
                Variables.currentTPFolder = (TextView) v;       //Установка созданной панели подстанции - текущей
                v.setBackgroundColor(Color.RED);                //Установка цвета панели подстанции
                for (TP tp:Variables.tpList){           //Поиск подстанции по нажатию на панель подстанции
                    if (tp.textView == v){
                        Variables.currentTP = tp;
                        break;
                    }
                }
                Methods.showCurrentTPInfo();
                return false;
            }
        });
        Variables.activity.runOnUiThread(() -> {
            Variables.TPList.addView(txt);      //Добавление панели подстанции в список панелей подстанции
        });
    }


    static void addTPToListFromFile(TP tp){       //Функция добавления панели подстанции в список
        TextView txt = new TextView(Variables.activity);        //Создание текстового поля
        txt.setLayoutParams(new ViewGroup.LayoutParams(180, 40));       //Задание размеров
        txt.setText(tp.name);      //Задание текста
        txt.setTextSize(20);        //Задание размера текста
        txt.setBackgroundColor(Color.WHITE);
        tp.textView = txt;     //Установка созданной подстанции - текущей
        txt.setOnTouchListener(new View.OnTouchListener() {     //Слушатель нажатий на панель подстанции
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Methods.disactiveLamp();
                if (Variables.currentTPFolder!=null){       //Если была предыдущая активная подстанция
                    Variables.currentTPFolder.setBackgroundColor(Color.WHITE);      //Сброс цвета панели предыдущей активной подстанции
                }
                Variables.currentTPFolder = (TextView) v;       //Установка созданной панели подстанции - текущей
                v.setBackgroundColor(Color.RED);                //Установка цвета панели подстанции
                for (TP tp:Variables.tpList){           //Поиск подстанции по нажатию на панель подстанции
                    if (tp.textView == v){
                        Variables.currentTP = tp;
                        break;
                    }
                }
                Methods.showCurrentTPInfo();
                return false;
            }
        });
        Variables.activity.runOnUiThread(() -> {
            Variables.TPList.addView(txt);      //Добавление панели подстанции в список панелей подстанции
        });
    }

}
