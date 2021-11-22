package ir.sharif.gamein2021.core.domain.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DynamicConfig {
    @Id
    @Column(name = "key_string")
    private String key;

    @Column(name = "value_string")
    private String value;
}
