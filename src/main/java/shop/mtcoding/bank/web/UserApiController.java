package shop.mtcoding.bank.web;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.bank.config.auth.LoginUser;
import shop.mtcoding.bank.domain.user.UserEnum;
import shop.mtcoding.bank.dto.ResponseDto;
import shop.mtcoding.bank.dto.user.UserReqDto.UserJoinReqDto;
import shop.mtcoding.bank.dto.user.UserReqDto.UserPasswordUpdateReqDto;
import shop.mtcoding.bank.dto.user.UserRespDto.UserJoinRespDto;
import shop.mtcoding.bank.handler.ex.CustomForbiddenException;
import shop.mtcoding.bank.service.UserService;

@RequiredArgsConstructor
@RestController
public class UserApiController {
    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody @Valid UserJoinReqDto userJoinReqDto, BindingResult bindingResult) {
        UserJoinRespDto userJoinRespDto = userService.회원가입(userJoinReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "회원가입 성공", userJoinRespDto), HttpStatus.CREATED);
    }

    // LoginUser로 패스워드 변경을 하게 되면, ADMIN은 절대 패스워드 변경을 할 수 없다.
    // 변경할 파라메터를 받고, 유저,어드민의 권한을 체크하자.
    // 어드민은 서버를 따로 만드는 것이 좋다. 그래야 이렇게 userId를 매번 받아서 체크하는 로직이 사라진다.
    // 그래도 공부겸 하나 추가함.
    @PutMapping("/user/{userId}/password")
    public ResponseEntity<?> updatePassword(
            @PathVariable Long userId,
            @RequestBody @Valid UserPasswordUpdateReqDto userPasswordUpdateReqDto,
            BindingResult bindingResult,
            @AuthenticationPrincipal LoginUser loginUser) {
        if (userId != loginUser.getUser().getId()) {
            if (loginUser.getUser().getRole() != UserEnum.ADMIN) {
                throw new CustomForbiddenException("해당 API 권한이 없습니다");
            }
        }

        userService.패스워드변경(userPasswordUpdateReqDto, userId);
        return new ResponseEntity<>(new ResponseDto<>(1, "유저 패스워드 변경 완료", null), HttpStatus.OK);
    }
}
