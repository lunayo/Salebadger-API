package app.saleBadger;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.security.RolesAllowed;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.groups.Default;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.bson.types.ObjectId;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.geo.Point;

import app.saleBadger.model.Product;
import app.saleBadger.model.ProductList;
import app.saleBadger.model.Role;
import app.saleBadger.model.dao.ProductRepository;
import app.saleBadger.model.dao.UserRepository;
import app.saleBadger.model.dao.config.SpringMongoConfig;
import app.saleBadger.validator.ErrorMessagesMapper;
import app.saleBadger.webexception.BadRequestException;
import app.saleBadger.webexception.ConflictException;
import app.saleBadger.webexception.NotFoundException;

@Path("users/{username}/products/")
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed({ Role.ADMIN, Role.USER })
public class ProductResource {

	private final ApplicationContext context = new AnnotationConfigApplicationContext(
			SpringMongoConfig.class);
	private final ProductRepository productRepository = context
			.getBean(ProductRepository.class);
	private final UserRepository userRepository = context
			.getBean(UserRepository.class);
	@Size(min = 5, max = 20, message = "{user.wrong.username}")
	@PathParam("username")
	private String username;

	// The Java method will process HTTP GET requests
	@GET
	@Path("/{id}")
	public Product getProduct(@NotNull @PathParam("id") ObjectId id) {
		List<String> errors = new ArrayList<String>();

		if (!userRepository.exists(username)) {
			errors.add(ErrorMessagesMapper.getString("user.does.not.exist"));
			throw new NotFoundException(errors);
		}

		Product result = productRepository.findOne(id.toString());

		if (result == null) {
			errors.add(ErrorMessagesMapper.getString("product.does.not.exist"));
			throw new NotFoundException(errors);
		}

		return result;
	}

	// Query params ?near=longitude;latitude
	@GET
	public ProductList getProducts(@QueryParam("near") String location) {
		List<Product> products = new ArrayList<Product>();
		if (location == null || location.length() == 0) {
			products = productRepository.findAll();
		} else {
			// parse the locations variable
			List<Double> locations = Product.getLocation(location);
			// validate the value
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

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Product addProduct(@Valid Product product, @Context UriInfo uriInfo) {
		List<String> errors = new ArrayList<String>();

		product.setOwnerId(username);

		if (!userRepository.exists(username)) {
			errors.add(ErrorMessagesMapper.getString("user.does.not.exist"));
			throw new NotFoundException(errors);
		}

		if (productRepository.exists(product.getId().toString())) {
			// product exists in the repository
			// throw conflict
			errors.add(ErrorMessagesMapper.getString("product.does.exist"));
			throw new ConflictException(errors, uriInfo.getBaseUriBuilder()
					.path("/products/{product}").build(product.getId()));
		} else {
			Product result = productRepository.save(product);

			if (result != null) {
				return result;
			} else {
				// throw bad request
				errors.add(ErrorMessagesMapper.getString("app.unknown.error"));
				throw new BadRequestException(errors);
			}
		}

	}

	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Product updateProduct(@NotNull @PathParam("id") ObjectId id,
			@Valid Product product) {
		List<String> errors = new ArrayList<String>();

		product.setOwnerId(username);
		product.setId(id);

		if (!userRepository.exists(username)) {
			errors.add(ErrorMessagesMapper.getString("user.does.not.exist"));
			throw new NotFoundException(errors);
		}

		if (productRepository.exists(id.toString())) {
			Product result = productRepository.save(product);
			if (result != null) {
				return result;
			} else {
				// throw bad request
				errors.add(ErrorMessagesMapper.getString("app.unknown.error"));
				throw new BadRequestException(errors);
			}
		} else {
			errors.add(ErrorMessagesMapper.getString("product.does.not.exist"));
			throw new NotFoundException(errors);
		}
	}

	@DELETE
	@Path("/{id}")
	public Response deleteProduct(@NotNull @PathParam("id") ObjectId id) {
		List<String> errors = new ArrayList<String>();

		if (!userRepository.exists(username)) {
			errors.add(ErrorMessagesMapper.getString("user.does.not.exist"));
			throw new NotFoundException(errors);
		}

		if (!productRepository.exists(id.toString())) {
			errors.add(ErrorMessagesMapper.getString("product.does.not.exist"));
			throw new NotFoundException(errors);
		} else {
			productRepository.delete(id.toString());
			return Response.status(Response.Status.NO_CONTENT).build();
		}
	}
}
