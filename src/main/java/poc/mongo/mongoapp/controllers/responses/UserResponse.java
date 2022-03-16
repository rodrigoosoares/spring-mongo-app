package poc.mongo.mongoapp.controllers.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UserResponse {

    private String firstName;
    private String lastName;
    private String email;
    private String birthDate;
    private String status;

}
