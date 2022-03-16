package poc.mongo.mongoapp.controllers.requests;

import lombok.*;

import java.time.LocalDate;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpsertRequest {

    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthDate;
    private String status;

}
