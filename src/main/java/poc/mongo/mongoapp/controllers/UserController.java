package poc.mongo.mongoapp.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import poc.mongo.mongoapp.adapters.UsersPageableResponseAdapter;
import poc.mongo.mongoapp.adapters.UsersResponseAdapter;
import poc.mongo.mongoapp.controllers.requests.UserUpsertRequest;
import poc.mongo.mongoapp.controllers.responses.UserResponse;
import poc.mongo.mongoapp.controllers.responses.UsersPageableResponse;
import poc.mongo.mongoapp.controllers.responses.UsersResponse;
import poc.mongo.mongoapp.database.gateways.UserGateway;
import poc.mongo.mongoapp.exceptions.AlreadyExistentException;
import poc.mongo.mongoapp.exceptions.BadRequestException;
import poc.mongo.mongoapp.exceptions.NotFoundException;
import poc.mongo.mongoapp.rest.models.Pagination;
import poc.mongo.mongoapp.validation.StatusValidation;

import static poc.mongo.mongoapp.commons.HeadersConstants.TRACE_ID;

@Validated
@RestController()
@RequestMapping(path = "/user")
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    private final UserGateway userGateway;

    @Autowired
    public UserController(final UserGateway userGateway) {

        this.userGateway = userGateway;
    }

    @GetMapping(path = "/users")
    public ResponseEntity<UsersResponse> getAllUsersByStatus(@RequestHeader(name = TRACE_ID) final String traceId,
                                                     @RequestParam(defaultValue = "active") @StatusValidation final String status) {

        LOG.info("GET - Start request for get users by status {}", status);

        final UsersResponse usersResponse = UsersResponseAdapter.fromUserDTOList(userGateway.getUsersByStatus(status));

        LOG.info("GET - Finish request for get users by status {}", status);

        return ResponseEntity.ok(usersResponse);
    }

    @GetMapping(path = "/users/pageable")
    public ResponseEntity<UsersPageableResponse> getAllUsersByStatusPageable(@RequestHeader(name = TRACE_ID) final String traceId,
                                                                             @RequestParam final Integer page,
                                                                             @RequestParam final Integer size,
                                                                             @RequestParam(defaultValue = "active") @StatusValidation final String status) {

        LOG.info("GET - Start request for get users by status {}, page {}, size {}", status, page, size);

        final Pagination pagination = new Pagination(page, size);

        final UsersPageableResponse usersResponse = UsersPageableResponseAdapter.fromUserDTOList(
                userGateway.getUsersByStatusPageable(status, pagination),
                pagination
        );

        LOG.info("GET - Finish request for get users by status {}, page {}, size {}", status, page, size);

        return ResponseEntity.ok(usersResponse);
    }

    @GetMapping
    public ResponseEntity<UserResponse> getUserByEmail(@RequestHeader(name = TRACE_ID) final String traceId,
                                                       @RequestParam final String email) throws NotFoundException {

        LOG.info("GET - Start request for get user by email {}", email);

        final UserResponse usersResponse = UsersResponseAdapter.fromUserDTO(userGateway.getUserByEmail(email));

        LOG.info("GET - Finish request for get user by email {}", email);

        return ResponseEntity.ok(usersResponse);
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestHeader(name = TRACE_ID) final String traceId,
                                           @RequestBody final UserUpsertRequest userUpsertRequest) throws AlreadyExistentException {

        LOG.info("POST - Start request for insert new user");

        userGateway.insertUser(userUpsertRequest);

        LOG.info("POST - Finished request for insert new user");

        return ResponseEntity.accepted().build();

    }

    @PutMapping
    public ResponseEntity<Void> updateUser(@RequestHeader(name = TRACE_ID) final String traceId,
                                           @RequestBody final UserUpsertRequest userUpsertRequest) {

        LOG.info("PUT - Start request for update an user");

        userGateway.updateUser(userUpsertRequest);

        LOG.info("PUT - Finished request for update an user");

        return ResponseEntity.accepted().build();

    }

    @DeleteMapping
    public ResponseEntity<Void> inactiveUser(@RequestHeader(name = TRACE_ID) final String traceId,
                                             @RequestParam(name = "email") final String email) throws BadRequestException {

        LOG.info("DELETE - Start request for inactive the user: {}", email);

        userGateway.inactiveUser(email);

        LOG.info("DELETE - Finished request for inactive an user {}", email);

        return ResponseEntity.accepted().build();

    }

}
