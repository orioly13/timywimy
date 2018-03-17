package timywimy.web.util.converters;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import timywimy.util.TimeFormatUtil;

import java.io.IOException;
import java.time.LocalTime;

public class LocalTimeSerializer extends StdSerializer<LocalTime> {

    public LocalTimeSerializer() {
        this(null);
    }

    public LocalTimeSerializer(Class<LocalTime> t) {
        super(t);
    }

    @Override
    public void serialize(LocalTime value, JsonGenerator jgen,
                          SerializerProvider provider) throws IOException {
        jgen.writeString(TimeFormatUtil.toString(value));
    }
}