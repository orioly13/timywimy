package timywimy.model.bo.events.extensions.common;

import timywimy.model.common.NamedEntity;
import timywimy.model.common.OrderedEntity;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class AbstractDefaultEventExtension extends AbstractEventExtension implements NamedEntity {

    @Column(name = "name", columnDefinition = "varchar(50)")
    private String name;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
