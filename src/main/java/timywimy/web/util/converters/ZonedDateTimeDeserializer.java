package timywimy.web.util.converters;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import timywimy.util.TimeFormatUtil;

import java.io.IOException;
import java.time.ZonedDateTime;

public class ZonedDateTimeDeserializer extends StdDeserializer<ZonedDateTime> {

    public ZonedDateTimeDeserializer() {
        this(null);
    }

    public ZonedDateTimeDeserializer(Class<ZonedDateTime> t) {
        super(t);
    }

    @Override
    public ZonedDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return TimeFormatUtil.parseZonedDateTime(jsonParser.getValueAsString());
    }
}
