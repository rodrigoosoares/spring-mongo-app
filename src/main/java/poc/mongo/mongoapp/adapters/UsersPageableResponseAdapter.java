package poc.mongo.mongoapp.adapters;

import poc.mongo.mongoapp.controllers.responses.UserResponse;
import poc.mongo.mongoapp.controllers.responses.UsersPageableResponse;
import poc.mongo.mongoapp.rest.models.Pagination;
import poc.mongo.mongoapp.rest.models.User;

import java.util.List;

public class UsersPageableResponseAdapter {

    private UsersPageableResponseAdapter() {}

    public static UsersPageableResponse fromUserDTOList(final List<User> userList, final Pagination pagination) {

        final UsersPageableResponse usersPageableResponse = new UsersPageableResponse();

        userList.forEach(userDTO -> usersPageableResponse.getUsers().add(fromUserDTO(userDTO)));
        usersPageableResponse.setPagination(pagination);

        return usersPageableResponse;

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
