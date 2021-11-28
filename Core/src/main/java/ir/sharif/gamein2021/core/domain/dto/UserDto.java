package ir.sharif.gamein2021.core.domain.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Integer id;
    private String username;
    private String password;
    private Integer teamId;
}
