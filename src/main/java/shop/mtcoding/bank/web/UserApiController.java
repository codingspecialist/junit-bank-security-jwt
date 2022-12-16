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
import shop.mtcoding.bank.dto.ResponseDto;
import shop.mtcoding.bank.dto.user.UserReqDto.UserJoinReqDto;
import shop.mtcoding.bank.dto.user.UserReqDto.UserPasswordUpdateReqDto;
import shop.mtcoding.bank.dto.user.UserRespDto.UserJoinRespDto;
import shop.mtcoding.bank.handler.ex.CustomApiException;
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

    // 어드민 컨트롤러나 어드민 서버가 있는 경우
    @PutMapping("/user/{userId}/password")
    public ResponseEntity<?> updatePassword(
            @PathVariable Long userId,
            @RequestBody @Valid UserPasswordUpdateReqDto userPasswordUpdateReqDto,
            BindingResult bindingResult,
            @AuthenticationPrincipal LoginUser loginUser) {

        if (userId != loginUser.getUser().getId()) {
            throw new CustomForbiddenException("권한이 없습니다");
        }

        userService.패스워드변경(userPasswordUpdateReqDto, loginUser.getUser().getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "유저 패스워드 변경 완료", null), HttpStatus.OK);
    }

    // 어드민 컨트롤러나 어드민 서버가 따로 없는 경우
    // 주소 파라미터를 서비스에 전달하면 검증이 꼭 필요하다.
    // 세션으로 검증할 수 있는 것은 Controller에서 그게 안되면 Service에서 검증
    // @PutMapping("/user/{userId}/password")
    // public ResponseEntity<?> updatePassword(
    // @PathVariable Long userId,
    // @RequestBody @Valid UserPasswordUpdateReqDto userPasswordUpdateReqDto,
    // BindingResult bindingResult,
    // @AuthenticationPrincipal LoginUser loginUser) {
    // if (userId != loginUser.getUser().getId()) {
    // if (loginUser.getUser().getRole() != UserEnum.ADMIN) {
    // throw new CustomForbiddenException("해당 API 권한이 없습니다");
    // }
    // }

    // userService.패스워드변경(userPasswordUpdateReqDto, userId);
    // return new ResponseEntity<>(new ResponseDto<>(1, "유저 패스워드 변경 완료", null),
    // HttpStatus.OK);
    // }
}
