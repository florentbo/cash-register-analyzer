package be.florentbo.register.repository;

import javax.persistence.*;

@Entity
@Table(name="kassaorder_detail")
public class RegisterOrderDetail {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private long id;

    @ManyToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name="orderid", nullable=false, updatable=false)
    private RegisterOrder registerOrder;

    @ManyToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name="productid", nullable=false, updatable=false)
    private Item product;

    @Column(name="aantal")
    private long quantity;

    public RegisterOrder getRegisterOrder() {
        return registerOrder;
    }

    public Item getProduct() {
        return product;
    }

    public long getQuantity() {
        return quantity;
    }
}
