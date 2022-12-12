package shop.mtcoding.bank.config.jwt;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import shop.mtcoding.bank.config.auth.LoginUser;
import shop.mtcoding.bank.domain.user.User;
import shop.mtcoding.bank.domain.user.UserEnum;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class JwtAuthorizationFilterTest {
    @Autowired
    private MockMvc mvc;

    @Test
    public void authorization_success_test() throws Exception {
        // given
        User user = User.builder().id(1L).role(UserEnum.CUSTOMER).build();
        LoginUser loginUser = new LoginUser(user);
        String token = JwtProcess.create(loginUser);
        System.out.println("테스트 : " + token);

        // when
        ResultActions resultActions = mvc
                .perform(get("/api/user/hello/test").header(JwtVO.HEADER, token));

        // then
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    public void authorization_fail_test() throws Exception {
        // given

        // when
        ResultActions resultActions = mvc
                .perform(get("/api/user/hello/test"));

        // then
        resultActions.andExpect(status().isForbidden());
    }

    // https://docs.spring.io/spring-security/reference/servlet/authorization/authorize-http-requests.html
    // Role_ 안붙여도 된다는 문서를 발견
    @Test
    public void authorization_admin_test() throws Exception {
        // given
        User user = User.builder().id(1L).role(UserEnum.ADMIN).build(); // CUSTOMER, ADMIN 변경해서 해보자
        LoginUser loginUser = new LoginUser(user);
        String token = JwtProcess.create(loginUser);
        System.out.println("테스트 : " + token);

        // when
        ResultActions resultActions = mvc
                .perform(get("/api/admin/test").header(JwtVO.HEADER, token));

        // then
        resultActions.andExpect(status().isNotFound());
    }
}