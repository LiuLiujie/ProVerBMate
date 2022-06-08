package nl.utwente.proverb.exceptions;

public class NoSuchArticleException extends RuntimeException{

    public NoSuchArticleException(){
        super("No such article");
    }

    public NoSuchArticleException(String msg){
        super(msg);
    }
}
