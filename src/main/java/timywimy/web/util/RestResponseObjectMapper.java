package timywimy.web.util;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class RestResponseObjectMapper extends ObjectMapper {
    public RestResponseObjectMapper() {
        super();
        setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }
}
