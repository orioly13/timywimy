package timywimy.web.util.converters;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import timywimy.util.TimeFormatUtil;

import java.io.IOException;
import java.time.LocalTime;

public class LocalTimeDeserializer extends StdDeserializer<LocalTime> {

    public LocalTimeDeserializer() {
        this(null);
    }

    public LocalTimeDeserializer(Class<LocalTime> t) {
        super(t);
    }

    @Override
    public LocalTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return TimeFormatUtil.parseLocalTime(jsonParser.getValueAsString());
    }
}
