package com.lar.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Component
public class DateFormatConfig implements Formatter<Date> {
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


    @Override
    public Date parse(String s, Locale locale) throws  ParseException {
        return dateFormat.parse(s);
    }

    @Override
    public String print(Date date, Locale locale) {
        return dateFormat.format(date);
    }
}
