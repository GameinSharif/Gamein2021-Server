package ir.sharif.gamein2021.core.domain.entity;

import lombok.*;

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
    private Integer id;

    @Column
    private LocalDateTime latestMessageDate;

    @OneToMany
    private List<Team> teams;

    @OneToMany
    private List<Message> messages;

}
