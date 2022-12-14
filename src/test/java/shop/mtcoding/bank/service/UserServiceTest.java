package shop.mtcoding.bank.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import shop.mtcoding.bank.config.dummy.DummyObject;
import shop.mtcoding.bank.domain.user.User;
import shop.mtcoding.bank.domain.user.UserRepository;
import shop.mtcoding.bank.dto.user.UserReqDto.UserJoinReqDto;
import shop.mtcoding.bank.dto.user.UserRespDto.UserJoinRespDto;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest extends DummyObject {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Spy
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    public void 회원가입_test() throws Exception {
        // given
        UserJoinReqDto userJoinReqDto = new UserJoinReqDto();
        userJoinReqDto.setUsername("ssar");
        userJoinReqDto.setFullname("쌀");
        userJoinReqDto.setPassword("1234");
        userJoinReqDto.setEmail("ssar@nate.com");

        // stub 1
        when(userRepository.findByUsername(userJoinReqDto.getUsername())).thenReturn(Optional.empty());

        // stub 2
        User ssar = newMockUser(1L, "ssar", "쌀");
        when(userRepository.save(any())).thenReturn(ssar);

        // when
        UserJoinRespDto userJoinRespDto = userService.회원가입(userJoinReqDto);

        // then
        assertThat(userJoinRespDto.getId()).isEqualTo(1L);
        assertThat(userJoinRespDto.getUsername()).isEqualTo("ssar");
    }
}
