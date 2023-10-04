package oauthlearn.hello.global.config.security.jwt;

import lombok.RequiredArgsConstructor;
import oauthlearn.hello.domain.member.domain.Member;
import oauthlearn.hello.domain.member.dao.MemberRepository;
import oauthlearn.hello.domain.member.domain.Role;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUserId(Long.parseLong(username))
                .orElse(null);

        return User.withUsername(username)
                .password(member.getPassword())
                .authorities(Role.USER.name())
                .build();
    }
}
