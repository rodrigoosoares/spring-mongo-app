package poc.mongo.mongoapp.controllers.responses;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserResponse {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String birthDate;
    private String status;

}
