package timywimy.web.util.converters;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import timywimy.util.TimeFormatUtil;
import timywimy.web.dto.common.DateTimeZone;

import java.io.IOException;

public class DateTimeZoneSerializer extends StdSerializer<DateTimeZone> {

    public DateTimeZoneSerializer() {
        this(null);
    }

    public DateTimeZoneSerializer(Class<DateTimeZone> t) {
        super(t);
    }

    @Override
    public void serialize(DateTimeZone value, JsonGenerator jgen,
                          SerializerProvider provider)
            throws IOException {
        jgen.writeStartObject();
        jgen.writeStringField("date", TimeFormatUtil.toString(value.getDate()));
        jgen.writeStringField("time", TimeFormatUtil.toString(value.getTime()));
        jgen.writeStringField("zone", TimeFormatUtil.toString(value.getZone()));
        jgen.writeEndObject();
    }
}
