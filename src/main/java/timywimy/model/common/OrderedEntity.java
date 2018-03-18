package timywimy.model.common;

public interface OrderedEntity extends BaseEntity {

    Integer getOrder();

    void setOrder(Integer order);
}
