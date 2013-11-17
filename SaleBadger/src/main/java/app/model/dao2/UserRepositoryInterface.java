package app.model.dao2;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import app.model.User;

public interface UserRepositoryInterface extends CrudRepository<User,String>{
	
//	public User findByUserName(String username);

}
