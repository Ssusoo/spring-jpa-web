package me.ssu.springjpaweb.accounts;

import lombok.RequiredArgsConstructor;
import me.ssu.springjpaweb.validators.SignUpFormValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final SignUpFormValidator signUpFormValidator;
    private final AccountService accountService;
    private final AccountRepository accountRepository;

    // TODO 프로필 뷰
    @GetMapping("/profile/{nickname}")
    public String viewProfile(@PathVariable String nickname, Model model,
                              @CurrentAccount Account account) {

        Account byNickname = accountRepository.findByNickname(nickname);

        if (byNickname == null) {
            throw new IllegalArgumentException(nickname + "에 해당하는 사용자가 없습니다.");
        }

        // TODO "account" 생략하면 기본 값으로 "account" 설정됨.
        model.addAttribute("account", byNickname);
        model.addAttribute("isOwner", byNickname.equals(account));

        return "accounts/profile";
    }

    // TODO 가입 확인 이메일 재전송 기능(이메일 전송 유무)-1
    @GetMapping("/check-email")
    public String checkEmail(@CurrentAccount Account account, Model model) {
        model.addAttribute("email", account.getEmail());
        return "accounts/check-email";
    }

    // TODO 가입 확인 이메일 재전송 기능(재전송)-2
    // TODO 이메일 재전송
    @GetMapping("/resend-confirm-email")
    public String resendConfirmEmail(@CurrentAccount Account account, Model model) {
        // TODO 이메일 보낼 수 있는지 유무
        if (!account.canSendConfirmEmail()) {
            model.addAttribute("error", "인증 이메일은 1시간에 한 번만 전송할 수 있습니다.");
            model.addAttribute("email", account.getEmail());
            return "accounts/check-email";
        }

        // TODO 이메일 재전송
        accountService.sendSignUpConfirmEmail(account);

        return "redirect:/";
    }

    // TODO 회원가입 인증메일 처리
    @GetMapping("/check-email-token")
    public String checkEmailToken(String email, String token,
                                  Model model) {
        // TODO 회원정보 조회
        Account account = accountRepository.findByEmail(email);
        String view = "accounts/checked-email";

        // TODO 회원이 없는 경우
        if (account == null) {
            model.addAttribute("error", "wrong.email");
            return view;
        }

        // TODO 토큰 값이 없는 경우(리팩토링 전)
//        if (!account.getEmailCheckToken().equals(token)) {
//            model.addAttribute("error", "wrong.token");
//            return view;
//        }
        // TODO 토큰 값이 없는 경우(리팩토링 후 Account 로직 처리)
        // TODO accountService.processNewAccount()에서 @Transaction 처리를 하지않으면
        // TODO 이메일 토큰 값이 null로 떨어짐.
        if (!account.isValidToken(token)) {
            model.addAttribute("error", "wrong.token");
            return view;
        }

        // TODO 토큰 값이 없는 경우(리팩토링 후 Account 로직 처리)
        account.compleSignUp();
        // TODO 자동 로그인
        accountService.login(account);
        /*
            이메일을 확인했습니다. 10번째 회원, ssu님 가입을 축하합니다.
            {}번째 {}님 필요!!!
        */
        // TODO 00번째 유저
        model.addAttribute("numberOfUser", accountRepository.count());
        model.addAttribute("nickname", account.getNickname());

        // TODO 회원가입 인증메일 처리
        return view;
    }

    // TODO InitBinder
    @InitBinder("signUpForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signUpFormValidator);
    }

    // TODO 회원가입 Post HTML Form
    @PostMapping("/sign-up")
    public String signUpSubmit(@Valid SignUpForm signUpForm,
                               Errors errors) {
        // TODO 입력값 제한하기
        if (errors.hasErrors()) {
            return "accounts/sign-up";
        }

        // TODO 회원가입처리 & 이메일 토큰 & 이메일 전송 리팩토링(AccountService에서 로직처리)
        Account account = accountService.processNewAccount(signUpForm);

        // TODO 자동 로그인
        accountService.login(account);

        // TODO 회원가입 처리
        return "redirect:/";
    }

    // TODO 회원가입 페이지 보여주기
    @GetMapping("/sign-up")
    public String signUpForm(Model model) {
        model.addAttribute(new SignUpForm());
        return "accounts/sign-up";
    }
}
