package poc.mongo.mongoapp.database.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import poc.mongo.mongoapp.database.entities.UserEntity;

import java.util.List;

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

    public void upsert(final UserEntity userEntity) {

        final Query query = new Query();
        query.addCriteria(Criteria.where("email").is(userEntity.getEmail()));

        final Update update = new Update();
        update.set("firstName", userEntity.getFirstName());
        update.set("lastName", userEntity.getLastName());
        update.set("email", userEntity.getEmail());
        update.set("birthDate", userEntity.getBirthDate());
        update.set("status", userEntity.getStatus());

        mongoOperations.upsert(query, update, COLLECTION_NAME);
    }

}
