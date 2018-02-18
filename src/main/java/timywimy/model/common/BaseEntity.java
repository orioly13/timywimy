package timywimy.model.common;

import java.time.ZonedDateTime;
import java.util.UUID;

public interface BaseEntity {

    UUID getId();

    void setId(UUID id);

    UUID getCreatedBy();

    void setCreatedBy(UUID createdBy);

    ZonedDateTime getCreatedTs();

    void setCreatedTs(ZonedDateTime createdTs);

    UUID getUpdatedBy();

    void setUpdatedBy(UUID updatedBy);

    ZonedDateTime getUpdatedTs();

    void setUpdatedTs(ZonedDateTime updatedTs);

    UUID getDeletedBy();

    void setDeletedBy(UUID deletedBy);

    ZonedDateTime getDeletedTs();

    void setDeletedTs(ZonedDateTime deletedTs);

    int getVersion();

    void setVersion(int version);

    //other methods (default, lol)
    default boolean isNew() {
        return getId() == null;
    }
}
