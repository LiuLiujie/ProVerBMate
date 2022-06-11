package nl.utwente.proverb.exceptions;

public class InvalidResourceURLException extends RuntimeException{

    public InvalidResourceURLException(){
        super();
    }

    public InvalidResourceURLException(String msg){
        super(msg);
    }
}
