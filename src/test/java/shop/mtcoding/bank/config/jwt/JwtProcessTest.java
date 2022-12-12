package shop.mtcoding.bank.config.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import shop.mtcoding.bank.config.auth.LoginUser;
import shop.mtcoding.bank.domain.user.User;
import shop.mtcoding.bank.domain.user.UserEnum;

public class JwtProcessTest {

    @Test
    public void create_test() {
        // given
        User user = User.builder().id(1L).role(UserEnum.CUSTOMER).build();
        LoginUser loginUser = new LoginUser(user);

        // when
        String token = JwtProcess.create(loginUser);
        System.out.println("테스트 : " + token);

        // then
        assertTrue(token.startsWith(JwtVO.TOKEN_PREFIX));
    }

    @Test
    public void verify_test() throws Exception {
        // given
        // Bearer 붙이면 테스트 실패함
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOm51bGwsInJvbGUiOiJDVVNUT01FUiIsImlkIjoxLCJleHAiOjE2NzE0NjczNDN9.lt4rzDDOypJJV5syGjJ0YfgKCYiL4ooUzmCpEX_pqPQIFTEAZ9DUkTHll0czF0p8TOTnmDuAt83gCL-0l0bonQ";

        // when
        LoginUser loginUser = JwtProcess.verify(token);
        System.out.println("테스트 : " + loginUser.getUser().getId());

        // then
        assertThat(loginUser.getUser().getId()).isEqualTo(1L);
    }
}
