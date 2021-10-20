package ir.sharif.gamein2021.core.domain.entity;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chat implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Integer id;

    @Column(name = "latest_message_date", nullable = false)
    private LocalDateTime latestMessageDate;

    @OneToOne
    private Team team1;

    @OneToOne
    private Team team2;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "chat_id")
    private List<Message> messages;
}
