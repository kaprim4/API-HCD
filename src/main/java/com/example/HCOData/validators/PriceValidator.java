package com.example.HCOData.validators;


import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
public class PriceValidator {


    public static boolean checkPriceBetweenMinMax(String min, String max, BigDecimal price, String offre) {

        if (min == null || max == null || price == null || offre == null) {
            return false;
        }

        min = min.trim();
        max = max.trim();
        offre = offre.trim();

        if (min.isEmpty() || max.isEmpty() || offre.isEmpty()) {
            return false;
        }

        try {

            Double priceValue = price.doubleValue();
            Double value = Double.parseDouble(offre);

            if (priceValue >= calcMin(Double.parseDouble(min), value) && priceValue <= calcMax(Double.parseDouble(max), value)) {
                return true;
            } else {
                return false;
            }
        } catch (NumberFormatException e) {
            System.err.println("Error parsing min, max, or price as Double: " + e.getMessage());
            return false;
        }
    }

    public static BigDecimal parseToBigDecimal(String value) {
        try {
            if (value != null && !value.isEmpty() && value.trim().length() > 0) {
                String cleanedValue = value.trim().replace(",", ".");
                if (DateTimeValidator.matches(cleanedValue, "\\d+\\.\\d+") || DateTimeValidator.isNumeric(cleanedValue)) {
                    return new BigDecimal(Double.parseDouble(cleanedValue));
                }
            }
        } catch (NumberFormatException e) {
            log.error("Failed to parse value: {} ", value, e);
        }
        return BigDecimal.ZERO;
    }

    public static Double calcMin(Double minValue, Double value) {
        return minValue - (value / 100);
    }

    public static Double calcMax(Double maxValue, Double value) {
        return maxValue + (value / 100);
    }

}
