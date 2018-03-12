package timywimy.service.converters;

import timywimy.web.dto.Task;
import timywimy.web.dto.User;
import timywimy.web.dto.common.DateTimeZone;

public class Converter {
    private Converter() {
    }

    public static User userEntityToUserDTO(timywimy.model.security.User entity) {
        if (entity == null)
            return null;
        User dto = new User();
        dto.setId(entity.getId());
        dto.setEmail(entity.getEmail().toLowerCase());
        dto.setName(entity.getName());
        dto.setBanned(entity.isBanned());
        dto.setBannedTill(entity.getBannedTill());
        dto.setRole(entity.getRole());
        return dto;
    }

    public static timywimy.model.security.User userDTOtoUserEntity(User dto) {
        if (dto == null) {
            return null;
        }
        timywimy.model.security.User entity = new timywimy.model.security.User();
        //to lowercase to prevent changes
        entity.setEmail(dto.getEmail().toLowerCase());
        entity.setPassword(dto.getPassword());
        entity.setName(dto.getName());
        entity.setRole(dto.getRole());
        entity.setBanned(dto.isBanned() == null ? false : dto.isBanned());
        entity.setBannedTill(dto.getBannedTill());
        return entity;
    }

    public static Task taskEntityToTaskDTO(timywimy.model.bo.tasks.Task entity) {
        if (entity == null)
            return null;
        Task dto = new Task();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setPriority(entity.getPriority());
        dto.setCompleted(entity.isCompleted());
        dto.setDeadline(dateTimeZoneEntityToDTO(entity.getDateTimeZone()));
        //todo parent,children, event
        return dto;
    }

    public static DateTimeZone dateTimeZoneEntityToDTO(timywimy.model.common.util.DateTimeZone dateTimeZone) {
        return new DateTimeZone(
                dateTimeZone.getDate(), dateTimeZone.getTime(), dateTimeZone.getZone());
    }

    public static timywimy.model.common.util.DateTimeZone dateTimeZoneDTOToEntity(DateTimeZone dateTimeZone) {
        return new timywimy.model.common.util.DateTimeZone(
                dateTimeZone.getDate(), dateTimeZone.getTime(), dateTimeZone.getZone());
    }

}
