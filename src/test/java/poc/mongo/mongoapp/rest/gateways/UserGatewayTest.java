package poc.mongo.mongoapp.rest.gateways;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import poc.mongo.mongoapp.database.entities.UserEntity;
import poc.mongo.mongoapp.database.gateways.UserGateway;
import poc.mongo.mongoapp.database.repository.UserRepository;
import poc.mongo.mongoapp.rest.models.UserDTO;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class UserGatewayTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserGateway userGateway;

    @Test
    @DisplayName("")
    void shouldReturnListOfUsersFromRepository() {

        // Given
        final String status = "status";

        doReturn(mockUsersFromRepository()).when(userRepository).findUsersByStatus(status);

        // When
        final List<UserDTO> result = userGateway.getUsers(status);

        // Then
        Mockito.verify(userRepository, times(1)).findUsersByStatus(status);

        assertThat(result.size(), is(1));
        assertThat(result.get(0).getFirstName(), is("Alis"));
        assertThat(result.get(0).getLastName(), is("Landale"));
        assertThat(result.get(0).getBirthDate(), is(LocalDate.parse("1987-12-20")));
        assertThat(result.get(0).getStatus(), is("Active"));

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