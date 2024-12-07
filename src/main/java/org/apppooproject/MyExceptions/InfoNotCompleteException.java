package org.apppooproject.MyExceptions;

public class InfoNotCompleteException extends Exception{
    public InfoNotCompleteException(){}
    public InfoNotCompleteException(String msg){
        super("Some infos are missing in the fields");
    }
}
