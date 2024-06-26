package com.example.naruzhkaapp;

import android.content.res.Resources;
import android.os.Build;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class FileParser {
    public static void saveFile(String path) throws IOException {
        if (path.equals("")) {
            String filePath = Variables.getCurrentTimeStamp()+";";
            for (TP tp: Variables.tpList){
                filePath+=tp.name;
                filePath+=";";
            }
            filePath+=".txt";
            File directory = new File(Variables.folderPath + "/" + "NaruzhkaApp");
            if (!directory.exists()) {
                directory.mkdir();
                // If you require it to make the entire directory path including parents,
                // use directory.mkdirs(); here instead.
            }
            directory = new File(Variables.folderPath + "/" + "NaruzhkaApp"+"/Saves");
            if (!directory.exists()) {
                directory.mkdir();
                // If you require it to make the entire directory path including parents,
                // use directory.mkdirs(); here instead.
            }
            String date = Variables.getCurrentDate();
            directory = new File(Variables.folderPath + "/" + "NaruzhkaApp"+"/Saves/"+date);
            if (!directory.exists()) {
                directory.mkdir();
                // If you require it to make the entire directory path including parents,
                // use directory.mkdirs(); here instead.
            }
            File file = new File(directory+"/"+filePath);
            file.createNewFile();
            try (FileWriter fw = new FileWriter(directory+"/"+filePath, true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw)) {       //Начинаем записывать информацию о комнатах
                 for (TP tp:Variables.tpList){          //  //START//
                     //%TP-1;adress;latitude;longtitude;comments;color;%
                     //#type;power;stolbNumber;latitude;longtitude;adress;comments;montage#
                     //     //END//
                     out.println("//START//");
                     String str1 = "%"+tp.name+";"+tp.adress+";"+String.valueOf(tp.latitude)+";"+String.valueOf(tp.longtitude)+";"+tp.comments+";"+String.valueOf(tp.color);
                     String str2 = ";";
                     if (tp.photoPaths.size() != 0) {
                         for (String str : tp.photoPaths) {
                             str2 += str;
                             str2 += "!";
                         }
                     }
                     str2+="%";
                     out.println(str1 + str2);
                     for (Lamp lamp:tp.lamps){
                         String str11 = "#"+lamp.type+";"+lamp.roadWidth+";"+lamp.roadPolosSelection+";"+lamp.roadLength+";"+lamp.roadOsobennostSelection+";"+lamp.roadOsobennost+";"+lamp.roadRasstanovka+";"+lamp.oporaHeight+";"+lamp.typeSelection+";"+lamp.fromRoadDist+";"+lamp.typeKronstSelection+";"+lamp.viletKronst+";"+lamp.power+";"+String.valueOf(lamp.stolbNumber)+";"+String.valueOf(lamp.latitude)+";"+String.valueOf(lamp.longtitude)+";"+lamp.adress+";"+lamp.comments+";"+String.valueOf(lamp.montageSelection)+";"+lamp.lampHeight+";"+lamp.lampAmountSelection;
                         String str22 = ";";
                         if (lamp.photoPaths.size() != 0) {
                             for (String str : lamp.photoPaths) {
                                 str22 += str;
                                 str22 += "!";
                             }
                         }
                             if (lamp.roadPhotoPaths.size()!=0){
                                 for (String str : lamp.roadPhotoPaths) {
                                     str22 += str;
                                     str22 += "!";
                                 }
                             }
                         str22+="#";
                         out.println(str11 + str22);
                     }

                     out.println("//END//");
                 }
                 out.close();
            } catch (IOException e) {
                //exception handling left as an exercise for the reader
            }
        }else{
            PrintWriter writer = new PrintWriter(path);
            writer.print("");
            writer.close();
            try (FileWriter fw = new FileWriter(path, true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw)) {       //Начинаем записывать информацию о комнатах
                for (TP tp:Variables.tpList){          //  //START//
                    //%TP-1;adress;latitude;longtitude;comments;color;%
                    //#type;power;stolbNumber;latitude;longtitude;adress;comments;montage#
                    //     //END//
                    out.println("//START//");
                    String str1 = "%"+tp.name+";"+tp.adress+";"+String.valueOf(tp.latitude)+";"+String.valueOf(tp.longtitude)+";"+tp.comments+";"+String.valueOf(tp.color);
                    String str2 = ";";
                    if (tp.photoPaths.size() != 0) {
                        for (String str : tp.photoPaths) {
                            str2 += str;
                            str2 += "!";
                        }
                    }
                    str2+="%";
                    out.println(str1 + str2);
                    for (Lamp lamp:tp.lamps){
                        String str11 = "#"+lamp.type+";"+lamp.roadWidth+";"+lamp.roadPolosSelection+";"+lamp.roadLength+";"+lamp.roadOsobennostSelection+";"+lamp.roadOsobennost+";"+lamp.roadRasstanovka+";"+lamp.oporaHeight+";"+lamp.typeSelection+";"+lamp.fromRoadDist+";"+lamp.typeKronstSelection+";"+lamp.viletKronst+";"+lamp.power+";"+String.valueOf(lamp.stolbNumber)+";"+String.valueOf(lamp.latitude)+";"+String.valueOf(lamp.longtitude)+";"+lamp.adress+";"+lamp.comments+";"+String.valueOf(lamp.montageSelection)+";"+lamp.lampHeight+";"+lamp.lampAmountSelection;
                        String str22 = ";";
                        if (lamp.photoPaths.size() != 0) {
                            for (String str : lamp.photoPaths) {
                                str22 += str;
                                str22 += "!";
                            }
                        }
                        if (lamp.roadPhotoPaths.size()!=0){
                            for (String str : lamp.roadPhotoPaths) {
                                str22 += str;
                                str22 += "!";
                            }
                        }
                        str22+="#";
                        out.println(str11 + str22);
                    }

                    out.println("//END//");
                }
                out.close();
            } catch (IOException e) {
                //exception handling left as an exercise for the reader
            }
        }
        //try (FileWriter fw = new FileWriter(pathFile, true);
    }

    public static void loadFile(String path) {
        Buttons.disableCopyPaste();
        Methods.clearAll();
        Variables.currentTP=null;
        Variables.currentLamp=null;
        Variables.lastLamp=null;
        Variables.lastOperation="";
        BufferedReader reader;
        try {           //Если открываем в текущей вкладке - очищаем все комнаты текущео этажа
            reader = new BufferedReader(new FileReader(path));
            String line = reader.readLine();
            boolean infoStarted = false;
            boolean infoEnded = false;
            TP current_tp = null;


            while (line != null) {  //Пока файл не закончился считываем строка за строкой
                switch (line) {
                    case "//START//":
                        infoStarted = true;
                        infoEnded = false;
                        break;
                    case "//END//":
                        infoStarted = false;
                        infoEnded = true;
                        current_tp = null;
                        break;
                }
                    if (infoStarted && line.charAt(0) != '/') {
                        if (line.charAt(0) == '%') {
                            String[] split_line = line.split("%");
                            String[] split_tp_info = split_line[1].split(";");
                            String name = split_tp_info[0];
                            String adress = split_tp_info[1];
                            double latitude = Double.parseDouble(split_tp_info[2]);
                            double longtitude = Double.parseDouble(split_tp_info[3]);
                            String comments = split_tp_info[4];
                            int color = Integer.parseInt(split_tp_info[5]);
                            TP tp = new TP();
                            tp.name = name;
                            tp.adress = adress;
                            tp.latitude = latitude;
                            tp.longtitude = longtitude;
                            tp.comments = comments;
                            tp.color = color;
                            if (split_tp_info.length > 6) {
                                String paths = split_tp_info[6];
                                String[] split_tp_photos = paths.split("!");
                                for (int i = 0; i < split_tp_photos.length; i++) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        if (!Objects.equals(split_tp_photos[i], "%") && Files.exists(Paths.get(split_tp_photos[i]))) {
                                            tp.photoPaths.add(split_tp_photos[i]);
                                        }
                                    }
                                }
                            }
                            current_tp = tp;
                            Variables.tpList.add(tp);
                        } else if (line.charAt(0) == '#') {
                            if (current_tp != null) {
                                String[] split_line = line.split("#");
                                String[] split_lamp_info = split_line[1].split(";");
                                String type = split_lamp_info[0];
                                if (split_lamp_info.length < 21) {
                                    System.out.println();
                                    if (line.charAt(line.length()-1)!='#'){
                                        int tries=10;
                                        while(line.charAt(line.length()-1)!='#' && tries>0) {
                                            line = line.replaceAll("[\n\r]", "");
                                            line+=reader.readLine();
                                            tries--;
                                        }
                                    }
                                    if (line.charAt(line.length()-1)=='#'){
                                        split_line = line.split("#");
                                        split_lamp_info = split_line[1].split(";");
                                        type = split_lamp_info[0];
                                        String roadWidth = split_lamp_info[1];
                                        int roadPolosSelection = Integer.parseInt(split_lamp_info[2]);
                                        String roadLenght = split_lamp_info[3];
                                        int roadOsobennostSelection = Integer.parseInt(split_lamp_info[4]);
                                        String roadOsobennost = split_lamp_info[5];
                                        String roadRasstanovka = split_lamp_info[6];
                                        String oporaHeight = split_lamp_info[7];
                                        String typeSelection = split_lamp_info[8];
                                        String roadDist = split_lamp_info[9];
                                        int typeKronst = Integer.parseInt(split_lamp_info[10]);
                                        String viletKronst = split_lamp_info[11];
                                        String power = split_lamp_info[12];
                                        int stolbNumber = Integer.parseInt(split_lamp_info[13]);
                                        double latitude = Double.parseDouble(split_lamp_info[14]);
                                        double longtitude = Double.parseDouble(split_lamp_info[15]);
                                        String adress = split_lamp_info[16];
                                        String comments = split_lamp_info[17];
                                        String montage = split_lamp_info[18];
                                        int lampsAmount = Integer.parseInt(split_lamp_info[20]);
                                        String lampHeight = split_lamp_info[19];
                                        Lamp lamp = new Lamp();
                                        lamp.type = type;
                                        lamp.roadWidth = roadWidth;
                                        lamp.roadPolosSelection = roadPolosSelection;
                                        lamp.roadLength = roadLenght;
                                        lamp.roadOsobennostSelection = roadOsobennostSelection;
                                        lamp.roadOsobennost = roadOsobennost;
                                        lamp.roadRasstanovka = roadRasstanovka;
                                        lamp.power = power;
                                        lamp.stolbNumber = stolbNumber;
                                        lamp.latitude = latitude;
                                        lamp.longtitude = longtitude;
                                        lamp.adress = adress;
                                        lamp.comments = comments;
                                        lamp.montageSelection = Integer.parseInt(montage);
                                        lamp.lampAmountSelection = lampsAmount;
                                        lamp.lampHeight = lampHeight;
                                        lamp.fromRoadDist = roadDist;
                                        lamp.typeSelection = Integer.parseInt(typeSelection);
                                        lamp.typeKronstSelection = typeKronst;
                                        lamp.viletKronst = viletKronst;
                                        lamp.oporaHeight = oporaHeight;
                                        if (split_lamp_info.length > 21) {
                                            String paths = split_lamp_info[21];
                                            String[] split_lamp_photos = paths.split("!");
                                            for (int i = 0; i < split_lamp_photos.length; i++) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                    String[] tempSplit = split_lamp_photos[i].split("&");
                                                    System.out.println(tempSplit);
                                                    if (tempSplit.length > 1) {
                                                        if (!Objects.equals(split_lamp_photos[i], "#") && Files.exists(Paths.get(split_lamp_photos[i]))) {
                                                            lamp.roadPhotoPaths.add(split_lamp_photos[i]);
                                                        }
                                                    } else {
                                                        if (!Objects.equals(split_lamp_photos[i], "#") && Files.exists(Paths.get(split_lamp_photos[i]))) {
                                                            lamp.photoPaths.add(split_lamp_photos[i]);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        current_tp.lamps.add(lamp);
                                    }
                                } else{
                                    String roadWidth = split_lamp_info[1];
                                int roadPolosSelection = Integer.parseInt(split_lamp_info[2]);
                                String roadLenght = split_lamp_info[3];
                                int roadOsobennostSelection = Integer.parseInt(split_lamp_info[4]);
                                String roadOsobennost = split_lamp_info[5];
                                String roadRasstanovka = split_lamp_info[6];
                                String oporaHeight = split_lamp_info[7];
                                String typeSelection = split_lamp_info[8];
                                String roadDist = split_lamp_info[9];
                                int typeKronst = Integer.parseInt(split_lamp_info[10]);
                                String viletKronst = split_lamp_info[11];
                                String power = split_lamp_info[12];
                                int stolbNumber = Integer.parseInt(split_lamp_info[13]);
                                double latitude = Double.parseDouble(split_lamp_info[14]);
                                double longtitude = Double.parseDouble(split_lamp_info[15]);
                                String adress = split_lamp_info[16];
                                String comments = split_lamp_info[17];
                                String montage = split_lamp_info[18];
                                int lampsAmount = Integer.parseInt(split_lamp_info[20]);
                                String lampHeight = split_lamp_info[19];
                                Lamp lamp = new Lamp();
                                lamp.type = type;
                                lamp.roadWidth = roadWidth;
                                lamp.roadPolosSelection = roadPolosSelection;
                                lamp.roadLength = roadLenght;
                                lamp.roadOsobennostSelection = roadOsobennostSelection;
                                lamp.roadOsobennost = roadOsobennost;
                                lamp.roadRasstanovka = roadRasstanovka;
                                lamp.power = power;
                                lamp.stolbNumber = stolbNumber;
                                lamp.latitude = latitude;
                                lamp.longtitude = longtitude;
                                lamp.adress = adress;
                                lamp.comments = comments;
                                lamp.montageSelection = Integer.parseInt(montage);
                                lamp.lampAmountSelection = lampsAmount;
                                lamp.lampHeight = lampHeight;
                                lamp.fromRoadDist = roadDist;
                                lamp.typeSelection = Integer.parseInt(typeSelection);
                                lamp.typeKronstSelection = typeKronst;
                                lamp.viletKronst = viletKronst;
                                lamp.oporaHeight = oporaHeight;
                                if (split_lamp_info.length > 21) {
                                    String paths = split_lamp_info[21];
                                    String[] split_lamp_photos = paths.split("!");
                                    for (int i = 0; i < split_lamp_photos.length; i++) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            String[] tempSplit = split_lamp_photos[i].split("&");
                                            System.out.println(tempSplit);
                                            if (tempSplit.length > 1) {
                                                if (!Objects.equals(split_lamp_photos[i], "#") && Files.exists(Paths.get(split_lamp_photos[i]))) {
                                                    lamp.roadPhotoPaths.add(split_lamp_photos[i]);
                                                }
                                            } else {
                                                if (!Objects.equals(split_lamp_photos[i], "#") && Files.exists(Paths.get(split_lamp_photos[i]))) {
                                                    lamp.photoPaths.add(split_lamp_photos[i]);
                                                }
                                            }
                                        }
                                    }
                                }
                                current_tp.lamps.add(lamp);
                            }
                        }
                        }
                    }
                line = reader.readLine();
            }
            Variables.filePath = path;
            Methods.showTpsAndLamps();
            Methods.clearPolylines();
            Methods.displayPolylines();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void backUpFile() throws IOException {
        saveFile(Variables.filePath);
        String filePath = Variables.getCurrentTimeStamp()+";";
        for (TP tp: Variables.tpList){
            filePath+=tp.name;
            filePath+=";";
        }
        filePath+=".txt";
        File directory = new File(Variables.folderPath + "/" + "NaruzhkaApp");
        if (!directory.exists()) {
            directory.mkdir();
            // If you require it to make the entire directory path including parents,
            // use directory.mkdirs(); here instead.
        }
        directory = new File(Variables.folderPath + "/" + "NaruzhkaApp/"+"backup");
        if (!directory.exists()) {
            directory.mkdir();
            // If you require it to make the entire directory path including parents,
            // use directory.mkdirs(); here instead.
        }
        File file = new File(directory+"/"+filePath);
        file.createNewFile();
        try (FileWriter fw = new FileWriter(directory+"/"+filePath, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {       //Начинаем записывать информацию о комнатах
            for (TP tp:Variables.tpList){          //  //START//
                //%TP-1;adress;latitude;longtitude;comments;color;%
                //#type;power;stolbNumber;latitude;longtitude;adress;comments;montage#
                //     //END//
                out.println("//START//");
                String str1 = "%"+tp.name+";"+tp.adress+";"+String.valueOf(tp.latitude)+";"+String.valueOf(tp.longtitude)+";"+tp.comments+";"+String.valueOf(tp.color);
                String str2 = ";";
                if (tp.photoPaths.size() != 0) {
                    for (String str : tp.photoPaths) {
                        str2 += str;
                        str2 += "!";
                    }
                }
                str2+="%";
                out.println(str1 + str2);
                for (Lamp lamp:tp.lamps){
                    String str11 = "#"+lamp.type+";"+lamp.roadWidth+";"+lamp.roadPolosSelection+";"+lamp.roadLength+";"+lamp.roadOsobennostSelection+";"+lamp.roadOsobennost+";"+lamp.roadRasstanovka+";"+lamp.oporaHeight+";"+lamp.typeSelection+";"+lamp.fromRoadDist+";"+lamp.typeKronstSelection+";"+lamp.viletKronst+";"+lamp.power+";"+String.valueOf(lamp.stolbNumber)+";"+String.valueOf(lamp.latitude)+";"+String.valueOf(lamp.longtitude)+";"+lamp.adress+";"+lamp.comments+";"+String.valueOf(lamp.montageSelection)+";"+lamp.lampHeight+";"+lamp.lampAmountSelection;
                    String str22 = ";";
                    if (tp.photoPaths.size() != 0) {
                        for (String str : lamp.photoPaths) {
                            str22 += str;
                            str22 += "!";
                        }
                    }
                        if (lamp.roadPhotoPaths.size()!=0){
                            for (String str : lamp.roadPhotoPaths) {
                                str22 += str;
                                str22 += "!";
                            }
                        }
                    str2+="#";
                    out.println(str11 + str22);
                }

                out.println("//END//");
            }
            out.close();
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
    }
}
