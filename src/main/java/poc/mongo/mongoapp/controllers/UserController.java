package poc.mongo.mongoapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import poc.mongo.mongoapp.adapters.UsersResponseAdapter;
import poc.mongo.mongoapp.controllers.requests.UserUpsertRequest;
import poc.mongo.mongoapp.controllers.responses.UsersResponse;
import poc.mongo.mongoapp.database.gateways.UserGateway;
import poc.mongo.mongoapp.validation.StatusValidation;

@Validated
@RestController()
@RequestMapping(path = "/users")
public class UserController {

    private final UserGateway userGateway;

    @Autowired
    public UserController(final UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    @GetMapping
    public ResponseEntity<UsersResponse> getAllUsers(@RequestParam(defaultValue = "active") @StatusValidation final String status) {

        return ResponseEntity.ok(UsersResponseAdapter.fromUserDTO(userGateway.getUsers(status)));
    }

    @PutMapping
    public ResponseEntity<Void> upsertUser(@RequestBody final UserUpsertRequest userUpsertRequest) {

        userGateway.insertOrUpdateUser(userUpsertRequest);

        return ResponseEntity.accepted().build();

    }

}
