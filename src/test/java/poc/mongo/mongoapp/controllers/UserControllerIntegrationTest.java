package poc.mongo.mongoapp.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import poc.mongo.mongoapp.controllers.requests.UserUpsertRequest;
import poc.mongo.mongoapp.database.gateways.UserGateway;
import poc.mongo.mongoapp.exceptions.AlreadyExistentException;
import poc.mongo.mongoapp.exceptions.NotFoundException;
import poc.mongo.mongoapp.rest.models.Pagination;
import poc.mongo.mongoapp.rest.models.User;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static poc.mongo.mongoapp.testhelpers.AssertResponse.assertResponse;
import static poc.mongo.mongoapp.testhelpers.LoadPayload.loadJson;

@AutoConfigureWebMvc
@WebMvcTest(controllers = UserController.class)
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserGateway userGateway;

    private static final String RESPONSE_PATH = "payloads/controllers/user/response/";
    private static final String REQUEST_PATH = "payloads/controllers/user/request/";

    @ParameterizedTest
    @ValueSource(strings = {"active", "deleted"})
    @DisplayName("GET /user/users - Should return all users filtered by status, when receive a valid status parameter")
    void shouldReturnAllUsersFilteredByStatusWhenReceiveAValidStatusParameter(final String statusArg) throws Exception {

        // Given
        doReturn(mockUsersFromGateway()).when(userGateway).getUsersByStatus(statusArg);

        final String expectedResponse = loadJson(RESPONSE_PATH + "success-get-users.json");

        // When/Then
        mvc.perform(get("/user/users")
                .param("status", statusArg)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(result -> assertResponse(result.getResponse().getContentAsString(), expectedResponse));
    }

    @Test
    @DisplayName("GET /user/users - Should return invalid type error message payload, when receive an invalid status parameter")
    void shouldReturnInvalidTypeErrorMessagePayloadWhenReceiveAnInValidStatusParameter() throws Exception {

        // Given
        final String invalidStatus = "INVALID";

        final String expectedResponse = loadJson(RESPONSE_PATH + "failed-get-users-invalid-status.json");

        // When/Then
        mvc.perform(get("/user/users")
                        .param("status", invalidStatus)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(result -> assertResponse(result.getResponse().getContentAsString(), expectedResponse));
    }

    @ParameterizedTest
    @ValueSource(strings = {"active", "deleted"})
    @DisplayName("GET /user/users/pageable - Should return all users filtered by status, when receive a valid status parameter")
    void shouldReturnAllUsersFilteredByStatusWhenReceiveAValidStatusAndPaginationParameters(final String statusArg) throws Exception {

        // Given
        final Pagination pagination = new Pagination();
        pagination.setPage(1);
        pagination.setSize(3);
        doReturn(mockUsersFromGateway()).when(userGateway).getUsersByStatusPageable(statusArg, pagination);

        final String expectedResponse = loadJson(RESPONSE_PATH + "success-get-users-with-pagination.json");

        // When/Then
        mvc.perform(get("/user/users/pageable")
                        .param("status", statusArg)
                        .param("page", pagination.getPage().toString())
                        .param("size", pagination.getSize().toString())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(result -> assertResponse(result.getResponse().getContentAsString(), expectedResponse));
    }

    @Test
    @DisplayName("GET /user - Should return user filtered by email, when receive a valid email parameter")
    void shouldReturnAllUsersFilteredByStatusWhenReceiveAValidStatusParameter() throws Exception, NotFoundException {

        // Given
        final String email = "alis@email.com";

        doReturn(mockUserFromGateway()).when(userGateway).getUserByEmail(email);

        final String expectedResponse = loadJson(RESPONSE_PATH + "success-get-user-by-email.json");

        // When/Then
        mvc.perform(get("/user")
                        .param("email", email)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(result -> assertResponse(result.getResponse().getContentAsString(), expectedResponse));
    }

    @Test
    @DisplayName("GET /user - Should return Bad Request when try to get user filtered by email, when user does not exist")
    void shouldReturnBadRequestWhenTryToGetUserFilteredByEmailWhenUserDoesNotExist() throws Exception, NotFoundException {

        // Given
        final String email = "alis@email.com";

        doThrow(new NotFoundException("Not found user with e-mail " + email)).when(userGateway).getUserByEmail(email);

        final String expectedResponse = loadJson(RESPONSE_PATH + "failed-get-user-by-email-inexistent-user.json");

        // When/Then
        mvc.perform(get("/user")
                        .param("email", email)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(result -> assertResponse(result.getResponse().getContentAsString(), expectedResponse));
    }

    @Test
    @DisplayName("POST /user - Should create user when receive valid body")
    void shouldCreateUserWhenReceiveValidBody() throws Exception, AlreadyExistentException {

        // Given
        final String requestBody = loadJson(REQUEST_PATH + "valid-request-insert-and-update-user.json");

        // When/Then
        mvc.perform(post("/user")
                        .content(requestBody)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isAccepted());

        Mockito.verify(userGateway, times(1)).insertUser(any(UserUpsertRequest.class));

    }

    @Test
    @DisplayName("POST /user - Should return Bad Request when try to insert an existent user")
    void shouldReturnBadRequestWhenTryToInsertAnExistentUser() throws Exception, AlreadyExistentException {

        // Given
        final String requestBody = loadJson(REQUEST_PATH + "valid-request-insert-and-update-user.json");
        final String expectedResponse = loadJson(RESPONSE_PATH + "failed-insert-already-existent-user.json");

        doThrow(new AlreadyExistentException("User with e-mail rodrigo@email.com already exist."))
                .when(userGateway).insertUser(any(UserUpsertRequest.class));

        // When/Then
        mvc.perform(post("/user")
                        .content(requestBody)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertResponse(result.getResponse().getContentAsString(), expectedResponse));

        Mockito.verify(userGateway, times(1)).insertUser(any(UserUpsertRequest.class));

    }

    @Test
    @DisplayName("PUT /user - Should update user when receive valid body")
    void shouldUpdateUserWhenReceiveValidBody() throws Exception {

        // Given
        final String requestBody = loadJson(REQUEST_PATH + "valid-request-insert-and-update-user.json");

        // When/Then
        mvc.perform(put("/user")
                        .content(requestBody)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isAccepted());

        Mockito.verify(userGateway, times(1)).updateUser(any(UserUpsertRequest.class));
    }

    @Test
    @DisplayName("DELETE /user - Should inactive user when receive valid email")
    void shouldInactiveUserWhenReceiveValidEmail() throws Exception {

        // Given
        final String email = "email@email.com";

        // When/Then
        mvc.perform(delete("/user")
                        .param("email", email)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isAccepted());

        Mockito.verify(userGateway, times(1)).inactiveUser(email);
    }

    private List<User> mockUsersFromGateway() {

        return Collections.singletonList(
                User.builder()
                    .firstName("Alis")
                    .lastName("Landale")
                    .email("alis@email.com")
                    .birthDate(LocalDate.parse("1987-12-20"))
                    .status("active")
                    .build());
    }

    private User mockUserFromGateway() {

        return User.builder()
                .firstName("Alis")
                .lastName("Landale")
                .email("alis@email.com")
                .birthDate(LocalDate.parse("1987-12-20"))
                .status("active")
                .build();
    }
}