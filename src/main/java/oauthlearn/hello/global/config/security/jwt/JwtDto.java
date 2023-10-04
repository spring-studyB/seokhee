package oauthlearn.hello.global.config.security.jwt;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
public class JwtDto {
    private String accessToken;
    private Date expirationDate;
}
