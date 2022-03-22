package poc.mongo.mongoapp.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NotFoundException extends Throwable {

    @JsonProperty("message")
    private final String exceptionMessage;

    public NotFoundException(final String message) {
        this.exceptionMessage = message;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }
}
