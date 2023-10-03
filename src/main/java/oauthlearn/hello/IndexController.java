package oauthlearn.hello;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@Slf4j
public class IndexController {

    private final MemberRepository memberRepository;
    private final HttpSession httpSession;

    @GetMapping("/")
    public String home(Model model){
        return "home";
    }

    @GetMapping("/login")
    public String test() {
        return "login";
    }
}
