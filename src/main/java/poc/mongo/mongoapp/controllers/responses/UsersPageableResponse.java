package poc.mongo.mongoapp.controllers.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import poc.mongo.mongoapp.rest.models.Pagination;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class UsersPageableResponse {

    private List<UserResponse> users = new ArrayList<>();
    private Pagination pagination;

}

