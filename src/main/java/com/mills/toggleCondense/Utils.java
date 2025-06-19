package com.mills.toggleCondense;

public class Utils {

    public static String format(String string) {

        StringBuilder builder = new StringBuilder();

        for (String word : string.split("_")) {
            String first = word.substring(0, 1);
            String second = word.substring(1);

            builder.append(first.toUpperCase()).append(second.toLowerCase()).append(" ");
        }

        return builder.toString().trim();
    }

}
