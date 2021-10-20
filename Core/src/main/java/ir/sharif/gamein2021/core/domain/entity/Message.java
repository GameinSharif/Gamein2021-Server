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
public class Message implements BaseEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    private Team senderTeam;

    @OneToOne
    private Team receiverTeam;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @Override
    public Integer getId()
    {
        return null;
    }
}
