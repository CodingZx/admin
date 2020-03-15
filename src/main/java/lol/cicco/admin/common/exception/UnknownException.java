package lol.cicco.admin.common.exception;

public class UnknownException extends RuntimeException {

    public static UnknownException make(Exception e) {
        return new UnknownException(e);
    }

    public UnknownException(Throwable throwable) {
        super(throwable);
    }
}
