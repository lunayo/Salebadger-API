package app.saleBadger;

import java.io.IOException;
import java.net.URI;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.ssl.SSLContextConfigurator;
import org.glassfish.grizzly.ssl.SSLEngineConfigurator;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

/**
 * Main class.
 * 
 */
public class Main {
	// Base URI the Grizzly HTTP server will listen on
	public static final String BASE_URI = "https://localhost:4463/v1/";
	private static final String KEYSTORE_SERVER_FILE = "./keystore_server";
	private static final String KEYSTORE_SERVER_PWD = "ARi=vZg4aPNy3P";
	private static final String TRUSTSTORE_SERVER_FILE = "./truststore_server";
	private static final String TRUSTSTORE_SERVER_PWD = "ARi=vZg4aPNy3P";
	private static final String APP_PACKAGES_NAME = "app.saleBadger";

	/**
	 * Starts Grizzly HTTP server exposing JAX-RS resources defined in this
	 * application.
	 * 
	 * @return Grizzly HTTP server.
	 */
	public static HttpServer startServer() {
		// create a resource config that scans for JAX-RS resources and
		// providers
		// in com.example package
		final ResourceConfig rc = new ResourceConfig()
				.packages(APP_PACKAGES_NAME)
				.register(RolesAllowedDynamicFeature.class)
				.register(JacksonFeature.class);

		// ssl configuration
		SSLContextConfigurator sslContext = new SSLContextConfigurator();

		// setup security context
		sslContext.setKeyStoreFile(KEYSTORE_SERVER_FILE);
		sslContext.setKeyPass(KEYSTORE_SERVER_PWD);
		sslContext.setTrustStoreFile(TRUSTSTORE_SERVER_FILE);
		sslContext.setTrustStorePass(TRUSTSTORE_SERVER_PWD);

		SSLEngineConfigurator sslEngine = new SSLEngineConfigurator(sslContext);

		sslEngine.setClientMode(false);
		sslEngine.setNeedClientAuth(false);

		// create and start a new instance of grizzly http server
		// exposing the Jersey application at BASE_URI
		return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI),
				rc, true, sslEngine);
	}

	/**
	 * Main method.
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		final HttpServer server = startServer();
		System.out.println(String.format(
				"Jersey app started with WADL available at "
						+ "%sapplication.wadl\nHit enter to stop it...",
				BASE_URI));
		System.in.read();
		server.stop();
	}
}
