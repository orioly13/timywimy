package timywimy.web.controllers.entities;


import timywimy.web.dto.common.DateTimeZone;
import timywimy.web.dto.common.Response;
import timywimy.web.dto.events.Event;

import java.util.List;
import java.util.UUID;

public interface EventController extends EntityController<Event> {

    Response<List<Event>> getBetween(Integer requestId, UUID session, DateTimeZone start, DateTimeZone finish);
}
