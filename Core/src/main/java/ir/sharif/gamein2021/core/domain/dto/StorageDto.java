package ir.sharif.gamein2021.core.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StorageDto {

    private Integer id;
    private Boolean dc;
    private Integer buildingId;
    private List<StorageProductDto> products;
}
