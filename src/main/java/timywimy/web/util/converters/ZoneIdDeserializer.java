package timywimy.web.util.converters;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import timywimy.util.TimeFormatUtil;

import java.io.IOException;
import java.time.ZoneId;

public class ZoneIdDeserializer extends StdDeserializer<ZoneId> {

    public ZoneIdDeserializer() {
        this(null);
    }

    public ZoneIdDeserializer(Class<ZoneId> t) {
        super(t);
    }

    @Override
    public ZoneId deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return TimeFormatUtil.parseZoneId(jsonParser.getValueAsString());
    }
}
