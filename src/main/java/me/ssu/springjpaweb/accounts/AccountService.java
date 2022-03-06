package me.ssu.springjpaweb.accounts;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final JavaMailSender javaMailSender;

    // TODO 회원가입 후 자동 로그인 메소드 구현-2
    public void login(Account account) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                account.getNickname(),
                account.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(token);
    }

    // TODO 자동 로그인 void -> Account Return-1
    @Transactional
    public Account processNewAccount(SignUpForm signUpForm) {
        // TODO 회원가입 처리 리팩토링
        Account newAccount = saveNewAccount(signUpForm);

        // TODO 이메일 토큰(Account에서 로직처리)
        newAccount.generateEmailCheckToken();

        // TODO 이메일 전송 리팩토링
        sendSignUpConfirmEmail(newAccount);

        return newAccount;
    }

    private void sendSignUpConfirmEmail(Account newAccount) {
        // TODO 이메일 전송
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        // TODO 받는사람
        mailMessage.setTo(newAccount.getEmail());
        // TODO 제목
        mailMessage.setSubject("스터디 올레, 회원가입 인증");
        // TODO 본문
        mailMessage.setText("/check-email-token?token=" + newAccount.getEmailCheckToken() +
                "&email=" + newAccount.getEmail());

        // TODO 이메일 전송
        javaMailSender.send(mailMessage);
    }

    private Account saveNewAccount(SignUpForm signUpForm) {
        // TODO 회원가입 입력(객체 생성 후 Transient)
        // TODO Hibernate와 Jpa가 전혀 모르는 상태(DB 맵핑 레코드 없음)
        Account account = Account.builder()
                .email(signUpForm.getEmail())
                .nickname(signUpForm.getNickname())
                // TODO 패스워드 인코딩
                .password(passwordEncoder.encode(signUpForm.getPassword()))
                .studyCreatedByWeb(true)
                .studyUpdatedByWeb(true)
                .studyEnrollmentResultByWeb(true)
                .build();

        // TODO 회원정보 저장(Jpa 관리 중인 Persistent)
        Account newAccount = accountRepository.save(account);

        return newAccount;
    }
}
