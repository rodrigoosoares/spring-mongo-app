package poc.mongo.mongoapp.controllers.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class UsersResponse {

    private List<UserResponse> users = new ArrayList<>();

}

