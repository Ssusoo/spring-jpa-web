package me.ssu.springjpaweb.modules.accounts.validators;

import lombok.RequiredArgsConstructor;
import me.ssu.springjpaweb.modules.accounts.forms.SignUpForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
// TODO 회원가입 커스텀 검증
public class SignUpFormValidator implements Validator {


    // TODO 인스턴스가 검증 대상 타입인지 확인
    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(SignUpForm.class);
    }

    // TODO 실질적인 검증 작업
    @Override
    public void validate(Object object, Errors fieldError) {
        SignUpForm signUpForm = (SignUpForm)object;


    }
}
