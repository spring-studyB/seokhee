package oauthlearn.hello.jwt;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class JwtDto {
    private String accessToken;
    private LocalDateTime expirationDate;
}
