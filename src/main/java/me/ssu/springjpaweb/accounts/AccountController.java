package me.ssu.springjpaweb.accounts;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccountController {

    @GetMapping("/sign-up")
    public String signUp() {
        return "accounts/sign-up";
    }
}
