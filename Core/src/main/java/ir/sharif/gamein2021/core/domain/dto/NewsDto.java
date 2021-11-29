package ir.sharif.gamein2021.core.domain.dto;

import ir.sharif.gamein2021.core.util.Enums;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsDto implements BaseDto<Integer>{
    private Integer id;
    private Integer week;
    private Enums.NewsType newsType;
    private String mainTitle;
    private String mainText;
    private List<String> subTexts;
    private Integer imageIndex;

}
