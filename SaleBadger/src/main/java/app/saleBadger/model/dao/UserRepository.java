package app.saleBadger.model.dao;
import org.springframework.data.repository.CrudRepository;

import app.saleBadger.model.User;

public interface UserRepository extends CrudRepository<User, String>, UserRepositoryCustom {

}
