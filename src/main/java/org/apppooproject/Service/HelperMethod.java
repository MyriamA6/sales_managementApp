package org.apppooproject.Service;

public class HelperMethod {

    //helper method to remove extra spaces from a string
    public static String removeExtraSpaces(String input) {
        if (input == null) {
            return "";
        }
        return input.trim().replaceAll("\\s+", " ");
    }

    //method to check if the given arguments are null
    public static boolean nothingIsNullOrEmpty(String ... strings) {
        for (String s : strings) {
            if (s == null) return false;
            else{
                if(removeExtraSpaces(s).isEmpty()) return false;
            }
        }
        return true;
    }
}
