package me.ssu.springjpaweb.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.ssu.springjpaweb.accounts.AccountRepository;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@Disabled
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class BaseTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected AccountRepository accountRepository;g

    // TODO
    @MockBean
    protected JavaMailSender javaMailSender;
}
