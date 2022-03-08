package poc.mongo.mongoapp.controllers.responses;

import java.util.ArrayList;
import java.util.List;

public class UsersResponse {

    private List<UserResponse> users = new ArrayList<>();

    public UsersResponse() {}


    public List<UserResponse> getUsers() {
        return users;
    }

    public void setUsers(List<UserResponse> users) {
        this.users = users;
    }
}

