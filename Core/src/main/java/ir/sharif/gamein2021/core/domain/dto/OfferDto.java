package ir.sharif.gamein2021.core.domain.dto;

import ir.sharif.gamein2021.core.util.Enums.OfferStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OfferDto implements BaseDto<Integer>
{
    private Integer id;
    private Integer teamId;
    private String type;
    private Integer volume;
    private OfferStatus offerStatus;
    private Integer costPerUnit;
    private LocalDateTime earliestExpectedArrival;
    private LocalDateTime latestExpectedArrival;
    private LocalDateTime offerDeadline;

    @Override
    public String toString()
    {
        return "OfferDto{" +
                ", id=" + id +
                ", teamId=" + teamId +
                ", type='" + type + '\'' +
                ", volume=" + volume +
                ", costPerUnit=" + costPerUnit +
                ", earliestExpectedArrival=" + earliestExpectedArrival +
                ", latestExpectedArrival=" + latestExpectedArrival +
                ", offerDeadline=" + offerDeadline +
                '}';
    }
}
