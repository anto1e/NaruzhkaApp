package com.example.naruzhkaapp;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

public class SaveExcelThread extends Thread{        //Поток для сохранения данных в эксель
    public void run() {
        try {
            if (!Variables.isExporting) {
                Variables.isExporting=true;
                ImageView rotationElement = Variables.loadingImage;     //Колесо вращения


                // Aply animation to image view
                Variables.activity.runOnUiThread(() -> {        //Включаем вращение
                    rotationElement.setVisibility(View.VISIBLE);
                });
                ExcelExporter.exportToExel();
                Variables.activity.runOnUiThread(() -> {           //Выключаем вращение и выводим текст об удачном экспорте в эксель
                    rotationElement.setVisibility(View.GONE);
                    Toast.makeText(Variables.activity.getApplicationContext(), "Отчет создан!", Toast.LENGTH_SHORT).show();
                });
                Variables.isExporting=false;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
