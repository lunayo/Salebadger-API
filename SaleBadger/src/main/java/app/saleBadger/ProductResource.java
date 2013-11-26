package app.saleBadger;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import app.model.Product;
import app.model.Role;
import app.model.dao.ProductRepository;
import app.model.dao.config.SpringMongoConfig;
import app.saleBadger.validator.ErrorMessagesMapper;
import app.saleBadger.webexception.BadRequestException;
import app.saleBadger.webexception.ConflictException;

@Path("products/")
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed({ Role.ADMIN, Role.USER })
public class ProductResource {

	private final ApplicationContext context = new AnnotationConfigApplicationContext(
			SpringMongoConfig.class);
	private final ProductRepository productRepository = context
			.getBean(ProductRepository.class);

	// The Java method will process HTTP GET requests
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Product addProduct(@Valid Product product, @Context UriInfo uriInfo) {

		List<String> errors = new ArrayList<String>();

		if (productRepository.exists(product.getId().toString())) {
			// product exists in the repository
			// throw conflict
			errors.add(ErrorMessagesMapper.getString("product.conflict.exist"));
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
}
