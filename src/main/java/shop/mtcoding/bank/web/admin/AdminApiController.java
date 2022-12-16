package shop.mtcoding.bank.web.admin;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.bank.config.auth.LoginUser;
import shop.mtcoding.bank.dto.ResponseDto;
import shop.mtcoding.bank.dto.user.UserReqDto.UserPasswordUpdateReqDto;
import shop.mtcoding.bank.service.AccountService;
import shop.mtcoding.bank.service.UserService;

@RequiredArgsConstructor
@RestController
public class AdminApiController {
    private final UserService userService;
    private final AccountService accountService;

    @PutMapping("/admin/user/{userId}/password")
    public ResponseEntity<?> updatePassword(
            @PathVariable Long userId,
            @RequestBody @Valid UserPasswordUpdateReqDto userPasswordUpdateReqDto,
            BindingResult bindingResult) {

        userService.패스워드변경(userPasswordUpdateReqDto, userId);
        return new ResponseEntity<>(new ResponseDto<>(99, "관리자 유저 패스워드 변경 완료", null),
                HttpStatus.OK);
    }

    @DeleteMapping("/admin/user/{userId}/account/{accountNumber}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long userId, @PathVariable Long accountNumber) {
        accountService.계좌삭제(accountNumber, userId);
        return new ResponseEntity<>(new ResponseDto<>(1, "계좌 삭제 완료", null), HttpStatus.OK);
    }
}
