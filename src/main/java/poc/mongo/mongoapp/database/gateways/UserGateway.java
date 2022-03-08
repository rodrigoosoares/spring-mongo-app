package poc.mongo.mongoapp.database.gateways;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import poc.mongo.mongoapp.adapters.UserDTOAdapter;
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

}
