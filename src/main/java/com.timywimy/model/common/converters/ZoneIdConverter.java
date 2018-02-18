package com.timywimy.model.common.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.ZoneId;

@Converter
public class ZoneIdConverter implements AttributeConverter<ZoneId, String> {

    @Override
    public String convertToDatabaseColumn(ZoneId zoneId) {
        return zoneId == null ? null : zoneId.toString();
    }

    @Override
    public ZoneId convertToEntityAttribute(String zoneId) {
        return zoneId == null ? null : ZoneId.of(zoneId);
    }
}
