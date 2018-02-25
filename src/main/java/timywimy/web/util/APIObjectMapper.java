package timywimy.web.util;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class APIObjectMapper extends ObjectMapper {
    public APIObjectMapper() {
        super();
        setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }
}
