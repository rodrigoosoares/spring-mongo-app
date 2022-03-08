package poc.mongo.mongoapp.adapters;

import poc.mongo.mongoapp.controllers.responses.UserResponse;
import poc.mongo.mongoapp.controllers.responses.UsersResponse;
import poc.mongo.mongoapp.rest.models.UserDTO;

import java.util.List;

public class UsersResponseAdapter {

    private UsersResponseAdapter() {}

    public static UsersResponse fromUserDTO(final List<UserDTO> userDTOList) {

        final UsersResponse usersResponse = new UsersResponse();

        userDTOList.forEach(userDTO -> usersResponse.getUsers().add(fromUserDTO(userDTO)));

        return usersResponse;

    }

    private static UserResponse fromUserDTO(final UserDTO userDTO) {

        final UserResponse userResponse = new UserResponse();

        userResponse.setFirstName(userDTO.getFirstName());
        userResponse.setLastName(userDTO.getLastName());
        userResponse.setEmail(userDTO.getEmail());
        userResponse.setBirthDate(userDTO.getBirthDate().toString());
        userResponse.setStatus(userDTO.getStatus());

        return userResponse;
    }

}
