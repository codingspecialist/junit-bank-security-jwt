package shop.mtcoding.bank.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.bank.config.auth.LoginUser;
import shop.mtcoding.bank.dto.ResponseDto;
import shop.mtcoding.bank.service.AccountService;

@RequiredArgsConstructor
@RestController
public class AccountApiController {
    private final AccountService accountService;

    @DeleteMapping("/account/{accountId}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long accountId, @AuthenticationPrincipal LoginUser loginUser) {
        accountService.계좌삭제(accountId, loginUser.getUser().getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "계좌 삭제 완료", null), HttpStatus.OK);
    }
}
