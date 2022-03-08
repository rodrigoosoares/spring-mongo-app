package poc.mongo.mongoapp.database.entities;

import lombok.*;
import org.bson.types.ObjectId;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

        private ObjectId id;

        private String firstName;

        private String lastName;

        private String email;

        private LocalDate birthDate;

        private String status;

        public UserEntity(String firstName, String lastName, String email, LocalDate birthDate, String status) {
                this.firstName = firstName;
                this.lastName = lastName;
                this.email = email;
                this.birthDate = birthDate;
                this.status = status;
        }
}
