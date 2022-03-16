package poc.mongo.mongoapp.adapters;

import poc.mongo.mongoapp.controllers.requests.UserUpsertRequest;
import poc.mongo.mongoapp.database.entities.UserEntity;

public class UserEntityAdapter {

    private UserEntityAdapter() {}

    public static UserEntity fromUserUpsertRequest(final UserUpsertRequest userUpsertRequest) {

        final UserEntity userEntity = new UserEntity();

        userEntity.setFirstName(userUpsertRequest.getFirstName());
        userEntity.setLastName(userUpsertRequest.getLastName());
        userEntity.setEmail(userUpsertRequest.getEmail());
        userEntity.setBirthDate(userUpsertRequest.getBirthDate());
        userEntity.setStatus(userUpsertRequest.getStatus());

        return userEntity;

    }

}
