package com.example.manhvdse61952.vrc_android.controller.resources;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GeneralController {
    public static String convertPrice(String defaultPrice){
        List<String> convertPriceList = new ArrayList<>();
        String reverse = new StringBuffer(defaultPrice).reverse().toString();
        String convertedPrice = "";
        if(reverse.length() > 3){
            for(int i=0;i <= reverse.length(); i+=3){
                convertPriceList.add(reverse.substring(i, Math.min(reverse.length(), i+3)));
            }
            convertedPrice = convertPriceList.get(0);
            for (int j = 1; j < convertPriceList.size(); j++){
                convertedPrice += "." + convertPriceList.get(j);
            }
            convertedPrice = new StringBuffer(convertedPrice).reverse().toString();
            String check = convertedPrice.substring(0,1);
            if (check.equals(".")){
                convertedPrice = convertedPrice.substring(1);
            }
        }
        else {
            convertedPrice = defaultPrice;
        }
        return convertedPrice;
    }
    public static String convertTime(long time){
        Date date = new Date(time);
        Format format = new SimpleDateFormat("HH : mm, dd/MM/yyyy");
        return format.format(date);
    }

    public static String convertFullTime(long time){
        Date date = new Date(time);
        Format format = new SimpleDateFormat("HH:mm:ss, dd-MM-yyyy");
        return format.format(date);
    }

    public static String generateChildFDB(long time){
        Date date = new Date(time);
        Format format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss:SSS");
        return format.format(date);
    }
}
