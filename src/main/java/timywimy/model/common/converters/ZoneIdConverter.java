package timywimy.model.common.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Locale;

@Converter
public class ZoneIdConverter implements AttributeConverter<ZoneId, String> {

    @Override
    public String convertToDatabaseColumn(ZoneId zoneId) {
        return zoneId == null ? null : zoneId.getDisplayName(TextStyle.NARROW, Locale.ENGLISH);
    }

    @Override
    public ZoneId convertToEntityAttribute(String displayName) {
        return displayName == null ? null : ZoneId.of(displayName);
    }
}
