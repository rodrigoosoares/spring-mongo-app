package poc.mongo.mongoapp.exceptions;

public class AlreadyExistentException extends Throwable {

    private final String exceptionMessage;

    public AlreadyExistentException(final String message) {
        this.exceptionMessage = message;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }
}
