package me.ssu.springjpaweb.accounts;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
// TODO 회원가입 폼 서브밋 검증(JSR 303)
public class SignUpForm {

    @NotBlank
    @Length(min = 3, max =20)
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9_-]{3,20}$")
    private String nickname;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Length(min =8, max = 50)
    private String password;
}
