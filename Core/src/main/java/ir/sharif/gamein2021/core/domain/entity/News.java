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
    @Column(name = "type", nullable = false)
    private Enums.NewsType newsTypeEng;

    @Column(name = "main_title_eng", nullable = false)
    private String mainTitleEng;

    @Column(name = "main_text_eng", nullable = false)
    private String mainTextEng;

    @Column(name = "sub_texts_eng_1")
    private String subTextsEng1;

    @Column(name = "sub_texts_eng_2")
    private String subTextsEng2;

    @Column(name = "sub_texts_eng_3")
    private String subTextsEng3;

    @Column(name = "main_title_fa", nullable = false)
    private String mainTitleFa;

    @Column(name = "main_text_fa", nullable = false)
    private String mainTextFa;

    @Column(name = "sub_texts_fa_1")
    private String subTextsFa1;

    @Column(name = "sub_texts_fa_2")
    private String subTextsFa2;

    @Column(name = "sub_texts_fa_3")
    private String subTextsFa3;

    @Column(name = "image_index", nullable = false)
    private Integer imageIndex;

}
