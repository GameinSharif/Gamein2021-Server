package ir.sharif.gamein2021.core.domain.dto;

import lombok.*;

import javax.persistence.Column;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProviderDto implements BaseDto<Integer> {

    private Integer id;
    private String company; // TODO : string?
    private String type; // TODO : string?
    private Integer maxMonthlyCap;
    private Integer providerAverageCost;
    private Integer providerMinOnRecord;
    private Integer providerMaxOnRecord;

    @Override
    public void setId(Integer id) {
        // TODO
    }

    @Override
    public Integer getId() {
        return id;
    }
}
