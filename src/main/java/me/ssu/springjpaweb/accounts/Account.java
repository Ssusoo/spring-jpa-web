package me.ssu.springjpaweb.accounts;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity @EqualsAndHashCode(of = "id")
@NoArgsConstructor @AllArgsConstructor
@Getter @Builder
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

    private String location;

    // TODO Lob, 사진을 저장하는 칼럼으로 사용할 경우
    // TODO Basic, null이 가능한지를 옵션으로 줄 수 있음.
    @Lob @Basic(fetch = FetchType.EAGER)
    private String profileImage;

    private boolean studyCreatedByEmail;

    private boolean studyCreatedByWeb = true;

    private boolean studyUpdatedByEmail;

    private boolean studyUpdatedByWeb = true;

    private boolean studyEnrollmentResultByEmail;

    private boolean studyEnrollmentResultByWeb = true;
}
