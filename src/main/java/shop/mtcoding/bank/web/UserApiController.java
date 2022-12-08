package shop.mtcoding.bank.web;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.bank.dto.ResponseDto;
import shop.mtcoding.bank.dto.user.UserReqDto.UserJoinReqDto;
import shop.mtcoding.bank.dto.user.UserRespDto.UserJoinRespDto;
import shop.mtcoding.bank.service.UserService;

@RequiredArgsConstructor
@RestController
public class UserApiController {
    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody @Valid UserJoinReqDto userJoinReqDto, BindingResult bindingResult) {
        UserJoinRespDto userJoinRespDto = userService.회원가입(userJoinReqDto);
        return new ResponseEntity<>(new ResponseDto<>("회원가입 성공", userJoinRespDto), HttpStatus.CREATED);
    }
}
