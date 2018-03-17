package timywimy.web.util.converters;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import timywimy.util.TimeFormatUtil;

import java.io.IOException;
import java.time.ZoneId;

public class ZoneIdSerializer extends StdSerializer<ZoneId> {

    public ZoneIdSerializer() {
        this(null);
    }

    public ZoneIdSerializer(Class<ZoneId> t) {
        super(t);
    }

    @Override
    public void serialize(ZoneId value, JsonGenerator jgen,
                          SerializerProvider provider) throws IOException {
        jgen.writeString(TimeFormatUtil.toString(value));
    }
}