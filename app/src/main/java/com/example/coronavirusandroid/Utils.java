package com.example.coronavirusandroid;

import androidx.core.util.Pair;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Locale;

public class Utils {
    static LocalDate localDate = LocalDate.now();
    static int month = localDate.getMonth().getValue();
    static int currentDay = localDate.getDayOfMonth();
    static int dayOfWeek = localDate.getDayOfWeek().getValue();
    static int prevMonth = month - 1;
    static int year = localDate.getYear();
    static String formattedMonth = "";
    static String prevFormattedMonth = "";

    public static boolean validateCountryInput(String countryInput) {
        String[] locales = Locale.getISOCountries();
        Locale localeObject;
        for (String countryCode : locales) {
            localeObject = new Locale("English", countryCode);
            if (localeObject.getDisplayCountry().equalsIgnoreCase(countryInput)) {
                return true;
            }
        }
        return false;
    }

    public static int getThisMonthCases(List<Pair<String, Integer>> casesPerDayList) {
        int thisMonthCases = 0;
        int prevMonthCases = 0;
        YearMonth yearPrevMonth = YearMonth.of(year, month - 1);
        int daysPrevMonth = yearPrevMonth.lengthOfMonth();


        if (Integer.toString(month).length() == 1) {
            formattedMonth = "0" + Integer.toString(month);
        } else {
            formattedMonth = Integer.toString(month);
        }

        if (Integer.toString(prevMonth).length() == 1) {
            prevFormattedMonth = "0" + Integer.toString(prevMonth);
        } else {
            prevFormattedMonth = Integer.toString(prevMonth);
        }

        for (Pair<String, Integer> cases : casesPerDayList) {
            if (cases.first.substring(5, 7).contentEquals(
                    formattedMonth)) {
                thisMonthCases = cases.second;
            } else if (cases.first.substring(5, 7).contentEquals(prevFormattedMonth)) {
                prevMonthCases = cases.second;
                if (Integer.parseInt(cases.first.substring(8, 10)) == daysPrevMonth) {
                    thisMonthCases = cases.second;
                }
            }
        }

        thisMonthCases -= prevMonthCases;
        return thisMonthCases;
    }

    public static int getThisWeekCases(List<Pair<String, Integer>> casesPerDayList) {
        int thisWeekCases = 0;
        int lastWeekCases = 0;
        YearMonth yearPrevMonth = YearMonth.of(year, month - 1);
        int daysPrevMonth = yearPrevMonth.lengthOfMonth();
        int dayOfWeekPrevMonth = LocalDate.of(year, prevMonth, daysPrevMonth).
                getDayOfWeek().getValue();
        // get data starting from the current day to the first day of the week
        int startDay;
        int startDayPrevMonth;
        int prevDayOfCurrent = 0;
        if (dayOfWeek != 1) {
            startDay = currentDay - (dayOfWeek - 1);
        } else {
            startDay = currentDay;
        }

        if (Integer.toString(currentDay).length() == 1) {
            String currentDayString = "0" + Integer.toString(currentDay);
            String prevDayOfCurrentString = "0" + Integer.toString(currentDay - 1);
            currentDay = Integer.parseInt(currentDayString);
            prevDayOfCurrent = Integer.parseInt(prevDayOfCurrentString);

        }

        if (dayOfWeek != 1) {
            startDayPrevMonth = daysPrevMonth - (dayOfWeekPrevMonth - 1);
        } else {
            startDayPrevMonth = daysPrevMonth;
        }


        if (Integer.toString(month).length() == 1) {
            formattedMonth = "0" + Integer.toString(month);
        } else {
            formattedMonth = Integer.toString(month);
        }

        if (Integer.toString(prevMonth).length() == 1) {
            prevFormattedMonth = "0" + Integer.toString(prevMonth);
        } else {
            prevFormattedMonth = Integer.toString(prevMonth);
        }

            for (Pair<String, Integer> cases : casesPerDayList) {
                int getDayOfPrevMonth = Integer.parseInt(cases.first.substring(8, 10));
                String prevMonth = cases.first.substring(5, 7);

                if (startDay < 0) {
                    // need to get cases from previous month
                    // need to get last week's cases to subtract from current week's cases
                    if (getDayOfPrevMonth ==
                    startDayPrevMonth - 1 && prevFormattedMonth.contentEquals(prevMonth)) {
                        lastWeekCases = cases.second;
                    }
                } else {
                    // get last week cases where the current week does not start in the
                    // previous month
                    if (getDayOfPrevMonth == daysPrevMonth
                            && prevFormattedMonth.contentEquals(prevMonth)) {
                        lastWeekCases = cases.second;
                    }
                }
            }

        for (Pair<String, Integer> cases : casesPerDayList) {
            String monthString = cases.first.substring(5, 7);
            int getDayOfMonth = Integer.parseInt(cases.first.substring(8, 10));
            if ( (getDayOfMonth == currentDay || getDayOfMonth == prevDayOfCurrent)
                    &&
            formattedMonth.contentEquals(monthString)) {
                // add to the week's cases
                thisWeekCases = cases.second;
            } else if (getDayOfMonth == daysPrevMonth && prevFormattedMonth.contentEquals(
                    monthString)) {
                thisWeekCases = cases.second;
            }
            if (startDay - 1 >= 1) {
                if (getDayOfMonth == startDay - 1 &&
                        formattedMonth.contentEquals(monthString)) {
                    lastWeekCases = cases.second;
                }
            }

            // get last week's cases to subtract from current week's cases
        }
        return thisWeekCases - lastWeekCases;
    }
}
