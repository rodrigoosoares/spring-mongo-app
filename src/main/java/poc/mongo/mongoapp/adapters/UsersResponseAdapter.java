package poc.mongo.mongoapp.adapters;

import poc.mongo.mongoapp.controllers.responses.UserResponse;
import poc.mongo.mongoapp.controllers.responses.UsersResponse;
import poc.mongo.mongoapp.rest.models.User;

import java.util.List;

public class UsersResponseAdapter {

    private UsersResponseAdapter() {}

    public static UsersResponse fromUserDTOList(final List<User> userList) {

        final UsersResponse usersResponse = new UsersResponse();

        userList.forEach(userDTO -> usersResponse.getUsers().add(fromUserDTO(userDTO)));

        return usersResponse;

    }

    public static UserResponse fromUserDTO(final User user) {

        final UserResponse userResponse = new UserResponse();

        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setEmail(user.getEmail());
        userResponse.setBirthDate(user.getBirthDate().toString());
        userResponse.setStatus(user.getStatus());

        return userResponse;
    }

}
