package shop.mtcoding.bank.domain.transaction;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import shop.mtcoding.bank.config.dummy.DummyObject;
import shop.mtcoding.bank.domain.account.Account;
import shop.mtcoding.bank.domain.account.AccountRepository;
import shop.mtcoding.bank.domain.user.User;
import shop.mtcoding.bank.domain.user.UserRepository;

@DataJpaTest
public class TransactionRepositoryTest extends DummyObject {
        private final Logger log = LoggerFactory.getLogger(getClass());

        @Autowired
        private TransactionRepository transactionRepository;
        @Autowired
        private UserRepository userRepository;
        @Autowired
        private AccountRepository accountRepository;
        @Autowired
        private EntityManager em;

        @BeforeEach
        public void setUp() {
                autoincrementReset();
                dataSetting();
        }

        @Test
        public void findTransactionList_deposit_test() throws Exception {
                // given
                Long userId = 1L;

                // when
                List<Transaction> transactionListPS = transactionRepository.findTransactionList(userId, "DEPOSIT", 0);
                log.debug("디버그 : size : " + transactionListPS.size());
                log.debug("디버그 : " + transactionListPS.get(1).getSender());
                log.debug("디버그 : " + transactionListPS.get(1).getReciver());
                log.debug("디버그 : " + transactionListPS.get(1).getDepositAccountBalance());

                // then
                assertThat(transactionListPS.get(1).getDepositAccountBalance()).isEqualTo(900L);
        }

        @Test
        public void findTransactionList_withdraw_test() throws Exception {
                // given
                Long userId = 1L;

                // when
                List<Transaction> transactionListPS = transactionRepository.findTransactionList(userId, "WITHDRAW", 0);
                log.debug("디버그 : size : " + transactionListPS.size());
                log.debug("디버그 : " + transactionListPS.get(2).getSender());
                log.debug("디버그 : " + transactionListPS.get(2).getReciver());
                log.debug("디버그 : " + transactionListPS.get(2).getWithdrawAccountBalance());

                // then
                assertThat(transactionListPS.get(2).getWithdrawAccountBalance()).isEqualTo(800L);
        }

        @Test
        public void findTransactionList_all_test() throws Exception {
                // given
                Long userId = 1L;

                // when
                List<Transaction> transactionListPS = transactionRepository.findTransactionList(userId, "ALL", 0);
                log.debug("디버그 : size : " + transactionListPS.size());
                log.debug("디버그 : " + transactionListPS.get(4).getSender());
                log.debug("디버그 : " + transactionListPS.get(4).getReciver());
                log.debug("디버그 : " + transactionListPS.get(4).getWithdrawAccountBalance());
                log.debug("디버그 : " + transactionListPS.get(4).getDepositAccountBalance());

                // then
                assertThat(transactionListPS.get(4).getDepositAccountBalance()).isEqualTo(900L);
        }

        private void dataSetting() {
                User ssar = userRepository.save(newUser("ssar", "쌀"));
                User cos = userRepository.save(newUser("cos", "코스,"));
                User love = userRepository.save(newUser("love", "러브"));
                User admin = userRepository.save(newUser("admin", "관리자"));
                Account ssarAccount1 = accountRepository.save(newAccount(1111L, ssar));
                Account cosAccount = accountRepository.save(newAccount(2222L, cos));
                Account loveAccount = accountRepository.save(newAccount(3333L, love));
                Account ssarAccount2 = accountRepository.save(newAccount(4444L, ssar));
                Transaction withdrawTransaction1 = transactionRepository
                                .save(newWithdrawTransaction(ssarAccount1, accountRepository));
                Transaction depositTransaction1 = transactionRepository
                                .save(newDepositTransaction(ssarAccount1, accountRepository));
                Transaction transferTransaction1 = transactionRepository
                                .save(newTransferTransaction(ssarAccount1, cosAccount, accountRepository));
                Transaction transferTransaction2 = transactionRepository
                                .save(newTransferTransaction(ssarAccount1, loveAccount, accountRepository));
                Transaction transferTransaction3 = transactionRepository
                                .save(newTransferTransaction(cosAccount, ssarAccount1, accountRepository));
        }

        private void autoincrementReset() {
                this.em
                                .createNativeQuery("ALTER TABLE user_tb ALTER COLUMN `id` RESTART WITH 1")
                                .executeUpdate();
                this.em
                                .createNativeQuery("ALTER TABLE account_tb ALTER COLUMN `id` RESTART WITH 1")
                                .executeUpdate();
                this.em
                                .createNativeQuery("ALTER TABLE transaction_tb ALTER COLUMN `id` RESTART WITH 1")
                                .executeUpdate();
        }
}
