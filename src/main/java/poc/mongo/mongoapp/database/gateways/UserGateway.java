package poc.mongo.mongoapp.database.gateways;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import poc.mongo.mongoapp.adapters.UserDTOAdapter;
import poc.mongo.mongoapp.adapters.UserEntityAdapter;
import poc.mongo.mongoapp.controllers.requests.UserUpsertRequest;
import poc.mongo.mongoapp.database.entities.UserEntity;
import poc.mongo.mongoapp.database.repository.UserRepository;
import poc.mongo.mongoapp.rest.models.UserDTO;

import java.util.List;

@Service
public class UserGateway {

    private final UserRepository userRepository;

    @Autowired
    public UserGateway(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDTO> getUsers(final String status) {

        return UserDTOAdapter.fromUserEntityList(userRepository.findUsersByStatus(status));
    }

    public void insertUser(final UserUpsertRequest userUpsertRequest) {

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
