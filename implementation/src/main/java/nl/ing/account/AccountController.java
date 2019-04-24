package nl.ing.account;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class AccountController {

    @PostMapping(value = "/createAccount")
    public void createAccount(@RequestParam Account account,
                              Principal principal) {

        ((Authentication) principal).getPrincipal();
    }
}
