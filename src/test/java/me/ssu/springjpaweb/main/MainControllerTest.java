package me.ssu.springjpaweb.main;

import me.ssu.springjpaweb.accounts.SignUpForm;
import me.ssu.springjpaweb.common.BaseTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MainControllerTest extends BaseTest {

    // TODO 이메일과 닉네임 로그인 처리시 중복 코드 발생
    // TODO BeforeEach처리하기(모든 테스트가 실행될 때마다 한 번씩 실행됨)
    @BeforeEach
    void beforeEach() {
        // TODO 회원정보 입력
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setNickname("ssu");
        signUpForm.setEmail("ssu@mail.com");
        signUpForm.setPassword("12345678");

        // TODO 회원정보 저장
        accountService.processNewAccount(signUpForm);
    }

    // TODO 닉네임이랑 이메일 리셋 처리
    @AfterEach
    void afterEach() {
        accountRepository.deleteAll();
    }

    @DisplayName("이메일 로그인-성공")
    @Test
    void loginWithEmail() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", "ssu@mail.com")
                        .param("password", "12345678")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                // TODO UserAccount에서 nickname으로 했기 때문에
                .andExpect(authenticated().withUsername("ssu"));
    }

    // TODO 닉네임으로 로그인 성공
    @DisplayName("닉네임으로 로그인-성공")
    @Test
    void loginWithNickname() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", "ssu")
                        .param("password", "12345678")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                // TODO UserAccount에서 nickname으로 했기 때문에
                .andExpect(authenticated().withUsername("ssu"));
    }
    // TODO 로그인 실패
    @DisplayName("로그인-실패")
    @Test
    void loginFail() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", "1111111111")
                        .param("password", "00000000000")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                // TODO login?error라는 redirection
                .andExpect(redirectedUrl("/login?error"))
                // TODO 인증이 안되기 때문에
                .andExpect(unauthenticated());
    }
    // TODO 로그아웃
    @DisplayName("로그아웃")
    @Test
    void logout() throws Exception {
        mockMvc.perform(post("/logout")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                // TODO logout은 home으로 세팅했기 때문에 "/"
                .andExpect(redirectedUrl("/"))
                // TODO 인증이 안되기 때문에
                .andExpect(unauthenticated());
    }
}