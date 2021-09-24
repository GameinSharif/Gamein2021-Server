package ir.sharif.gamein2021.core.domain.entity;

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

    // TODO : name?
    @Column(nullable = false)
    private String company; // TODO : string?

    @Column(nullable = false, unique = true)
    private String type; // TODO : string?

    @Column(nullable = false)
    private Integer maxMonthlyCap;

    @Column(nullable = false)
    private Integer providerAverageCost;

    @Column(nullable = false)
    private Integer providerMinOnRecord;

    @Column(nullable = false)
    private Integer providerMaxOnRecord;

    @Override
    public Integer getId() {
        return id;
    }
}
