package timywimy.web.util;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.stereotype.Component;
import timywimy.web.dto.common.DateTimeZone;
import timywimy.web.util.converters.DateTimeZoneSerializer;
import timywimy.web.util.converters.ZonedDateTimeSerializer;

import java.time.ZonedDateTime;

@Component
public class RestResponseObjectMapper extends ObjectMapper {
    public RestResponseObjectMapper() {
        super();
        setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //custom serializers
        SimpleModule module = new SimpleModule();
        module.addSerializer(ZonedDateTime.class, new ZonedDateTimeSerializer());
        module.addSerializer(DateTimeZone.class, new DateTimeZoneSerializer());
        registerModule(module);
    }
}
