package fr.onecraft.adventCalendar.core.helpers;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Time {
    private static SimpleDateFormat weekDay;
    private static SimpleDateFormat day;

    static {
        DateFormatSymbols symbols = new DateFormatSymbols();
        symbols.setWeekdays(new String[]{
                "?",
                "Dimanche",
                "Lundi",
                "Mardi",
                "Mercredi",
                "Jeudi",
                "Vendredi",
                "Samedi",
        });

        weekDay = new SimpleDateFormat("EEEEE", symbols);
        day = new SimpleDateFormat("dd");
    }

    public static int getDayNumber(Date date) {
        return Integer.parseInt(day.format(date));
    }

    public static String getDay(Date date) {
        return weekDay.format(date);
    }
}
