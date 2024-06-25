package ua.kiev.prog.oauth2.loginviagoogle.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.kiev.prog.oauth2.loginviagoogle.dto.PageCountDTO;
import ua.kiev.prog.oauth2.loginviagoogle.model.Account;
import ua.kiev.prog.oauth2.loginviagoogle.services.AuthService;
import ua.kiev.prog.oauth2.loginviagoogle.services.GeneralService;


@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {
    private static final int PAGE_SIZE = 5;

    private final GeneralService generalService;
    private final AuthService authService;


    @GetMapping
    public ResponseEntity<Account> getAccount(@AuthenticationPrincipal Object principal) {
        String email = authService.getEmailFromPrincipal(principal);
        Account account = generalService.getAccountByEmail(email);
        return ResponseEntity.ok(account);
    }

    @GetMapping("/count")
    public ResponseEntity<PageCountDTO> count(@AuthenticationPrincipal Object principal) {
        String email = authService.getEmailFromPrincipal(principal);
        boolean isAdmin = authService.isAdmin();
        PageCountDTO countDTO = isAdmin
                ? PageCountDTO.of(generalService.countAllTasks(), PAGE_SIZE)
                : PageCountDTO.of(generalService.count(email), PAGE_SIZE);
        return ResponseEntity.ok(countDTO);
    }

}