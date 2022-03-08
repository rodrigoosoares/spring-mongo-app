package poc.mongo.mongoapp.controllers.requests;

public enum Status {

    ACTIVE ("active"), DELETED ("deleted");

    private final String status;

    Status(final String status) {
        this.status = status;

    }

    public String getStatus() {
        return status;
    }
}
