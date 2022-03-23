package poc.mongo.mongoapp.database.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import poc.mongo.mongoapp.database.entities.UserEntity;
import poc.mongo.mongoapp.exceptions.AlreadyExistentException;
import poc.mongo.mongoapp.rest.models.Pagination;

import java.util.List;
import java.util.Objects;

@Repository
public class UserRepository {

    private static final String COLLECTION_NAME = "user_info";

    private final MongoOperations mongoOperations;

    @Autowired
    public UserRepository(final MongoOperations mongoOperations) {

        this.mongoOperations = mongoOperations;
    }

    public List<UserEntity> findUsersByStatus(final String status) {

        final Query query = new Query();
        query.addCriteria(Criteria.where("status").is(status));

        return mongoOperations.find(query, UserEntity.class, COLLECTION_NAME);
    }

    public List<UserEntity> findUsersByStatus(final String status, final Pagination pagination) {

        final Query query = new Query();
        query.addCriteria(Criteria.where("status").is(status));

        query.limit(pagination.getSize());
        query.skip((long) pagination.getPage() * pagination.getSize());

        return mongoOperations.find(query, UserEntity.class, COLLECTION_NAME);
    }

    public UserEntity findUsersByEmail(final String email) {

        final Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));

        return mongoOperations.findOne(query, UserEntity.class, COLLECTION_NAME);
    }

    public void insert(final UserEntity userEntity) throws AlreadyExistentException {

        final UserEntity alreadyExistentUsers = findUsersByEmail(userEntity.getEmail());

        if(Objects.nonNull(alreadyExistentUsers)) {
            throw new AlreadyExistentException("User with e-mail " + userEntity.getEmail() + " already exist.");
        }


        mongoOperations.insert(userEntity, COLLECTION_NAME);
    }

    public void update(final UserEntity userEntity) {

        final Query query = new Query();
        query.addCriteria(Criteria.where("email").is(userEntity.getEmail()));

        final Update update = new Update();
        update.set("firstName", userEntity.getFirstName());
        update.set("lastName", userEntity.getLastName());
        update.set("email", userEntity.getEmail());
        update.set("birthDate", userEntity.getBirthDate());

        mongoOperations.updateFirst(query, update, COLLECTION_NAME);
    }

    public void inactiveUser(final String email) {

        final Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));

        final Update update = new Update();
        update.set("status", "deleted");

        mongoOperations.updateFirst(query, update, COLLECTION_NAME);
    }

}
