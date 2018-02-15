package com.timywimy.model.model.common;

import java.time.OffsetDateTime;
import java.util.UUID;

public interface BaseEntity {

    UUID getId();

    void setId(UUID id);

    UUID getCreatedBy();

    void setCreatedBy(UUID createdBy);

    OffsetDateTime getCreatedTs();

    void setCreatedTs(OffsetDateTime createdTs);

    UUID getUpdatedBy();

    void setUpdatedBy(UUID updatedBy);

    OffsetDateTime getUpdatedTs();

    void setUpdatedTs(OffsetDateTime updatedTs);

    UUID getDeletedBy();

    void setDeletedBy(UUID deletedBy);

    OffsetDateTime getDeletedTs();

    void setDeletedTs(OffsetDateTime deletedTs);

    Integer getVersion();

    void setVersion(Integer version);

    //other methods (default, lol)
    default boolean isNew() {
        return getId() == null;
    }
}
