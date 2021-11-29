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
    private Enums.NewsType newsType;

    @Column(name = "main_title", nullable = false)
    private String mainTitle;

    @Column(name = "main_text", nullable = false)
    private String mainText;

    @Column(name = "sub_texts")
    private List<String> subTexts;

    @Column(name = "image_index", nullable = false)
    private Integer imageIndex;

}
