package ir.sharif.gamein2021.core.domain.entity;


import ir.sharif.gamein2021.core.util.Enums;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class News implements BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "week", nullable = false)
    private Integer week;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private Enums.NewsType newsTypeEng;

    @Column(name = "main_title_eng", nullable = false)
    private String mainTitleEng;

    @Column(name = "main_text_eng", nullable = false)
    private String mainTextEng;

    @Column(name = "sub_texts_eng")
    private List<String> subTextsEng;

    @Column(name = "main_title_fa", nullable = false)
    private String mainTitleFa;

    @Column(name = "main_text_fa", nullable = false)
    private String mainTextFa;

    @Column(name = "sub_texts_fa")
    private List<String> subTextsFa;

    @Column(name = "image_index", nullable = false)
    private Integer imageIndex;

}
