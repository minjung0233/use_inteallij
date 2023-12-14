package com.example.smartteamdailyapplication.helper;

import androidx.databinding.InverseMethod;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Converter {
    @InverseMethod("stringToDate")
    public static String dateToString(Date value) {
        DateFormat format = new SimpleDateFormat("dd.MM.yyyy.");
        Date date = value;

        return format.format(date);
    }

    public static Date stringToDate(String value) {
        DateFormat isFormat = new SimpleDateFormat("dd.MM.yyyy.");
        Date date;

        try {
            date = isFormat.parse(value);
        } catch (ParseException e) {
            date  = null;
        }

        return date;
    }
}
