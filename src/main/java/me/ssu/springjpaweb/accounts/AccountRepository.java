package me.ssu.springjpaweb.accounts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface AccountRepository extends JpaRepository<Account, Long> {

    // TODO 회원가입 폼 커스텀 검증(이메일 중복 체크)
    boolean existsByEmail(String email);

    // TODO 회원가입 폼 커스텀 검증(닉네임 중복 체크)
    boolean existsNickname(String nickname);
}
