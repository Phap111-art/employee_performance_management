package com.example.springemployee.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import static java.sql.Date.valueOf;

public class DateUtils {
    final Logger logger = LoggerFactory.getLogger(DateUtils.class);
    private final static DateUtils INSTANCE = new DateUtils();
    DateUtils() {
    }
    public static DateUtils getInstance(){
        return INSTANCE;
    }


    public  String getDateToString(Date dateToString) {
        SimpleDateFormat faDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return faDateFormat.format(dateToString);
    }

    public Date getStringToDate(String stringToDate) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(stringToDate);
        } catch (ParseException e) {
            logger.debug(e.toString(), 1);
        }
        return null;
    }
    public  LocalDateTime getCurrentDate() {
        LocalDateTime currentDate = LocalDateTime.now();
        return currentDate;
    }


}
