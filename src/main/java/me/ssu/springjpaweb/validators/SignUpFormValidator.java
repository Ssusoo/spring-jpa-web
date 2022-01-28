package me.ssu.springjpaweb.validators;

import lombok.RequiredArgsConstructor;
import me.ssu.springjpaweb.accounts.AccountRepository;
import me.ssu.springjpaweb.accounts.SignUpForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
// TODO 회원가입 폼 커스텀(중복 검증)
public class SignUpFormValidator implements Validator {

    private final AccountRepository accountRepository;

    // TODO 인스턴스가 검증 대상 타입인지 확인
    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(SignUpForm.class);
    }

    // TODO 실질적인 검증 작업
    @Override
    public void validate(Object object, Errors fieldErrors) {

        // TODO 객체 생성
        SignUpForm signUpForm = (SignUpForm)object;

        // TODO 이메일 중복 체크
        if (accountRepository.existsByEmail(signUpForm.getEmail())) {
            // TODO Field Error(rejectValue)
            fieldErrors.rejectValue("email", "invalid.email",
                    new Object[]{signUpForm.getEmail()}, "이미 사용중인 이메일입니다.");
        }

        // TODO 닉네임 중복 체크
        if (accountRepository.existsByNickname(signUpForm.getNickname())) {
            // TODO Field Error(rejectValue)
            fieldErrors.rejectValue("nickname", "invalid.nickname",
                    new Object[]{signUpForm.getNickname()}, "이미 사용중인 닉네임입니다.");
        }
    }
}
