package app.model.dao;
import org.springframework.data.repository.CrudRepository;

import app.model.User;

public interface UserRepositoryInterface extends CrudRepository<User, String> {

}
