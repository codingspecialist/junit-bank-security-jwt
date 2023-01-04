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
import shop.mtcoding.bank.domain.transaction.Transaction;
import shop.mtcoding.bank.domain.transaction.TransactionRepository;
import shop.mtcoding.bank.domain.user.User;
import shop.mtcoding.bank.domain.user.UserRepository;
import shop.mtcoding.bank.dto.account.AccountReqDto.AccountDepositReqDto;
import shop.mtcoding.bank.dto.account.AccountReqDto.AccountSaveReqDto;
import shop.mtcoding.bank.dto.account.AccountReqDto.AccountTransferReqDto;
import shop.mtcoding.bank.dto.account.AccountReqDto.AccountWithdrawReqDto;
import shop.mtcoding.bank.dto.account.AccountRespDto.AccountDepositRespDto;
import shop.mtcoding.bank.dto.account.AccountRespDto.AccountSaveRespDto;
import shop.mtcoding.bank.dto.account.AccountRespDto.AccountTransferRespDto;
import shop.mtcoding.bank.dto.account.AccountRespDto.AccountWithdrawRespDto;
import shop.mtcoding.bank.handler.ex.CustomApiException;

/*
        * 입금하기
        * insert, update, delete에서 하나의 stub은 다음 stub에 영향을 주면 안된다.
        * when에서 정의해둔 객체를 다른 곳에서 사용하게 되면 실행시점에 값이 변경될 수 있기 떄문이다.
*/
@ExtendWith(MockitoExtension.class)
public class AccountServiceTest extends DummyObject {
    private final Logger log = LoggerFactory.getLogger(getClass());
    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionRepository transactionRepository;

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
        Account ssarAccount = newMockAccount(1L, 1111L, 1000L, ssar);
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
        Account ssarAccount = newMockAccount(1L, 1111L, 1000L, ssar);
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
        Account ssarAccount = newMockAccount(1L, 1111L, 1000L, ssar);
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

    @Test
    public void 계좌입금_test() throws Exception {
        // given
        AccountDepositReqDto accountDepositReqDto = new AccountDepositReqDto();
        accountDepositReqDto.setNumber(1111L);
        accountDepositReqDto.setAmount(100L);
        accountDepositReqDto.setGubun("DEPOSIT");
        accountDepositReqDto.setTel("010-2222-6666");

        // stub 1
        User ssar = newMockUser(1L, "ssar", " 쌀");
        Account ssarAccountStub1 = newMockAccount(1L, 1111L, 1000L, ssar);
        when(accountRepository.findByNumber(any()))
                .thenReturn(Optional.of(ssarAccountStub1));

        // stub 2
        Account ssarAccountStub2 = newMockAccount(1L, 1111L, 1100L, ssar);

        Transaction transaction = newMockDepositTransaction(1L, ssarAccountStub2);
        when(transactionRepository.save(any())).thenReturn(transaction);

        // when
        AccountDepositRespDto accountDepositRespDto = accountService.계좌입금(accountDepositReqDto);
        String responseBody = om.writeValueAsString(accountDepositRespDto);
        log.debug("디버그 : " + responseBody);

        // then
        assertThat(ssarAccountStub1.getBalance()).isEqualTo(1100L);
        assertThat(accountDepositRespDto.getTransaction().getDepositAccountBalance()).isEqualTo(1100L);
    }

    @Test
    public void 계좌출금_test() throws Exception {
        // given
        Long userId = 1L;
        AccountWithdrawReqDto accountWithdrawReqDto = new AccountWithdrawReqDto();
        accountWithdrawReqDto.setNumber(1111L);
        accountWithdrawReqDto.setPassword(1234L);
        accountWithdrawReqDto.setAmount(100L);
        accountWithdrawReqDto.setGubun("DEPOSIT");

        // stub 1
        User ssar = newMockUser(1L, "ssar", " 쌀");
        Account ssarAccountStub1 = newMockAccount(1L, 1111L, 1000L, ssar);
        when(accountRepository.findByNumber(any()))
                .thenReturn(Optional.of(ssarAccountStub1));

        // stub 2
        Account ssarAccountStub2 = newMockAccount(1L, 1111L, 900L, ssar);
        Transaction transaction = newMockWithdrawTransaction(1L, ssarAccountStub2);
        when(transactionRepository.save(any())).thenReturn(transaction);

        // when
        AccountWithdrawRespDto accountWithdrawRespDto = accountService.계좌출금(accountWithdrawReqDto, userId);
        String responseBody = om.writeValueAsString(accountWithdrawRespDto);
        log.debug("디버그 : " + responseBody);

        // then
        assertThat(ssarAccountStub1.getBalance()).isEqualTo(900L);
        assertThat(accountWithdrawRespDto.getTransaction().getWithdrawAccountBalance()).isEqualTo(900L);
    }

    @Test
    public void 계좌이체_test() throws Exception {
        // given
        Long userId = 1L;
        AccountTransferReqDto accountTransferReqDto = new AccountTransferReqDto();
        accountTransferReqDto.setWithdrawNumber(1111L);
        accountTransferReqDto.setDepositNumber(2222L);
        accountTransferReqDto.setWithdrawPassword(1234L);
        accountTransferReqDto.setAmount(100L);
        accountTransferReqDto.setGubun("TRANSFER");

        // stub 1
        User ssar = newMockUser(1L, "ssar", " 쌀");
        Account ssarAccountStub1 = newMockAccount(1L, 1111L, 1000L, ssar);
        when(accountRepository.findByNumber(accountTransferReqDto.getWithdrawNumber()))
                .thenReturn(Optional.of(ssarAccountStub1));

        User cos = newMockUser(2L, "cos", "코스");
        Account cosAccountStub1 = newMockAccount(2L, 2222L, 1000L, cos);
        when(accountRepository.findByNumber(accountTransferReqDto.getDepositNumber()))
                .thenReturn(Optional.of(cosAccountStub1));

        // stub 2
        Account ssarAccountStub2 = newMockAccount(1L, 1111L, 900L, ssar);
        Account cosAccountStub2 = newMockAccount(2L, 2222L, 1100L, cos);
        Transaction transaction = newMockTransferTransaction(1L, ssarAccountStub2, cosAccountStub2);
        when(transactionRepository.save(any())).thenReturn(transaction);

        // when
        AccountTransferRespDto accountTransferRespDto = accountService.계좌이체(accountTransferReqDto, userId);
        String responseBody = om.writeValueAsString(accountTransferRespDto);
        log.debug("디버그 : " + responseBody);

        // then
        assertThat(ssarAccountStub1.getBalance()).isEqualTo(900L);
        assertThat(cosAccountStub1.getBalance()).isEqualTo(1100L);
        assertThat(accountTransferRespDto.getTransaction().getWithdrawAccountBalance()).isEqualTo(900L);
        assertThat(accountTransferRespDto.getTransaction().getDepositAccountBalance()).isEqualTo(1100L);
    }

}
