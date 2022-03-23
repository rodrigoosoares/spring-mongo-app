package poc.mongo.mongoapp.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BadRequestException extends Throwable {

    @JsonProperty("message")
    private final String exceptionMessage;

    public BadRequestException(final String message) {
        this.exceptionMessage = message;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }
}
