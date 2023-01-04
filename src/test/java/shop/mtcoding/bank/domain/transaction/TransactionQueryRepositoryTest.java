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
import org.springframework.context.annotation.Import;

import shop.mtcoding.bank.config.dummy.DummyObject;
import shop.mtcoding.bank.domain.account.Account;
import shop.mtcoding.bank.domain.account.AccountRepository;
import shop.mtcoding.bank.domain.user.User;
import shop.mtcoding.bank.domain.user.UserRepository;

@Import(TransactionQueryRepository.class)
@DataJpaTest
public class TransactionQueryRepositoryTest extends DummyObject {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private TransactionQueryRepository transactionQueryRepository;
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
    public void findByAccountId_deposit_test() throws Exception {
        // given
        Long userId = 1L;

        // when
        List<Transaction> transactionListPS = transactionQueryRepository.findByAccountId(userId, "DEPOSIT", 0);
        log.debug("디버그 : size : " + transactionListPS.size());
        log.debug("디버그 : " + transactionListPS.get(0).getSender());
        log.debug("디버그 : " + transactionListPS.get(0).getReciver());
        log.debug("디버그 : " + transactionListPS.get(0).getDepositAccountBalance());

        // then
        assertThat(transactionListPS.get(0).getDepositAccountBalance()).isEqualTo(800L);
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

        ssarAccount1.withdraw(100L);
        Transaction withdrawTransaction1 = transactionRepository
                .save(newWithdrawTransaction(ssarAccount1));

        cosAccount.deposit(100L);
        Transaction depositTransaction1 = transactionRepository
                .save(newDepositTransaction(cosAccount));

        ssarAccount1.withdraw(100L);
        cosAccount.deposit(100L);
        Transaction transferTransaction1 = transactionRepository
                .save(newTransferTransaction(ssarAccount1, cosAccount));

        ssarAccount1.withdraw(100L);
        loveAccount.deposit(100L);
        Transaction transferTransaction2 = transactionRepository
                .save(newTransferTransaction(ssarAccount1, loveAccount));

        cosAccount.withdraw(100L);
        ssarAccount1.deposit(100L);
        Transaction transferTransaction3 = transactionRepository
                .save(newTransferTransaction(cosAccount, ssarAccount1));

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
