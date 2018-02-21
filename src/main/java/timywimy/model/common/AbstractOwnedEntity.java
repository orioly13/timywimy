package timywimy.model.common;

import timywimy.model.security.UserImpl;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractOwnedEntity extends AbstractBaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private UserImpl owner;

    public UserImpl getOwner() {
        return owner;
    }

    public void setOwner(UserImpl owner) {
        this.owner = owner;
    }
}
