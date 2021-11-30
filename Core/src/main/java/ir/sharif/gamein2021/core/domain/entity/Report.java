package ir.sharif.gamein2021.core.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report implements BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "message_text", nullable = false)
    private String messageText;

    @OneToOne
    private Team reporterTeam;

    @OneToOne
    private Team reportedTeam;

    @OneToOne
    private Chat chat;

    @Column(nullable = false)
    private LocalDateTime reportedAt;

    @Override
    public Integer getId() {
        return id;
    }

}
