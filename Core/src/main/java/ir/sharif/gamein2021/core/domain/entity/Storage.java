package ir.sharif.gamein2021.core.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Storage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private boolean dc;
    @Column(nullable = false)
    private Integer buildingId;
    @OneToMany
    private List<StorageProduct> products;
}
