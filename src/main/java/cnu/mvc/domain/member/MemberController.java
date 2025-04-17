package cnu.mvc.domain.member;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 초기 데이터 등록 (테스트용)
    @PostConstruct
    public void init() {
        try {
            memberService.join(new Member("kim", "kim@gmail.com", "010-1234-5678", "1234"));
        } catch (IllegalStateException e) {
            // 이미 등록되어 있으면 무시
        }
    }

    // 회원가입 폼(GET): joinMemberForm.html 사용
    @GetMapping("/join")
    public String joinForm(Model model) {
        model.addAttribute("member", new Member());
        return "member/joinMemberForm";
    }

    // 회원가입 처리(POST)
    @PostMapping("/join")
    public String join(Member member, Model model) {
        try {
            memberService.join(member);
            return "member/member"; // 가입 성공 시 회원 상세 화면
        } catch (IllegalStateException e) {
            // 중복 이메일 시 에러 메시지를 모델에 담고 다시 가입 폼으로 전달
            model.addAttribute("error", e.getMessage());
            model.addAttribute("member", member);
            return "member/joinMemberForm";
        }
    }

    // 로그인 폼(GET): loginForm.html 사용
    @GetMapping("/login")
    public String loginForm() {
        return "member/loginHome";
    }

    // 로그인 처리(POST)
    @PostMapping("/login")
    public String login(@RequestParam("email") String email,
                        @RequestParam("pwd") String pwd,
                        HttpSession session,
                        Model model) {
        try {
            Member loginMember = memberService.validateMember(email, pwd);
            session.setAttribute("currentMember", loginMember);
            model.addAttribute("memberName", loginMember.getName());
            return "loginHome"; // 로그인 성공 시 loginHome.html로 이동
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "index"; // 로그인 실패 시 index.html(로그인 폼)로 이동
        }
    }



    // 로그아웃 처리(POST)
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/"; // 로그아웃 후 메인으로 리다이렉트
    }
}
