package me.ssu.springjpaweb.accounts;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class AccountController {

    // TODO 회원가입 폼
    @GetMapping("/sign-up")
    public String signUp(Model model) {
        model.addAttribute(new SignUpForm());
        return "accounts/sign-up";
    }

    // TODO 회원가입 폼 서브밋 검증
    @PostMapping("/sign-up")
    public String signUpForm(@Valid SignUpForm signUpForm, Errors errors) {
        if (errors.hasErrors()) {
            return "accounts/sign-up";
        }
        // TODO 회원 가입 처리
        return "redirect:/";
    }
}
