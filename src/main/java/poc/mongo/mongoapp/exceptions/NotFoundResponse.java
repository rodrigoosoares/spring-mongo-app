package poc.mongo.mongoapp.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class NotFoundResponse {

    private int status = 404;
    private String message;

    public NotFoundResponse(String message) {
        this.message = message;
    }
}
