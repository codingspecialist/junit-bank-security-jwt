package shop.mtcoding.bank.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
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
import shop.mtcoding.bank.domain.transaction.TransactionQueryRepository;
import shop.mtcoding.bank.domain.transaction.TransactionRepository;
import shop.mtcoding.bank.domain.user.User;
import shop.mtcoding.bank.domain.user.UserRepository;
import shop.mtcoding.bank.dto.transaction.TransactionRespDto.TransactionListRespDto;

/*
        * 입금하기
        * insert, update, delete에서 하나의 stub은 다음 stub에 영향을 주면 안된다.
        * when에서 정의해둔 객체를 다른 곳에서 사용하게 되면 실행시점에 값이 변경될 수 있기 떄문이다.
*/
@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest extends DummyObject {
        private final Logger log = LoggerFactory.getLogger(getClass());
        @InjectMocks
        private TransactionService transactionService;

        @Mock
        private AccountRepository accountRepository;

        @Mock
        private UserRepository userRepository;

        @Mock
        private TransactionRepository transactionRepository;

        @Mock
        private TransactionQueryRepository transactionQueryRepository;

        @Spy
        private ObjectMapper om;

        // 입출금목록보기
        @Test
        public void 입출금목록보기_test() throws Exception {
                // given
                Long userId = 1L;
                Long accountNumber = 1111L;
                String gubun = "ALL";
                Integer page = 0;

                // stub
                User ssar = newMockUser(1L, "ssar", "쌀");
                User cos = newMockUser(2L, "cos", "코스,");
                User love = newMockUser(3L, "love", "러브");
                Account ssarAccount = newMockAccount(1L, 1111L, 1000L, ssar);
                Account cosAccount = newMockAccount(2L, 2222L, 1000L, cos);
                Account loveAccount = newMockAccount(3L, 3333L, 1000L, love);
                Transaction withdrawTransaction1 = newMockWithdrawTransaction(1L, ssarAccount);
                Transaction depositTransaction1 = newMockDepositTransaction(2L, ssarAccount);
                Transaction transferTransaction1 = newMockTransferTransaction(3L, ssarAccount, cosAccount);
                Transaction transferTransaction2 = newMockTransferTransaction(4L, ssarAccount, loveAccount);
                Transaction transferTransaction3 = newMockTransferTransaction(5L, cosAccount, ssarAccount);
                List<Transaction> transactions = Arrays.asList(withdrawTransaction1, depositTransaction1,
                                transferTransaction1,
                                transferTransaction2, transferTransaction3);

                when(accountRepository.findByNumber(any())).thenReturn((Optional.of(ssarAccount)));
                when(transactionQueryRepository.findByAccountId(any(), any(), any())).thenReturn(transactions);

                // when
                TransactionListRespDto transactionListRespDto = transactionService.입출금목록보기(userId, accountNumber, gubun,
                                page);
                String body = om.writeValueAsString(transactionListRespDto);
                log.debug("디버그 : " + body);

                // then
                // assertThat(transactionListRespDto.getTransactions().size()).isEqualTo(2);
                // assertThat(transactionListRespDto.getTransactions().get(0).getBalance()).isEqualTo(800L);
        }

}
