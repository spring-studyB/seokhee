package oauthlearn.hello.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.io.Decoders;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;


@Component
public class JwtProvider {

    private final Key key;
    private final UserDetailsService userDetailsService;

    public JwtProvider(@Value("${jwt.secret}") String secretKey,
                       UserDetailsService userDetailsService) {
        byte[] bytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(bytes);
        this.userDetailsService = userDetailsService;
    }

    public JwtDto createToken(Long userId) {
        final long accessTokenValidTime = 30 * 60 * 1000; // 30분

        Date now = new Date();
        LocalDateTime expirationDate = LocalDateTime.now().plusMinutes(30);

        Claims claims = Jwts.claims().setSubject(String.valueOf(userId));

        String accessToken = Jwts.builder()
                .setClaims(claims)
                .signWith(key)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenValidTime))
                .compact();

        return JwtDto.builder()
                .accessToken(accessToken)
                .expirationDate(expirationDate)
                .build();
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());
        if (userDetails == null) {
            return null;
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    private Claims parseClaims(String accessToken) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(accessToken).getBody();
    }
}
