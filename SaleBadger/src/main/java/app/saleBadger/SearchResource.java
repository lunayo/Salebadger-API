package app.saleBadger;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.security.PermitAll;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.groups.Default;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.geo.Point;

import app.saleBadger.model.Product;
import app.saleBadger.model.ProductList;
import app.saleBadger.model.dao.ProductRepository;
import app.saleBadger.model.dao.config.SpringMongoConfig;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@PermitAll
public class SearchResource {
	
	private final ApplicationContext context = new AnnotationConfigApplicationContext(
			SpringMongoConfig.class);
	private final ProductRepository productRepository = context
			.getBean(ProductRepository.class);

	// product search
	// Query params ?near=longitude;latitude
	@GET
	@Path("products")
	public ProductList getProducts(
			@QueryParam("q") String keyword,
			@QueryParam("near") String location) {
		List<Product> products = new ArrayList<Product>();
		
		if (location == null || location.length() == 0) {
			products = productRepository.findAll();
		} else {
			// parse the locations variable
			List<Double> locations = Product.getLocation(location);
			// validate the value programmatically
			Set<ConstraintViolation<Product>> constraints = Validation
					.buildDefaultValidatorFactory()
					.getValidator()
					.validateValue(Product.class, "location", locations,
							Default.class);
			if (locations == null || locations.size() != 2
					|| constraints.size() > 0) {
				throw new ConstraintViolationException(constraints);
			}

			products = productRepository.findNearby(new Point(locations.get(0),
					locations.get(1)), 0, 10);
		}

		return new ProductList(products);
	}
	
}
