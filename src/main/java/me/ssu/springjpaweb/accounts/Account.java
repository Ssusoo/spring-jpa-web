package me.ssu.springjpaweb.accounts;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Getter @NoArgsConstructor
@Builder @EqualsAndHashCode(of = "id")
@Entity @Setter
public class Account {

    @Id @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String nickname;

    private String password;

    private boolean emailVerified;

    private String emailCheckToken;

    private LocalDateTime emailCheckTokenGeneratedAt;

    private LocalDateTime joinedAt;

    private String bio;

    private String url;

    private String occupation;

    private String location;

    // TODO Lob, 사진을 저장하는 칼럼으로 사용할 경우
    // TODO Basic, null이 가능한지를 옵션으로 줄 수 있음.
    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String profileImage;

    private boolean studyCreatedByEmail;

    private boolean studyCreatedByWeb = true;

    private boolean studyEnrollmentResultByEmail;

    private boolean studyEnrollmentResultByWeb = true;

    private boolean studyUpdatedByEmail;

    private boolean studyUpdatedByWeb = true;

    // TODO 회원가입 폼 서브밋 처리(회원가입 처리)
    // TODO 이메일 토큰 메시지 만들기
    public void generateEmailCheckToken() {
        this.emailCheckToken = UUID.randomUUID().toString();
        // TODO 누락하면 Null로 빼먹지 말기
        this.emailCheckTokenGeneratedAt = LocalDateTime.now();
    }

    // TODO 토큰 값이 없는 경우(리팩토링 후 Account 로직 처리)
    // TODO 이 부분은 2개가 같이 이루어져야 한다.
    public void compleSignUp() {
        // TODO If문 통과 후 이메일 인증 확인
//        account.setEmailVerified(true);
        this.emailVerified = true;
        // TODO 가입일자
//        account.setJoinedAt(LocalDateTime.now());
        this.joinedAt = LocalDateTime.now();
    }

    // TODO 토큰값이 없는 경우 리팩토링
    public boolean isValidToken(String token) {
        return this.emailCheckToken.equals(token);
    }

    // TODO 현재 인증된 사용자 정보 참조
    // TODO 1시간 이전에 만들었으면 보낼 수 있는 유무
    public boolean canSendConfirmEmail() {
        return this.emailCheckTokenGeneratedAt.isBefore(LocalDateTime.now().minusHours(1));
    }
}
