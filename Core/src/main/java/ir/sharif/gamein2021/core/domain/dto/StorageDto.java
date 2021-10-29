package ir.sharif.gamein2021.core.domain.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@Builder
public class StorageDto {

    private Integer id;
    private boolean isDc;
    private Integer buildingId;
    private List<StorageProductDto> products;
}
