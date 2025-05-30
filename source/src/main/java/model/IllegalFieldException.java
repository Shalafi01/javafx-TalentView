package model;

public class IllegalFieldException extends IllegalArgumentException{

    private final String err;
    public IllegalFieldException(String err) {
        this.err = err;
    }

    @Override
    public String toString() {
        return err;
    }
}
