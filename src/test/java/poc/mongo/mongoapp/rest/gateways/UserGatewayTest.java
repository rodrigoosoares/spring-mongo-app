package poc.mongo.mongoapp.rest.gateways;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import poc.mongo.mongoapp.controllers.requests.UserUpsertRequest;
import poc.mongo.mongoapp.database.entities.UserEntity;
import poc.mongo.mongoapp.database.gateways.UserGateway;
import poc.mongo.mongoapp.database.repository.UserRepository;
import poc.mongo.mongoapp.exceptions.AlreadyExistentException;
import poc.mongo.mongoapp.rest.models.Pagination;
import poc.mongo.mongoapp.rest.models.User;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class UserGatewayTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserGateway userGateway;

    @Test
    @DisplayName("Should return a list of users from repository")
    void shouldReturnListOfUsersFromRepository() {

        // Given
        final String status = "status";

        doReturn(mockUsersFromRepository()).when(userRepository).findUsersByStatus(status);

        // When
        final List<User> result = userGateway.getUsersByStatus(status);

        // Then
        Mockito.verify(userRepository, times(1)).findUsersByStatus(status);

        assertThat(result.size(), is(1));
        assertThat(result.get(0).getFirstName(), is("Alis"));
        assertThat(result.get(0).getLastName(), is("Landale"));
        assertThat(result.get(0).getBirthDate(), is(LocalDate.parse("1987-12-20")));
        assertThat(result.get(0).getStatus(), is("Active"));

    }

    @Test
    @DisplayName("Should return a list of users from repository with pagination")
    void shouldReturnListOfUsersFromRepositoryWithPagination() {

        // Given
        final String status = "status";
        final Pagination pagination = new Pagination(1, 1);

        doReturn(mockUsersFromRepository()).when(userRepository).findUsersByStatus(status, pagination);

        // When
        final List<User> result = userGateway.getUsersByStatusPageable(status, pagination);

        // Then
        Mockito.verify(userRepository, times(1)).findUsersByStatus(status, pagination);

        assertThat(result.size(), is(1));
        assertThat(result.get(0).getFirstName(), is("Alis"));
        assertThat(result.get(0).getLastName(), is("Landale"));
        assertThat(result.get(0).getBirthDate(), is(LocalDate.parse("1987-12-20")));
        assertThat(result.get(0).getStatus(), is("Active"));

    }

    @Test
    @DisplayName("Should call repository insert method when receive valid UserUpsertRequest")
    void shouldCallRepositoryUpsertMethodWhenReceiveValidUserUpsertRequest() throws AlreadyExistentException {

        // Given
        final UserUpsertRequest userUpsertRequest = mockUserUpsertRequest();

        // When
        userGateway.insertUser(userUpsertRequest);

        // Then
        Mockito.verify(userRepository, times(1)).insert(any(UserEntity.class));
    }

    @Test
    @DisplayName("Should call repository update method when receive valid UserUpsertRequest")
    void shouldCallRepositoryUpdateMethodWhenReceiveValidUserUpsertRequest() {

        // Given
        final UserUpsertRequest userUpsertRequest = mockUserUpsertRequest();

        // When
        userGateway.updateUser(userUpsertRequest);

        // Then
        Mockito.verify(userRepository, times(1)).update(any(UserEntity.class));
    }

    @Test
    @DisplayName("Should call repository inactiveUser method when receive valid email")
    void shouldCallRepositoryInactiveUserMethodWhenReceiveValidUserUpsertRequest() {

        // Given
        final String email = "email@email.com";

        // When
        userGateway.inactiveUser(email);

        // Then
        Mockito.verify(userRepository, times(1)).inactiveUser(email);
    }

    private UserUpsertRequest mockUserUpsertRequest() {
        return UserUpsertRequest.builder()
                .firstName("Alis")
                .lastName("Landale")
                .email("alis@email.com")
                .birthDate(LocalDate.parse("1987-12-20"))
                .build();
    }

    private List<UserEntity> mockUsersFromRepository() {

        return Collections.singletonList(
                UserEntity.builder()
                        .firstName("Alis")
                        .lastName("Landale")
                        .email("alis@email.com")
                        .birthDate(LocalDate.parse("1987-12-20"))
                        .status("Active")
                        .build());

    }

}