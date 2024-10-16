package pl.pollub.integracja_projekt.Config;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AuthenticationResponse {
    private String token;
    private Integer status;
}
