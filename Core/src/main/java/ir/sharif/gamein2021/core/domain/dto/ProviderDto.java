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
    private String productId;
    private Integer maxMonthlyCap;
    private Integer providerAverageCost;
    private Integer providerMinOnRecord;
    private Integer providerMaxOnRecord;

    @Override
    public Integer getId() {
        return id;
    }

    public void setupToZero() {
        this.providerAverageCost = 0;
        this.providerMinOnRecord = 0;
        this.providerMaxOnRecord = 0;
    }
}
