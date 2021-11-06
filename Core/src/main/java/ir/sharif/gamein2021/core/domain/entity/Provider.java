package ir.sharif.gamein2021.core.domain.entity;

import ir.sharif.gamein2021.core.util.Enums;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Provider implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Team team;

    @Column(name = "product_id", nullable = false)
    private Integer productId;

    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @Column(name = "price", nullable = false)
    private Float price;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "state", nullable = false)
    private Enums.ProviderState state;

    @Override
    public Integer getId() {
        return id;
    }
}
