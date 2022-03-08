package poc.mongo.mongoapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import poc.mongo.mongoapp.adapters.UsersResponseAdapter;
import poc.mongo.mongoapp.controllers.responses.UsersResponse;
import poc.mongo.mongoapp.database.gateways.UserGateway;
import poc.mongo.mongoapp.validation.StatusValidation;

@Validated
@RestController
public class UserController {

    private final UserGateway userGateway;

    @Autowired
    public UserController(final UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    @GetMapping("/users")
    public UsersResponse getAllUsers(@RequestParam(defaultValue = "active") @StatusValidation final String status) {

        return UsersResponseAdapter.fromUserDTO(userGateway.getUsers(status));
    }

}
