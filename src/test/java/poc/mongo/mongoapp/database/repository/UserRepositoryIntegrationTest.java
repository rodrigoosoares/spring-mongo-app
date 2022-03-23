package poc.mongo.mongoapp.database.repository;

import org.hamcrest.core.Is;
import org.junit.ClassRule;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import poc.mongo.mongoapp.database.entities.UserEntity;
import poc.mongo.mongoapp.exceptions.AlreadyExistentException;
import poc.mongo.mongoapp.rest.models.Pagination;

import java.io.File;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("integration-test")
class UserRepositoryIntegrationTest {

    private final String COLLECTION_NAME = "user_info";

    @ClassRule
    private static final DockerComposeContainer dockerContainer = new DockerComposeContainer(
      new File(Objects.requireNonNull(UserRepositoryIntegrationTest.class.getResource("/environment/docker-compose.yaml")).getPath()))
            .withExposedService("mongo-integration-test", 27017,
                    Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(30)));

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MongoOperations mongoOperations;

    @BeforeAll
    private static void beforeAll() {
        dockerContainer.start();
    }

    @AfterEach
    private void afterEach() {
        mongoOperations.dropCollection(COLLECTION_NAME);
    }

    @Test
    @DisplayName("Should find data given 'active' status")
    void shouldFindDataGivenActiveStatus() {

        //Given
        mongoOperations.insert(mockData(), COLLECTION_NAME);
        final String status = "active";

        // When
        final List<UserEntity> result = userRepository.findUsersByStatus(status);

        // Then
        assertThat(result.size(), Is.is(2));
        
        final UserEntity firstEntity = result.get(0);
        assertThat(firstEntity.getFirstName(), Is.is("user1"));
        assertThat(firstEntity.getLastName(), Is.is("01"));
        assertThat(firstEntity.getEmail(), Is.is("user-1@email.com"));
        assertThat(firstEntity.getBirthDate(), Is.is(LocalDate.parse("2022-02-22")));
        assertThat(firstEntity.getStatus(), Is.is("active"));

        final UserEntity secondEntity = result.get(1);
        assertThat(secondEntity.getFirstName(), Is.is("user2"));
        assertThat(secondEntity.getLastName(), Is.is("02"));
        assertThat(secondEntity.getEmail(), Is.is("user-2@email.com"));
        assertThat(secondEntity.getBirthDate(), Is.is(LocalDate.parse("2022-02-23")));
        assertThat(secondEntity.getStatus(), Is.is("active"));
    }

    @Test
    @DisplayName("Should find data given 'deleted' status")
    void shouldFindDataGivenDeletedStatus() {

        //Given
        mongoOperations.insert(mockData(), COLLECTION_NAME);
        final String status = "deleted";

        // When
        final List<UserEntity> result = userRepository.findUsersByStatus(status);

        // Then
        assertThat(result.size(), Is.is(2));

        final UserEntity firstEntity = result.get(0);
        assertThat(firstEntity.getFirstName(), Is.is("user3"));
        assertThat(firstEntity.getLastName(), Is.is("03"));
        assertThat(firstEntity.getEmail(), Is.is("user-3@email.com"));
        assertThat(firstEntity.getBirthDate(), Is.is(LocalDate.parse("2022-02-24")));
        assertThat(firstEntity.getStatus(), Is.is("deleted"));

        final UserEntity secondEntity = result.get(1);
        assertThat(secondEntity.getFirstName(), Is.is("user4"));
        assertThat(secondEntity.getLastName(), Is.is("04"));
        assertThat(secondEntity.getEmail(), Is.is("user-4@email.com"));
        assertThat(secondEntity.getBirthDate(), Is.is(LocalDate.parse("2022-02-25")));
        assertThat(secondEntity.getStatus(), Is.is("deleted"));
    }

    @Test
    @DisplayName("Should find data given valid status and pagination info")
    void shouldFindDataGivenValidStatusAndPaginationInfo() {

        //Given
        mongoOperations.insert(mockData(), COLLECTION_NAME);
        final String status = "deleted";
        final Pagination pagination = new Pagination();
        pagination.setPage(1);
        pagination.setSize(2);

        // When
        final List<UserEntity> result = userRepository.findUsersByStatus(status, pagination);

        // Then
        assertThat(result.size(), Is.is(2));

        final UserEntity firstEntity = result.get(0);
        assertThat(firstEntity.getFirstName(), Is.is("user7"));
        assertThat(firstEntity.getLastName(), Is.is("07"));
        assertThat(firstEntity.getEmail(), Is.is("user-7@email.com"));
        assertThat(firstEntity.getBirthDate(), Is.is(LocalDate.parse("2022-02-26")));
        assertThat(firstEntity.getStatus(), Is.is("deleted"));

        final UserEntity secondEntity = result.get(1);
        assertThat(secondEntity.getFirstName(), Is.is("user8"));
        assertThat(secondEntity.getLastName(), Is.is("08"));
        assertThat(secondEntity.getEmail(), Is.is("user-8@email.com"));
        assertThat(secondEntity.getBirthDate(), Is.is(LocalDate.parse("2022-02-27")));
        assertThat(secondEntity.getStatus(), Is.is("deleted"));
    }

    @Test
    @DisplayName("Should find data given a valid email")
    void shouldFindDataGivenAValidEmail() {

        //Given
        mongoOperations.insert(mockData(), COLLECTION_NAME);
        final String email = "user-3@email.com";

        // When
        final UserEntity result = userRepository.findUsersByEmail(email);

        // Then
        assertThat(result.getFirstName(), Is.is("user3"));
        assertThat(result.getLastName(), Is.is("03"));
        assertThat(result.getEmail(), Is.is("user-3@email.com"));
        assertThat(result.getBirthDate(), Is.is(LocalDate.parse("2022-02-24")));
        assertThat(result.getStatus(), Is.is("deleted"));
    }

    @Test
    @DisplayName("Should insert given new valid UserEntity")
    void shouldInsertGivenNewValidUserEntity() throws AlreadyExistentException {

        //Given
        mongoOperations.insert(mockData(), COLLECTION_NAME);

        final UserEntity userEntityToInsert = new UserEntity();
        userEntityToInsert.setFirstName("user5");
        userEntityToInsert.setLastName("05");
        userEntityToInsert.setEmail("user-5@email.com");
        userEntityToInsert.setBirthDate(LocalDate.parse("2022-01-22"));
        userEntityToInsert.setStatus("active");

        // When
        userRepository.insert(userEntityToInsert);

        // Then
        final List<UserEntity> updatedDocumentFind = mongoOperations.find(
                new Query().addCriteria(Criteria.where("firstName").is(userEntityToInsert.getFirstName())),
                UserEntity.class,
                COLLECTION_NAME
        );

        assertThat(updatedDocumentFind.size(), Is.is(1));

        final UserEntity updatedDocument = updatedDocumentFind.get(0);
        assertThat(updatedDocument.getFirstName(), Is.is("user5"));
        assertThat(updatedDocument.getLastName(), Is.is("05"));
        assertThat(updatedDocument.getEmail(), Is.is("user-5@email.com"));
        assertThat(updatedDocument.getBirthDate(), Is.is(LocalDate.parse("2022-01-22")));
        assertThat(updatedDocument.getStatus(), Is.is("active"));
    }

    @Test
    @DisplayName("Should thrown AlreadyExistentException when trying to insert a existent user")
    void shouldThrownAlreadyExistentExceptionWhenTryingToInsertAExistentUser() {

        //Given
        mongoOperations.insert(mockData(), COLLECTION_NAME);

        final UserEntity userEntityToInsert = new UserEntity();
        userEntityToInsert.setFirstName("user6");
        userEntityToInsert.setLastName("06");
        userEntityToInsert.setEmail("user-1@email.com");
        userEntityToInsert.setBirthDate(LocalDate.parse("2022-01-12"));

        // When/Then
        assertThrows(AlreadyExistentException.class, () -> userRepository.insert(userEntityToInsert));
    }

    @Test
    @DisplayName("Should update given new valid UserEntity")
    void shouldUpdateGivenNewValidUserEntity() {

        //Given
        mongoOperations.insert(mockData(), COLLECTION_NAME);

        final UserEntity userEntityToUpdate = new UserEntity();
        userEntityToUpdate.setFirstName("user1-updated");
        userEntityToUpdate.setLastName("01-updated");
        userEntityToUpdate.setEmail("user-1@email.com");
        userEntityToUpdate.setBirthDate(LocalDate.parse("2022-01-22"));

        // When
        userRepository.update(userEntityToUpdate);

        // Then
        final List<UserEntity> updatedDocumentFind = mongoOperations.find(
                new Query().addCriteria(Criteria.where("firstName").is(userEntityToUpdate.getFirstName())),
                UserEntity.class,
                COLLECTION_NAME
        );

        assertThat(updatedDocumentFind.size(), Is.is(1));

        final UserEntity updatedDocument = updatedDocumentFind.get(0);
        assertThat(updatedDocument.getFirstName(), Is.is("user1-updated"));
        assertThat(updatedDocument.getLastName(), Is.is("01-updated"));
        assertThat(updatedDocument.getEmail(), Is.is("user-1@email.com"));
        assertThat(updatedDocument.getBirthDate(), Is.is(LocalDate.parse("2022-01-22")));
        assertThat(updatedDocument.getStatus(), Is.is("active"));
    }

    @Test
    @DisplayName("Should inactive user given valid email")
    void shouldInactiveUserGivenValidEmail() {

        //Given
        mongoOperations.insert(mockData(), COLLECTION_NAME);

        final String email = "user-1@email.com";

        // When
        userRepository.inactiveUser(email);

        // Then
        final List<UserEntity> updatedDocumentFind = mongoOperations.find(
                new Query().addCriteria(Criteria.where("email").is(email)),
                UserEntity.class,
                COLLECTION_NAME
        );

        assertThat(updatedDocumentFind.size(), Is.is(1));

        final UserEntity updatedDocument = updatedDocumentFind.get(0);
        assertThat(updatedDocument.getFirstName(), Is.is("user1"));
        assertThat(updatedDocument.getLastName(), Is.is("01"));
        assertThat(updatedDocument.getEmail(), Is.is("user-1@email.com"));
        assertThat(updatedDocument.getBirthDate(), Is.is(LocalDate.parse("2022-02-22")));
        assertThat(updatedDocument.getStatus(), Is.is("deleted"));
    }

    private List<UserEntity> mockData() {

        return List.of(
                new UserEntity("user1", "01", "user-1@email.com", LocalDate.parse("2022-02-22"), "active"),
                new UserEntity("user2", "02", "user-2@email.com", LocalDate.parse("2022-02-23"), "active"),
                new UserEntity("user3", "03", "user-3@email.com", LocalDate.parse("2022-02-24"), "deleted"),
                new UserEntity("user4", "04", "user-4@email.com", LocalDate.parse("2022-02-25"), "deleted"),
                new UserEntity("user7", "07", "user-7@email.com", LocalDate.parse("2022-02-26"), "deleted"),
                new UserEntity("user8", "08", "user-8@email.com", LocalDate.parse("2022-02-27"), "deleted"),
                new UserEntity("user9", "09", "user-9@email.com", LocalDate.parse("2022-02-28"), "deleted")
        );


    }

}