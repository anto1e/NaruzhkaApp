package com.example.naruzhkaapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class Buttons {
    public static void initBtns(){              //Инициализация кнопок

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

        Variables.addTPToList.setOnTouchListener(new View.OnTouchListener() {       //Добавление подстанции в список
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (Variables.TPAdded) {            //Если подстанция была добавлена
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
    }

    private static void disableAddLamp(){       //Деактивация функции добавления светильников
        Variables.addLampFlag=false;            //Сброс флага
        Variables.addLamp.setBackgroundColor(Color.WHITE);          //Сброс цвета кнопки
    }

    private static void addTPToList(String name){       //Функция добавления панели подстанции в список
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
                if (Variables.currentTPFolder!=null){       //Если была предыдущая активная подстанция
                    Variables.currentTPFolder.setBackgroundColor(Color.WHITE);      //Сброс цвета панели предыдущей активной подстанции
                }
                Variables.currentTPFolder = (TextView) v;       //Установка созданной панели подстанции - текущей
                v.setBackgroundColor(Color.RED);                //Установка цвета панели подстанции
                for (TP tp:Variables.tpList){           //Поиск подстанции по нажатию на панель подстанции
                    if (tp.textView == v){
                        Variables.currentTP = tp;
                        System.out.println(tp.name);
                        break;
                    }
                }
                return false;
            }
        });
        Variables.activity.runOnUiThread(() -> {
            Variables.TPList.addView(txt);      //Добавление панели подстанции в список панелей подстанции
        });
    }

}
