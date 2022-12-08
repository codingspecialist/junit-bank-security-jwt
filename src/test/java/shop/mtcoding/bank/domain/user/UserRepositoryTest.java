package shop.mtcoding.bank.domain.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import shop.mtcoding.bank.config.dummy.DummyObject;

/*
 * JPA 제공 메서드는 테스트할 필요가 없다.
 */

@ActiveProfiles("test")
@DataJpaTest // @Transactional 이 포함되어 있어서 메서드마다 자동 Rollback 됨.
public class UserRepositoryTest extends DummyObject {

    @Autowired
    private EntityManager em;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        autoincrementReset();
        dataSetting();
    }

    @Test
    public void findByUsername_test() throws Exception {
        // given
        String username = "ssar";

        // when
        Optional<User> userOP = userRepository.findByUsername(username);

        // then
        assertTrue(userOP.isPresent());
        assertThat(userOP.get().getUsername()).isEqualTo("ssar");
    }

    private void autoincrementReset() {
        this.em
                .createNativeQuery("ALTER TABLE users ALTER COLUMN `id` RESTART WITH 1")
                .executeUpdate();
    }

    private void dataSetting() {
        User ssarPS = userRepository.save(newUser("ssar", "쌀"));
    }
}
