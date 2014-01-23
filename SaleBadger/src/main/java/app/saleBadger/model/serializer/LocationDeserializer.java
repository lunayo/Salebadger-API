package app.saleBadger.model.serializer;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;
import org.springframework.data.mongodb.core.geo.Point;

public class LocationDeserializer extends JsonDeserializer<Point> {

	@Override
	public Point deserialize(JsonParser jsonParser,
			DeserializationContext deserializationContext) throws IOException,
			JsonProcessingException {
		// TODO Auto-generated method stub
		ObjectMapper mapper = new ObjectMapper();
		List<Double> locations = mapper.readValue(
				jsonParser.getText(),
				TypeFactory.defaultInstance().constructCollectionType(
						List.class, Double.class));
		return new Point(locations.get(0), locations.get(1));
	}

}
