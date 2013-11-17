package app.model.dao;

import org.springframework.data.repository.CrudRepository;

import app.model.Product;
import app.model.User;

public interface ProductRepository extends CrudRepository<Product, String>{

	public void doSomeCrap();
}
