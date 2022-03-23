package me.ssu.springjpaweb.accounts;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.validation.Valid;
import java.util.List;

@Service
@RequiredArgsConstructor
// TODO 트랜잭셔널 추가
@Transactional
public class AccountService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final JavaMailSender javaMailSender;
    // TODO 정석적인 방법
//    private final AuthenticationManager authenticationManager;

    // TODO Pricipal 정보(Userdetail)-1
    // TODO 로그인/로그아웃 처리와 트랜잭션 추가(데이터를 변경하는 게 아니라 로그인 시 체크로)-2
    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String emailOrNickname) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(emailOrNickname);

        // TODO 이메일이 없으면 닉네임 조회
        if (account == null) {
            account = accountRepository.findByNickname(emailOrNickname);
        }

        // TODO 그래도 회원정보가 Null이면
        if (account == null) {
            // TODo 이메일이나 패스워드가 잘못됐다고 처리할 거임.
            throw new UsernameNotFoundException(emailOrNickname);
        }

        // TODO Principal 정보를 넘겨주면 됨
        return new UserAccount(account);
    }

    // TODO 현재 인증된 사용자 정보 참조(private -> public)
    public void sendSignUpConfirmEmail(Account newAccount) {
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


    // TODO 회원가입 후 자동 로그인 메소드 구현-2
    public void login(Account account) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                // TODO Principal 객체사용(UserAccount)
                // account.getNickname(),
                new UserAccount(account),
                account.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(token);

        // TODO 원래 정석적인 코드
//        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
//            new UserAccount(account), account.getPassword()
//        );
//        Authentication authentication = authenticationManager.authenticate(token);
//        SecurityContext context = SecurityContextHolder.getContext();
//        context.setAuthentication(authentication);
    }

    // TODO 자동 로그인 void -> Account Return-1
    // TODO 메소드 당 트랜잭션 주는 게 번거롭기 때문에 Service에 위임.
//    @Transactional
    public Account processNewAccount(SignUpForm signUpForm) {
        // TODO 회원가입 처리 리팩토링
        Account newAccount = saveNewAccount(signUpForm);

        // TODO 이메일 토큰(Account에서 로직처리)
        newAccount.generateEmailCheckToken();

        // TODO 이메일 전송 리팩토링
        sendSignUpConfirmEmail(newAccount);

        return newAccount;
    }

    private Account saveNewAccount(@Valid SignUpForm signUpForm) {
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

    public void completeSignUp(Account account) {

        // TODO 토큰 값이 없는 경우(리팩토링 후 Account 로직 처리)-1
        // TODO 트랜잭셔널로 감싸기-2(AccountService에서 로직처리)
//        account.compleSignUp();
        account.compleSignUp();

        // TODO 자동 로그인-1
        // TODO 트랜잭셔널로 감싸기-2(AccountService에서 로직처리)
//        accountService.login(account);
        login(account);
    }
}
