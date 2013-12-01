package app.saleBadger.model;

import java.util.List;

public class ProductList {
	
	private List<Product> products;
	
	public ProductList() {
		
	}
	
	public ProductList(List<Product> products) {
		this.products = products;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	@Override
	public String toString() {
		return "ProductList [products=" + products + "]";
	}
		
}