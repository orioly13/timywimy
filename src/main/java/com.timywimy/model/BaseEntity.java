package com.timywimy.model;

import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.internal.util.compare.EqualsHelper;

import javax.persistence.*;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

@MappedSuperclass
public abstract class BaseEntity implements Serializable, Cloneable {
    //todo check timestamps,check version
    //add named and described entities

    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Id
    @Column(name = "id", columnDefinition = "UUID")
    protected UUID id;

    @Column(name = "created_by", columnDefinition = "UUID", nullable = false)
    protected UUID createdBy;
    @Column(name = "created_ts", columnDefinition = "TIMESTAMP WITH TIME ZONE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected OffsetDateTime createdTs;
    @Column(name = "updated_by", columnDefinition = "UUID")
    protected UUID updatedBy;
    @Column(name = "updated_ts", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @Temporal(TemporalType.TIMESTAMP)
    protected OffsetDateTime updatedTs;
    @Column(name = "deleted_by", columnDefinition = "UUID")
    protected UUID deletedBy;
    @Column(name = "deleted_ts", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @Temporal(TemporalType.TIMESTAMP)
    protected OffsetDateTime deletedTs;

    @Version
    @Column(name = "version", columnDefinition = "TIMESTAMP WITH TIME ZONE", nullable = false)
    protected Integer version;

    protected BaseEntity() {
    }

    protected BaseEntity(UUID id) {
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

    public boolean isNew() {
        return this.id == null;
    }


    @Override
    public String toString() {
        return String.format("Base Entity (%s, %s)", id, version);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !getClass().equals(Hibernate.getClass(o))) {
            return false;
        }
        BaseEntity that = (BaseEntity) o;
        return EqualsHelper.equals(id, that.id) &&
                EqualsHelper.equals(createdBy, that.createdBy) &&
                EqualsHelper.equals(createdTs, that.createdTs);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }
}
