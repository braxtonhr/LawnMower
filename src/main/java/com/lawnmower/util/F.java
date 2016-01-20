package com.lawnmower.util;

public class F {

    // right justified(string)
    // leftrightjustified(String left, String right)

    public static final int width = 70;

    public static String left(String string) {

        StringBuilder message = new StringBuilder();

        String copy = string;
        int length = copy.length();

        if (length >= width) {
            while (length >= width) {

                message.append(copy.substring(0, width));
                message.append(newLine(1));
                copy = copy.substring(width, length);
                length = copy.length();

            }
        }

        int offset = width - copy.length();
        message.append(copy);
        message.append(whiteSpace(offset));
        message.append(newLine(1));

        return message.toString();

    }

    public static String right(String string) {

        StringBuilder message = new StringBuilder();

        String copy = string;
        int length = copy.length();

        while (length >= width) {

            message.append(copy.substring(0, width));
            message.append(newLine(1));
            copy = copy.substring(width, length);
            length = copy.length();

        }

        int offset = width - copy.length();
        message.append(whiteSpace(offset));
        message.append(copy);
        message.append(newLine(1));

        return message.toString();

    }

    public static String leftRight(String left, String right) {

        StringBuilder message = new StringBuilder();

        int offset = width - left.length() - right.length();

        message.append(left);
        message.append(whiteSpace(offset));
        message.append(right);
        message.append(newLine(1));

        return message.toString();

    }

    public static String whiteSpace(int length) {
        String space = "";

        for ( int i = 0 ; i < length ; i++ ) {
            space += " ";
        }

        return space;

    }

    public static String divider() {

        StringBuilder divider = new StringBuilder();

        for ( int i = 0 ; i < width ; i++ ) {
            divider.append("_");
        }
        divider.append("\n");
        return divider.toString();

    }

    public static String centered(String text) {

        StringBuilder result = new StringBuilder();

        int offset = (width - text.length())/2;

        result.append(whiteSpace(offset));
        result.append(text);
        offset = (offset%2 == 1) ? offset+=1 : offset;
        result.append(whiteSpace(offset));
        result.append("\n");

        return result.toString();

    }

    public static String newLine(int n) {

        StringBuilder message = new StringBuilder();

        for ( int i = 0 ; i < n ; i++ ) {
            message.append("\n");
        }

        return message.toString();

    }

}
