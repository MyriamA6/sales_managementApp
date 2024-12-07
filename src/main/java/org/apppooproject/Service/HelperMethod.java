package org.apppooproject.Service;

public class HelperMethod {

    //helper method to remove extra spaces from a string
    public static String removeExtraSpaces(String input) {
        if (input == null) {
            return "";
        }
        return input.trim().replaceAll("\\s+", " ");
    }
}
