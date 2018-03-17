package timywimy.web.util.converters;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import timywimy.util.TimeFormatUtil;

import java.io.IOException;
import java.time.LocalDate;

public class LocalDateDeserializer extends StdDeserializer<LocalDate> {

    public LocalDateDeserializer() {
        this(null);
    }

    public LocalDateDeserializer(Class<LocalDate> t) {
        super(t);
    }

    @Override
    public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return TimeFormatUtil.parseLocalDate(jsonParser.getValueAsString());
    }
}
