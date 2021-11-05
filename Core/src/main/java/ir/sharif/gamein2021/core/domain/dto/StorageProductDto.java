package ir.sharif.gamein2021.core.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StorageProductDto {
    private Integer id;
    private int productId;
    private int amount;
}
