package com.dpdc.realestate.utils;

import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Component
public class Utils {
    private DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SS");
    public DateFormat getDateFormatter(){
        return formatter;
    }
}
