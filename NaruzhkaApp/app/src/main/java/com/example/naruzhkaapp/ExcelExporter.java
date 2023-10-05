package com.example.naruzhkaapp;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.widget.ArrayAdapter;


import com.aspose.cells.Cell;
import com.aspose.cells.Cells;
import com.aspose.cells.ChartCollection;
import com.aspose.cells.Workbook;
import com.aspose.cells.Worksheet;
import com.aspose.cells.WorksheetCollection;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Vector;

public class ExcelExporter {

    String path;

    static Workbook workbook;
    int lastIndex;
    static WorksheetCollection worksheets;
    static Worksheet sheet;
    static Cells cells;
    static int rowCount = 4;

    public static void init() throws Exception {       //Инициализация Эксель файла
        rowCount = 2;
        workbook = new Workbook(Variables.folderPath + "/NaruzhkaApp/Template/otchet.xlsx");
        worksheets = workbook.getWorksheets();
        sheet = worksheets.get(0);
        cells = sheet.getCells();
    }

    public static void exportToExel() throws Exception {           //Функция экспорта в Эксель(дорабатывается)
        File directory = new File(Variables.folderPath + "/NaruzhkaApp/Otcheti");
        if (!directory.exists()) {
            directory.mkdir();
            // If you require it to make the entire directory path including parents,
            // use directory.mkdirs(); here instead.
        }
        init();
        for (TP tp:Variables.tpList){
            for (Lamp lamp:tp.lamps){
                Cell cell = cells.get("B" + Integer.toString(rowCount));
                cell.setValue(lamp.adress);
                cell = cells.get("C" + Integer.toString(rowCount));
                cell.setValue(tp.name+"/"+tp.adress);
                cell = cells.get("G" + Integer.toString(rowCount));
                cell.setValue(lamp.comments);
                cell = cells.get("I" + Integer.toString(rowCount));
                cell.setValue(lamp.latitude+"/"+lamp.longtitude);
                cell = cells.get("J" + Integer.toString(rowCount));
                cell.setValue(lamp.lampHeight);
                cell = cells.get("M" + Integer.toString(rowCount));
                cell.setValue(lamp.type+"-"+lamp.power);
                cell = cells.get("R" + Integer.toString(rowCount));
                cell.setValue(lamp.power);
                cell = cells.get("N" + Integer.toString(rowCount));
                cell.setValue(lamp.montage);
                cell = cells.get("Q" + Integer.toString(rowCount));
                cell.setValue(lamp.lampAmount);
                rowCount++;
            }
        }
        save();
    }

    public static void save() throws Exception {       //Сохранение в новый файл(Aspose Cells)
        String file_name=Variables.folderPath + "/" + "/NaruzhkaApp/Otcheti/"+Variables.getCurrentTimeStamp() + ".xlsx";
        workbook.save(file_name);
        //Пересохранение файла, с удалением страницы о пробной лицензии(Apache POI Excel)
        FileInputStream inputStream = new FileInputStream(new File(file_name));
        XSSFWorkbook workBook = new XSSFWorkbook(inputStream);

//Delete Sheet
        workBook.removeSheetAt(1);

//Save the file
        FileOutputStream outFile = new FileOutputStream(new File(file_name));
        workBook.write(outFile);
        outFile.close();
    }
}