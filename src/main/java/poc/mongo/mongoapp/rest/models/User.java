package poc.mongo.mongoapp.rest.models;

import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthDate;
    private String status;

}
