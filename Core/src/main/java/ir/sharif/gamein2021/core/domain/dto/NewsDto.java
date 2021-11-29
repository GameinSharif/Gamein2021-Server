package ir.sharif.gamein2021.core.domain.dto;

import ir.sharif.gamein2021.core.util.Enums;
import lombok.*;

import javax.persistence.Column;
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
    private List<String> subTextsEng;
    private String mainTitleFa;
    private String mainTextFa;
    private List<String> subTextsFa;
    private Integer imageIndex;

}
