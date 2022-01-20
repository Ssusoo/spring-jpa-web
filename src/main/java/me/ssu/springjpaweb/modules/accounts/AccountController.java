package me.ssu.springjpaweb.modules.accounts;

import lombok.RequiredArgsConstructor;
import me.ssu.springjpaweb.modules.accounts.forms.SignUpForm;
import me.ssu.springjpaweb.modules.accounts.validators.SignUpFormValidator;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
    private final AccountRepository accountRepository ;
    private final JavaMailSender javaMailSender;

    // TODO 회원가입 폼 커스텀 검증-3(타입의 이름(signUpForm)을 따라간다)
    // TODO InitBinder 사용하기(커스텀 검증은 회원 가입 처리할 때 알 수 있음)
    @InitBinder("signUpForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signUpFormValidator);
    }

    @PostMapping("/sign-up")
    public String signUpForm(@Valid SignUpForm signUpForm, Errors errors) {

        // TODO 회원가입 폼 서브밋 검증(Filed Errors)-1
        if (errors.hasErrors()) {
            return "accounts/sign-up";
        }
        // TODO 회원가입 폼 커스텀 검증(아래 코드는 InitBinder로 처리하기)-2
//        signUpFormValidator.validate(signUpForm, errors);
//        if (errors.hasErrors()) {
//            return "accounts/sign-up";
//        }

        // TODO 회원가입 폼 서브밋 처리(회원가입 처리)-4
        // TODO 회원 정보
        Account account = Account.builder()
                .email(signUpForm.getEmail())
                .nickname(signUpForm.getNickname())
                // TODO Encoding 처리
                .password(signUpForm.getPassword())
                .studyCreatedByWeb(true)
                .studyUpdatedByWeb(true)
                .studyEnrollmentResultByWeb(true)
                .build();

        Account newAccount = accountRepository.save(account);

        // TODO 회원가입 폼 서브밋 처리(회원가입 처리)-5
        // TODO 이메일 토큰 처리(Account 로직처리)
        newAccount.generateEmailCheckToken();
        // TODO 이메일 전송(ConsoleMailSender 로직처리)
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(newAccount.getEmail());              // 받는 사람
        mailMessage.setSubject("스터디 올래, 회원가입 인증");       // 제목
        mailMessage.setText("/check-mail-token?token=" + newAccount.getEmailCheckToken() +
                "&email=" + newAccount.getEmail());           // 본문
        javaMailSender.send(mailMessage);

        return "redirect:/";
    }

    // TODO 회원가입 폼
    @GetMapping("/sign-up")
    public String signUp(Model model) {
        model.addAttribute(new SignUpForm());
        return "accounts/sign-up";
    }
}
