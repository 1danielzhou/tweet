package com.daniel.ltc20.utils;

public class NumberFormatUtils {
    public static String formatNumber(int number, int digits) {
        String numberStr = String.valueOf(number);
        if (numberStr.length() >= digits) {
            return numberStr;
        } else {
            StringBuilder formattedNumber = new StringBuilder();
            for (int i = 0; i < digits - numberStr.length(); i++) {
                formattedNumber.append('0');
            }
            formattedNumber.append(numberStr);
            return formattedNumber.toString();
        }
    }

    public static String convertTo26Base(int number) {
        StringBuilder result = new StringBuilder();
        while (number > 0) {
            number--; // 由于26进制不包含0，所以每次减1进行转换
            int remainder = number % 26;
            char ch = (char) (remainder + 'a');
            result.insert(0, ch);
            number /= 26;
        }
        return result.toString();
    }

}
