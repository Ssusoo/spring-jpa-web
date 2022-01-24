//package me.ssu.springjpaweb.accounts;
//
//import me.ssu.springjpaweb.common.BaseTest;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentMatchers;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//
//import static junit.framework.TestCase.assertTrue;
//import static org.assertj.core.api.BDDAssertions.then;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//class AccountControllerTest extends BaseTest {
//
//    @MockBean
//    JavaMailSender javaMailSender;
//
//    @Test
//    @DisplayName("회원가입 페이지 테스트")
//    void signUpForm() throws Exception {
//        mockMvc.perform(get("/sign-up"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(view().name("accounts/sign-up"))
//                .andExpect(model().attributeExists("signUpForm"))
//        ;
//    }
//
//    // TODO 회원가입 처리(성공)-1
//    @Test
//    @DisplayName("회원 가입 처리 - 입력값 정상")
//    void signUpSubmit_with_correct_input() throws Exception {
//        mockMvc.perform(post("/sign-up")
//                        .param("nickname", "ssu")
//                        .param("email", "ssu@email.com")
//                        .param("password", "12345678")
//                        // TODO csrf Token
//                        .with(csrf()))
//                // TODO Redirect
//                .andExpect(status().is3xxRedirection())
//                .andExpect(view().name("redirect:/"));
//
//        // TODO 이메일 전송 여부
////        then(javaMailSender)
//////                .send(ArgumentMatchers.any(SimpleMailMessage.class));
//        // TODO 유저 조회
//        assertTrue(accountRepository.existsByEmail("ssu@email.com"));
//    }
//
//    // TODO 회원가입 처리(실패)-2
//    @Test
//    @DisplayName("회원 가입 처리 - 입력값 오류")
//    void signUpSubmit_with_wrong_input() throws Exception {
//        mockMvc.perform(post("/sign-up")
//                        .param("nickname", "ssu")
//                        .param("email", "ssus...com")
//                        .param("password", "12345678")
//                        // TODO csrf Token
//                        .with(csrf()))
//                // TODO Redirect
//                .andExpect(status().isOk())
//                .andExpect(view().name("accounts/sign-up"));
//    }
//}