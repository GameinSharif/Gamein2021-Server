package ir.sharif.gamein2021.core.domain.dto;

import ir.sharif.gamein2021.core.domain.entity.Team;
import lombok.*;

import javax.persistence.Column;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProviderDto implements BaseDto<Integer> {

    private Integer id;
    private Team team;
    private Integer productId;
    private Integer capacity;
    private Integer averagePrice;
    private Integer minPriceOnRecord;
    private Integer maxPriceOnRecord;

    @Override
    public Integer getId() {
        return id;
    }

    public void setupToZero() {
        this.averagePrice = 0;
        this.minPriceOnRecord = 0;
        this.maxPriceOnRecord = 0;
    }
}
