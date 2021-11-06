package ir.sharif.gamein2021.core.domain.dto;

import ir.sharif.gamein2021.core.util.Enums.OfferStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OfferDto implements BaseDto<Integer>
{
    private Integer id;
    private Integer teamId;
    private Integer productId;
    private Integer volume;
    private OfferStatus offerStatus;
    private Float costPerUnit;
    private LocalDate offerDeadline;

    @Override
    public String toString()
    {
        return "OfferDto{" +
                ", id=" + id +
                ", teamId=" + teamId +
                ", productId='" + productId + '\'' +
                ", volume=" + volume +
                ", costPerUnit=" + costPerUnit +
                ", offerDeadline=" + offerDeadline +
                '}';
    }
}
