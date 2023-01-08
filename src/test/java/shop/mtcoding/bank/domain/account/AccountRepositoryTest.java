package shop.mtcoding.bank.domain.account;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import shop.mtcoding.bank.config.dummy.DummyObject;
import shop.mtcoding.bank.domain.user.User;
import shop.mtcoding.bank.domain.user.UserRepository;

@ActiveProfiles("test")
@DataJpaTest // @Transactional 이 포함되어 있어서 메서드마다 자동 Rollback 됨.
public class AccountRepositoryTest extends DummyObject {
    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private EntityManager em;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    public void setUp() {
        autoincrementReset();
        dataSetting();
    }

    @Test
    public void findByUser_id_test() throws Exception {
        // given
        Long userId = 1L;

        // when
        List<Account> accountListPS = accountRepository.findByUser_id(userId);
        log.debug("디버그 : size : " + accountListPS.size());
        log.debug("디버그 : " + accountListPS.get(0).getId());
        log.debug("디버그 : " + accountListPS.get(0).getNumber());
        log.debug("디버그 : " + accountListPS.get(0).getPassword());
        log.debug("디버그 : " + accountListPS.get(0).getBalance());

        // then
        assertThat(accountListPS.size()).isEqualTo(2);
    }

    private void autoincrementReset() {
        this.em
                .createNativeQuery("ALTER TABLE user_tb ALTER COLUMN `id` RESTART WITH 1")
                .executeUpdate();
        this.em
                .createNativeQuery("ALTER TABLE account_tb ALTER COLUMN `id` RESTART WITH 1")
                .executeUpdate();
    }

    private void dataSetting() {
        User ssar = userRepository.save(newUser("ssar", "쌀"));
        Account ssarAccount1 = accountRepository.save(newAccount(1111L, ssar));
        Account ssarAccount2 = accountRepository.save(newAccount(4444L, ssar));
    }
}
