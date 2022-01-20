package me.ssu.springjpaweb.modules.accounts;

import me.ssu.springjpaweb.common.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class AccountControllerTest extends BaseTest {

    @Test
    @DisplayName("회원 가입 페이지 테스트")
    void signUp() throws Exception {
        mockMvc.perform(get("/sign-up"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("accounts/sign-up"))
        ;
    }
    // TODO 회원가입 성공-1(회원 처리)
    @Test
    @DisplayName("회원가입 처리- 성공")
    void signUpSubmit_account_success () throws Exception {
        mockMvc.perform(post("/sign-up")
                        .param("nickname", "ssu")
                        .param("email", "ssu@email.com")
                        .param("password", "1234")
                        .with(csrf()))
                .andDo(print())
                .andExpect(view().name("accounts/sign-up"))
        ;


    }
    // TODO 회원가입 성공-2(이메일 인증)
    @Test
    @DisplayName("회원가입 처리 - 인증 메일 발송 성공")
    void signUpSubmit_email_success() throws Exception {
        mockMvc.perform(post("/sign-up"))

        ;
    }
    // TODO 회원가입 실패-3
}