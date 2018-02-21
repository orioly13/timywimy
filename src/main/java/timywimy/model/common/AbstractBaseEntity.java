package timywimy.model.common;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.UUID;

@MappedSuperclass
public abstract class AbstractBaseEntity implements BaseEntity {

    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Id
    @Column(name = "id", columnDefinition = "uuid")
    @Type(type="org.hibernate.type.PostgresUUIDType")
    private UUID id;
    @Column(name = "created_by", columnDefinition = "uuid")
    @Type(type="org.hibernate.type.PostgresUUIDType")
    private UUID createdBy;
    @Column(name = "created_ts", columnDefinition = "timestamp with time zone")
    private ZonedDateTime createdTs;
    @Column(name = "updated_by", columnDefinition = "uuid")
    @Type(type="org.hibernate.type.PostgresUUIDType")
    private UUID updatedBy;
    @Column(name = "updated_ts", columnDefinition = "timestamp with time zone")
    private ZonedDateTime updatedTs;
    @Column(name = "deleted_by", columnDefinition = "uuid")
    @Type(type="org.hibernate.type.PostgresUUIDType")
    private UUID deletedBy;
    @Column(name = "deleted_ts", columnDefinition = "timestamp with time zone")
    private ZonedDateTime deletedTs;
    @Version
    @Column(name = "version", columnDefinition = "integer", nullable = false)
    private int version;

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public UUID getCreatedBy() {
        return createdBy;
    }

    @Override
    public void setCreatedBy(UUID createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public ZonedDateTime getCreatedTs() {
        return createdTs;
    }

    @Override
    public void setCreatedTs(ZonedDateTime createdTs) {
        this.createdTs = createdTs;
    }

    @Override
    public UUID getUpdatedBy() {
        return updatedBy;
    }

    @Override
    public void setUpdatedBy(UUID updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Override
    public ZonedDateTime getUpdatedTs() {
        return updatedTs;
    }

    @Override
    public void setUpdatedTs(ZonedDateTime updatedTs) {
        this.updatedTs = updatedTs;
    }

    @Override
    public UUID getDeletedBy() {
        return deletedBy;
    }

    @Override
    public void setDeletedBy(UUID deletedBy) {
        this.deletedBy = deletedBy;
    }

    @Override
    public ZonedDateTime getDeletedTs() {
        return deletedTs;
    }

    @Override
    public void setDeletedTs(ZonedDateTime deletedTs) {
        this.deletedTs = deletedTs;
    }

    @Override
    public int getVersion() {
        return version;
    }

    @Override
    public void setVersion(int version) {
        this.version = version;
    }


    @PreUpdate
    @PrePersist
    public void updateTimeStamps() {
        updatedTs = ZonedDateTime.now();
        if (createdTs == null) {
            createdTs = ZonedDateTime.now();
        }
    }
//    @Override
//    public String toString() {
//        return String.format("(id:%s)", id);
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) {
//            return true;
//        }
//        if (o == null || !(o instanceof BaseEntity)) {
//            return false;
//        }
//        BaseEntityImpl that = (BaseEntityImpl) o;
//        return Objects.equals(createdBy, that.createdBy) &&
//                Objects.equals(createdTs, that.createdTs) &&
//                Objects.equals(updatedBy, that.updatedBy) &&
//                Objects.equals(updatedTs, that.updatedTs) &&
//                Objects.equals(deletedBy, that.deletedBy) &&
//                Objects.equals(deletedTs, that.deletedTs) &&
//                Objects.equals(version, that.version);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(
//                createdBy, createdTs,
//                updatedBy, updatedTs,
//                deletedBy, deletedTs,
//                version, version);
//    }
}
