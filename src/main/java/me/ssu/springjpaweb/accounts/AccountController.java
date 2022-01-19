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

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final SignUpFormValidator signUpFormValidator;

    // TODO 회원가입 폼 커스텀 검증(타입의 이름(signUpForm)을 따라간다)
    // TODO InitBinder 사용하기(커스텀 검증은 회원 가입 처리할 때 알 수 있음)
    @InitBinder("signUpForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signUpFormValidator);
    }

    // TODO 회원가입 폼
    @GetMapping("/sign-up")
    public String signUp(Model model) {
        model.addAttribute(new SignUpForm());
        return "accounts/sign-up";
    }

    @PostMapping("/sign-up")
    public String signUpForm(@Valid SignUpForm signUpForm, Errors errors) {

        // TODO 회원가입 폼 서브밋 검증
        if (errors.hasErrors()) {
            return "accounts/sign-up";
        }
        // TODO 회원가입 폼 커스텀 검증(아래 코드는 InitBinder로 처리하기)
//        signUpFormValidator.validate(signUpForm, errors);
//        if (errors.hasErrors()) {
//            return "accounts/sign-up";
//        }

        // TODO 회원 가입 처리
        Account account = Account.builder()
                .email(signUpForm.getEmail())
                .nickname(signUpForm.getNickname())
                // TODO encoding하기
                .password(signUpForm.getPassword())
                .stuEn
                .studyUpdatedByWeb(true)
                .build();
        return "redirect:/";
    }
}
