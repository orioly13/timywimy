package com.timywimy.model.model.common;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@MappedSuperclass
public abstract class BaseEntityImpl implements BaseEntity {
    //todo serilizable,clonable (to clone events or whatever)
    //todo check timestamps,check version

    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Id
    @Column(name = "id", columnDefinition = "UUID")
    private UUID id;

    @Column(name = "created_by", columnDefinition = "UUID", nullable = false)
    private UUID createdBy;
    @Column(name = "created_ts", columnDefinition = "TIMESTAMP WITH TIME ZONE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private OffsetDateTime createdTs;
    @Column(name = "updated_by", columnDefinition = "UUID")
    private UUID updatedBy;
    @Column(name = "updated_ts", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @Temporal(TemporalType.TIMESTAMP)
    private OffsetDateTime updatedTs;
    @Column(name = "deleted_by", columnDefinition = "UUID")
    private UUID deletedBy;
    @Column(name = "deleted_ts", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @Temporal(TemporalType.TIMESTAMP)
    private OffsetDateTime deletedTs;

    @Version
    @Column(name = "version", columnDefinition = "NUMERIC", nullable = false)
    private Integer version;

    protected BaseEntityImpl() {
    }

    protected BaseEntityImpl(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UUID createdBy) {
        this.createdBy = createdBy;
    }

    public OffsetDateTime getCreatedTs() {
        return createdTs;
    }

    public void setCreatedTs(OffsetDateTime createdTs) {
        this.createdTs = createdTs;
    }

    public UUID getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(UUID updatedBy) {
        this.updatedBy = updatedBy;
    }

    public OffsetDateTime getUpdatedTs() {
        return updatedTs;
    }

    public void setUpdatedTs(OffsetDateTime updatedTs) {
        this.updatedTs = updatedTs;
    }

    public UUID getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(UUID deletedBy) {
        this.deletedBy = deletedBy;
    }

    public OffsetDateTime getDeletedTs() {
        return deletedTs;
    }

    public void setDeletedTs(OffsetDateTime deletedTs) {
        this.deletedTs = deletedTs;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return String.format("(id:%s)", id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof BaseEntity)) {
            return false;
        }
        BaseEntityImpl that = (BaseEntityImpl) o;
        return Objects.equals(createdBy, that.createdBy) &&
                Objects.equals(createdTs, that.createdTs) &&
                Objects.equals(updatedBy, that.updatedBy) &&
                Objects.equals(updatedTs, that.updatedTs) &&
                Objects.equals(deletedBy, that.deletedBy) &&
                Objects.equals(deletedTs, that.deletedTs) &&
                Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                createdBy, createdTs,
                updatedBy, updatedTs,
                deletedBy, deletedTs,
                version, version);
    }
}
