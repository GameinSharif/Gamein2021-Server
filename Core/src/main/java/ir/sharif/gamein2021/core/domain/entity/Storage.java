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
public class Storage implements BaseEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "storage_id")
    private Integer id;

    @Column(name = "is_dc", nullable = false)
    private Boolean dc;

    @Column(nullable = false)
    private Integer buildingId;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "storage_id")
    private List<StorageProduct> products;
}
