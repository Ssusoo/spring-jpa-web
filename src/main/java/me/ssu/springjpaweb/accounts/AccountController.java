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

    // TODO Init Binder(중복 체크 검증)
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

        // TODO 회원가입 (중복체크 검증)
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
