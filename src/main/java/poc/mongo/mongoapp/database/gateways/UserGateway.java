package poc.mongo.mongoapp.database.gateways;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import poc.mongo.mongoapp.adapters.UserAdapter;
import poc.mongo.mongoapp.adapters.UserEntityAdapter;
import poc.mongo.mongoapp.controllers.requests.UserUpsertRequest;
import poc.mongo.mongoapp.database.entities.UserEntity;
import poc.mongo.mongoapp.database.repository.UserRepository;
import poc.mongo.mongoapp.exceptions.AlreadyExistentException;
import poc.mongo.mongoapp.exceptions.NotFoundException;
import poc.mongo.mongoapp.rest.models.Pagination;
import poc.mongo.mongoapp.rest.models.User;

import java.util.List;
import java.util.Objects;

@Service
public class UserGateway {

    private final UserRepository userRepository;

    @Autowired
    public UserGateway(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsersByStatus(final String status) {

        final List<UserEntity> userEntities = userRepository.findUsersByStatus(status);

        return UserAdapter.fromUserEntityList(userEntities);
    }

    public List<User> getUsersByStatusPageable(final String status, final Pagination pagination) {

        final List<UserEntity> userEntities = userRepository.findUsersByStatus(status, pagination);

        return UserAdapter.fromUserEntityList(userEntities);

    }

    public User getUserByEmail(final String email) throws NotFoundException {

        final UserEntity userDTO = userRepository.findUsersByEmail(email);

        if(Objects.isNull(userDTO)) {
            throw new NotFoundException("Not found user with e-mail " + email);
        }

        return UserAdapter.fromUserDTO(userDTO);
    }

    public void insertUser(final UserUpsertRequest userUpsertRequest) throws AlreadyExistentException {

        final UserEntity userEntity = UserEntityAdapter.fromUserUpsertRequest(userUpsertRequest);
        userEntity.setStatus("active");

        userRepository.insert(userEntity);
    }

    public void updateUser(final UserUpsertRequest userUpsertRequest) {

        userRepository.update(
                UserEntityAdapter.fromUserUpsertRequest(userUpsertRequest)
        );
    }

    public void inactiveUser(final String email) {

        userRepository.inactiveUser(email);
    }

}
