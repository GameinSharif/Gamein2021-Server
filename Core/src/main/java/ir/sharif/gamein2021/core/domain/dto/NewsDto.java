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
    private String mainTitleEng;
    private String mainTextEng;
    private String subTextsEng1;
    private String subTextsEng2;
    private String subTextsEng3;

    private String mainTitleFa;
    private String mainTextFa;
    private String subTextsFa1;
    private String subTextsFa2;
    private String subTextsFa3;

    private Integer imageIndex;

}
