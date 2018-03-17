package timywimy.web.util.converters;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.Duration;

public class DurationDeserializer extends StdDeserializer<Duration> {

    public DurationDeserializer() {
        this(null);
    }

    public DurationDeserializer(Class<Duration> t) {
        super(t);
    }

    @Override
    public Duration deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return Duration.ofSeconds(jsonParser.getLongValue());
    }
}
