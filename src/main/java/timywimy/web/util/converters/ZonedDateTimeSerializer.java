package timywimy.web.util.converters;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import timywimy.util.TimeFormatUtil;

import java.io.IOException;
import java.time.ZonedDateTime;

public class ZonedDateTimeSerializer extends StdSerializer<ZonedDateTime> {

    public ZonedDateTimeSerializer() {
        this(null);
    }

    public ZonedDateTimeSerializer(Class<ZonedDateTime> t) {
        super(t);
    }

    @Override
    public void serialize(ZonedDateTime value, JsonGenerator jgen,
                          SerializerProvider provider)
            throws IOException {
        jgen.writeString(TimeFormatUtil.toString(value));
    }
}
