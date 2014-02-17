package app.saleBadger;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.security.KeyStore;
import java.util.Properties;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

import org.glassfish.grizzly.http.server.HttpServer;
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
	private static final String KEYSTORE_SERVER_FILE = "/server/keystore_server";
	private static final String KEYSTORE_SERVER_PWD = "ARi=vZg4aPNy3P";
	private static final String TRUSTSTORE_SERVER_FILE = "src/main/resources/server/truststore_server";
	private static final String TRUSTSTORE_SERVER_PWD = "ARi=vZg4aPNy3P";
	private static final String APP_PACKAGES_NAME = "app.saleBadger";
	public static final String KEYSTORE_CLIENT_FILE = "/server/keystore_client";
	public static final String KEYSTORE_CLIENT_PWD = "ARi=vZg4aPNy3P";
	public static final String TRUSTSTORE_CLIENT_FILE = "src/main/resources/server/truststore_client";
	public static final String TRUSTSTORE_CLIENT_PWD = "ARi=vZg4aPNy3P";

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

		SSLContext sslContext = createSSLContext(true);
		SSLEngineConfigurator sslEngine = new SSLEngineConfigurator(sslContext);

		sslEngine.setClientMode(false);
		sslEngine.setNeedClientAuth(false);

		// create and start a new instance of grizzly http server
		// exposing the Jersey application at BASE_URI
		return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI),
				rc, true, sslEngine);
	}

	public static SSLContext createSSLContext(boolean isServer) {

		try {
			String keystoreFile = KEYSTORE_SERVER_FILE;
			String keystorePassword = KEYSTORE_SERVER_PWD;
			String trustStoreFile = TRUSTSTORE_SERVER_FILE;
			String trustStorePassword = TRUSTSTORE_SERVER_PWD;

			if (!isServer) {
				keystoreFile = KEYSTORE_CLIENT_FILE;
				keystorePassword = KEYSTORE_CLIENT_PWD;
				trustStoreFile = TRUSTSTORE_CLIENT_FILE;
				trustStorePassword = TRUSTSTORE_CLIENT_PWD;
			}

			// Set up the trust store
			Properties systemProps = System.getProperties();
			systemProps.put("javax.net.ssl.trustStore", trustStoreFile);
			systemProps.put("javax.net.ssl.trustStorePassword",
					trustStorePassword);
			System.setProperties(systemProps);

			// Load the key store.
			KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
			InputStream keyStream = Main.class.getClass().getResourceAsStream(
					keystoreFile);
			keyStore.load(keyStream, keystorePassword.toCharArray());
			keyStream.close();

			// Create the factory from the keystore.
			String kmfAlgorithm = System.getProperty(
					"ssl.KeyManagerFactory.algorithm",
					KeyManagerFactory.getDefaultAlgorithm());
			KeyManagerFactory keyManagerFactory = KeyManagerFactory
					.getInstance(kmfAlgorithm);
			keyManagerFactory.init(keyStore, keystorePassword.toCharArray());

			// Create the SSLContext
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(keyManagerFactory.getKeyManagers(), null, null);
			return sslContext;
		}

		// Wrap all Exceptions in a RuntimeException.
		catch (Exception e) {
			throw new RuntimeException(e);
		}
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
		server.shutdown();
	}
}
