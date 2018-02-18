package timywimy.model.security.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class RoleConverter implements AttributeConverter<Role, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Role role) {
        return role == null ? Role.UNKNOWN.code : role.code;
    }

    @Override
    public Role convertToEntityAttribute(Integer code) {
        return code == null ? Role.UNKNOWN : Role.fromCode(code);
    }
}
