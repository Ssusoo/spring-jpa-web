package me.ssu.springjpaweb.accounts;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Getter
public class UserAccount extends User {

    /* TODO account의 이름이 달라지면
        CurrentUser에서 null : account(여기 이름역시 달라져야 함.)
    */
    private Account account;

    public UserAccount(Account account) {
        // TODO Super 설정 후
        super(account.getNickname(), account.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
        // TODO Account도 설정하기
        // TODO 설정하지 않으면 Account = null 값으로 입력 됨.
        this.account = account;
    }
}
