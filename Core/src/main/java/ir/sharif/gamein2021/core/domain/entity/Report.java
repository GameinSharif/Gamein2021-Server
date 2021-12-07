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

    @ManyToOne
    private Team reporterTeam;

    @ManyToOne
    private Team reportedTeam;

    @ManyToOne
    private Chat chat;

    @Column(nullable = false)
    private LocalDateTime reportedAt;

    @Column(nullable = false)
    private LocalDateTime sentAt;

    @Override
    public Integer getId() {
        return id;
    }

}
