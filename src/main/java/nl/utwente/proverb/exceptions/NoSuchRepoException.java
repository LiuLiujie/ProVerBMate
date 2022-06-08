package nl.utwente.proverb.exceptions;

public class NoSuchRepoException extends RuntimeException{

    public NoSuchRepoException() {
        super();
    }

    public NoSuchRepoException(String msg) {
        super(msg);
    }
}
