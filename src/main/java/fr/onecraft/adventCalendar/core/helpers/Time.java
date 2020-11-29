package fr.onecraft.adventCalendar.core.helpers;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Time {
    private static final SimpleDateFormat weekDay;
    private static final SimpleDateFormat day;
    private static final SimpleDateFormat month;

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
        month = new SimpleDateFormat("MM");
    }

    public static int getMonthNumber(Date date) {
        return Integer.parseInt(month.format(date));
    }

    public static int getDayNumber(Date date) {
        return Integer.parseInt(day.format(date));
    }

    public static String getDay(Date date) {
        return weekDay.format(date);
    }
}
