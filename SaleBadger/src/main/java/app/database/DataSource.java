package app.database;

import app.model.Product;

public interface DataSource {


	public void connect(String connectionString, int port);

	public void insertProduct(Product product);

	public long countProducts();



}
