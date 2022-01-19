package me.ssu.springjpaweb.modules.accounts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface AccountRepository extends JpaRepository<Account, Long> {

        // TODO 이메일 중복 체크(커스텀 검증하기)
        boolean existsByEmail(String email);

        // TODO 닉네임 중복 체크(커스텀 검증하기)
        boolean existsNickname(String nickname);
}
