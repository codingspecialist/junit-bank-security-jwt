package shop.mtcoding.bank.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import shop.mtcoding.bank.config.dummy.DummyObject;
import shop.mtcoding.bank.domain.user.User;
import shop.mtcoding.bank.domain.user.UserRepository;
import shop.mtcoding.bank.dto.user.UserReqDto.JoinReqDto;
import shop.mtcoding.bank.dto.user.UserReqDto.UserPasswordUpdateReqDto;
import shop.mtcoding.bank.dto.user.UserRespDto.JoinRespDto;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest extends DummyObject {
    private final Logger log = LoggerFactory.getLogger(getClass());
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Spy
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    public void 회원가입_test() throws Exception {
        // given
        JoinReqDto joinReqDto = new JoinReqDto();
        joinReqDto.setUsername("ssar");
        joinReqDto.setFullname("쌀");
        joinReqDto.setPassword("1234");
        joinReqDto.setEmail("ssar@nate.com");

        // stub 1
        when(userRepository.findByUsername(joinReqDto.getUsername())).thenReturn(Optional.empty());

        // stub 2
        User ssar = newMockUser(1L, "ssar", "쌀");
        when(userRepository.save(any())).thenReturn(ssar);

        // when
        JoinRespDto joinRespDto = userService.회원가입(joinReqDto);

        // then
        assertThat(joinRespDto.getId()).isEqualTo(1L);
        assertThat(joinRespDto.getUsername()).isEqualTo("ssar");
    }

    // Bcrpt는 salt값을 반영하여 암호화된다.
    @Test
    public void 패스워드변경_test() throws Exception {
        // given
        UserPasswordUpdateReqDto userPasswordUpdateReqDto = new UserPasswordUpdateReqDto();
        userPasswordUpdateReqDto.setCurrentPassword("1234");
        userPasswordUpdateReqDto.setNewPassword("5678");

        // stub
        User ssar = newMockUser(1L, "ssar", "쌀");
        when(userRepository.findById(1L)).thenReturn(Optional.of(ssar));

        // when
        userService.패스워드변경(userPasswordUpdateReqDto, 1L);

        // then
        assertTrue(passwordEncoder.matches("5678", ssar.getPassword()));
    }
}
