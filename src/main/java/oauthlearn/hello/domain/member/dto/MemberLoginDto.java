package oauthlearn.hello.domain.member.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberLoginDto {
    private String email;
    private String name;
    private String picture;
    private String role;
}
