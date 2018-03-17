package timywimy.repository.common;

import timywimy.model.common.AbstractOwnedEntity;
import timywimy.model.common.DateTimeZoneEntity;
import timywimy.model.common.util.DateTimeZone;
import timywimy.util.RequestUtil;
import timywimy.util.exception.ErrorCode;
import timywimy.util.exception.RepositoryException;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractEventTaskEntityRepository<T extends AbstractOwnedEntity> extends
        AbstractOwnedEntityRepository<T> implements EventTaskEntityRepository<T> {

    protected void assertBetween(DateTimeZone start, DateTimeZone end) {
        RequestUtil.validateEmptyField(RepositoryException.class, start, "start");
        RequestUtil.validateEmptyField(RepositoryException.class, end, "end");
        if (start.isAfter(end)) {
            throw new RepositoryException(ErrorCode.REQUEST_VALIDATION_INVALID_FIELDS, "start is after end");
        }
    }


    protected <D extends DateTimeZoneEntity> List<D> getBetween(List<D> dateTimeZoneList, DateTimeZone start, DateTimeZone end) {
        assertBetween(start, end);
        List<D> result = new ArrayList<>();
        for (D dateTimeZoneEntity : dateTimeZoneList) {
            if (dateTimeZoneEntity.getDateTimeZone() == null) {
                continue;
            }
            if (start.isBefore(dateTimeZoneEntity.getDateTimeZone()) && end.isAfter(dateTimeZoneEntity.getDateTimeZone())) {
                result.add(dateTimeZoneEntity);
            }
        }
        return result;
    }

}
