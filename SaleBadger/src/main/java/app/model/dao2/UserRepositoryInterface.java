package app.model.dao2;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import app.model.User;

@Repository
public interface UserRepositoryInterface extends MongoRepository<User,String>{
	
	public User findByUserName(String username);

}
