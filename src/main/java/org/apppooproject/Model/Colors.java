package org.apppooproject.Model;

public enum Colors {
    BLUE, GREEN, RED, YELLOW, PURPLE, WHITE, BLACK, PINK, ORANGE;

    public static Colors giveTheColor(String color){
        if(color==null){
            return null;
        }
        if(color.equalsIgnoreCase("blue")){
            return BLUE;
        }
        if(color.equalsIgnoreCase("green")){
            return GREEN;
        }
        if(color.equalsIgnoreCase("red")){
            return RED;
        }
        if(color.equalsIgnoreCase("yellow")){
            return YELLOW;
        }
        if(color.equalsIgnoreCase("purple")){
            return PURPLE;
        }
        if(color.equalsIgnoreCase("white")){
            return WHITE;
        }
        if(color.equalsIgnoreCase("black")){
            return BLACK;
        }
        if(color.equalsIgnoreCase("pink")){
            return PINK;
        }
        if(color.equalsIgnoreCase("orange")){
            return ORANGE;
        }
        return null;
    }

    public static String getString(Colors color){
        if (color==BLUE){
            return "blue";
        }
        if (color==GREEN){
            return "green";
        }
        if (color==RED){
            return "red";
        }
        if (color==YELLOW){
            return "yellow";
        }
        if (color==PURPLE){
            return "purple";
        }
        if (color==WHITE){
            return "white";
        }
        if (color==BLACK){
            return "black";
        }
        if (color==PINK){
            return "pink";
        }
        if (color==ORANGE){
            return "orange";
        }
        return null;
    }

}
