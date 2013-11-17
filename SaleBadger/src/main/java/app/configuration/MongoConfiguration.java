package app.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.Mongo;

@Configuration
@EnableMongoRepositories(basePackages = "app.model.dao2")
public class MongoConfiguration extends AbstractMongoConfiguration{
	
	@SuppressWarnings("deprecation")
	@Override
	@Bean
	public Mongo mongo() throws Exception {
		return new Mongo();
	}

	@Override
	protected String getDatabaseName() {
		return "saleBadger";
	}
}
