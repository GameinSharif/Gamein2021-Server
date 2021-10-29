package ir.sharif.gamein2021.core.domain.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@Builder
public class StorageProductDto {
    private Integer id;
    private int productId;
    private int amount;
}
