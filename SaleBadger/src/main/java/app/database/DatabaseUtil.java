package app.database;

import app.model.Product;

public interface DatabaseUtil {


	public void connect(String connectionString, int port);

	public void insertProduct(Product product);

	public long countProducts();



}
