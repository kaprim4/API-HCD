package com.example.HCOData.validators;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddressCityValidator {

    public static String getStringFromStringRegex(String str, String pattern) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(str);
        if (m.find()) {
            return m.group();
        }
        return null;
    }
}
