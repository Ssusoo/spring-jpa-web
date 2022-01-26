package me.ssu.springjpaweb.accounts;

import lombok.RequiredArgsConstructor;
import me.ssu.springjpaweb.validators.SignUpFormValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final SignUpFormValidator signUpFormValidator;
    private final AccountService accountService;
    private final AccountRepository accountRepository;

    // TODO 회원가입 인증메일 확인
    @GetMapping("/check-email-token")
    public String checkEmailToken(String token, String email, Model model) {
        // TODO 이메일 유저 확인
        Account account = accountRepository.findByEmail(email);

        // TODO 회원 정보가 Null인 경우
        if (account == null) {
            model.addAttribute("error", "wrong.account");
            return "account/checked-email";
        }

        // TODO 이메일 토큰 값이 없는 경우
        if (!account.getEmailCheckToken().equals(token)) {
            model.addAttribute("error", "wrong.token");
        }

        // TODO 회원가입 완료
        account.setEmailVerified(true);                 // 이메일 확인
        account.setJoinedAt(LocalDateTime.now());       // 가입 날짜
        // TODO 00 번째 유저
        model.addAttribute("numberOfUser", accountRepository.count());
        model.addAttribute("nickname", account.getNickname());

        return "account/checked-email";
    }

    // TODO Init Binder
    @InitBinder("signUpForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signUpFormValidator);
    }

    // TODO 회원가입 페이지
    @PostMapping("/sign-up")
    public String signUpSubmit(@Valid SignUpForm signUpForm,
                               Errors errors) {

        // TODO 입력값 제한하기
        if (errors.hasErrors()) {
            return "accounts/sign-up";
        }

        // TODO 회원가입, 이메일 전송, 이메일 처리 리팩토링
        accountService.processNewAccount(signUpForm);

        // TODO Bad Request
        // TODO Init Binder로 처리하기
//        signUpFormValidator.validate(signUpForm, errors);
//        if (errors.hasErrors()) {
//            return "accounts/sign-up";
//        }

        return "redirect:/";
    }

    // TODO 회원가입 페이지
    @GetMapping("/sign-up")
    public String signUp(Model model) {
        model.addAttribute(new SignUpForm());
        return "accounts/sign-up";
    }
}
