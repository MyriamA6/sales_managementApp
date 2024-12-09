package org.apppooproject.MyExceptions;

// Class extended from Exception to demonstrate how to create a custom Exception
public class InfoNotCompleteException extends Exception{

    public InfoNotCompleteException(){}

    public InfoNotCompleteException(String msg){
        super("Some infos are missing in the fields");
    }
}
