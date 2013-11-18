package app.model.dao;
import org.springframework.data.repository.CrudRepository;

import app.model.User;

public interface UserRepository extends CrudRepository<User, String>, UserRepositoryCustom {

}
