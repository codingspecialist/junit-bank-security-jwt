package shop.mtcoding.bank.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import shop.mtcoding.bank.config.dummy.DummyObject;
import shop.mtcoding.bank.domain.account.Account;
import shop.mtcoding.bank.domain.account.AccountRepository;
import shop.mtcoding.bank.domain.user.User;
import shop.mtcoding.bank.domain.user.UserRepository;
import shop.mtcoding.bank.dto.account.AccountReqDto.AccountSaveReqDto;
import shop.mtcoding.bank.dto.account.AccountRespDto.AccountSaveRespDto;
import shop.mtcoding.bank.handler.ex.CustomApiException;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest extends DummyObject {
    private final Logger log = LoggerFactory.getLogger(getClass());
    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @Spy
    private ObjectMapper om;

    // DTO 테스트
    @Test
    public void 계좌등록_test() throws Exception {
        // given
        AccountSaveReqDto accountSaveReqDto = new AccountSaveReqDto();
        accountSaveReqDto.setNumber(1111L);
        accountSaveReqDto.setPassword(1234L);

        // stub 1
        User ssar = newMockUser(1L, "ssar", "쌀");
        when(userRepository.findById(ssar.getId())).thenReturn(Optional.of(ssar));

        // stub 2
        Account ssarAccount = newMockAccount(1L, 1111L, ssar);
        when(accountRepository.save(any())).thenReturn(ssarAccount);

        // when
        AccountSaveRespDto accountSaveRespDto = accountService.계좌등록(accountSaveReqDto, ssar.getId());
        String responseBody = om.writeValueAsString(accountSaveRespDto);
        log.debug("테스트 : " + responseBody);

        // then
        assertThat(accountSaveRespDto.getNumber()).isEqualTo(1111L);
    }

    // 계좌 소유자 확인 테스트
    @Test
    public void 계좌삭제_test1() throws Exception {
        // given
        Long accountNumber = 1111L;
        Long userId = 2L;

        // stub
        User ssar = newMockUser(1L, "ssar", "쌀");
        Account ssarAccount = newMockAccount(1L, 1111L, ssar);
        when(accountRepository.findByNumber(accountNumber)).thenReturn(Optional.of(ssarAccount));

        // when then
        assertThrows(CustomApiException.class, () -> accountService.계좌삭제(accountNumber, userId));
    }

    // 계좌 소유자 확인 테스트
    @Test
    public void 계좌삭제_test2() throws Exception {
        // given
        Long accountNumber = 1111L;
        Long userId = 2L;

        // stub
        User ssar = newMockUser(1L, "ssar", "쌀");
        Account ssarAccount = newMockAccount(1L, 1111L, ssar);
        when(accountRepository.findByNumber(accountNumber)).thenReturn(Optional.of(ssarAccount));

        // when
        try {
            accountService.계좌삭제(accountNumber, userId);
        } catch (Exception e) {
            return;
        }

        // then
        fail("예외 발생 안함");
    }

}
