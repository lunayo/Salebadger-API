package app.saleBadger.model.serializer;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.springframework.data.mongodb.core.geo.Point;

public class LocationSerializer extends JsonSerializer<Point> {

	@Override
	public void serialize(Point location, JsonGenerator jsonGenerator,
			SerializerProvider serializerProvider) throws IOException,
			JsonProcessingException {
		// TODO Auto-generated method stub
		if (location == null) {
			jsonGenerator.writeNull();
		} else {
			jsonGenerator.writeString("["+ location.getX() + ", " + location.getY() + "]");
		}
	}

}
