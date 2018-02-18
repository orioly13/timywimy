package timywimy.model.common;

public interface OrderedEntity extends BaseEntity {

    int getOrder();

    void setOrder(int order);
}
