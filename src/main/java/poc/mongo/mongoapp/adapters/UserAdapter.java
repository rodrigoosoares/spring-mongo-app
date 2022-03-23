package poc.mongo.mongoapp.adapters;

import poc.mongo.mongoapp.database.entities.UserEntity;
import poc.mongo.mongoapp.rest.models.User;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter {

    private UserAdapter() {}

    public static List<User> fromUserEntityList(final List<UserEntity> userEntityList) {

        final List<User> usersDTO = new ArrayList<>();

        userEntityList.forEach(userEntity -> usersDTO.add(fromUserDTO(userEntity)));

        return usersDTO;

    }

    public static User fromUserDTO(final UserEntity userDTO) {

        final User userResponse = new User();

        userResponse.setFirstName(userDTO.getFirstName());
        userResponse.setLastName(userDTO.getLastName());
        userResponse.setEmail(userDTO.getEmail());
        userResponse.setBirthDate(userDTO.getBirthDate());
        userResponse.setStatus(userDTO.getStatus());

        return userResponse;
    }

}
