package poc.mongo.mongoapp.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import poc.mongo.mongoapp.database.gateways.UserGateway;
import poc.mongo.mongoapp.rest.models.UserDTO;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @ParameterizedTest
    @ValueSource(strings = {"active", "deleted"})
    @DisplayName("Should return all users filtered by status, when receive a valid status parameter")
    void shouldReturnAllUsersFilteredByStatusWhenReceiveAValidStatusParameter(final String statusArg) throws Exception {
        // Given

        doReturn(mockUsersFromGateway()).when(userGateway).getUsers(statusArg);

        final String expectedResponse = loadJson(RESPONSE_PATH + "success-get-users.json");

        // When/Then
        mvc.perform(get("/users")
                .param("status", statusArg)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(result -> assertResponse(result.getResponse().getContentAsString(), expectedResponse));
    }

    @Test
    @DisplayName("Should return all users filtered by status, when receive an invalid status parameter")
    void shouldReturnAllUsersFilteredByStatusWhenReceiveAnInValidStatusParameter() throws Exception {
        // Given

        final String invalidStatus = "INVALID";

        final String expectedResponse = loadJson(RESPONSE_PATH + "failed-get-users-invalid-status.json");

        // When/Then
        mvc.perform(get("/users")
                        .param("status", invalidStatus)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(result -> assertResponse(result.getResponse().getContentAsString(), expectedResponse));
    }

    private List<UserDTO> mockUsersFromGateway() {

        return Collections.singletonList(
                UserDTO.builder()
                        .firstName("Alis")
                        .lastName("Landale")
                        .email("alis@email.com")
                        .birthDate(LocalDate.parse("1987-12-20"))
                        .status("active")
                        .build());
    }
}