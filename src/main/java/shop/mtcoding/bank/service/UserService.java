package shop.mtcoding.bank.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.bank.domain.user.User;
import shop.mtcoding.bank.domain.user.UserRepository;
import shop.mtcoding.bank.dto.user.UserReqDto.UserJoinReqDto;
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
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public UserJoinRespDto 회원가입(UserJoinReqDto userJoinReqDto) {
        // ready
        String rawPassword = userJoinReqDto.getPassword();
        String encPassword = passwordEncoder.encode(rawPassword);
        userJoinReqDto.setPassword(encPassword);

        // 동일 유저 검사
        boolean isSameUser = userRepository.findByUsername(userJoinReqDto.getUsername()).isPresent();

        if (isSameUser) {
            throw new CustomApiException("동일한 username이 존재합니다");
        }

        // action
        User userPS = userRepository.save(userJoinReqDto.toEntity());

        // dto response
        return new UserJoinRespDto(userPS);
    }
}
