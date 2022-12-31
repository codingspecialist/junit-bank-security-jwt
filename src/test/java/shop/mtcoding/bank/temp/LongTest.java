package shop.mtcoding.bank.temp;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LongTest {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Test
    public void Long_test() throws Exception {
        // given
        Long password = 1234L;
        Long reqPassword = 1234L;

        // when
        if (password.longValue() == reqPassword.longValue()) {
            log.debug("디버그 : 계좌 비밀번호가 같습니다.");
        } else {
            log.debug("디버그 : 계좌 비밀번호가 다릅니다.");
        }

        // then

    }
}
