package poc.mongo.mongoapp.adapters;

import poc.mongo.mongoapp.database.entities.UserEntity;
import poc.mongo.mongoapp.rest.models.UserDTO;

import java.util.ArrayList;
import java.util.List;

public class UserDTOAdapter {

    private UserDTOAdapter() {}

    public static List<UserDTO> fromUserEntityList(final List<UserEntity> userEntityList) {

        final List<UserDTO> usersDTO = new ArrayList<>();

        userEntityList.forEach(userEntity -> usersDTO.add(fromUserDTO(userEntity)));

        return usersDTO;

    }

    private static UserDTO fromUserDTO(final UserEntity userDTO) {

        final UserDTO userResponse = new UserDTO();

        userResponse.setFirstName(userDTO.getFirstName());
        userResponse.setLastName(userDTO.getLastName());
        userResponse.setEmail(userDTO.getEmail());
        userResponse.setBirthDate(userDTO.getBirthDate());
        userResponse.setStatus(userDTO.getStatus());

        return userResponse;
    }

}
