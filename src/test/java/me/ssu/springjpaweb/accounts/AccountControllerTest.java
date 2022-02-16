package me.ssu.springjpaweb.accounts;

import me.ssu.springjpaweb.common.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.mail.SimpleMailMessage;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AccountControllerTest extends BaseTest {

    // TODO 메소드 실행 전 디비 리셋
    @BeforeEach
    void setUp() {
        this.accountRepository.deleteAll();
    }

    // TODO 회원가입 인증 메일(실패)-1
    @Test
    @DisplayName("인증 메일 확인 - 입력값 오류")
    void checkEmailTokenWithWrongInput() throws Exception {
        mockMvc.perform(get("/check-email-token")
                                .param("token", "asdjfkasdjfkasdf")
                                .param("email", "ssu@mail.com")
                                .with(csrf()))
                .andDo(print())
                // TODO status(200)
                .andExpect(status().isOk())
                // TODO model
                .andExpect(model().attributeExists("error"))
                // TODO view
                .andExpect(view().name("accounts/checked-email"));
    }

    // TODO 회원가입 인증 메일(성공)-2
    @Test
    @DisplayName("인증 메일 확인 - 입력값 정상")
    void checkEmailToken() throws Exception {
        // TODO Account 객체
        Account account = Account.builder()
                .email("ssu@amail.com")
                .password("12345678")
                .nickname("ssu")
                .build();

        // TODO 회원가입 처리
        Account newAccount = accountRepository.save(account);
        // TODO 인증 메일 토큰
        newAccount.generateEmailCheckToken();

        mockMvc.perform(get("/check-email-token")
                    .param("token", newAccount.getEmailCheckToken())
                    .param("email", newAccount.getEmail()))
                .andDo(print())
                // TODO status(200)
                .andExpect(status().isOk())
                // TODO model
                .andExpect(model().attributeDoesNotExist("error"))
                .andExpect(model().attributeExists("nickname"))
                .andExpect(model().attributeExists("numberOfUser"))
                // TODO view
                .andExpect(view().name("accounts/checked-email"))
                // TODO 자동로그인
                .andExpect(authenticated().withAuthenticationName("ssu"));
    }

    // TODO 패스워드 인코딩
    @Test
    @DisplayName("패스워드 인코딩 - 평문 그대로 저장 X")
    void signUpSubmitPasswordEncoding() throws Exception {
        mockMvc.perform(post("/sign-up")
                                .param("nickname", "ssu")
                                .param("email", "ssu@email.com")
                                .param("password", "12345678")
                                .with(csrf()));
        // TODO 패스워드 인코딩
        Account account = accountRepository.findByEmail("ssu@email.com");
        // TODO nullcheck
        assertNotNull(account);
        // TODO 패스워드
        assertNotEquals(account.getPassword(), "12345678");
    }

    // TODO 이메일 토큰 처리하기
    @Test
    @DisplayName("토큰 값 확인하기")
    void signUpSubmitEmailToken() throws Exception {
        mockMvc.perform(post("/sign-up")
                                .param("nickname", "ssu")
                                .param("email", "ssu@email.com")
                                .param("password", "12345678")
                                .with(csrf()));
        // TODO 토큰값 확인하기
        Account account = accountRepository.findByEmail("ssu@email.com");
        // TODO nullcheck
        assertNotNull(account);
        // TODO 패스워드
        assertNotNull(account.getEmailCheckToken());
    }

    // TODO 회원가입 처리(실패)-1
    @Test
    @DisplayName("회원 가입 처리 - 입력값 오류")
    void signUpSubmitWithWrongInput() throws Exception {
        mockMvc.perform(post("/sign-up")
                                .param("nickname", "ssu")
                                .param("email", "ssus...com")
                                .param("password", "12345678")
                                // TODO Csrf Token
                                .with(csrf()))
                // TODO 입력값 제한하기(실패시 회원가입 페이지 다시 보여주기)
                .andExpect(status().isOk())
                // TODO view
                .andExpect(view().name("accounts/sign-up"));
    }

    // TODO 회원가입 처리(성공)-2
    // TODO 회원가입 이메일&닉네임 중복여부(웹 어플리케이션으로)-3
    @Test
    @DisplayName("회원 가입 처리 - 입력값 정상")
    void signUpSubmitWithCorrectInput() throws Exception {
        mockMvc.perform(post("/sign-up")
                                .param("nickname", "ssu")
                                .param("email", "ssu@email.com")
                                .param("password", "12345678")
                                // TODO Csrf Token
                                .with(csrf()))
                // TODO Redirect(301)
                .andExpect(status().is3xxRedirection())
                // TODO view
                .andExpect(view().name("redirect:/"));

        // TODO 이메일 전송 여부
        then(javaMailSender).should().send(ArgumentMatchers.any(SimpleMailMessage.class));

        // TODO 유저 조회
        assertTrue(accountRepository.existsByEmail("ssu@email.com"));
    }

    // TODO 회원가입 페이지가 보이는지
    @DisplayName("회원가입 페이지 테스트")
    @Test
    void signUpForm() throws Exception {
        mockMvc.perform(get("/sign-up"))
                .andDo(print())
                // TODO status(200)
                .andExpect(status().isOk())
                // TODO view
                .andExpect(view().name("accounts/sign-up"))
                // TODO model
                .andExpect(model().attributeExists("signUpForm"));
    }
}

