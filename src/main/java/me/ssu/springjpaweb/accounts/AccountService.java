package me.ssu.springjpaweb.accounts;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;

@Service
@RequiredArgsConstructor
public class AccountService {

    // TODO Service로 빼기(Controller)의 부담을 줄여주기
    private final AccountRepository accountRepository;
    private final JavaMailSender javaMailSender;
    // TODO 패스워드 인코딩 처리하기
    private final PasswordEncoder passwordEncoder;

    public void processNewAccount(SignUpForm signUpForm) {
        // TODO 회원가입(리팩토링)-2
        Account newAccount = saveNewAccount(signUpForm);
        // TODO 이메일 토큰 처리(Account 로직 처리)
        newAccount.generateEmailCheckToken();
        // TODO 이메일 전송(리팩토링)-2
        sendSignUpConfirmEmail(newAccount);
    }

    // TODO 이메일 전송-1
    private void sendSignUpConfirmEmail(Account newAccount) {
        // TODO 회원가입 폼 서브밋 처리(회원가입 처리)-5
        // TODO 이메일 전송(ConsoleMailSender)
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        // TODO 받는사람
        mailMessage.setTo(newAccount.getEmail());
        // TODO 제목
        mailMessage.setSubject("스터디 올레, 회원가입 인증");
        // TODO 본문
        mailMessage.setText("/check-email-token?token=" + newAccount.getEmailCheckToken() +
                "&email=" + newAccount.getEmail());

        // TODO 메일 보내기
        javaMailSender.send(mailMessage);
    }

    // TODO 회원가입-1
    private Account saveNewAccount(@Valid SignUpForm signUpForm) {
        // TODO 회원가입 폼 서브밋 처리(회원가입 처리)-4
        Account account = Account.builder()
                .email(signUpForm.getEmail())
                .nickname(signUpForm.getNickname())
                // TODO Encoding 처리
                .password(passwordEncoder.encode(signUpForm.getPassword()))
                .studyCreatedByWeb(true)
                .studyUpdatedByWeb(true)
                .studyEnrollmentResultByWeb(true)
                .build();

        // TODO 리턴 객체는 여기 안에는 트랜잭션임.
        return accountRepository.save(account);
    }
}
