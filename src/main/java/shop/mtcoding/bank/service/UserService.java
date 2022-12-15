package shop.mtcoding.bank.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.bank.domain.user.User;
import shop.mtcoding.bank.domain.user.UserRepository;
import shop.mtcoding.bank.dto.user.UserReqDto.UserJoinReqDto;
import shop.mtcoding.bank.dto.user.UserReqDto.UserPasswordUpdateReqDto;
import shop.mtcoding.bank.dto.user.UserRespDto.UserJoinRespDto;
import shop.mtcoding.bank.handler.ex.CustomApiException;

/*
 * @Transactional 어노테이션이 없으면
 * DB에 요청 후 응답 받으면 db session 사라짐(OSIV=false 일 경우)
 * 서비스 레이어에서 Lazy Loading을 할 수 없게됨.
 */
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public UserJoinRespDto 회원가입(UserJoinReqDto userJoinReqDto) {
        // 1. 동일 유저네임 존재 검사
        Optional<User> userOP = userRepository.findByUsername(userJoinReqDto.getUsername());
        if (userOP.isPresent()) {
            throw new CustomApiException("동일한 username이 존재합니다");
        }

        // 2. 패스워드 인코딩 + DB 저장
        User userPS = userRepository.save(userJoinReqDto.toEntity(passwordEncoder));

        // dto response
        return new UserJoinRespDto(userPS);
    }

    @Transactional
    public void 패스워드변경(UserPasswordUpdateReqDto userPasswordUpdateReqDto, Long userId) {
        // 1. 유저존재 확인
        User userPS = userRepository.findById(userId).orElseThrow(
                () -> new CustomApiException("유저가 존재하지 않습니다"));

        // 2. 패스워드 변경
        userPS.updatePassword(userPasswordUpdateReqDto.getCurrentPassword(), userPasswordUpdateReqDto.getNewPassword(),
                passwordEncoder);
    }

}
