package timywimy.model.bo.tasks.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class PriorityConverter implements AttributeConverter<Priority, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Priority priority) {
        return priority == null ? Priority.UNKNOWN.code : priority.code;
    }

    @Override
    public Priority convertToEntityAttribute(Integer i) {
        return i == null ? Priority.UNKNOWN : Priority.fromCode(i);
    }
}