package com.example.HCOData.constant;


import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;


public class Patterns {

    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");

    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public static final String PATTERN_CITY = "([A-Za-z\\s_.-]+)";

    public static final String PATTERN_POSTAL_CODE = "[0-9][0-9][0-9][0-9][0-9]";

    public static final String[] ARRAY_ACC_CHARS = {"Š", "Ž", "š", "ž", "Ÿ", "À", "Á", "Â", "Ã", "Ä", "Å", "Ç", "È", "É", "Ê", "Ë", "Ì", "Í", "Î", "Ï", "Ð", "Ñ", "Ò", "Ó", "Ô", "Õ", "Ö", "Ù", "Ú", "Û", "Ü", "Ý", "à", "á", "â", "ã", "ä", "å", "ç", "è", "é", "ê", "ë", "ì", "í", "î", "ï", "ð", "ñ", "ò", "ó", "ô", "õ", "ö", "ù", "ú", "û", "ü", "ý", "ÿ"};

    public static final String[] ARRAY_REG_CHARS = {"S", "Z", "s", "z", "Y", "A", "A", "A", "A", "A", "A", "C", "E", "E", "E", "E", "I", "I", "I", "I", "D", "N", "O", "O", "O", "O", "O", "U", "U", "U", "U", "Y", "a", "a", "a", "a", "a", "a", "c", "e", "e", "e", "e", "i", "i", "i", "i", "d", "n", "o", "o", "o", "o", "o", "u", "u", "u", "u", "y", "y"};
}
