package timywimy.web.util;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.stereotype.Component;
import timywimy.web.util.converters.*;

import java.time.*;

@Component
public class RestResponseObjectMapper extends ObjectMapper {
    public RestResponseObjectMapper() {
        super();
        setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //custom serializers
        SimpleModule module = new SimpleModule();

        module.addSerializer(ZonedDateTime.class, new ZonedDateTimeSerializer());
        module.addDeserializer(ZonedDateTime.class, new ZonedDateTimeDeserializer());

        module.addSerializer(LocalDate.class, new LocalDateSerializer());
        module.addDeserializer(LocalDate.class, new LocalDateDeserializer());

        module.addSerializer(LocalTime.class, new LocalTimeSerializer());
        module.addDeserializer(LocalTime.class, new LocalTimeDeserializer());

        module.addSerializer(ZoneId.class, new ZoneIdSerializer());
        module.addDeserializer(ZoneId.class, new ZoneIdDeserializer());

        module.addSerializer(Duration.class, new DurationSerializer());
        module.addDeserializer(Duration.class, new DurationDeserializer());

        registerModule(module);
    }
}
