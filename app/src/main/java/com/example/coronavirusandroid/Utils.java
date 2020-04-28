package com.example.coronavirusandroid;

import java.util.Locale;

public class Utils {

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
}
