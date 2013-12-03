package app.saleBadger.model.dao;
import org.springframework.data.mongodb.repository.MongoRepository;

import app.saleBadger.model.User;

public interface UserRepository extends MongoRepository<User, String>, UserRepositoryCustom {

}
